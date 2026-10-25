/*
 * Copyright (c) 2025 MirraNET, Niklas Linz. All rights reserved.
 *
 * This file is part of the MirraNET project and is licensed under the
 * GNU Lesser General Public License v3.0 (LGPLv3).
 *
 * You may use, distribute and modify this code under the terms
 * of the LGPLv3 license. You should have received a copy of the
 * license along with this file. If not, see <https://www.gnu.org/licenses/lgpl-3.0.html>
 * or contact: niklas.linz@mirranet.de
 */

package de.linzn.calendar;

import de.linzn.calendar.objects.ICalendarType;
import de.linzn.calendar.objects.TrashType;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.data.UnfoldingReader;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Clazz;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Holds all public events of the .ics files in the calendarFiles folder.
 * Files are only parsed again when one of them has changed.
 */
public class CalendarManager {
    /* look-ahead of the reminder list: 30 hours */
    private static final long REMINDER_WINDOW_MILLIS = 108_000_000L;

    private volatile List<VEvent> events = List.of();
    private String loadedSignature = null;

    public CalendarManager() {
        this.loadCalendar();
    }

    static File calendarDirectory() {
        File calendarDirectory = new File(CalendarPlugin.calendarPlugin.getDataFolder(), "calendarFiles");
        if (!calendarDirectory.exists() && !calendarDirectory.mkdirs()) {
            CalendarPlugin.calendarPlugin.getPluginLogger().error("Could not create " + calendarDirectory);
        }
        return calendarDirectory;
    }

    /**
     * Parse an ics file. The reader is always closed.
     */
    public static Calendar parse(File file) throws IOException, ParserException {
        try (Reader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            return new CalendarBuilder().build(new UnfoldingReader(reader));
        }
    }

    private static long startOfToday() {
        return LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private static long startOf(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private static Date startDate(VEvent vEvent) {
        if (vEvent.getStartDate() == null || vEvent.getStartDate().getDate() == null) {
            return null;
        }
        return vEvent.getStartDate().getDate();
    }

    /**
     * @return the next (earliest upcoming) entry of the given trash type or null
     */
    public ICalendarType getNextTrash(TrashType trashType) {
        long today = startOfToday();
        VEvent next = null;
        long nextDate = Long.MAX_VALUE;
        for (VEvent vEvent : this.events) {
            Date start = startDate(vEvent);
            if (start == null || start.getTime() < today || start.getTime() >= nextDate) {
                continue;
            }
            if (ICalendarType.getCalendarTypeInstance(vEvent).getType() == trashType) {
                next = vEvent;
                nextDate = start.getTime();
            }
        }
        return next == null ? null : ICalendarType.getCalendarTypeInstance(next);
    }

    /**
     * @return entries from the start of the given day until 30 hours later, sorted by date
     */
    public List<ICalendarType> getCalenderEntriesList(Date date) {
        long dayStart = startOf(date);
        List<ICalendarType> iCalendarTypes = new ArrayList<>();
        for (VEvent vEvent : this.events) {
            Date start = startDate(vEvent);
            if (start == null) {
                continue;
            }
            long dateEvent = start.getTime();
            if (dateEvent >= dayStart && dateEvent <= dayStart + REMINDER_WINDOW_MILLIS) {
                iCalendarTypes.add(ICalendarType.getCalendarTypeInstance(vEvent));
            }
        }
        iCalendarTypes.sort(Comparator.comparing(ICalendarType::getDate));
        return iCalendarTypes;
    }

    private static File[] calendarFiles() {
        File[] files = calendarDirectory().listFiles(file -> file.isFile() && file.getName().toLowerCase(Locale.ROOT).endsWith(".ics"));
        if (files == null) {
            return new File[0];
        }
        Arrays.sort(files, Comparator.comparing(File::getName));
        return files;
    }

    private static String signature(File[] files) {
        StringBuilder builder = new StringBuilder();
        for (File file : files) {
            builder.append(file.getName()).append(':').append(file.lastModified()).append(':').append(file.length()).append(';');
        }
        return builder.toString();
    }

    /**
     * Reload all calendar files if one of them changed.
     */
    public synchronized void loadCalendar() {
        File[] files = calendarFiles();
        String signature = signature(files);
        if (signature.equals(this.loadedSignature)) {
            return;
        }
        List<VEvent> loaded = new ArrayList<>();
        for (File file : files) {
            try {
                Calendar tempCalendar = parse(file);
                for (Object o : tempCalendar.getComponents("VEVENT")) {
                    VEvent vevent = (VEvent) o;
                    if (vevent.getClassification() != Clazz.PRIVATE) {
                        loaded.add(vevent);
                    }
                }
                CalendarPlugin.calendarPlugin.getPluginLogger().debug("Calendar data loaded :: " + file.getName());
            } catch (IOException | ParserException | RuntimeException e) {
                CalendarPlugin.calendarPlugin.getPluginLogger().error("Error while parsing calendar: " + file.getName(), e);
            }
        }
        this.events = List.copyOf(loaded);
        this.loadedSignature = signature;
    }
}

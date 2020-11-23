/*
 * Copyright (C) 2020. Niklas Linz - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the LGPLv3 license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the LGPLv3 license with
 * this file. If not, please write to: niklas.linz@enigmar.de
 *
 */

package de.linzn.calender;


import de.linzn.calender.objects.ICalendarType;
import de.linzn.calender.objects.TrashType;
import de.stem.stemSystem.STEMSystemApp;
import de.stem.stemSystem.utils.Color;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.data.UnfoldingReader;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Clazz;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarManager {
    private Calendar calendar;

    public CalendarManager() {
        this.loadCalendar();
    }


    public ICalendarType getNextTrash(TrashType trashType) {
        Date date = new Date();
        java.util.Calendar tempCalendar = java.util.Calendar.getInstance();
        tempCalendar.setTime(date);
        tempCalendar.set(java.util.Calendar.MILLISECOND, 0);
        tempCalendar.set(java.util.Calendar.SECOND, 0);
        tempCalendar.set(java.util.Calendar.MINUTE, 0);
        tempCalendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        date = tempCalendar.getTime();

        if (calendar != null) {
            for (Object o : calendar.getComponents("VEVENT")) {
                VEvent vEvent = (VEvent) o;
                long dateToday = date.getTime();
                long dateEvent = vEvent.getStartDate().getDate().getTime();

                if ((dateEvent - dateToday >= 0)) {
                    ICalendarType iTrash = ICalendarType.getCalendarTypeInstance(vEvent);
                    if (iTrash.getType() == trashType) {
                        return iTrash;
                    }
                }
            }
        }
        return null;
    }


    public List<ICalendarType> getCalenderEntriesList(Date date) {
        java.util.Calendar tempCalendar = java.util.Calendar.getInstance();
        tempCalendar.setTime(date);
        tempCalendar.set(java.util.Calendar.MILLISECOND, 0);
        tempCalendar.set(java.util.Calendar.SECOND, 0);
        tempCalendar.set(java.util.Calendar.MINUTE, 0);
        tempCalendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        date = tempCalendar.getTime();

        List<ICalendarType> iCalendarTypes = new ArrayList<>();
        if (calendar != null) {
            for (Object o : calendar.getComponents("VEVENT")) {
                VEvent vEvent = (VEvent) o;

                long dateToday = date.getTime();
                long dateEvent = vEvent.getStartDate().getDate().getTime();


                if ((dateEvent - dateToday >= 0) && dateEvent <= (dateToday + 108000000)) {
                    iCalendarTypes.add(ICalendarType.getCalendarTypeInstance(vEvent));
                }
            }
        }
        return iCalendarTypes;
    }

    public void loadCalendar() {
        Calendar calendar = new Calendar();

        /* Load calendar files */
        File calendarDirectory = new File(CalenderPlugin.calenderPlugin.getDataFolder(), "calendarFiles");
        if (!calendarDirectory.exists()) {
            calendarDirectory.mkdir();
        }

        File[] calendarDirectoryFiles = calendarDirectory.listFiles();

        for (File file : calendarDirectoryFiles) {
            if (file.isFile()) {
                if (file.getName().toLowerCase().endsWith(".ics")) {
                    Calendar tempCalendar = null;
                    try {
                        tempCalendar = new CalendarBuilder().build(new UnfoldingReader(new FileReader(file)));
                        STEMSystemApp.LOGGER.DEBUG(Color.GREEN + "New calender data added :: " + file.getName());
                    } catch (IOException | ParserException e) {
                        STEMSystemApp.LOGGER.ERROR("Error while parsing calendar: " + file.getName());
                        e.printStackTrace();
                    }
                    if (tempCalendar != null) {
                        for (Object o : tempCalendar.getComponents("VEVENT")) {
                            VEvent vevent = (VEvent) o;
                            if (vevent.getClassification() != Clazz.PRIVATE) {
                                calendar.getComponents().add(vevent);
                            }
                        }
                    }
                }
            }
        }
        this.calendar = calendar;
    }
}

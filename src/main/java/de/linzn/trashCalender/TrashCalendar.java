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

package de.linzn.trashCalender;


import de.linzn.trashCalender.objects.ITrash;
import de.stem.stemSystem.AppLogger;
import de.stem.stemSystem.utils.Color;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrashCalendar {
    public File calenderFile;
    public File blueCalenderFile;
    private Calendar calendar;
    private Calendar blueCalendar;

    public TrashCalendar() {
        this.loadCalendar();
    }


    public List<ITrash> getTrashList(Date date) {
        java.util.Calendar tempCalendar = java.util.Calendar.getInstance();
        tempCalendar.setTime(date);
        tempCalendar.set(java.util.Calendar.MILLISECOND, 0);
        tempCalendar.set(java.util.Calendar.SECOND, 0);
        tempCalendar.set(java.util.Calendar.MINUTE, 0);
        tempCalendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        date = tempCalendar.getTime();

        List<ITrash> iTrashes = new ArrayList<>();
        if (calendar != null) {
            for (Object o : calendar.getComponents("VEVENT")) {
                VEvent vEvent = (VEvent) o;

                long dateToday = date.getTime();
                long dateEvent = vEvent.getStartDate().getDate().getTime();


                if ((dateEvent - dateToday >= 0) && dateEvent <= (dateToday + 108000000)) {
                    iTrashes.add(ITrash.getTrash(vEvent));
                }
            }
        }

        /* Temp solution todo */
        if (blueCalendar != null) {
            for (Object o : blueCalendar.getComponents("VEVENT")) {
                VEvent vEvent = (VEvent) o;

                long dateToday = date.getTime();
                long dateEvent = vEvent.getStartDate().getDate().getTime();


                if ((dateEvent - dateToday >= 0) && dateEvent <= (dateToday + 108000000)) {
                    iTrashes.add(ITrash.getTrash(vEvent));
                }
            }
        }

        return iTrashes;
    }

    public void loadCalendar() {
        this.calenderFile = new File(TrashCalenderPlugin.trashCalenderPlugin.getDataFolder(), "EVSTrash.ics");
        this.blueCalenderFile = new File(TrashCalenderPlugin.trashCalenderPlugin.getDataFolder(), "BlaueTonne.ics");
        if (this.calenderFile.exists()) {
            try {
                calendar = new CalendarBuilder().build(new FileInputStream(this.calenderFile));
                blueCalendar = new CalendarBuilder().build(new FileInputStream(this.blueCalenderFile));
                AppLogger.debug(Color.GREEN + "New calender data loaded");
            } catch (IOException | ParserException e) {
                e.printStackTrace();
            }
        }
    }

}

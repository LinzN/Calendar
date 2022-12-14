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

package de.linzn.calendar.restfulapi;

import de.linzn.calendar.CalendarPlugin;
import de.linzn.calendar.objects.ICalendarType;
import de.linzn.calendar.objects.TrashType;
import de.linzn.restfulapi.api.jsonapi.IRequest;
import de.linzn.restfulapi.api.jsonapi.RequestData;
import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class GET_TrashCalendar implements IRequest {

    private CalendarPlugin calendarPlugin;

    public GET_TrashCalendar(CalendarPlugin calendarPlugin) {
        this.calendarPlugin = calendarPlugin;
    }

    @Override
    public Object proceedRequestData(RequestData requestData) {
        JSONObject jsonObject = new JSONObject();
        Format dateFormat = new SimpleDateFormat("EEEE d MMMMM yyyy", Locale.GERMANY);

        ICalendarType blackTrash = this.calendarPlugin.getCalendarManager().getNextTrash(TrashType.BLACK);
        ICalendarType greenTrash = this.calendarPlugin.getCalendarManager().getNextTrash(TrashType.GREEN);
        ICalendarType yellowTrash = this.calendarPlugin.getCalendarManager().getNextTrash(TrashType.YELLOW);
        ICalendarType blueTrash = this.calendarPlugin.getCalendarManager().getNextTrash(TrashType.BLUE);

        if (blackTrash != null) {
            JSONObject blackTrashJson = new JSONObject();
            blackTrashJson.put("name", blackTrash.getName());
            blackTrashJson.put("date", dateFormat.format(blackTrash.getDate()));
            blackTrashJson.put("type", blackTrash.getType().name());
            jsonObject.put(blackTrash.getType().name(), blackTrashJson);
        } else {
            ICalendarType dummyTrash = ICalendarType.getDummyType(TrashType.BLACK);
            JSONObject blackTrashJson = new JSONObject();
            blackTrashJson.put("name", dummyTrash.getName());
            blackTrashJson.put("date", "No date yet");
            blackTrashJson.put("type", dummyTrash.getType().name());
            jsonObject.put(dummyTrash.getType().name(), blackTrashJson);
        }

        if (greenTrash != null) {
            JSONObject greenTrashJson = new JSONObject();
            greenTrashJson.put("name", greenTrash.getName());
            greenTrashJson.put("date", dateFormat.format(greenTrash.getDate()));
            greenTrashJson.put("type", greenTrash.getType().name());
            jsonObject.put(greenTrash.getType().name(), greenTrashJson);
        } else {
            ICalendarType dummyTrash = ICalendarType.getDummyType(TrashType.GREEN);
            JSONObject greenTrashJson = new JSONObject();
            greenTrashJson.put("name", dummyTrash.getName());
            greenTrashJson.put("date", "No date yet");
            greenTrashJson.put("type", dummyTrash.getType().name());
            jsonObject.put(dummyTrash.getType().name(), greenTrashJson);
        }
        if (yellowTrash != null) {
            JSONObject yellowTrashJson = new JSONObject();
            yellowTrashJson.put("name", yellowTrash.getName());
            yellowTrashJson.put("date", dateFormat.format(yellowTrash.getDate()));
            yellowTrashJson.put("type", yellowTrash.getType().name());
            jsonObject.put(yellowTrash.getType().name(), yellowTrashJson);
        } else {
            ICalendarType dummyTrash = ICalendarType.getDummyType(TrashType.YELLOW);
            JSONObject yellowTrashJson = new JSONObject();
            yellowTrashJson.put("name", dummyTrash.getName());
            yellowTrashJson.put("date", "No date yet");
            yellowTrashJson.put("type", dummyTrash.getType().name());
            jsonObject.put(dummyTrash.getType().name(), yellowTrashJson);
        }
        if (blueTrash != null) {
            JSONObject blueTrashJson = new JSONObject();
            blueTrashJson.put("name", blueTrash.getName());
            blueTrashJson.put("date", dateFormat.format(blueTrash.getDate()));
            blueTrashJson.put("type", blueTrash.getType().name());
            jsonObject.put(blueTrash.getType().name(), blueTrashJson);
        } else {
            ICalendarType dummyTrash = ICalendarType.getDummyType(TrashType.BLUE);
            JSONObject blueTrashJson = new JSONObject();
            blueTrashJson.put("name", dummyTrash.getName());
            blueTrashJson.put("date", "No date yet");
            blueTrashJson.put("type", dummyTrash.getType().name());
            jsonObject.put(dummyTrash.getType().name(), blueTrashJson);
        }
        return jsonObject;
    }

    @Override
    public Object genericData() {
        return proceedRequestData(null);
    }

    @Override
    public String name() {
        return "trash-calendar";
    }
}

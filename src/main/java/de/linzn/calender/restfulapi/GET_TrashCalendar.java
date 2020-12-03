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

package de.linzn.calender.restfulapi;

import de.linzn.calender.CalenderPlugin;
import de.linzn.calender.objects.ICalendarType;
import de.linzn.calender.objects.TrashType;
import de.linzn.restfulapi.api.jsonapi.IRequest;
import de.linzn.restfulapi.api.jsonapi.RequestData;
import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class GET_TrashCalendar implements IRequest {

    private CalenderPlugin calenderPlugin;

    public GET_TrashCalendar(CalenderPlugin calenderPlugin) {
        this.calenderPlugin = calenderPlugin;
    }

    @Override
    public Object proceedRequestData(RequestData requestData) {
        JSONObject jsonObject = new JSONObject();
        Format dateFormat = new SimpleDateFormat("EEEE d MMMMM yyyy", Locale.GERMANY);

        ICalendarType blackTrash = this.calenderPlugin.getCalendarManager().getNextTrash(TrashType.BLACK);
        ICalendarType greenTrash = this.calenderPlugin.getCalendarManager().getNextTrash(TrashType.GREEN);
        ICalendarType yellowTrash = this.calenderPlugin.getCalendarManager().getNextTrash(TrashType.YELLOW);
        ICalendarType blueTrash = this.calenderPlugin.getCalendarManager().getNextTrash(TrashType.BLUE);


        JSONObject blackTrashJson = new JSONObject();
        blackTrashJson.put("name", blackTrash.getName());
        blackTrashJson.put("date", dateFormat.format(blackTrash.getDate()));
        blackTrashJson.put("type", blackTrash.getType().name());
        jsonObject.put(blackTrash.getType().name(), blackTrashJson);

        JSONObject greenTrashJson = new JSONObject();
        greenTrashJson.put("name", greenTrash.getName());
        greenTrashJson.put("date", dateFormat.format(greenTrash.getDate()));
        greenTrashJson.put("type", greenTrash.getType().name());
        jsonObject.put(greenTrash.getType().name(), greenTrashJson);

        JSONObject yellowTrashJson = new JSONObject();
        yellowTrashJson.put("name", yellowTrash.getName());
        yellowTrashJson.put("date", dateFormat.format(yellowTrash.getDate()));
        yellowTrashJson.put("type", yellowTrash.getType().name());
        jsonObject.put(yellowTrash.getType().name(), yellowTrashJson);

        JSONObject blueTrashJson = new JSONObject();
        blueTrashJson.put("name", blueTrash.getName());
        blueTrashJson.put("date", dateFormat.format(blueTrash.getDate()));
        blueTrashJson.put("type", blueTrash.getType().name());
        jsonObject.put(blueTrash.getType().name(), blueTrashJson);
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

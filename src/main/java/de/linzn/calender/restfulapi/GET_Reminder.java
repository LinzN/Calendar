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
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GET_Reminder implements IRequest {

    private CalenderPlugin calenderPlugin;

    public GET_Reminder(CalenderPlugin calenderPlugin) {
        this.calenderPlugin = calenderPlugin;
    }

    @Override
    public Object proceedRequestData(RequestData requestData) {
        JSONArray jsonArray = new JSONArray();

        List<ICalendarType> iCalendarTypes = this.calenderPlugin.getCalendarManager().getCalenderEntriesList(new Date());

        for (ICalendarType iCalendarType : iCalendarTypes) {
            Format dateFormat = new SimpleDateFormat("EEEE d MMMMM yyyy", Locale.GERMANY);

            String reminderType = "trash";

            if (iCalendarType.getType() == TrashType.OTHER) {
                reminderType = "other";
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("reminderType", reminderType);
            jsonObject.put("name", iCalendarType.getName());
            jsonObject.put("date", dateFormat.format(iCalendarType.getDate()));
            jsonObject.put("type", iCalendarType.getType().name());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    @Override
    public Object genericData() {
        return proceedRequestData(null);
    }

    @Override
    public String name() {
        return "reminder";
    }
}

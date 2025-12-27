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

package de.linzn.calendar.webapi;

import com.sun.net.httpserver.HttpExchange;
import de.linzn.calendar.CalendarPlugin;
import de.linzn.calendar.objects.ICalendarType;
import de.linzn.calendar.objects.TrashType;
import de.linzn.webapi.core.ApiResponse;
import de.linzn.webapi.core.HttpRequestClientPayload;
import de.linzn.webapi.modules.RequestInterface;
import org.json.JSONObject;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TrashWebApi extends RequestInterface {
    private CalendarPlugin calendarPlugin;

    public TrashWebApi(CalendarPlugin calendarPlugin) {
        this.calendarPlugin = calendarPlugin;
    }

    @Override
    public Object callHttpEvent(HttpExchange httpExchange, HttpRequestClientPayload httpRequestClientPayload) throws IOException {
        ApiResponse apiResponse = new ApiResponse();

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
            apiResponse.getJSONObject().put(blackTrash.getType().name(), blackTrashJson);
        } else {
            ICalendarType dummyTrash = ICalendarType.getDummyType(TrashType.BLACK);
            JSONObject blackTrashJson = new JSONObject();
            blackTrashJson.put("name", dummyTrash.getName());
            blackTrashJson.put("date", "No date yet");
            blackTrashJson.put("type", dummyTrash.getType().name());
            apiResponse.getJSONObject().put(dummyTrash.getType().name(), blackTrashJson);
        }

        if (greenTrash != null) {
            JSONObject greenTrashJson = new JSONObject();
            greenTrashJson.put("name", greenTrash.getName());
            greenTrashJson.put("date", dateFormat.format(greenTrash.getDate()));
            greenTrashJson.put("type", greenTrash.getType().name());
            apiResponse.getJSONObject().put(greenTrash.getType().name(), greenTrashJson);
        } else {
            ICalendarType dummyTrash = ICalendarType.getDummyType(TrashType.GREEN);
            JSONObject greenTrashJson = new JSONObject();
            greenTrashJson.put("name", dummyTrash.getName());
            greenTrashJson.put("date", "No date yet");
            greenTrashJson.put("type", dummyTrash.getType().name());
            apiResponse.getJSONObject().put(dummyTrash.getType().name(), greenTrashJson);
        }
        if (yellowTrash != null) {
            JSONObject yellowTrashJson = new JSONObject();
            yellowTrashJson.put("name", yellowTrash.getName());
            yellowTrashJson.put("date", dateFormat.format(yellowTrash.getDate()));
            yellowTrashJson.put("type", yellowTrash.getType().name());
            apiResponse.getJSONObject().put(yellowTrash.getType().name(), yellowTrashJson);
        } else {
            ICalendarType dummyTrash = ICalendarType.getDummyType(TrashType.YELLOW);
            JSONObject yellowTrashJson = new JSONObject();
            yellowTrashJson.put("name", dummyTrash.getName());
            yellowTrashJson.put("date", "No date yet");
            yellowTrashJson.put("type", dummyTrash.getType().name());
            apiResponse.getJSONObject().put(dummyTrash.getType().name(), yellowTrashJson);
        }
        if (blueTrash != null) {
            JSONObject blueTrashJson = new JSONObject();
            blueTrashJson.put("name", blueTrash.getName());
            blueTrashJson.put("date", dateFormat.format(blueTrash.getDate()));
            blueTrashJson.put("type", blueTrash.getType().name());
            apiResponse.getJSONObject().put(blueTrash.getType().name(), blueTrashJson);
        } else {
            ICalendarType dummyTrash = ICalendarType.getDummyType(TrashType.BLUE);
            JSONObject blueTrashJson = new JSONObject();
            blueTrashJson.put("name", dummyTrash.getName());
            blueTrashJson.put("date", "No date yet");
            blueTrashJson.put("type", dummyTrash.getType().name());
            apiResponse.getJSONObject().put(dummyTrash.getType().name(), blueTrashJson);
        }
        return apiResponse.buildResponse();
    }
}

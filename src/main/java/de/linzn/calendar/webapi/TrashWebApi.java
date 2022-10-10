package de.linzn.calendar.webapi;

import com.sun.net.httpserver.HttpExchange;
import de.linzn.calendar.CalendarPlugin;
import de.linzn.calendar.objects.ICalendarType;
import de.linzn.calendar.objects.TrashType;
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
        }
        if (greenTrash != null) {
            JSONObject greenTrashJson = new JSONObject();
            greenTrashJson.put("name", greenTrash.getName());
            greenTrashJson.put("date", dateFormat.format(greenTrash.getDate()));
            greenTrashJson.put("type", greenTrash.getType().name());
            jsonObject.put(greenTrash.getType().name(), greenTrashJson);
        }
        if (yellowTrash != null) {
            JSONObject yellowTrashJson = new JSONObject();
            yellowTrashJson.put("name", yellowTrash.getName());
            yellowTrashJson.put("date", dateFormat.format(yellowTrash.getDate()));
            yellowTrashJson.put("type", yellowTrash.getType().name());
            jsonObject.put(yellowTrash.getType().name(), yellowTrashJson);
        }
        if (blueTrash != null) {
            JSONObject blueTrashJson = new JSONObject();
            blueTrashJson.put("name", blueTrash.getName());
            blueTrashJson.put("date", dateFormat.format(blueTrash.getDate()));
            blueTrashJson.put("type", blueTrash.getType().name());
            jsonObject.put(blueTrash.getType().name(), blueTrashJson);
        }
        return jsonObject;
    }
}
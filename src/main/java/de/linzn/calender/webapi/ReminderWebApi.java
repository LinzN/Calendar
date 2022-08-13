package de.linzn.calender.webapi;

import com.sun.net.httpserver.HttpExchange;
import de.linzn.calender.CalenderPlugin;
import de.linzn.calender.objects.ICalendarType;
import de.linzn.calender.objects.TrashType;
import de.linzn.webapi.core.HttpRequestClientPayload;
import de.linzn.webapi.modules.RequestInterface;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReminderWebApi extends RequestInterface {

    private CalenderPlugin calenderPlugin;

    public ReminderWebApi(CalenderPlugin calenderPlugin) {
        this.calenderPlugin = calenderPlugin;
    }

    @Override
    public Object callHttpEvent(HttpExchange httpExchange, HttpRequestClientPayload httpRequestClientPayload) throws IOException {
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
}

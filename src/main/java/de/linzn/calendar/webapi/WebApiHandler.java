package de.linzn.calendar.webapi;

import de.linzn.calendar.CalendarPlugin;
import de.linzn.webapi.WebApiPlugin;
import de.linzn.webapi.modules.WebModule;
import de.stem.stemSystem.modules.pluginModule.STEMPlugin;

public class WebApiHandler {

    private final STEMPlugin stemPlugin;
    private final WebModule stemWebModule;

    public WebApiHandler(CalendarPlugin calendarPlugin) {
        this.stemPlugin = calendarPlugin;
        stemWebModule = new WebModule("calendar");
        stemWebModule.registerSubCallHandler(new ReminderWebApi(calendarPlugin), "reminder");
        stemWebModule.registerSubCallHandler(new TrashWebApi(calendarPlugin), "trash");
        WebApiPlugin.webApiPlugin.getWebServer().enableCallModule(stemWebModule);
    }
}

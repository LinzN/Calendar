package de.linzn.calender.webapi;

import de.linzn.calender.CalenderPlugin;
import de.linzn.webapi.WebApiPlugin;
import de.linzn.webapi.modules.WebModule;
import de.stem.stemSystem.modules.pluginModule.STEMPlugin;

public class WebApiHandler {

    private final STEMPlugin stemPlugin;
    private final WebModule stemWebModule;

    public WebApiHandler(CalenderPlugin calenderPlugin) {
        this.stemPlugin = calenderPlugin;
        stemWebModule = new WebModule("calendar");
        stemWebModule.registerSubCallHandler(new ReminderWebApi(calenderPlugin), "reminder");
        stemWebModule.registerSubCallHandler(new TrashWebApi(calenderPlugin), "trash");
        WebApiPlugin.webApiPlugin.getWebServer().enableCallModule(stemWebModule);
    }
}

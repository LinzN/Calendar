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

import de.linzn.calendar.CalendarPlugin;
import de.linzn.stem.modules.pluginModule.STEMPlugin;
import de.linzn.webapi.WebApiPlugin;
import de.linzn.webapi.modules.WebModule;

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

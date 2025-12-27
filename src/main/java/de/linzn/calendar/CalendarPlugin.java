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

package de.linzn.calendar;


import de.linzn.calendar.callback.CalendarBuilderCallback;
import de.linzn.calendar.callback.EVSCallback;
import de.linzn.calendar.callback.SharedCalendarCallback;
import de.linzn.calendar.webapi.WebApiHandler;
import de.linzn.stem.STEMApp;
import de.linzn.stem.modules.pluginModule.STEMPlugin;


public class CalendarPlugin extends STEMPlugin {

    public static CalendarPlugin calendarPlugin;
    private WebApiHandler webApiHandler;
    private CalendarManager calendarManager;

    public CalendarPlugin() {
        calendarPlugin = this;
    }

    @Override
    public void onEnable() {
        this.getDefaultConfig().save();
        STEMApp.getInstance().getCallBackService().registerCallbackListener(new CalendarBuilderCallback(), this);
        STEMApp.getInstance().getCallBackService().registerCallbackListener(new EVSCallback(), this);
        STEMApp.getInstance().getCallBackService().registerCallbackListener(new SharedCalendarCallback(), this);
        this.calendarManager = new CalendarManager();
        this.webApiHandler = new WebApiHandler(this);
    }

    @Override
    public void onDisable() {
    }


    public CalendarManager getCalendarManager() {
        return calendarManager;
    }
}

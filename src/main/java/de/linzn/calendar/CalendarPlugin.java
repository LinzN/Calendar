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

package de.linzn.calendar;


import de.linzn.calendar.callback.CalendarBuilderCallback;
import de.linzn.calendar.callback.EVSCallback;
import de.linzn.calendar.callback.SharedCalendarCallback;
import de.linzn.calendar.restfulapi.GET_Reminder;
import de.linzn.calendar.restfulapi.GET_TrashCalendar;
import de.linzn.calendar.webapi.WebApiHandler;
import de.linzn.restfulapi.RestFulApiPlugin;
import de.stem.stemSystem.STEMSystemApp;
import de.stem.stemSystem.modules.pluginModule.STEMPlugin;


public class CalendarPlugin extends STEMPlugin {

    private WebApiHandler webApiHandler;

    public static CalendarPlugin calendarPlugin;
    private CalendarManager calendarManager;

    public CalendarPlugin() {
        calendarPlugin = this;
    }

    @Override
    public void onEnable() {
        this.getDefaultConfig().save();
        STEMSystemApp.getInstance().getCallBackService().registerCallbackListener(new CalendarBuilderCallback(), this);
        STEMSystemApp.getInstance().getCallBackService().registerCallbackListener(new EVSCallback(), this);
        STEMSystemApp.getInstance().getCallBackService().registerCallbackListener(new SharedCalendarCallback(), this);
        this.calendarManager = new CalendarManager();
        this.webApiHandler = new WebApiHandler(this);
        RestFulApiPlugin.restFulApiPlugin.registerIGetJSONClass(new GET_Reminder(this));
        RestFulApiPlugin.restFulApiPlugin.registerIGetJSONClass(new GET_TrashCalendar(this));
    }

    @Override
    public void onDisable() {
    }


    public CalendarManager getCalendarManager() {
        return calendarManager;
    }
}

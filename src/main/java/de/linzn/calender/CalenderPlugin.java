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

package de.linzn.calender;


import de.linzn.calender.callback.CalendarBuilderCallback;
import de.linzn.calender.callback.EVSCallback;
import de.linzn.calender.callback.SharedCalendarCallback;
import de.stem.stemSystem.STEMSystemApp;
import de.stem.stemSystem.modules.pluginModule.STEMPlugin;


public class CalenderPlugin extends STEMPlugin {

    public static CalenderPlugin calenderPlugin;
    private CalendarManager calendarManager;

    public CalenderPlugin() {
        calenderPlugin = this;
    }

    @Override
    public void onEnable() {
        this.getDefaultConfig().save();
        STEMSystemApp.getInstance().getCallBackService().registerCallbackListener(new CalendarBuilderCallback(), this);
        STEMSystemApp.getInstance().getCallBackService().registerCallbackListener(new EVSCallback(), this);
        STEMSystemApp.getInstance().getCallBackService().registerCallbackListener(new SharedCalendarCallback(), this);
        this.calendarManager = new CalendarManager();
    }

    @Override
    public void onDisable() {
    }


    public CalendarManager getCalendarManager() {
        return calendarManager;
    }
}

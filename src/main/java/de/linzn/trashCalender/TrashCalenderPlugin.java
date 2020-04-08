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

package de.linzn.trashCalender;



import de.linzn.trashCalender.callback.EVSCallback;
import de.stem.stemSystem.STEMSystemApp;
import de.stem.stemSystem.modules.pluginModule.STEMPlugin;


public class TrashCalenderPlugin extends STEMPlugin {

    public static TrashCalenderPlugin trashCalenderPlugin;
    private TrashCalendar trashCalendar;

    public TrashCalenderPlugin() {
        trashCalenderPlugin = this;
    }

    @Override
    public void onEnable() {
        this.getDefaultConfig().save();
        STEMSystemApp.getInstance().getCallBackService().registerCallbackListener(new EVSCallback(), this);
        this.trashCalendar = new TrashCalendar();
    }

    @Override
    public void onDisable() {
    }


    public TrashCalendar getTrashCalendar() {
        return trashCalendar;
    }
}

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
import de.linzn.stem.api.scheduler.TaskScheduler;
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
        this.getDefaultConfig().getInt("general.shared_refresh_minutes", 15);
        this.getDefaultConfig().save();
        this.calendarManager = new CalendarManager();
        TaskScheduler scheduler = this.getStemApi().scheduler();
        scheduler.registerCallback(new CalendarBuilderCallback());
        scheduler.registerCallback(new EVSCallback());
        scheduler.registerCallback(new SharedCalendarCallback());
        this.webApiHandler = new WebApiHandler(this);
    }

    @Override
    public void onDisable() {
        if (this.webApiHandler != null) {
            this.webApiHandler.disable();
        }
    }

    public CalendarManager getCalendarManager() {
        return calendarManager;
    }
}

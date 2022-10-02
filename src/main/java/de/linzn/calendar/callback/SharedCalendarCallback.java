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

package de.linzn.calendar.callback;


import de.linzn.calendar.CalendarPlugin;
import de.stem.stemSystem.STEMSystemApp;
import de.stem.stemSystem.taskManagment.AbstractCallback;
import de.stem.stemSystem.taskManagment.CallbackTime;
import de.stem.stemSystem.taskManagment.operations.OperationOutput;
import de.stem.stemSystem.taskManagment.operations.defaultOperations.ShellOperation;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class SharedCalendarCallback extends AbstractCallback {

    @Override
    public void operation() {
        File calendarDirectory = new File(CalendarPlugin.calendarPlugin.getDataFolder(), "calendarFiles");
        if (!calendarDirectory.exists()) {
            calendarDirectory.mkdir();
        }

        for (File file : calendarDirectory.listFiles()) {
            if (file.getName().startsWith("shared_")) {
                file.delete();
            }
        }

        HashMap<String, Object> sharedCalendars = (HashMap<String, Object>) CalendarPlugin.calendarPlugin.getDefaultConfig().get("sharedCalendars");


        for (String key : sharedCalendars.keySet()) {
            String link = CalendarPlugin.calendarPlugin.getDefaultConfig().getString("sharedCalendars." + key + ".link");
            String command = "curl " + link + " --output " + new File(calendarDirectory, "shared_" + key + ".ics").getAbsolutePath();
            ShellOperation shellOperation = new ShellOperation();
            shellOperation.setUseSSH(false);
            shellOperation.setScriptCommand(command);
            shellOperation.setUseOutput(false);
            addOperationData(shellOperation);
        }
    }

    @Override
    public void callback(OperationOutput operationOutput) {
        STEMSystemApp.LOGGER.DEBUG("Shared calender pull finish " + operationOutput.getExit());
    }

    @Override
    public CallbackTime getTime() {
        return new CallbackTime(1, 1, TimeUnit.MINUTES);
    }
}

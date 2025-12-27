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

package de.linzn.calendar.callback;


import de.linzn.calendar.CalendarPlugin;
import de.linzn.stem.STEMApp;
import de.linzn.stem.taskManagment.AbstractCallback;
import de.linzn.stem.taskManagment.CallbackTime;
import de.linzn.stem.taskManagment.operations.OperationOutput;
import de.linzn.stem.taskManagment.operations.defaultOperations.ShellOperation;

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
        STEMApp.LOGGER.DEBUG("Shared calender pull finish " + operationOutput.getExit());
    }

    @Override
    public CallbackTime getTime() {
        return new CallbackTime(1, 1, TimeUnit.MINUTES);
    }
}

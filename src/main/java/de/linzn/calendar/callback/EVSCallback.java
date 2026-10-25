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

import de.linzn.calendar.CalendarManager;
import de.linzn.calendar.CalendarPlugin;
import de.linzn.stem.taskManagment.AbstractCallback;
import de.linzn.stem.taskManagment.CallbackTime;
import de.linzn.stem.taskManagment.operations.OperationOutput;
import de.linzn.stem.taskManagment.operations.defaultOperations.ScriptOperation;
import net.fortuna.ical4j.data.ParserException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Downloads the EVS waste calendar once a day via script. The new file only replaces the current one
 * if it can be parsed.
 */
public class EVSCallback extends AbstractCallback {

    private static File calendarDirectory() {
        File calendarDirectory = new File(CalendarPlugin.calendarPlugin.getDataFolder(), "calendarFiles");
        if (!calendarDirectory.exists()) {
            calendarDirectory.mkdirs();
        }
        return calendarDirectory;
    }

    @Override
    public void operation() {
        File calendarDirectory = calendarDirectory();
        File file = new File(calendarDirectory, "_online_evs.temp");
        if (file.exists() && !file.delete()) {
            CalendarPlugin.calendarPlugin.getPluginLogger().warning("Could not delete old temp file " + file);
        }
        ScriptOperation scriptOperation = new ScriptOperation("get_evs-data");
        scriptOperation.addParameter("scriptdestination", calendarDirectory.getAbsolutePath());
        scriptOperation.addParameter("outfile", file.getAbsolutePath());
        addOperationData(scriptOperation);
    }

    @Override
    public void callback(OperationOutput operationOutput) {
        if (operationOutput.getExit() != 0 && operationOutput.getData() instanceof JSONObject jsonObject) {
            JSONArray lines = jsonObject.optJSONArray("outputLines");
            if (lines != null) {
                for (Object line : lines) {
                    CalendarPlugin.calendarPlugin.getPluginLogger().warning(String.valueOf(line));
                }
            }
        }
        File calendarDirectory = calendarDirectory();
        File tempFile = new File(calendarDirectory, "_online_evs.temp");
        if (!tempFile.exists()) {
            CalendarPlugin.calendarPlugin.getPluginLogger().warning("EVS calendar pull produced no file (exit " + operationOutput.getExit() + ")");
            return;
        }
        try {
            CalendarManager.parse(tempFile);
            Files.move(tempFile.toPath(), new File(calendarDirectory, "online_evs.ics").toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException | ParserException | RuntimeException e) {
            CalendarPlugin.calendarPlugin.getPluginLogger().warning("Error in new EVS file. Skipping!");
            tempFile.delete();
        }
        CalendarPlugin.calendarPlugin.getPluginLogger().debug("EVS calender pull finish " + operationOutput.getExit());
    }

    @Override
    public CallbackTime getTime() {
        return new CallbackTime("30 3 * * *");
    }
}

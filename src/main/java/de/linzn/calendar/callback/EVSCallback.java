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
import de.linzn.stem.taskManagment.operations.defaultOperations.ScriptOperation;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.data.UnfoldingReader;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class EVSCallback extends AbstractCallback {

    @Override
    public void operation() {
        File calendarDirectory = new File(CalendarPlugin.calendarPlugin.getDataFolder(), "calendarFiles");
        if (!calendarDirectory.exists()) {
            calendarDirectory.mkdir();
        }

        String mmseData = CalendarPlugin.calendarPlugin.getDefaultConfig().getString("trash.mmse_data");
        String proxy = CalendarPlugin.calendarPlugin.getDefaultConfig().getString("general.proxy_server");
        boolean useProxy = CalendarPlugin.calendarPlugin.getDefaultConfig().getBoolean("general.use_proxy", false);
        File file = new File(calendarDirectory, "_online_evs.temp");
        if (file.exists()) {
            file.delete();
        }


        ScriptOperation scriptOperation = new ScriptOperation("get_evs-data");
        scriptOperation.addParameter("scriptdestination", calendarDirectory.getAbsolutePath());
        scriptOperation.addParameter("outfile", file.getAbsolutePath());
        addOperationData(scriptOperation);
    }

    @Override
    public void callback(OperationOutput operationOutput) {
        JSONObject jsonObject = (JSONObject) operationOutput.getData();
        if (operationOutput.getExit() != 0) {
            for (Object line : jsonObject.getJSONArray("outputLines")) {
                STEMApp.LOGGER.WARNING(line);
            }
            for (Object line : jsonObject.getJSONArray("errorLines")) {
                STEMApp.LOGGER.WARNING(line);
            }
        }
        File calendarDirectory = new File(CalendarPlugin.calendarPlugin.getDataFolder(), "calendarFiles");
        File tempFile = new File(calendarDirectory, "_online_evs.temp");

        try {
            new CalendarBuilder().build(new UnfoldingReader(new FileReader(tempFile)));
            File file = new File(calendarDirectory, "online_evs.ics");
            if (file.exists()) {
                file.delete();
            }
            tempFile.renameTo(file);
        } catch (IOException | ParserException e) {
            STEMApp.LOGGER.WARNING("Error in new EVS file. Skipping!");
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }


        STEMApp.LOGGER.DEBUG("EVS calender pull finish " + operationOutput.getExit());
    }

    @Override
    public CallbackTime getTime() {
        return new CallbackTime("30 3 * * *");
    }
}

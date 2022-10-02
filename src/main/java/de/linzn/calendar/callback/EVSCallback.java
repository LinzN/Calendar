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
import java.util.concurrent.TimeUnit;

public class EVSCallback extends AbstractCallback {

    @Override
    public void operation() {
        File calendarDirectory = new File(CalendarPlugin.calendarPlugin.getDataFolder(), "calendarFiles");
        if (!calendarDirectory.exists()) {
            calendarDirectory.mkdir();
        }

        String mmseData = CalendarPlugin.calendarPlugin.getDefaultConfig().getString("trash.mmse_data");
        File file = new File(calendarDirectory, "EVSTrash.ics");
        if (file.exists()) {
            file.delete();
        }
        String command = "curl 'https://www.muellmax.de/abfallkalender/evs/res/EvsStart.php' -H 'authority: www.muellmax.de' -H 'cache-control: max-age=0' -H 'origin: https://www.muellmax.de' -H 'upgrade-insecure-requests: 1' -H 'content-type: application/x-www-form-urlencoded' -H 'user-agent: Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Mobile Safari/537.36' -H 'sec-fetch-user: ?1' -H 'accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9' -H 'sec-fetch-site: same-origin' -H 'sec-fetch-mode: nested-navigate' -H 'referer: https://www.muellmax.de/abfallkalender/evs/res/EvsStart.php' -H 'accept-encoding: gzip, deflate, br' -H 'accept-language: de-DE,de;q=0.9,en-US;q=0.8,en;q=0.7' --data 'mm_ses=" + mmseData + "&xxx=1&mm_frm_type=termine&mm_frm_fra_RM=RM&mm_frm_fra_BIO=BIO&mm_frm_fra_GT=GT&mm_ica_gen=iCalendar-Datei+laden' --compressed > " + new File(calendarDirectory, "EVSTrash.ics").getAbsolutePath();
        ShellOperation shellOperation = new ShellOperation();
        shellOperation.setUseSSH(false);
        shellOperation.setScriptCommand(command);
        shellOperation.setUseOutput(false);
        addOperationData(shellOperation);
    }

    @Override
    public void callback(OperationOutput operationOutput) {
        STEMSystemApp.LOGGER.DEBUG("EVS calender pull finish " + operationOutput.getExit());
    }

    @Override
    public CallbackTime getTime() {
        return new CallbackTime(1, 480, TimeUnit.MINUTES);
    }
}

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
import de.stem.stemSystem.taskManagment.operations.AbstractOperation;
import de.stem.stemSystem.taskManagment.operations.OperationOutput;

import java.util.concurrent.TimeUnit;

public class CalendarBuilderCallback extends AbstractCallback {

    @Override
    public void operation() {
        AbstractOperation operation = new AbstractOperation() {
            @Override
            public OperationOutput runOperation() {
                OperationOutput operationOutput = new OperationOutput(this);
                operationOutput.setExit(0);
                return operationOutput;
            }
        };

        addOperationData(operation);
    }

    @Override
    public void callback(OperationOutput operationOutput) {
        STEMSystemApp.LOGGER.DEBUG("Build calendar data  " + operationOutput.getExit());
        CalendarPlugin.calendarPlugin.getCalendarManager().loadCalendar();
    }
    /* todo tempfix time sync problem with other thread*/
    @Override
    public CallbackTime getTime() {
        return new CallbackTime(21, 30, TimeUnit.SECONDS);
    }
}

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
import de.linzn.stem.taskManagment.operations.AbstractOperation;
import de.linzn.stem.taskManagment.operations.OperationOutput;

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
        STEMApp.LOGGER.DEBUG("Build calendar data  " + operationOutput.getExit());
        CalendarPlugin.calendarPlugin.getCalendarManager().loadCalendar();
    }

    /* todo tempfix time sync problem with other thread*/
    @Override
    public CallbackTime getTime() {
        return new CallbackTime(21, 30, TimeUnit.SECONDS);
    }
}

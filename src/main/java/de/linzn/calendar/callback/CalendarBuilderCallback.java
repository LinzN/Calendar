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
import de.linzn.stem.taskManagment.AbstractCallback;
import de.linzn.stem.taskManagment.CallbackTime;
import de.linzn.stem.taskManagment.operations.AbstractOperation;
import de.linzn.stem.taskManagment.operations.OperationOutput;

import java.util.concurrent.TimeUnit;

/**
 * Checks the calendar files every 30 seconds and reloads them when they changed.
 */
public class CalendarBuilderCallback extends AbstractCallback {

    @Override
    public void operation() {
        addOperationData(new AbstractOperation() {
            @Override
            public OperationOutput runOperation() {
                OperationOutput operationOutput = new OperationOutput(this);
                CalendarPlugin.calendarPlugin.getCalendarManager().loadCalendar();
                operationOutput.setExit(0);
                return operationOutput;
            }
        });
    }

    @Override
    public void callback(OperationOutput operationOutput) {
    }

    @Override
    public CallbackTime getTime() {
        return new CallbackTime(21, 30, TimeUnit.SECONDS);
    }
}

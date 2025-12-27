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

package de.linzn.calendar.objects.trashes;

import de.linzn.calendar.objects.ICalendarType;
import de.linzn.calendar.objects.TrashType;

import java.util.Date;

public class BlueTrash implements ICalendarType {

    private Date date;

    public BlueTrash(Date date) {
        this.date = date;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public String getName() {
        return "Blaue Tonne";
    }

    @Override
    public TrashType getType() {
        return TrashType.BLUE;
    }
}

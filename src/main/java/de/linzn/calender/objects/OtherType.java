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

package de.linzn.calender.objects;

import java.util.Date;

public class OtherType implements ICalendarType {

    private Date date;
    private String summary;

    public OtherType(String summary, Date date) {
        this.summary = summary;
        this.date = date;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public String getName() {
        return summary;
    }

    @Override
    public TrashType getType() {
        return TrashType.OTHER;
    }
}

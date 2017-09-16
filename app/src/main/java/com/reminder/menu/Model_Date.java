package com.reminder.menu;

import java.util.Date;

/**
 * Created by admin on 02-02-2017.
 */

public class Model_Date {
    Date date;
    String type;
    int id;

    public Model_Date(int idd, Date dt, String tt)
    {
        this.setId(idd);
        this.setDate(dt);
        this.setType(tt);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

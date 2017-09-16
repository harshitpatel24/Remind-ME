package com.reminder.menu;

/**
 * Created by admin on 02-02-2017.
 */

public class History_Model {

    int id;
    String date;
    String name;

    public History_Model(int idd,String nm,String dd)
    {
        this.setId(idd);
        this.setName(nm);
        this.setDate(dd);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

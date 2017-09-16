package com.reminder.menu;

/**
 * Created by vishvraj on 22-03-2017.
 */

public class QuickReminderModel {

    String  Title,Quick_date,Quick_time,Description;
    int Id;
    long Days;

    public long getDays() {
        return Days;
    }

    public void setDays(long days) {
        Days = days;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getQuickDate(){
        return Quick_date;
    }
    public void setQuick_date(String quick_date){
        Quick_date=quick_date;
    }
    public String getQuickTime(){
        return Quick_time;
    }
    public void setQuick_time(String quick_time){
        Quick_time=quick_time;
    }
    public String getDescription(){return Description;}
    public void setDescription(String description){Description=description;}

    public QuickReminderModel(int id,String title,String date,String time,String description,long day){
        this.setId(id);
        this.setTitle(title);
        this.setQuick_date(date);
        this.setQuick_time(time);
        this.setDescription(description);
        this.setDays(day);
    }
}

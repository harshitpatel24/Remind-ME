package com.reminder.menu;

/**
 * Created by admin on 25-03-2017.
 */

public class Model_Holder {

    String Holder;
    int Age,Id;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
    public String getHolder() {
        return Holder;
    }

    public void setHolder(String holder) {
        Holder = holder;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public Model_Holder(int id, String holder, int age) {
        Holder = holder;
        Age = age;
        Id=id;
    }
}

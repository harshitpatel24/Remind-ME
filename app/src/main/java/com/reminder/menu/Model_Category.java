package com.reminder.menu;



/**
 * Created by Hitarth on 24-03-2017.
 */

public class Model_Category {
    String category_name;
    int category_img;

    public Model_Category(String category_name, int category_img) {
        this.setCategory_name(category_name);
        this.setCategory_img(category_img);
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getCategory_img() {
        return category_img;
    }

    public void setCategory_img(int category_img) {
        this.category_img = category_img;
    }
    public String getName(){
    return category_name;
    }
    public int getImage(){
        return  category_img;
    }

}

package com.example.happyhour.objects;

import java.util.Date;

public class Order {
    private Date date;
    private MyTime myTime;
    private String order_id;
    private String user_id;
    private String user_name;
    private String user_img;
    private String barId;

    public Order() {
    }

    public Date getDate() {
        return date;
    }

    public Order setDate(Date date) {
        this.date = date;
        return this;
    }

    public String getOrder_id() {
        return order_id;
    }

    public Order setOrder_id(String order_id) {
        this.order_id = order_id;
        return this;
    }

    public MyTime getMyTime() {
        return myTime;
    }

    public Order setMyTime(MyTime myTime) {
        this.myTime = myTime;
        return this;
    }

    public String getBarId() {
        return barId;
    }

    public Order setBarId(String barId) {
        this.barId = barId;
        return this;
    }

    public String getUser_id() {
        return user_id;
    }

    public Order setUser_id(String user_id) {
        this.user_id = user_id;
        return this;
    }

    public String getUser_name() {
        return user_name;
    }

    public Order setUser_name(String user_name) {
        this.user_name = user_name;
        return this;
    }

    public String getUser_img() {
        return user_img;
    }

    public Order setUser_img(String user_img) {
        this.user_img = user_img;
        return this;
    }
}

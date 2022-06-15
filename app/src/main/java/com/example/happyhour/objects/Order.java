package com.example.happyhour.objects;

import java.util.Date;

public class Order {
    private Date date;
    private String order_id;

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
}

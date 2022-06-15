package com.example.happyhour.objects;

import java.util.ArrayList;

public class Table {
    private String id;
    private int numOfPlaces;
    private ArrayList<Order> orders;

    public Table() {
    }

    public String getId() {
        return id;
    }

    public Table setId(String id) {
        this.id = id;
        return this;
    }

    public int getNumOfPlaces() {
        return numOfPlaces;
    }

    public Table setNumOfPlaces(int numOfPlaces) {
        this.numOfPlaces = numOfPlaces;
        return this;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public Table setOrders(ArrayList<Order> orders) {
        this.orders = orders;
        return this;
    }
}

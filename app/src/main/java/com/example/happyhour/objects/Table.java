package com.example.happyhour.objects;

import java.util.HashMap;

public class Table {
    private String id;
    private String name;
    private String description;
    private int numOfPlaces;
    private HashMap<String,Order> orders = new HashMap<String,Order>();

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

    public HashMap<String,Order> getOrders() {
        return orders;
    }

    public Table setOrders(HashMap<String,Order> orders) {
        this.orders = orders;
        return this;
    }
    public Table addOrder(String id ,Order order) {
        this.orders.put(id , order);
        return this;
    }

    public String getName() {
        return name;
    }

    public Table setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Table setDescription(String description) {
        this.description = description;
        return this;
    }

}

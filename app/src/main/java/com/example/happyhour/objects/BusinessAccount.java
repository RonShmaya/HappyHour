package com.example.happyhour.objects;

import java.util.HashMap;

public class BusinessAccount extends Account{
    private HashMap<String, Bar> myBars = new HashMap<>();

    public BusinessAccount() {
        super();
    }
    public HashMap<String, Bar> getMyBars() {
        return myBars;
    }

    public BusinessAccount setMyBars(HashMap<String, Bar> myBars) {
        this.myBars = myBars;
        return this;
    }
    public BusinessAccount addBar(Bar bar, String id) {
        this.myBars.put(id,bar);
        return this;
    }

    @Override
    public String toString() {
        return "BusinessAccount{" +
                "id='" + id + '\'' +
                ", myBars=" + myBars +
                '}';
    }
}

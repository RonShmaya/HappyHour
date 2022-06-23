package com.example.happyhour.objects;

import java.util.HashMap;

public class PrivateAccount extends Account {
    private String name;
    private HashMap<String , String> follow_bars = new HashMap<>();

    public PrivateAccount() {
        super();
    }

    public HashMap<String, String> getFollow_bars() {
        return follow_bars;
    }

    public PrivateAccount setFollow_bars(HashMap<String, String> follow_bars) {
        this.follow_bars = follow_bars;
        return this;
    }

    public String getName() {
        return name;
    }

    public PrivateAccount setName(String name) {
        this.name = name;
        return this;
    }
}

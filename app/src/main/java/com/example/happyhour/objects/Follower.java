package com.example.happyhour.objects;

public class Follower {
    private String name;
    //Todo image

    public Follower() {
    }

    public Follower(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Follower setName(String name) {
        this.name = name;
        return this;
    }
}

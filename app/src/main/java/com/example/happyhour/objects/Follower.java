package com.example.happyhour.objects;

public class Follower {
    private String name;
    private String img;


    public Follower() {
    }

    public Follower(String name, String img) {
        this.name = name;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public Follower setName(String name) {
        this.name = name;
        return this;
    }

    public String getImg() {
        return img;
    }

    public Follower setImg(String img) {
        this.img = img;
        return this;
    }
}

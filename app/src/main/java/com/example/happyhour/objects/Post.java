package com.example.happyhour.objects;

public class Post {
    public String img;
    public Post() {
    }

    public Post(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public Post setImg(String img) {
        this.img = img;
        return this;
    }
}

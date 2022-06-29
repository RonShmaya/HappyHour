package com.example.happyhour.objects;

import java.util.HashMap;

public class Post {
    private String img;
    private String bar_id;
    private String post_id;
    private HashMap<String , String> likes = new HashMap<>();

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

    public String getBar_id() {
        return bar_id;
    }

    public Post setBar_id(String bar_id) {
        this.bar_id = bar_id;
        return this;
    }

    public String getPost_id() {
        return post_id;
    }

    public Post setPost_id(String post_id) {
        this.post_id = post_id;
        return this;
    }

    public HashMap<String, String> getLikes() {
        return likes;
    }
    public  void addLikes(String user_id , String user_name) {
        likes.put(user_id , user_name);
    }
    public  void removeLikes(String user_id) {
        likes.remove(user_id);
    }


    public Post setLikes(HashMap<String, String> likes) {
        this.likes = likes;
        return this;
    }
}

package com.example.happyhour.objects;

import java.util.ArrayList;

public class Bar {
    private String name = "";
    private String description = "";
    private ArrayList<Review> reviews = new ArrayList<>();
    private eBarType barType;
    private ArrayList<eMusicType> musicTypes = new ArrayList<>();
    private ArrayList<String> followers = new ArrayList<>();
    private ArrayList<Table> tables = new ArrayList<>();
    private float sum_ranks = 0;

    //private String menu; // TODO image storage database?
    //private String location; // TODO change it?
   // private String main_image; //TODO image storage database?


    public Bar() {
    }

    public String getName() {
        return name;
    }

    public Bar setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Bar setDescription(String description) {
        this.description = description;
        return this;
    }
    public float getStarsAvg(){
        int size = this.reviews.size();
        if(size != 0 )
            return sum_ranks/(float) size;
        return 0;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public Bar setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
        return this;
    }

    public eBarType getBarType() {
        return barType;
    }

    public Bar setBarType(eBarType barType) {
        this.barType = barType;
        return this;
    }

    public ArrayList<eMusicType> getMusicTypes() {
        return musicTypes;
    }

    public Bar setMusicTypes(ArrayList<eMusicType> musicTypes) {
        this.musicTypes = musicTypes;
        return this;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public Bar setFollowers(ArrayList<String> followers) {
        this.followers = followers;
        return this;
    }

    public ArrayList<Table> getTables() {
        return tables;
    }

    public Bar setTables(ArrayList<Table> tables) {
        this.tables = tables;
        return this;
    }

    public float getSum_ranks() {
        return sum_ranks;
    }

    public Bar setSum_ranks(float sum_ranks) {
        this.sum_ranks = sum_ranks;
        return this;
    }
}

package com.example.happyhour.objects;

import java.util.Date;

public class Review {
    private String reviewer_id;
    private String description;
    private float stars_rating;
    private Date data;

    public Review() {

    }

    public Review(String reviewer_id, String description, float stars_rating) {
        this.reviewer_id = reviewer_id;
        this.description = description;
        this.stars_rating = stars_rating;
    }

    public String getReviewer_id() {
        return reviewer_id;
    }

    public Review setReviewer_id(String reviewer_id) {
        this.reviewer_id = reviewer_id;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Review setDescription(String description) {
        this.description = description;
        return this;
    }

    public float getStars_rating() {
        return stars_rating;
    }

    public Review setStars_rating(float stars_rating) {
        this.stars_rating = stars_rating;
        return this;
    }

    public Date getData() {
        return data;
    }

    public Review setData(Date data) {
        this.data = data;
        return this;
    }
}

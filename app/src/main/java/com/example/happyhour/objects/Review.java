package com.example.happyhour.objects;

public class Review {
    private String reviewer_name;
    private String id;
    private String description;
    private float stars_rating;

    public Review() {

    }

    public Review(String reviewer_id, String description, float stars_rating) {
        this.reviewer_name = reviewer_id;
        this.description = description;
        this.stars_rating = stars_rating;
    }

    public String getReviewer_name() {
        return reviewer_name;
    }

    public Review setReviewer_name(String reviewer_name) {
        this.reviewer_name = reviewer_name;
        return this;
    }

    public String getId() {
        return id;
    }

    public Review setId(String id) {
        this.id = id;
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

}

package com.example.happyhour.objects;

import java.util.HashMap;

public class PrivateAccount extends Account {
    private String name;
    private eBarType favorite_1;
    private eBarType favorite_2;
    private String imgUri = "";
    private AddressMaps addressMaps = new AddressMaps();
    private HashMap<String , String> follow_bars = new HashMap<>();
    private HashMap<String , Order> orders = new HashMap<>();

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

    public HashMap<String, Order> getOrders() {
        return orders;
    }

    public PrivateAccount setOrders(HashMap<String, Order> orders) {
        this.orders = orders;
        return this;
    }

    public eBarType getFavorite_1() {
        return favorite_1;
    }

    public PrivateAccount setFavorite_1(eBarType favorite_1) {
        this.favorite_1 = favorite_1;
        return this;
    }

    public eBarType getFavorite_2() {
        return favorite_2;
    }

    public PrivateAccount setFavorite_2(eBarType favorite_2) {
        this.favorite_2 = favorite_2;
        return this;
    }

    public String getImgUri() {
        return imgUri;
    }

    public PrivateAccount setImgUri(String imgUri) {
        this.imgUri = imgUri;
        return this;
    }

    public AddressMaps getAddressMaps() {
        return addressMaps;
    }

    public PrivateAccount setAddressMaps(AddressMaps addressMaps) {
        this.addressMaps = addressMaps;
        return this;
    }
}

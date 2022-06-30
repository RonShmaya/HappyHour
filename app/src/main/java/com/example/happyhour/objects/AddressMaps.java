package com.example.happyhour.objects;

public class AddressMaps {
    private String city="";
    private String street="";
    private String street_num="";
    private String country = "israel";

    public AddressMaps() {
    }

    public AddressMaps(String city, String street, String street_num) {
        this.city = city;
        this.street = street;
        this.street_num = street_num;
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public AddressMaps setCity(String city) {
        this.city = city;
        return this;
    }

    public String getStreet() {
        return street;
    }

    public AddressMaps setStreet(String street) {
        this.street = street;
        return this;
    }

    public String getStreet_num() {
        return street_num;
    }

    public AddressMaps setStreet_num(String street_num) {
        this.street_num = street_num;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public AddressMaps setCountry(String country) {
        this.country = country;
        return this;
    }

    @Override
    public String toString() {
        return street_num+" ,"+street+" ,"+city+" ,"+country;
    }
}

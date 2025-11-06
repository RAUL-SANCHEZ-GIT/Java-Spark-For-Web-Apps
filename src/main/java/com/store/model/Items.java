package com.store.model;

import java.text.NumberFormat;
import java.util.Locale;

public class Items {
    private String id;
    private String name;
    private String description;
    private double price;
    private String image_url; // <-- 1. ADD THIS FIELD

    // Default constructor (needed for some libraries, good practice)
    public Items() {}

    // Constructor for our service to use
    // UPDATED: price is now a double, and image_url is added
    // v-- 2. THIS IS THE CONSTRUCTOR THAT FIXES THE ERROR
    public Items(String id, String name, String description, double price, String image_url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image_url = image_url; // <-- 3. ADD THIS LINE
    }

    // --- Getters ---
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public double getPrice() {
        return price;
    }
    public String getImage_url() { // <-- 4. ADD THIS GETTER
        return image_url;
    }

    // NEW: Helper method to format the price for the frontend
    public String getFormattedPrice() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US"));
        return currencyFormat.format(this.price);
    }

    // --- Setters ---
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setImage_url(String image_url) { // <-- 5. ADD THIS SETTER
        this.image_url = image_url;
    }
}
package com.store.model;

import java.text.NumberFormat;
import java.util.Locale;

public class Items {
    private String id;
    private String name;
    private String description;
    // UPDATED: Changed from String to double for database and calculations
    private double price;

    // Default constructor (needed for some libraries, good practice)
    public Items() {}

    // Constructor for our service to use
    // UPDATED: price is now a double
    public Items(String id, String name, String description, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
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

    // UPDATED: Getter for double
    public double getPrice() {
        return price;
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

    // UPDATED: Setter for double
    public void setPrice(double price) {
        this.price = price;
    }
}

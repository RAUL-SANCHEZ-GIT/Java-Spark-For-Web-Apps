package com.store.model;
public class User {
    private String id;
    private String name;
    private String description;
    private String price;

    // A constructor for our service to use
    public User(String id, String username, String email,String price) {
        this.id = id;
        this.name = username;
        this.description = email;
        this.price = price;
    }

    // --- Getters ---
    public String getId() {
        return id;
    }

    public String getUsername() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    public String getPrice() {
        return price;
    }


    // --- Setters ---
    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.name = username;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
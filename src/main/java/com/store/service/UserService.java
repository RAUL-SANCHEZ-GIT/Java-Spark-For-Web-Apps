package com.store.service;

import com.store.model.User; // Your updated User (Item) model

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserService {

    // Our in-memory database
    private Map<String, User> userDatabase = new HashMap<>();

    public Collection<User> getAllUsers() {
        return userDatabase.values();
    }

    public User getUserById(String id) {
        return userDatabase.get(id);
    }

    /**
     * Creates a new user/item with a specific ID.
     */
    public User createUser(String id, User inputUser) {
        if (userDatabase.containsKey(id)) {
            // Return null if ID already exists
            return null;
        }

        // Use the getters from your new User class
        String name = inputUser.getUsername(); // Gets the 'name' field
        String description = inputUser.getDescription(); // Gets the 'description' field
        String price = inputUser.getPrice(); // Gets the 'price' field

        // Create the new User object
        User newUser = new User(id, name, description, price);
        userDatabase.put(id, newUser);
        return newUser;
    }

    /**
     * Updates an existing user/item.
     * Returns the updated user, or null if not found.
     */
    public User updateUser(String id, User inputUser) {
        User existingUser = userDatabase.get(id);

        if (existingUser != null) {
            // Use the setters from your new User class
            existingUser.setUsername(inputUser.getUsername()); // Sets the 'name' field
            existingUser.setDescription(inputUser.getDescription()); // Sets the 'description' field
            existingUser.setPrice(inputUser.getPrice()); // Sets the 'price' field

            userDatabase.put(id, existingUser);
            return existingUser;
        }
        // Not found
        return null;
    }

    /**
     * Deletes a user/item.
     * Returns the user that was removed, or null if not found.
     */
    public User deleteUser(String id) {
        return userDatabase.remove(id);
    }

    /**
     * Checks if a user/item exists.
     */
    public boolean userExists(String id) {
        return userDatabase.containsKey(id);
    }
}
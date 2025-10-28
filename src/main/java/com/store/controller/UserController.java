package com.store.controller;

import com.google.gson.Gson;
import com.store.model.User;
import com.store.service.UserService;
import spark.Request;
import spark.Response;

// This file works as-is!
public class UserController {

    private UserService userService;
    private Gson gson = new Gson();

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public String getAllUsers(Request req, Response res) {
        res.type("application/json");
        return gson.toJson(userService.getAllUsers());
    }

    public String getUserById(Request req, Response res) {
        // ... (no change)
        res.type("application/json");
        String id = req.params(":id");
        User user = userService.getUserById(id);

        if (user != null) {
            return gson.toJson(user);
        } else {
            res.status(404); // Not Found
            return gson.toJson("User not found");
        }
    }

    public String createUser(Request req, Response res) {
        // ... (no change)
        res.type("application/json");
        String id = req.params(":id");

        try {
            User inputUser = gson.fromJson(req.body(), User.class); // Gson handles the new fields
            User createdUser = userService.createUser(id, inputUser);

            if (createdUser == null) {
                res.status(409); // Conflict (ID already exists)
                return gson.toJson("User with this ID already exists");
            }

            res.status(201); // Created
            return gson.toJson(createdUser);

        } catch (Exception e) {
            res.status(400); // Bad Request
            return gson.toJson("Invalid user data: " + e.getMessage());
        }
    }

    public String updateUser(Request req, Response res) {
        // ... (no change)
        res.type("application/json");
        String id = req.params(":id");

        try {
            User inputUser = gson.fromJson(req.body(), User.class); // Gson handles the new fields
            User updatedUser = userService.updateUser(id, inputUser);

            if (updatedUser != null) {
                return gson.toJson(updatedUser);
            } else {
                res.status(404); // Not Found
                return gson.toJson("User not found");
            }
        } catch (Exception e) {
            res.status(400); // Bad Request
            return gson.toJson("Invalid user data: " + e.getMessage());
        }
    }

    public String deleteUser(Request req, Response res) {
        // ... (no change)
        res.type("application/json");
        String id = req.params(":id");
        User removedUser = userService.deleteUser(id);

        if (removedUser != null) {
            return gson.toJson("User deleted");
        } else {
            res.status(404); // Not Found
            return gson.toJson("User not found");
        }
    }

    public String checkUser(Request req, Response res) {
        // ... (no change)
        String id = req.params(":id");
        if (userService.userExists(id)) {
            res.status(200); // OK
            return "User exists";
        } else {
            res.status(404); // Not Found
            return "User not found";
        }
    }
}
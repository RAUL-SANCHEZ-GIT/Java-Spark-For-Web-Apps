package com.store;

// Import your other classes
import com.store.controller.UserController;
import com.store.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        // 1. Initialize the service (data layer)
        UserService userService = new UserService();

        // 2. Initialize the controller (http layer) and inject the service
        UserController userController = new UserController(userService);

        // 3. Configure Spark
        port(4567);
        logger.info("API Service starting on port 4567...");

        // 4. Define all routes and map them to controller methods

        // GET /users — Retrieve the list of all users
        get("/users", userController::getAllUsers);

        // GET /users/:id — Retrieve a user by the given ID
        get("/users/:id", userController::getUserById);

        // POST /users/:id — Add a user
        post("/users/:id", userController::createUser);

        // PUT /users/:id — Edit a specific user
        put("/users/:id", userController::updateUser);

        // OPTIONS /users/:id — Check whether a user with the given ID exists
        options("/users/:id", userController::checkUser);

        // DELETE /users/:id — Delete a specific user
        delete("/users/:id", userController::deleteUser);


        // --- Filters & Exception Handling ---

        // Log all requests
        before((req, res) -> {
            logger.info("{} request received for: {}", req.requestMethod(), req.pathInfo());
        });

        // Set all responses to application/json by default
        after((req, res) -> {
            res.type("application/json");
        });

        // Handle exceptions
        exception(Exception.class, (exception, req, res) -> {
            logger.error("Internal Server Error: ", exception);
            res.status(500);
            res.body("{\"error\": \"An internal server error occurred\"}");
        });
    }
}
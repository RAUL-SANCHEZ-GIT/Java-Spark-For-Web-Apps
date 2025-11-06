package com.store.controller;

import com.google.gson.Gson;
import com.store.model.Items;
import com.store.service.ItemsService;
import spark.Request;
import spark.Response;

public class ItemsController {

    private ItemsService itemsService;
    private Gson gson = new Gson();

    public ItemsController(ItemsService itemsService) {
        this.itemsService = itemsService;
    }

    public String getAllItems(Request req, Response res) {
        res.type("application/json");

        // 1. Get the optional "query" parameter from the request URL
        String query = req.queryParams("query");

        // 2. Pass the query (which can be null) to the new service method
        return gson.toJson(itemsService.getItems(query));
    }

    public String getItemById(Request req, Response res) {
        res.type("application/json");
        String id = req.params(":id");
        Items item = itemsService.getItemById(id);

        if (item != null) {
            return gson.toJson(item);
        } else {
            res.status(404); // Not Found
            return gson.toJson("Item not found");
        }
    }

    /**
     * FIXED: This method now matches the new ItemsService.createItem(Items item)
     */
    public String createItem(Request req, Response res) {
        res.type("application/json");
        String id = req.params(":id");

        try {
            Items inputItems = gson.fromJson(req.body(), Items.class);
            // Manually set the ID from the URL parameter into the object
            inputItems.setId(id);

            // Call the new service method which only takes one argument
            Items createdItem = itemsService.createItem(inputItems);

            if (createdItem == null) {
                res.status(409); // Conflict (ID already exists or DB error)
                return gson.toJson("Item with this ID already exists or invalid data");
            }

            res.status(201); // Created
            return gson.toJson(createdItem);

        } catch (Exception e) {
            res.status(400); // Bad Request
            return gson.toJson("Invalid item data: " + e.getMessage());
        }
    }

    /**
     * FIXED: This method now matches the new ItemsService.updateItem(String id, Items item)
     */
    public String updateItem(Request req, Response res) {
        res.type("application/json");
        String id = req.params(":id");

        try {
            Items inputItems = gson.fromJson(req.body(), Items.class);
            // The service updateItem still takes (id, item), so this was already correct!
            Items updatedItem = itemsService.updateItem(id, inputItems);

            if (updatedItem != null) {
                return gson.toJson(updatedItem);
            } else {
                res.status(404); // Not Found
                return gson.toJson("Item not found or update failed");
            }
        } catch (Exception e) {
            res.status(400); // Bad Request
            return gson.toJson("Invalid item data: " + e.getMessage());
        }
    }

    /**
     * FIXED: This method now handles the boolean return from ItemsService.deleteItem(String id)
     */
    public String deleteItem(Request req, Response res) {
        res.type("application/json");
        String id = req.params(":id");

        // The service now returns true or false
        boolean deleted = itemsService.deleteItem(id);

        if (deleted) {
            return gson.toJson("Item deleted");
        } else {
            res.status(404); // Not Found
            return gson.toJson("Item not found");
        }
    }

    /**
     * This method was already correct as it uses itemExists()
     */
    public String checkItem(Request req, Response res) {
        String id = req.params(":id");
        if (itemsService.itemExists(id)) {
            res.status(200); // OK
            return "Item exists";
        } else {
            res.status(404); // Not Found
            return "Item not found";
        }
    }

    // Example: Add this to ItemsController.java to test
    public String updatePrice(Request req, Response res) {
        String id = req.params(":id");
        String price = req.queryParams("price"); // e.g., "899.99"

        PriceWebSocketHandler.broadcastPriceUpdate(id, price);

        res.type("application/json");
        return gson.toJson("Price update broadcasted for " + id);
    }

}

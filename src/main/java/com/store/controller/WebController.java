package com.store.controller;

import com.store.model.Items;
import com.store.service.ItemsService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebController {

    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

    private ItemsService itemsService;

    public WebController(ItemsService itemsService) {
        this.itemsService = itemsService;
    }

    /**
     * Gets the user's cart from the session. If one doesn't exist, creates it.
     */
    private List<String> getCart(Request req) {
        Session session = req.session(true); // Get or create session
        List<String> cart = session.attribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.attribute("cart", cart);
        }
        return cart;
    }

    /**
     * Renders the main shop page.
     */
    /**
     * Renders the main shop page.
     */
    public String renderShop(Request req, Response res) {
        // 1. Get query param
        String query = req.queryParams("query");

        Map<String, Object> model = new HashMap<>();
        // 2. Use new service method
        model.put("items", itemsService.getItems(query));
        // 3. Pass query back to template
        model.put("query", query);

        // Renders templates/index.mustache inside templates/layout.mustache
        return new MustacheTemplateEngine().render(
                new ModelAndView(model, "index.mustache")
        );
    }

    /**
     * Renders the shopping cart page.
     */
    public String renderCart(Request req, Response res) {
        List<String> cartIds = getCart(req);
        List<Items> cartItems = new ArrayList<>();
        double total = 0.0;

        for (String id : cartIds) {
            Items item = itemsService.getItemById(id);
            if (item != null) {
                cartItems.add(item);
                total += item.getPrice();
            }
        }

        Map<String, Object> model = new HashMap<>();
        model.put("cartItems", cartItems);
        model.put("cartTotal", String.format("%.2f", total));
        model.put("cartCount", cartItems.size());

        // Renders templates/cart.mustache inside templates/layout.mustache
        return new MustacheTemplateEngine().render(
                new ModelAndView(model, "cart.mustache")
        );
    }

    /**
     * Handles adding an item to the cart (from a POST form).
     */
    public String addToCart(Request req, Response res) {
        String id = req.params(":id");
        if (itemsService.itemExists(id)) {
            getCart(req).add(id);
        }
        res.redirect("/"); // Redirect back to the shop page
        return "";
    }

    /**
     * Handles removing an item from the cart (from a POST form).
     */
    public String removeFromCart(Request req, Response res) {
        String idToRemove = req.params(":id");
        List<String> cart = getCart(req);

        // Remove only the first instance of this item
        cart.remove(idToRemove);

        res.redirect("/cart"); // Redirect back to the cart page
        return "";
    }

    /**
     * Handles placing a bid on an item (from a POST form).
     */
    public String placeBid(Request req, Response res) {
        String id = req.params(":id");
        double bidAmount = 0;

        try {
            // Get the "bidAmount" from the form
            String bidAmountStr = req.queryParams("bidAmount");
            bidAmount = Double.parseDouble(bidAmountStr);
        } catch (NumberFormatException e) {
            // Handle invalid bid amount
            logger.warn("Invalid bid amount received for item: {}", id);
            res.redirect("/");
            return "";
        }

        // Try to place the bid using our new service method
        boolean bidSuccess = itemsService.placeBid(id, bidAmount);

        if (bidSuccess) {
            // SUCCESS! Broadcast the update to all connected users.
            // We can re-use your existing WebSocket handler.
            // We'll format the price as a simple string.
            String newPriceStr = String.format("%.2f", bidAmount);
            PriceWebSocketHandler.broadcastPriceUpdate(id, newPriceStr);
        } else {
            // Bid failed (probably not high enough)
            // We could add feedback here, but for now just redirect.
        }

        res.redirect("/"); // Redirect back to the shop page
        return "";
    }

}

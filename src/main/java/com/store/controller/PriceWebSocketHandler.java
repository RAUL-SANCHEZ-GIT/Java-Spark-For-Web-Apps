package com.store.controller; // Or a new 'websocket' package

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import com.google.gson.Gson; // You mentioned you have GSON

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class PriceWebSocketHandler {

    // This map stores all connected user sessions
    private static final Map<Session, Session> sessions = new ConcurrentHashMap<>();
    private static final Gson gson = new Gson();

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        System.out.println("WebSocket connected: " + user.getRemoteAddress());
        sessions.put(user, user); // Add new session to the map
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        System.out.println("WebSocket closed: " + user.getRemoteAddress());
        sessions.remove(user); // Remove session from the map
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        // We don't need to do anything when a user sends a message,
        // but the method is required.
        System.out.println("Received message: " + message);
    }

    /**
     * This is the broadcast method.
     * Call this from anywhere in your server (e.g., an admin panel) to update all users.
     * @param itemId The ID of the item (e.g., "item1")
     * @param newPrice The new price as a string (e.g., "750.00")
     */
    public static void broadcastPriceUpdate(String itemId, String newPrice) {
        // We create a simple Map to send as JSON
        Map<String, String> priceUpdate = Map.of(
                "itemId", itemId,
                "newPrice", newPrice
        );
        String jsonMessage = gson.toJson(priceUpdate);

        // Iterate over all connected sessions and send them the update
        sessions.keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(jsonMessage);
            } catch (IOException e) {
                System.err.println("Error broadcasting price update: " + e.getMessage());
            }
        });
    }
}
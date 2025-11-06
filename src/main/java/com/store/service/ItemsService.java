package com.store.service;

import com.store.model.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemsService {

    private static final Logger logger = LoggerFactory.getLogger(ItemsService.class);

    // UPDATED: Replaced HashMap with a DataSource for database connection pooling
    private DataSource dataSource;

    public ItemsService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Re-usable helper method to map a ResultSet row to an Items object
    private Items mapRowToItem(ResultSet rs) throws SQLException {
        return new Items(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getString("image_url")
        );
    }

    /**
     * UPDATED: Now queries the database
     */
    public Collection<Items> getItems(String query) {
        List<Items> items = new ArrayList<>();
        // Start with base SQL
        String sql = "SELECT * FROM items";
        boolean hasQuery = query != null && !query.trim().isEmpty();

        // Add filter logic if query exists
        if (hasQuery) {
            sql += " WHERE name LIKE ? OR description LIKE ?";
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set query parameters if they exist
            if (hasQuery) {
                String likeQuery = "%" + query + "%";
                ps.setString(1, likeQuery);
                ps.setString(2, likeQuery);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(mapRowToItem(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching items with query: " + query, e);
        }
        return items;
    }

    /**
     * UPDATED: Now queries the database
     */
    public Items getItemById(String id) {
        String sql = "SELECT * FROM items WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToItem(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching item by id: " + id, e);
        }
        return null; // Not found
    }

    /**
     * UPDATED: Now inserts into the database
     */
    public Items createItem(Items item) {
        // FIXED: Added image_url to the query
        String sql = "INSERT INTO items (id, name, description, price, image_url) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, item.getId());
            ps.setString(2, item.getName());
            ps.setString(3, item.getDescription());
            ps.setDouble(4, item.getPrice());
            ps.setString(5, item.getImage_url()); // This is now correct

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return item;
            }
        } catch (SQLException e) {
            logger.error("Error creating item", e);
        }
        return null; // Failed to create
    }

    /**
     * UPDATED: Now updates the database
     */
    public Items updateItem(String id, Items item) {
        // FIXED: Added image_url = ? to the query
        String sql = "UPDATE items SET name = ?, description = ?, price = ?, image_url = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, item.getName());
            ps.setString(2, item.getDescription());
            ps.setDouble(3, item.getPrice());
            ps.setString(4, item.getImage_url()); // This is now parameter 4
            ps.setString(5, id); // This is now parameter 5

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                item.setId(id);
                return item;
            }
        } catch (SQLException e) {
            logger.error("Error updating item: " + id, e);
        }
        return null; // Not found or failed
    }

    /**
     * UPDATED: Now deletes from the database
     */
    public boolean deleteItem(String id) {
        String sql = "DELETE FROM items WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // True if deleted, false if not found
        } catch (SQLException e) {
            logger.error("Error deleting item: " + id, e);
        }
        return false;
    }

    /**
     * UPDATED: Now checks the database
     */
    public boolean itemExists(String id) {
        return getItemById(id) != null;
    }
    /**
     * Attempts to place a new bid on an item.
     *
     * @param id           The ID of the item to bid on.
     * @param newBidAmount The amount of the new bid.
     * @return true if the bid was successful, false otherwise (e.g., bid was not high enough).
     */
    public boolean placeBid(String id, double newBidAmount) {
        Items item = getItemById(id);
        if (item == null) {
            logger.warn("Bid placed on non-existent item: {}", id);
            return false;
        }

        // Check if the new bid is higher than the current price (current bid)
        if (newBidAmount > item.getPrice()) {
            // Update the item's price to the new bid amount
            item.setPrice(newBidAmount);

            // Use our existing updateItem method to save the new price to the DB
            Items updatedItem = updateItem(id, item);
            return updatedItem != null;
        }

        // Bid was not high enough
        return false;
    }
}

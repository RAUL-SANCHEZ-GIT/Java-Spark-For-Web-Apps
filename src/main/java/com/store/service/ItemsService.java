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
                rs.getDouble("price")
        );
    }

    /**
     * UPDATED: Now queries the database
     */
    public Collection<Items> getAllItems() {
        List<Items> items = new ArrayList<>();
        String sql = "SELECT * FROM items";

        // Use try-with-resources to auto-close connections
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                items.add(mapRowToItem(rs));
            }
        } catch (SQLException e) {
            logger.error("Error fetching all items", e);
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
        // Use the item's ID if provided, otherwise the DB might have an auto-increment
        // Our current design requires an ID from the client.
        String sql = "INSERT INTO items (id, name, description, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, item.getId());
            ps.setString(2, item.getName());
            ps.setString(3, item.getDescription());
            ps.setDouble(4, item.getPrice());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return item;
            }
        } catch (SQLException e) {
            // Log a 23505 (unique constraint violation) differently?
            logger.error("Error creating item", e);
        }
        return null; // Failed to create
    }

    /**
     * UPDATED: Now updates the database
     */
    public Items updateItem(String id, Items item) {
        String sql = "UPDATE items SET name = ?, description = ?, price = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, item.getName());
            ps.setString(2, item.getDescription());
            ps.setDouble(3, item.getPrice());
            ps.setString(4, id); // WHERE clause

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                item.setId(id); // Ensure the ID is set on the returned object
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
}

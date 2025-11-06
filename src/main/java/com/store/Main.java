package com.store;

import com.store.controller.ItemsController;
import com.store.controller.PriceWebSocketHandler;
import com.store.controller.WebController;
import com.store.service.ItemsService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

import static spark.Spark.*;


public class Main {


    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * NEW: Creates and configures the database connection pool.
     */


    private static DataSource createDataSource() {
        // TODO: Move credentials to environment variables
        String dbUrl = "jdbc:mysql://localhost:3306/store_db";
        String user = "root"; // <-- TODO: Change this to your MySQL username
        String password = "HandsomeJack15$$."; // <-- TODO: Change this to your MySQL password

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUrl);
        config.setUsername(user);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        logger.info("Initializing database connection pool...");
        return new HikariDataSource(config);
    }

    public static void main(String[] args) {

        webSocket("/price-updates", PriceWebSocketHandler.class);
        // 1. Initialize Database
        DataSource dataSource = createDataSource();

        // 2. Initialize the service (data layer) with the database
        ItemsService itemsService = new ItemsService(dataSource);

        // 3. Initialize the controllers and inject the service
        ItemsController apiController = new ItemsController(itemsService);
        WebController webController = new WebController(itemsService);

        // 4. Configure Spark
        port(4567);
        // NEW: Tell Spark where to find static files (CSS, JS, images)
        staticFiles.location("/public");
        logger.info("API and Web Service starting on port 4567...");

        // 5. Define all WEB (HTML) routes
        get("/", webController::renderShop);
        post("/bid/:id", webController::placeBid);
        // 6. Define all API (JSON) routes (prefixed with /api)
        // Note: We keep the old JSON API, but move it to /api
        path("/api", () -> {
            get("/items", apiController::getAllItems);
            get("/items/:id", apiController::getItemById);
            post("/items", apiController::createItem); // Use createItem
            put("/items/:id", apiController::updateItem); // Use updateItem
            delete("/items/:id", apiController::deleteItem);
            options("/items/:id", apiController::checkItem);
        });


        // --- Filters & Exception Handling ---
        before((req, res) -> {
            logger.info("{} request received for: {}", req.requestMethod(), req.pathInfo());
        });

        // Set API responses to application/json
        after("/api/*", (req, res) -> {
            res.type("application/json");
        });

        // Handle exceptions
        exception(Exception.class, (exception, req, res) -> {
            logger.error("Internal Server Error: ", exception);
            res.status(500);
            // Don't send JSON errors to HTML web routes
            if (!req.pathInfo().startsWith("/api")) {
                res.body("<html><body><h1>500 Internal Server Error</h1><p>An error occurred.</p></body></html>");
            } else {
                res.body("{\"error\": \"An internal server error occurred\"}");
            }
        });
    }
}

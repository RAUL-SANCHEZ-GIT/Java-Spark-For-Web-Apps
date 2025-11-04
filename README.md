# 🛍️ Online Shopping Cart (Java-Spark-Mustache)

![Java](https://img.shields.io/badge/Java-11%2B-ED8B00?style=for-the-badge&logo=openjdk)
![SparkJava](https://img.shields.io/badge/SparkJava-2.9.4-00B4F0?style=for-the-badge)
![WebSocket](https://img.shields.io/badge/WebSocket-Real--Time-blue?style=for-the-badge&logo=websocket)
![Mustache](https://img.shields.io/badge/Mustache.js-C1461C?style=for-the-badge&logo=mustache)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.8%2B-C71A36?style=for-the-badge&logo=apache-maven)
![Gson](https://img.shields.io/badge/Gson-2.10-4285F4?style=for-the-badge)

A full-stack web application for an online store, built with Java. This project uses the **SparkJava** framework for the backend RESTful API, **MySQL** for data persistence, and **Mustache** templates for dynamic server-side rendering of the storefront and cart.

---

## ✨ Features

* **Full-Stack MVC:** Organized in a clean Model-View-Controller pattern (`model`, `dao`, `service`, `controller`).
* **Real-Time Price Updates:** Uses **WebSockets** to broadcast price changes to all connected clients instantly.
* **Item Filtering:** Allows users to search for products directly from the home page.
* **Dynamic Rendering:** Implements **Mustache** templates to render web pages on the server side.
* **Static File Serving:** Serves static assets like CSS and product images from the `resources/public` directory.
* **RESTful API:** Provides full CRUD (Create, Read, Update, Delete) endpoints for managing products.
* **Database Integration:** Uses **MySQL** (via MySQL Workbench) to persist product, user, and order data.
* **JSON Handling:** Uses **Gson** for fast and efficient API serialization/deserialization.
* **Logging:** Includes **Logback** for robust application logging.

---

## 🛠️ Tech Stack

* **Backend:** SparkJava (Web Framework & WebSockets), Java 11
* **Frontend:** Mustache (Template Engine), HTML5, CSS3, JavaScript (for WebSocket client)
* **Database:** MySQL
* **Dependency/Build:** Maven
* **JSON Parsing:** Gson

---

## 🚀 How to Run

### Prerequisites

* Java 11 (or higher)
* Apache Maven 3.8 (or higher)
* **MySQL Server** & **MySQL Workbench** (or any other SQL client)

---

### 1. Database Setup (Required)

1.  Open MySQL Workbench and connect to your local database server.
2.  Create a new schema for the project (e.g., `online_store`).
3.  Run the `schema.sql` file (you'll need to create this) inside the new schema to create the `products`, `users`, and `cart` tables.
    * **Note:** Ensure your `products` (or `items`) table has a column for the image file name (e.g., `image_url VARCHAR(255)`).
4.  Navigate to the database configuration file in the project (e.g., `src/main/java/com/store/dao/Sql2oDao.java` or `config.properties`).
5.  Update the database **URL**, **username**, and **password** to match your local MySQL setup.

### 2. Static File Setup

1.  Place all static assets (e.g., `style.css`, images) in the `src/main/resources/public` directory.
2.  Create an `images` subfolder: `src/main/resources/public/images`.
3.  Add your product images to this folder (e.g., `item1.jpg`, `item2.jpg`).
4.  Update your database records so the `image_url` column matches the file names.

### 3. Run from your IDE (Recommended)

1.  Clone this repository.
2.  Open the project in your IDE (like IntelliJ or Eclipse) as a Maven project.
3.  Allow the IDE to download all Maven dependencies.
4.  Navigate to `src/main/java/com/store/Main.java`.
5.  Right-click the file and select **"Run 'Main.main()'"**.

### 4. Run from the Command Line

1.  Clone the repository:
    ```bash
    git clone [https://github.com/RAUL-SANCHEZ-GIT/Java-Spark-For-Web-Apps.git](https://github.com/RAUL-SANCHEZ-GIT/Java-Spark-For-Web-Apps.git)
    cd Java-Spark-For-Web-Apps
    ```

2.  Compile and run the application using the Maven wrapper:
    ```bash
    mvn compile exec:java -Dexec.mainClass="com.store.Main"
    ```

The application will start and be accessible at `http://localhost:4567`.

---

## 🗺️ App Endpoints

The application serves dynamic web pages, a JSON API, and a WebSocket connection.

### 💻 Web Pages (Server-Side Rendered)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/` | Renders the **Home Page**. Shows all products, or filters by a search term (e.g., `/?query=guitarra`). |
| `GET` | `/products/:id` | Renders the **Product Detail Page** for a single item. |
| `GET` | `/cart` | Renders the **Shopping Cart Page**. |
| `POST` | `/cart/add/:id` | Adds an item to the cart. |
| `POST` | `/cart/remove/:id` | Removes an item from the cart. |

### 🗄️ JSON API (for internal use or future mobile app)

| Method | Endpoint | Description | JSON Body (Example) |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/products` | Retrieves a list of all products. | N/A |
| `GET` | `/api/products/:id` | Retrieves a single product by its ID. | N/A |
| `POST` | `/api/products` | Adds a new product. | `{"name":"Item Name", "price":9.99}` |
| `PUT` | `/api/products/:id` | Edits an existing product by its ID. | `{"name":"New Name", "price":10.99}` |
| `DELETE` | `/api/products/:id` | Deletes a specific product by its ID. | N/A |
| `POST` | `/api/cart/add` | Adds a product to the cart. | `{"productId": 123, "quantity": 1}` |
| `POST` | `/api/cart/remove` | Removes a product from the cart. | `{"productId": 123}` |

### ⚡ WebSocket (Real-Time)

| Endpoint | Direction | Message (JSON) | Description |
| :--- | :--- | :--- | :--- |
| `/price-updates` | `Server -> Client` | `{"itemId":"item1", "newPrice":"650.00"}` | Broadcasts a price change for a specific item to all connected clients. |
# 🛍️ Online Store API (Java-Spark-For-Web-Apps)

![Java](https://img.shields.io/badge/Java-11%2B-ED8B00?style=for-the-badge&logo=openjdk)
![SparkJava](https://img.shields.io/badge/SparkJava-2.9.4-00B4F0?style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven-3.8%2B-C71A36?style=for-the-badge&logo=apache-maven)
![Gson](https://img.shields.io/badge/Gson-2.10-4285F4?style=for-the-badge)

A lightweight, microservice-style RESTful API for an online store, built with Java and the SparkJava framework. This project serves as the backend for managing store items, handling real-time updates, and more.

## ✨ Features

* **RESTful API:** Provides full CRUD (Create, Read, Update, Delete) endpoints for managing store items.
* **MVC Architecture:** Organized in a clean Model-View-Controller pattern (`model`, `service`, `controller`) for maintainability.
* **JSON Handling:** Uses **Gson** for fast and efficient serialization/deserialization of item data.
* **Logging:** Includes **Logback** for robust application logging.
* **Real-time (Sprint 3):** Designed to support real-time price updates using WebSockets.

---

## 🚀 How to Run

### Prerequisites

* Java 11 (or higher)
* Apache Maven 3.8 (or higher)

---

### 1. Run from your IDE (Recommended)

1.  Clone this repository.
2.  Open the project in your IDE (like IntelliJ or Eclipse) as a Maven project.
3.  Allow the IDE to download all Maven dependencies.
4.  Navigate to `src/main/java/com/store/Main.java`.
5.  Right-click the file and select **"Run 'Main.main()'"**.

---

### 2. Run from the Command Line

1.  Clone the repository:
    ```bash
    git clone [https://github.com/RAUL-SANCHEZ-GIT/Java-Spark-For-Web-Apps.git](https://github.com/RAUL-SANCHEZ-GIT/Java-Spark-For-Web-Apps.git)
    cd Java-Spark-For-Web-Apps
    ```

2.  Compile and run the application using the Maven wrapper:
    ```bash
    mvn compile exec:java -Dexec.mainClass="com.store.Main"
    ```

The API will start and be accessible at `http://localhost:4567`.

---

### 3. Build an Executable JAR

1.  Package the application into a single "fat" JAR:
    ```bash
    mvn package
    ```
2.  Run the generated JAR file (located in the `target/` directory):
    ```bash
    java -jar target/online-store-api-1.0-SPRINT1.jar 
    ```
    *(Note: The exact JAR name may vary)*



## API Endpoints (Sprint 1)

All endpoints run on `http://localhost:4567`.

| Method | Endpoint | Description | JSON Body (Example) |
| :--- | :--- | :--- | :--- |
| `GET` | `/users` | Retrieves a list of all items. | N/A |
| `GET` | `/users/:id` | Retrieves a single item by its ID. | N/A |
| `POST` | `/users/:id` | Adds a new item with a specific ID. | `{"name":"Item Name", "description":"Details", "price":"9.99"}` |
| `PUT` | `/users/:id` | Edits an existing item by its ID. | `{"name":"New Name", "description":"New Desc", "price":"10.99"}` |
| `DELETE` | `/users/:id` | Deletes a specific item by its ID. | N/A |
| `OPTIONS` | `/users/:id` | Checks if an item with the given ID exists. | N/A |

---

## 📋 Project Backlog

### Sprint 1: API Foundation & Resource Management
* **Project Foundation Setup:** Initialize the API project with all required dependencies.
* **User Data Retrieval:** Implement `GET /users` and `GET /users/:id` endpoints.
* **User Data Management:** Implement `POST /users/:id` and `PUT /users/:id` endpoints.
* **User Data Maintenance:** Implement `DELETE /users/:id` and `OPTIONS /users/:id`.
* **Project Collaboration:** Set up the GitHub repository with clear documentation.

### Sprint 3: Filtering & Real-Time Functionality
* **Item Filtering:** Implement logic to filter the item list.
* **Real-Time Price Visualization:** Implement a WebSocket for clients to see live price changes.
* **Price Update Broadcasting:** Allow the system to push price changes to all connected clients.
* **Sprint Quality Assurance:** Use a checklist to verify all new features meet quality criteria.
* **Sprint Deliverable Deployment:** Update the GitHub repository with all Sprint 3 code.


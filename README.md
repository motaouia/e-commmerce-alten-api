Project Name: Product Management API
Overview
This project is a back-end application built using Spring Boot 3 and JDK 17, designed for managing product data. It provides REST APIs to perform CRUD operations on products and includes features such as exception handling, data persistence with Spring Data JPA, an in-memory database (H2), database versioning with Liquibase, and unit testing with JUnit 5 and Mockito.

Technologies Used
Spring Boot 3: For building REST APIs quickly and efficiently.
JDK 17: The project is built with Java 17.
Spring Data JPA: For data persistence and database interaction.
H2 Database: An in-memory database used for development and testing.
Liquibase: For versioning and managing the database schema.
Maven: Used for dependency management and project building.
JUnit 5 and Mockito: For unit testing and mocking.
Swagger: API documentation and testing interface.
API Resources
The API manages products and supports the following endpoints:


| Resource           | POST                  | GET                            | PATCH                                    | PUT | DELETE           |
| ------------------ | --------------------- | ------------------------------ | ---------------------------------------- | --- | ---------------- |
| **/products**      | Create a new product  | Retrieve all products          | X                                        | X   |     X            |
| **/products/:id**  | X                     | Retrieve details for product 1 | Update details of product 1 if it exists | X   | Remove product 1 |


Product Entity
The Product entity includes the following attributes:

id: Long
code: String
name: String
description: String
image: String
category: String
price: Double
quantity: Integer
internalReference: String
shellId: Integer
inventoryStatus: ("INSTOCK", "LOWSTOCK", "OUTOFSTOCK")
rating: Double
createdAt: LocalDateTime
updatedAt: LocalDateTime


Features
1. CRUD Operations on Products
POST /products: Create a new product.
GET /products: Retrieve a list of all products.
GET /products/{id}: Retrieve details of a specific product.
PATCH /products/{id}: Update an existing product (partial updates).
DELETE /products/{id}: Remove a product from the system.
2. Exception Handling
Custom exception handling is in place to return meaningful error messages when validation fails or resources are not found.

3. Data Persistence with H2 and JPA
The product data is stored in an in-memory H2 database for testing purposes. Spring Data JPA is used to manage CRUD operations on the database.

4. Database Versioning with Liquibase
The database schema is versioned and managed using Liquibase, allowing easy migrations and tracking of changes in the database schema.

5. Unit Testing
Unit tests are written using JUnit 5 and Mockito to ensure the correctness of the business logic and API behavior.

6. API Documentation with Swagger
Swagger is integrated to provide interactive API documentation. You can access the Swagger UI to explore the API and test endpoints.

Installation
Clone the repository:

git clone https://github.com/your-repository-url
cd product-management-api
Build the project using Maven:

mvn clean install
Run the application:

mvn spring-boot:run
Access the H2 Console: Once the application is running, you can access the H2 database console at:

http://localhost:9988/h2-console The database URL and credentials can be found in the application-dev.properties file.
Access Swagger UI: You can explore the API and test the endpoints via Swagger UI at:

http://localhost:9988/swagger-ui
Running Tests
To run the tests, execute the following command:

mvn test
Unit tests using JUnit 5 and Mockito will be executed, ensuring that the service and repository layers function as expected.

Database Configuration
This project uses H2 as the default in-memory database. If you want to switch to another database, you can modify the application.properties file and configure the data source accordingly.

License
This project is licensed under the MIT License.

Contributors
Mohamed MOTAOUIA

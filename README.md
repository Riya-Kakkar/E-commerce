#  E-Commerce Spring Boot Application

- This project is a backend API for an E-Commerce platform developed using :- Java , Spring Boot , Spring Security , JWT , and MySQL.

# Project Features

- User Registration and Login with JWT Authentication
- Role-Based Access Control (Admin/User(CUSTOMER,SELLER))
- Product & Inventory Management 
- Order & Cart Management
- Review and Rating System
- Admin Dashboard 
- RESTful API Endpoints
- Secure Access with Spring Security + JWT
- Postman Collection for easy API testing

# Project Structure
<pre> <code>
E-commerce/                                                 // root directory of project
├── idea/                                                   // IntelliJ IDEA-related config files (e.g., project settings)
├── images/                                                 //  stores images used in documentation     
├── postman                                                 // Stores Postman collections for API testing.
 └── FINAl_E-commerce.postman_collection.json
├── src                                                    //Contains main source code.
 └── main
   └── java
     └── com.E_Commerce
       └──Config/                                         //Spring configuration classes (e.g., security etc)
       └──Controller/                                    // Handles HTTP requests (API endpoints). 
       └──Entity/                                        //  JPA entity classes, mapping to database tables. 
       └──Helper/                                       // helper classes (Global Exception handling)
       └──Model/                                        // Data transfer objects (DTOs)
       └──Services/                                     //  Business logic layer; contains service classes. 
         ECommerceApplication.java                       //Entry point for Spring Boot application.
   └── resources                                        //Contains app configuration and static files.
     └── application.properties                          //Defines application-level properties (e.g., DB config, ports, security).
 └── test/ 
├── target/ 
├──  mvnw
├── mvnw.cmd
├── pom.xml                                          //Defines dependencies, plugins, and project metadata.
├──README.md                                     //Handled by IDE; lists all Maven dependencies.
</code> </pre>

# Technologies Used

- Java 17+
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- Maven
- MySQL Workbench
- Postman

# Dependencies 

-  dependencies for this project go through :- [pom.xml](pom.xml)

# Getting Started


- Open the project in your IDE: IntelliJ IDEA (recommended) or Eclipse
    
   - If you are using IntelliJ IDEA, make sure the IDE opens project as Maven and recognizes the project as a Spring Boot project. 
 

## 1. Clone the Repository

git clone https://github.com/Riya-Kakkar/E-commerce.git

## 2. Configure the Database

- MySQL workbench s used as the database for this project. The database connection can be configured in the [application.properties](src/main/resources/application.properties), update with following properties:


- Make sure the "e_commerce" database exists in your MySQL server.

----
spring.application.name=E-commerce

spring.devtools.restart.enabled=true
spring.devtools.livereload.enabled=true

server.port=9090

#database configuration
spring.datasource.url=jdbc:mysql://localhost:3306/e_commerce
spring.datasource.username=root
spring.datasource.password=your_new_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update

#-1 is for infinite sizeeee
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=-1
spring.servlet.multipart.max-request-size=-1

project.image=images/

----


## 3. Build and Run the Project


- Run the project (by running the main method in [ECommerceApplication.java](src/main/java/com/E_commerce/ECommerceApplication.java))

#  API Testing with Postman
- A Postman collection is included in the project:
  [E-commerce.postman_collection_files](../../Downloads/E-commerce.postman_collection_files)


To Use:
- Open Postman
- Import the collection JSON :- [E-commerce.postman_collection_files](../../Downloads/E-commerce.postman_collection_files)

- Use preconfigured requests for registration, login, products, and orders.
- set this Root Endpoint - http://localhost:9090/e-commerce/ in your postman

## Endpoints Overview

| Method | Endpoint                                | Description              |
|--------|-----------------------------------------|--------------------------|
| POST   | `/e-commerce/user/register`             | Register a new user      |
| POST   | `/e-commerce/user/login`                | Login and get JWT token  |
| GET    | `/e-commerce/customer/getAllProducts`   | Get all products         |
| POST   | `/e-commerce/seller/create`             | Add new product (Seller) |
| POST   | `/e-commerce/orders/place`              | Place an order (Customer)|

- and other endpoints or Full details of endpoints are in Postman collection.
[E-commerce.postman_collection_files](../../Downloads/E-commerce.postman_collection_files)


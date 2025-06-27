# EasyShop API - E-Commerce Backend

This repository contains the backend API for EasyShop, a full-featured e-commerce web application built using Spring Boot. The project was developed during the YearUp Java Development capstone and focuses on both building new features and resolving critical application issues.

---

## 🔍 Project Description

EasyShop serves as an online retail platform that allows customers to:

- Browse and filter products
- View items by category
- Manage shopping cart contents
- Edit personal account information

Before final deployment, the project required fixing major bugs and implementing essential features to ensure system reliability and completeness.

---

## ✅ Key Fixes and Enhancements

**Resolved Issues:**

- 🛠️ *Broken Product Search:* Previously, combining filters like category, price, or color yielded no results even when valid matches existed. The query logic in the data layer was redesigned for accuracy.
- 🛠️ *Duplicate Products on Update:* Editing a product mistakenly created new entries. This was fixed by replacing `INSERT` with `UPDATE` operations and ensuring proper handling in the DAO and frontend.

**Features Added:**

- CRUD operations for categories
- Shopping cart logic tied to authenticated users

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/easyshop-api.git
cd easyshop-api
```

### 2. Set Up the Database

- Launch MySQL Workbench or another tool
- Execute the `create_database.sql` script in the `database/` folder

### 3. Start the Server

```bash
mvn spring-boot:run
```

Server will be live at:

```
http://localhost:8080
```

---

## 🔐 Authentication

EasyShop uses JWT-based authentication.

### Register

```http
POST /register
Content-Type: application/json

{
  "username": "admin",
  "password": "password",
  "confirmPassword": "password",
  "role": "ADMIN"
}
```

### Login

```http
POST /login
Content-Type: application/json

{
  "username": "admin",
  "password": "password"
}
```

Save the token from the response and include it in requests:

```
Authorization: Bearer <your_token_here>
```

**Default credentials:**

- Username: `user`, `admin`, `george`
- Password: `password`

---

## 📁 API Endpoints

### 🗂️ Categories

```http
GET /categories
GET /categories/{id}
POST /categories
PUT /categories/{id}
DELETE /categories/{id}
```

### 🛒 Shopping Cart (Authenticated)

```http
GET /cart
POST /cart/products/{productId}
PUT /cart/products/{productId}     // Body: { "quantity": 2 }
DELETE /cart
```

### 👤 User Profile

```http
GET /profile
PUT /profile
```

Update payload:
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "phone": "555-1234",
  "address": "123 Main St"
}
```

---

## 🧪 Testing with Postman

The repository includes two Postman collections:

- `easyshop.postman_collection.json`
- `easyshop-optional.postman_collection.json`

Steps:

1. Import into Postman.
2. Run endpoints individually or test suites.
3. Insert JWT tokens under the `Authorization` header.

---

## 🛠️ Tech Requirements

- Java 17+
- Spring Boot
- MySQL Server
- Postman for API testing

---

## 📈 Next Steps

A potential future addition is **checkout functionality**, including order finalization and payment integration.

---

## 📄 License

MIT License

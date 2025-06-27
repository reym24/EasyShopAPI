# EasyShop E-Commerce API

This project is a backend Spring Boot API for **EasyShop**, an online e-commerce website. The goal was not just to build new features, but also to **fix critical bugs and ensure the application was production-ready.**

This capstone was completed as part of the YearUp Java Development program.

---

## 📚 Project Overview

EasyShop is an online store where users can:
- browse products by category,
- search and filter products,
- manage a shopping cart,
- and update their user profile.

Before this project could be deployed to production, there were several key issues to solve:
- **Users had reported that the product search functionality was returning incorrect results.** The search API uses parameters like categoryId, minPrice, maxPrice, and color, but combinations of these often produced no results even when matching products existed.
- **Users also noticed duplicate products.** For example, the same laptop might appear three times with slightly different descriptions or prices. This happened because editing a product was incorrectly inserting new rows instead of updating existing records.

My task was to:
✅ identify and fix these bugs,  
✅ implement new features, and  
✅ ensure the API was ready for production use.

---

## 🚀 How to Run the Project

1. **Clone the repository:**

    ```bash
    git clone https://github.com/your-username/easyshop-api.git
    cd easyshop-api
    ```

2. **Set up the database:**

   - Open MySQL Workbench
   - Run the `create_database.sql` script found in the database folder
   - This creates the `easyshop` database with tables and sample data

3. **Run the application:**

    ```bash
    mvn spring-boot:run
    ```

4. **Access the API:**

    ```
    http://localhost:8080
    ```

---

## 🔑 Authentication

The API uses JWT authentication for all protected endpoints.

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

On successful login, you’ll receive a JWT token. Add this token to the `Authorization` header for all future requests:

```
Authorization: Bearer <token>
```

**Default demo users in the database:**
- user
- admin
- george

All use the password **password** by default.

---

## 📂 Categories Endpoints

Categories were implemented as part of this project to support product browsing and management.

- **Get all categories**

    ```
    GET /categories
    ```

  Example response:

    ```json
    [
      {
        "categoryId": 1,
        "name": "Electronics",
        "description": "Devices and gadgets"
      },
      {
        "categoryId": 2,
        "name": "Home",
        "description": "Home and kitchen products"
      }
    ]
    ```

- **Get a category by ID**

    ```
    GET /categories/{id}
    ```

- **Create a new category**

    ```
    POST /categories
    Body:
    {
      "name": "Sports",
      "description": "Sports equipment and apparel"
    }
    ```

- **Update an existing category**

    ```
    PUT /categories/{id}
    Body:
    {
      "name": "Updated Name",
      "description": "Updated description"
    }
    ```

- **Delete a category**

    ```
    DELETE /categories/{id}
    ```

## 🛒 Shopping Cart Endpoints

Implemented as part of this capstone project. Only available to logged-in users.

- **View Shopping Cart**

    ```
    GET /cart
    ```

- **Add Product to Cart**

    ```
    POST /cart/products/{productId}
    ```

- **Update Quantity of Product in Cart**

    ```
    PUT /cart/products/{productId}
    Body: { "quantity": 3 }
    ```

- **Clear Shopping Cart**

    ```
    DELETE /cart
    ```

Example JSON response:

```json
{
  "items": {
    "15": {
      "product": {
        "productId": 15,
        "name": "External Hard Drive",
        "price": 129.99,
        "categoryId": 1,
        "description": "Expand your storage capacity and backup your files.",
        "color": "Gray",
        "stock": 25,
        "imageUrl": "external-hard-drive.jpg",
        "featured": true
      },
      "quantity": 1,
      "discountPercent": 0,
      "lineTotal": 129.99
    }
  },
  "total": 129.99
}
```

---

## 👤 User Profile Endpoints

When a user registers, a profile is automatically created. Users can view and edit their profile details:

- **View User Profile**

    ```
    GET /profile
    ```

- **Update User Profile**

    ```
    PUT /profile
    Body:
    {
      "firstName": "John",
      "lastName": "Doe",
      "phone": "555-1234",
      "address": "123 Main St"
    }
    ```

---

## 🐛 Bugs Fixed

✅ **Product Search Bug Fixed**  
The original product search was returning incorrect results due to flawed SQL logic. I rewrote the SQL and parameter handling in the DAO layer so filters like category, price, and color now work correctly—even when left blank.

✅ **Duplicate Product Bug Fixed**  
Previously, editing a product incorrectly inserted a new row rather than updating the existing record. I fixed this by:
- replacing INSERT statements with proper SQL UPDATE logic in the Product DAO
- confirming that the frontend uses PUT requests instead of POST when updating products

---

## 🧪 Testing with Postman

The API is thoroughly tested using Postman. Provided Postman collections include:
- `easyshop.postman_collection.json`
- `easyshop-optional.postman_collection.json`

To test:
1. Import the collection into Postman.
2. Run individual requests or full test suites.
3. Include your JWT token in the `Authorization` header for all protected routes.

---

## ✨ Interesting Code Highlight

A key part of this project was adding products to the shopping cart. This method handles that logic:

```java
@PostMapping("products/{productId}")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
public ShoppingCart addProductToCart(@PathVariable int productId, Principal principal) {
    String userName = principal.getName();
    User user = userDao.getByUserName(userName);
    int userId = user.getId();

    ShoppingCart shoppingCart = shoppingCartDao.getByUserId(userId);

    if (shoppingCart.contains(productId)) {
        ShoppingCartItem item = shoppingCart.get(productId);
        item.setQuantity(item.getQuantity() + 1);
        shoppingCartDao.update(userId, item);
    } else {
        Product product = productDao.getById(productId);
        ShoppingCartItem item = new ShoppingCartItem();
        item.setProduct(product);
        shoppingCartDao.create(userId, item);
    }
    return shoppingCartDao.getByUserId(userId);
}
```

**Why it’s interesting:**
- Increases quantity if the product already exists in the cart.
- Adds a new item if it’s not there yet.
- Ties the cart to the logged-in user securely.
- Keeps the cart accurate without duplicates.

This method connects user identity, business logic, and database updates all in one place.

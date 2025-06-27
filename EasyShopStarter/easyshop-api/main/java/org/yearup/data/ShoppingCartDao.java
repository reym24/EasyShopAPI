package org.yearup.data;Add commentMore actions

import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
    ShoppingCartItem create(Product product);
    void update(int userId, Product product);Add commentMore actions
    void delete(int userId);
}
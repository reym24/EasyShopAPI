package org.yearup.data;

import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import java.util.List;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
    ShoppingCartItem create(int userId, ShoppingCartItem shoppingCartItem);
    void update(int userId, ShoppingCartItem shoppingCartItem);
    void delete(int userId);
}
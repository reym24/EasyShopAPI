package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        // get shopping cart by user id

        ShoppingCart shoppingCart = new ShoppingCart();

        String query = """
                                  SELECT
                                  p.*,
                                  sc.quantity,
                                  (p.price * sc.quantity) AS total
                                FROM shopping_cart sc
                                JOIN products p ON sc.product_id = p.product_id
                                WHERE sc.user_id = 3;
                """;

        try(
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(query);
        ) {
            ps.setInt(1, userId);
            try(ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()){

                    Product product = new Product();

                    product.setProductId(resultSet.getInt(2));
                    product.setName(resultSet.getString(3));
                    product.setPrice(resultSet.getBigDecimal(4));
                    product.setCategoryId(resultSet.getInt(5));
                    product.setDescription(resultSet.getString(6));
                    product.setColor(resultSet.getString(7));
                    product.setStock(resultSet.getInt(8));
                    product.setImageUrl(resultSet.getString(9));
                    product.setFeatured(resultSet.getBoolean(10));
                    int quantity = resultSet.getInt(11);

                    ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
                    shoppingCartItem.setProduct(product);
                    shoppingCartItem.setQuantity(quantity);
                    shoppingCart.add(shoppingCartItem);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return shoppingCart;
    }

    @Override
    public ShoppingCartItem create(Product product) {
        return null;
    }

    @Override
    public void update(int userId, Product product) {

    }

    @Override
    public void delete(int userId) {
        // delete shopping cart
        String query = """
                DELETE FROM shopping_cart
                WHERE user_id = ?""";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    // working

//    private ShoppingCart mapRow(ResultSet row) throws SQLException
//    {
//        int userId = row.getInt("user_id");
//        int productId = row.getInt("product_id");
//        int quantity = row.getInt("quantity");
//
//        ShoppingCart shoppingCart = new ShoppingCart()
//        {{
//            setUserId(userId);
//            contains(productId);
//            setItems(quantity);
//        }};
//
//        return shoppingCart;
//    }
}
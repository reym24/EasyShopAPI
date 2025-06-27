package org.yearup.data.mysql;More actions

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
        String query = """
                                   SELECT
                                   sc.product_id,
                                   p.name,
                                   p.price,
                                   p.category_id,
                                   p.description,
                                   p.color,
                                   p.stock,
                                   p.image_url,
                                   p.featured,
                                   sc.quantity
                                 FROM shopping_cart sc
                                 JOIN products p ON sc.product_id = p.product_id
                                 WHERE sc.user_id = ?;
                """;

        try(
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(query);
        ) {
            ps.setInt(1, userId);
            try(ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()){
                    int productId = resultSet.getInt(2);
                    String name = resultSet.getString(3);
                    double price = resultSet.getDouble(4);
                    int categoryId = resultSet.getInt(5);
                    String description = resultSet.getString(6);
                    String color = resultSet.getString(7);
                    int stock = resultSet.getInt(8);
                    String imageUrl = resultSet.getString(9);
                    boolean featured = resultSet.getBoolean(10);
                    int quantity = resultSet.getInt(11);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
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

    // work in progress

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
package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()
    {
        // get all categories
        List<Category> categories = new ArrayList<>();

        String query = """
                SELECT *
                FROM categories
                """;

        try(Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery()
        ) {

            while(resultSet.next()) {
                int categoryId = resultSet.getInt(1);
                String categoryName = resultSet.getString(2);
                String description = resultSet.getString(3);
                Category c = new Category(categoryId, categoryName, description);
                categories.add(c);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return categories;
    }

    @Override
    public Category getById(int categoryId)
    {
        // get category by id
        String query = """
                select
                category_Id,
                Name,
                description
                from
                categories
                where category_Id = ?
                """;

        try(
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(query);
        ) {
            ps.setInt(1, categoryId);
            try(ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()){
                    String categoryName = resultSet.getString(2);
                    String description = resultSet.getString(3);
                    return new Category(categoryId, categoryName, description);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Category create(Category category)
    {
        // create a new category

        String query = """
                INSERT INTO categories (name, description)
                VALUES (?, ?)
                """;

        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());

            ps.executeUpdate();

            try (ResultSet resultSet = ps.getGeneratedKeys()) {
                if (resultSet.next()) {
                    Category result = new Category();
                    result.setCategoryId(resultSet.getInt(1)); // Auto-generated ID
                    result.setName(category.getName());
                    result.setDescription(category.getDescription());
                    return result;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public void update(int categoryId, Category category)
    {
        // update category
        String query = """
                UPDATE categories
                SET name = ?
                  , description = ?
                WHERE category_id = ?
                """;

        try (Connection connection = getConnection())
        {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, category.getCategoryId());
            ps.setString(2, category.getName());
            ps.setString(3, category.getDescription());

            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int categoryId)
    {
        // delete category
        String query = """
                DELETE FROM categories
                WHERE category_id = ?
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        )
        {

            statement.setInt(1, categoryId);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
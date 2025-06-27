package org.yearup.data.mysql;More actions

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        return null;
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

        return null;
    }

    @Override
    public Category create(Category category)
    {
        // create a new category

        return null;
    }

    @Override
    public void update(int categoryId, Category category)
    {
        // update category

    }

    @Override
    public void delete(int categoryId)
    {
        // delete category

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
package m_vasyliev.ukma.zlagoda_ais.dao;

import m_vasyliev.ukma.zlagoda_ais.model.Category;
import m_vasyliev.ukma.zlagoda_ais.dao.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryDAO {
    private Connection connection;

    public CategoryDAO() {
        try {
            connection = DatabaseConnection.initializeDatabase();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM Category";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Category category = new Category();
                category.setCategoryNumber(resultSet.getInt("category_number"));
                category.setCategoryName(resultSet.getString("category_name"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public int getMaxCategoryNumber() {
        int maxNumber = 0;
        String query = "SELECT MAX(category_number) AS max_number FROM Category";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                maxNumber = rs.getInt("max_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxNumber;
    }

    public void addCategory(Category category) {
        String query = "INSERT INTO Category (category_number, category_name) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, category.getCategoryNumber());
            pstmt.setString(2, category.getCategoryName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCategory(Category category) {
        String query = "UPDATE Category SET category_name = ? WHERE category_number = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, category.getCategoryName());
            pstmt.setInt(2, category.getCategoryNumber());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteCategory(int categoryNumber) {
        String query = "DELETE FROM Category WHERE category_number = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, categoryNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void updateCategoryNumbers() {
        String selectQuery = "SELECT category_number FROM Category ORDER BY category_number";
        String updateQuery = "UPDATE Category SET category_number = ? WHERE category_number = ?";
        try (Statement selectStmt = connection.createStatement();
             ResultSet rs = selectStmt.executeQuery(selectQuery)) {
            int newNumber = 1;
            while (rs.next()) {
                int oldNumber = rs.getInt("category_number");
                if (oldNumber != newNumber) {
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, newNumber);
                        updateStmt.setInt(2, oldNumber);
                        updateStmt.executeUpdate();
                    }
                }
                newNumber++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Category getCategoryById(int id) {
        Category category = null;
        String query = "SELECT * FROM Category WHERE category_number = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("category_name");
                    category = new Category(id, name);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }

    public List<Map<String, Object>> getCategoriesWithSales() throws SQLException {
        List<Map<String, Object>> categories = new ArrayList<>();
        String query = "SELECT category_number, category_name " +
                "FROM Category " +
                "WHERE EXISTS ( " +
                "    SELECT 1 " +
                "    FROM Product " +
                "    WHERE Product.category_number = Category.category_number " +
                ") " +
                "AND NOT EXISTS ( " +
                "    SELECT 1 " +
                "    FROM Product " +
                "    WHERE Product.category_number = Category.category_number " +
                "    AND Product.id_product NOT IN ( " +
                "        SELECT Store_Product.id_product " +
                "        FROM Store_Product " +
                "        INNER JOIN Sale ON Sale.UPC = Store_Product.UPC " +
                "    ) " +
                ")";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("Category Number", resultSet.getInt("category_number"));
                row.put("Category Name", resultSet.getString("category_name"));
                categories.add(row);
            }
        }

        return categories;
    }

}

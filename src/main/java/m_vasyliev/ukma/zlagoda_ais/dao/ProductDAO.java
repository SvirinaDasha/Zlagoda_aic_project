package m_vasyliev.ukma.zlagoda_ais.dao;

import m_vasyliev.ukma.zlagoda_ais.model.Product;
import m_vasyliev.ukma.zlagoda_ais.dao.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private Connection connection;

    public ProductDAO() {
        try {
            connection = DatabaseConnection.initializeDatabase();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM Product";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("id_product"));
                product.setCategoryNumber(resultSet.getInt("category_number"));
                product.setProductName(resultSet.getString("product_name"));
                product.setCharacteristics(resultSet.getString("characteristics"));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public void addProduct(Product product) {
        String query = "INSERT INTO Product (category_number, product_name, characteristics) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, product.getCategoryNumber());
            preparedStatement.setString(2, product.getProductName());
            preparedStatement.setString(3, product.getCharacteristics());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Product getProductById(int id) {
        Product product = null;
        String query = "SELECT * FROM Product WHERE id_product = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                product = new Product();
                product.setId(resultSet.getInt("id_product"));
                product.setCategoryNumber(resultSet.getInt("category_number"));
                product.setProductName(resultSet.getString("product_name"));
                product.setCharacteristics(resultSet.getString("characteristics"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public void updateProduct(Product product) {
        String query = "UPDATE Product SET category_number = ?, product_name = ?, characteristics = ? WHERE id_product = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, product.getCategoryNumber());
            preparedStatement.setString(2, product.getProductName());
            preparedStatement.setString(3, product.getCharacteristics());
            preparedStatement.setInt(4, product.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct(int id) {
        String deleteQuery = "DELETE FROM Product WHERE id_product = ?";
        String resetAutoincrementQuery = "DELETE FROM sqlite_sequence WHERE name='Product'";

        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {

            deleteStatement.setInt(1, id);
            deleteStatement.executeUpdate();


            try (Statement resetStatement = connection.createStatement()) {
                resetStatement.executeUpdate(resetAutoincrementQuery);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkConnection() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

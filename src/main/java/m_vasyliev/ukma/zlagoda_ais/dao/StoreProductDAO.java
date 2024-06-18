package m_vasyliev.ukma.zlagoda_ais.dao;

import m_vasyliev.ukma.zlagoda_ais.model.StoreProduct;
import m_vasyliev.ukma.zlagoda_ais.dao.DatabaseConnection;
import m_vasyliev.ukma.zlagoda_ais.dto.StoreProductDetails;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StoreProductDAO {
    private Connection connection;

    public StoreProductDAO() {
        try {
            connection = DatabaseConnection.initializeDatabase();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<StoreProduct> getAllStoreProducts() {
        List<StoreProduct> storeProducts = new ArrayList<>();
        String query = "SELECT * FROM Store_Product ORDER BY products_number";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                StoreProduct storeProduct = new StoreProduct();
                storeProduct.setUpc(resultSet.getString("UPC"));
                storeProduct.setUpcProm(resultSet.getString("UPC_prom"));
                storeProduct.setProductId(resultSet.getInt("id_product"));
                storeProduct.setSellingPrice(resultSet.getDouble("selling_price"));
                storeProduct.setProductsNumber(resultSet.getInt("products_number"));
                storeProduct.setPromotionalProduct(resultSet.getBoolean("promotional_product"));
                storeProducts.add(storeProduct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return storeProducts;
    }

    public void addStoreProduct(StoreProduct storeProduct) throws SQLException {
        String query = "INSERT INTO Store_Product (UPC, UPC_prom, id_product, selling_price, products_number, promotional_product) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, storeProduct.getUpc());
            preparedStatement.setString(2, storeProduct.getUpcProm());
            preparedStatement.setInt(3, storeProduct.getProductId());
            preparedStatement.setDouble(4, storeProduct.getSellingPrice());
            preparedStatement.setInt(5, storeProduct.getProductsNumber());
            preparedStatement.setBoolean(6, storeProduct.isPromotionalProduct());
            preparedStatement.executeUpdate();
        }

        if (storeProduct.isPromotionalProduct()) {
            updatePromotionalInfo(storeProduct);
        }
    }

    public void updateStoreProduct(StoreProduct storeProduct) throws SQLException {
        String query = "UPDATE Store_Product SET UPC_prom = ?, id_product = ?, selling_price = ?, products_number = ?, promotional_product = ? WHERE UPC = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, storeProduct.getUpcProm());
            preparedStatement.setInt(2, storeProduct.getProductId());
            preparedStatement.setDouble(3, storeProduct.getSellingPrice());
            preparedStatement.setInt(4, storeProduct.getProductsNumber());
            preparedStatement.setBoolean(5, storeProduct.isPromotionalProduct());
            preparedStatement.setString(6, storeProduct.getUpc());
            preparedStatement.executeUpdate();
        }

        if (storeProduct.isPromotionalProduct()) {
            updatePromotionalInfo(storeProduct);
        } else {

            String query2 = "SELECT UPC_prom FROM Store_Product WHERE UPC = ?";
            try (PreparedStatement preparedStatement2 = connection.prepareStatement(query2)) {
                preparedStatement2.setString(1, storeProduct.getUpc());
                ResultSet resultSet = preparedStatement2.executeQuery();
                if (resultSet.next()) {
                    String upcProm = resultSet.getString("UPC_prom");
                    if (upcProm != null) {
                        double newPromPrice = storeProduct.getSellingPrice() * 0.8;
                        String updateQuery = "UPDATE Store_Product SET selling_price = ? WHERE UPC = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                            updateStatement.setDouble(1, newPromPrice);
                            updateStatement.setString(2, upcProm);
                            updateStatement.executeUpdate();
                        }
                    }
                }
            }
        }
    }

    private void updatePromotionalInfo(StoreProduct storeProduct) throws SQLException {
        String query = "SELECT * FROM Store_Product WHERE id_product = ? AND promotional_product = 0";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, storeProduct.getProductId());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String nonPromUPC = resultSet.getString("UPC");
                double nonPromPrice = resultSet.getDouble("selling_price");


                String updateQuery = "UPDATE Store_Product SET selling_price = ?, UPC_prom = ? WHERE UPC = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setDouble(1, nonPromPrice);
                    updateStatement.setString(2, storeProduct.getUpc());
                    updateStatement.setString(3, nonPromUPC);
                    updateStatement.executeUpdate();
                }
            }
        }
    }

    public void deleteStoreProduct(String upc) throws SQLException {
        String query = "DELETE FROM Store_Product WHERE UPC = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, upc);
            preparedStatement.executeUpdate();
        }

        clearPromotionalInfo(upc);
    }

    private void clearPromotionalInfo(String upc) throws SQLException {
        String query = "UPDATE Store_Product SET UPC_prom = NULL WHERE UPC_prom = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, upc);
            preparedStatement.executeUpdate();
        }
    }

    public StoreProduct getStoreProductByUpc(String upc) {
        StoreProduct storeProduct = null;
        String query = "SELECT * FROM Store_Product WHERE UPC = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, upc);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                storeProduct = new StoreProduct();
                storeProduct.setUpc(resultSet.getString("UPC"));
                storeProduct.setUpcProm(resultSet.getString("UPC_prom"));
                storeProduct.setProductId(resultSet.getInt("id_product"));
                storeProduct.setSellingPrice(resultSet.getDouble("selling_price"));
                storeProduct.setProductsNumber(resultSet.getInt("products_number"));
                storeProduct.setPromotionalProduct(resultSet.getBoolean("promotional_product"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return storeProduct;
    }

    public List<StoreProductDetails> getPromotionalStoreProducts(boolean isPromotional, String sort) {
        List<StoreProductDetails> storeProductDetails = new ArrayList<>();
        String query = "SELECT sp.*, p.product_name FROM Store_Product sp JOIN Product p ON sp.id_product = p.id_product WHERE sp.promotional_product = ? ORDER BY " + sort;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setBoolean(1, isPromotional);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                StoreProductDetails details = new StoreProductDetails();
                details.setUpc(resultSet.getString("UPC"));
                details.setUpcProm(resultSet.getString("UPC_prom"));
                details.setProductId(resultSet.getInt("id_product"));
                details.setSellingPrice(resultSet.getDouble("selling_price"));
                details.setProductsNumber(resultSet.getInt("products_number"));
                details.setPromotionalProduct(resultSet.getBoolean("promotional_product"));
                details.setProductName(resultSet.getString("product_name")); // Додаємо назву продукту
                storeProductDetails.add(details);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return storeProductDetails;
    }

    public boolean existsNonPromotionalProduct(int productId) throws SQLException {
        String query = "SELECT 1 FROM Store_Product WHERE id_product = ? AND promotional_product = 0";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, productId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    public int countNonPromotionalProducts(int productId) throws SQLException {
        String query = "SELECT COUNT(*) FROM Store_Product WHERE id_product = ? AND promotional_product = 0";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, productId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        }
    }

    public double getNonPromotionalPrice(int productId) throws SQLException {
        String query = "SELECT selling_price FROM Store_Product WHERE id_product = ? AND promotional_product = 0 LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, productId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("selling_price");
            } else {
                throw new SQLException("Non-promotional product not found.");
            }
        }
    }

    public void updatePromotionalPrice(String upc, double newPrice) throws SQLException {
        String query = "UPDATE Store_Product SET selling_price = ? WHERE UPC_prom = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, newPrice * 0.8);
            preparedStatement.setString(2, upc);
            preparedStatement.executeUpdate();
        }
    }

    public List<StoreProductDetails> getAllStoreProductDetails(String sort) {
        List<StoreProductDetails> storeProductDetails = new ArrayList<>();
        String query = "SELECT sp.*, p.product_name FROM Store_Product sp JOIN Product p ON sp.id_product = p.id_product ORDER BY " + sort;

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                StoreProductDetails details = new StoreProductDetails();
                details.setUpc(resultSet.getString("UPC"));
                details.setUpcProm(resultSet.getString("UPC_prom"));
                details.setProductId(resultSet.getInt("id_product"));
                details.setSellingPrice(resultSet.getDouble("selling_price"));
                details.setProductsNumber(resultSet.getInt("products_number"));
                details.setPromotionalProduct(resultSet.getBoolean("promotional_product"));
                details.setProductName(resultSet.getString("product_name"));
                storeProductDetails.add(details);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return storeProductDetails;
    }

    public StoreProductDetails getStoreProductDetailsByUpc(String upc) {
        StoreProductDetails details = null;
        String query = "SELECT sp.selling_price, sp.products_number, p.product_name, p.characteristics " +
                "FROM Store_Product sp " +
                "JOIN Product p ON sp.id_product = p.id_product " +
                "WHERE sp.UPC = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, upc);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                details = new StoreProductDetails();
                details.setSellingPrice(resultSet.getDouble("selling_price"));
                details.setProductsNumber(resultSet.getInt("products_number"));
                details.setProductName(resultSet.getString("product_name"));
                details.setCharacteristics(resultSet.getString("characteristics"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }

    public void updateProductQuantity(String upc, int newQuantity) throws SQLException {
        String query = "UPDATE Store_Product SET products_number = ? WHERE UPC = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            System.out.println(newQuantity+" "+upc);
            preparedStatement.setInt(1, newQuantity);
            preparedStatement.setString(2, upc);
            preparedStatement.executeUpdate();
        }
    }

    public StoreProduct convertToStoreProduct(StoreProductDetails details) {
        StoreProduct storeProduct = new StoreProduct();
        storeProduct.setUpc(details.getUpc());
        storeProduct.setUpcProm(details.getUpcProm());
        storeProduct.setProductId(details.getProductId());
        storeProduct.setSellingPrice(details.getSellingPrice());
        storeProduct.setProductsNumber(details.getProductsNumber());
        storeProduct.setPromotionalProduct(details.isPromotionalProduct());
        return storeProduct;
    }

}

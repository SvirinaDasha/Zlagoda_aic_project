package m_vasyliev.ukma.zlagoda_ais.dao;

import m_vasyliev.ukma.zlagoda_ais.model.Sale;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaleDAO {
    Connection connection;
    public SaleDAO() {
        try {
            connection = DatabaseConnection.initializeDatabase();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addSale(Sale sale) {
        String query = "INSERT INTO Sale (UPC, check_number, product_number, selling_price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, sale.getUpc());
            preparedStatement.setString(2, sale.getCheckNumber());
            preparedStatement.setInt(3, sale.getProductNumber());
            preparedStatement.setDouble(4, sale.getSellingPrice());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, Object>> getTotalSalesByCashierAndPeriod(String cashierId, String startDate, String endDate) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        String query = "SELECT id_employee, SUM(selling_price * product_number) as total_sales " +
                "FROM Sale " +
                "JOIN SalesCheck ON Sale.check_number = SalesCheck.check_number " +
                "WHERE id_employee = ? AND print_date BETWEEN ? AND ? " +
                "GROUP BY id_employee";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, cashierId);
            statement.setString(2, startDate);
            statement.setString(3, endDate);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("Cashier ID", resultSet.getString("id_employee"));
                row.put("Total Sales", resultSet.getDouble("total_sales"));
                results.add(row);
            }
        }
        return results;
    }

    public List<Map<String, Object>> getTotalSalesByPeriod(String startDate, String endDate) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        String query = "SELECT SUM(selling_price * product_number) as total_sales " +
                "FROM Sale " +
                "JOIN SalesCheck ON Sale.check_number = SalesCheck.check_number " +
                "WHERE print_date BETWEEN ? AND ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, startDate);
            statement.setString(2, endDate);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("Total Sales", resultSet.getDouble("total_sales"));
                results.add(row);
            }
        }
        return results;
    }

    public List<Map<String, Object>> getTotalUnitsSoldByProductAndPeriod(String productId, String startDate, String endDate) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        String query = "SELECT id_product, SUM(product_number) as total_units_sold " +
                "FROM Sale " +
                "JOIN Store_Product ON Sale.upc = Store_Product.upc " +
                "JOIN SalesCheck ON Sale.check_number = SalesCheck.check_number " +
                "WHERE id_product = ? AND print_date BETWEEN ? AND ? " +
                "GROUP BY id_product";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, productId);
            statement.setString(2, startDate);
            statement.setString(3, endDate);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("Product ID", resultSet.getString("id_product"));
                row.put("Total Units Sold", resultSet.getInt("total_units_sold"));
                results.add(row);
            }
        }
        return results;
    }

}

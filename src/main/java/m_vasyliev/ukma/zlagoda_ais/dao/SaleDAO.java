package m_vasyliev.ukma.zlagoda_ais.dao;

import m_vasyliev.ukma.zlagoda_ais.model.Sale;
import m_vasyliev.ukma.zlagoda_ais.dto.SaleDTO;
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

    public List<SaleDTO> getTotalSalesByCashierAndPeriod(String cashierId, String startDate, String endDate) throws SQLException {
        List<SaleDTO> results = new ArrayList<>();
        String query = "SELECT SalesCheck.id_employee, empl_surname, empl_name, SUM(selling_price * product_number) as total_sales " +
                "FROM Sale " +
                "JOIN SalesCheck ON Sale.check_number = SalesCheck.check_number " +
                "JOIN Employee ON SalesCheck.id_employee = Employee.id_employee " +
                "WHERE SalesCheck.id_employee = ? AND print_date BETWEEN ? AND ? " +
                "GROUP BY SalesCheck.id_employee, empl_surname, empl_name";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, cashierId);
            statement.setString(2, startDate);
            statement.setString(3, endDate);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                SaleDTO dto = new SaleDTO();
                dto.setCashierId(resultSet.getString("id_employee"));
                dto.setCashierName(resultSet.getString("empl_surname") + " " + resultSet.getString("empl_name"));
                dto.setTotalSales(resultSet.getDouble("total_sales"));
                results.add(dto);
            }
        }
        return results;
    }

    public List<SaleDTO> getTotalSalesByPeriod(String startDate, String endDate) throws SQLException {
        List<SaleDTO> results = new ArrayList<>();
        String query = "SELECT SUM(selling_price * product_number) as total_sales " +
                "FROM Sale " +
                "JOIN SalesCheck ON Sale.check_number = SalesCheck.check_number " +
                "WHERE print_date BETWEEN ? AND ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, startDate);
            statement.setString(2, endDate);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                SaleDTO dto = new SaleDTO();
                dto.setTotalSales(resultSet.getDouble("total_sales"));
                results.add(dto);
            }
        }
        return results;
    }

    public List<SaleDTO> getTotalUnitsSoldByProductAndPeriod(String productId, String startDate, String endDate) throws SQLException {
        List<SaleDTO> results = new ArrayList<>();
        String query = "SELECT Product.id_product, product_name, SUM(product_number) as total_units_sold " +
                "FROM Sale " +
                "JOIN Store_Product ON Sale.upc = Store_Product.upc " +
                "JOIN Product ON Store_Product.id_product = Product.id_product " +
                "JOIN SalesCheck ON Sale.check_number = SalesCheck.check_number " +
                "WHERE Product.id_product = ? AND print_date BETWEEN ? AND ? " +
                "GROUP BY Product.id_product, product_name";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, productId);
            statement.setString(2, startDate);
            statement.setString(3, endDate);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                SaleDTO dto = new SaleDTO();
                dto.setProductId(resultSet.getString("id_product"));
                dto.setProductName(resultSet.getString("product_name"));
                dto.setTotalUnitsSold(resultSet.getInt("total_units_sold"));
                results.add(dto);
            }
        }
        return results;
    }

    public List<SaleDTO> getSalesByCheckNumber(String checkNumber) throws SQLException {
        List<SaleDTO> sales = new ArrayList<>();
        String query = "SELECT Sale.upc, Sale.product_number, Sale.selling_price " +
                "FROM Sale " +
                "LEFT JOIN Store_Product ON Sale.upc = Store_Product.upc " +
                "WHERE Sale.check_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, checkNumber);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                SaleDTO saleDTO = new SaleDTO();
                saleDTO.setProductId(resultSet.getString("upc"));
                saleDTO.setTotalUnitsSold(resultSet.getInt("product_number"));
                saleDTO.setTotalSales(resultSet.getDouble("selling_price"));

                String productName = getProductNameByUpc(resultSet.getString("upc"));
                saleDTO.setProductName(productName);
                sales.add(saleDTO);
            }
        }

        return sales;
    }

    private String getProductNameByUpc(String upc) throws SQLException {
        String productName = "Unknown";
        String query = "SELECT Product.product_name " +
                "FROM Store_Product " +
                "JOIN Product ON Store_Product.id_product = Product.id_product " +
                "WHERE Store_Product.upc = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, upc);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                productName = resultSet.getString("product_name");
            }
        }
        return productName;
    }


    public List<Map<String, Object>> getProductSales(String productName, String startDate, String endDate) throws SQLException {
        List<Map<String, Object>> sales = new ArrayList<>();
        String query = "SELECT SalesCheck.print_date, SUM(Sale.product_number) AS total_sales " +
                "FROM SalesCheck INNER JOIN Sale ON SalesCheck.check_number = Sale.check_number " +
                "INNER JOIN Store_Product ON Store_Product.UPC = Sale.UPC " +
                "WHERE Sale.UPC IN ( " +
                "    SELECT UPC " +
                "    FROM Store_Product " +
                "    WHERE id_product IN ( " +
                "       SELECT id_product " +
                "       FROM Product " +
                "       WHERE product_name = ? " +
                "    ) " +
                ") " +
                "AND SalesCheck.print_date BETWEEN ? AND ? " +
                "GROUP BY SalesCheck.print_date";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, productName);
            statement.setString(2, startDate);
            statement.setString(3, endDate);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("Print Date", resultSet.getString("print_date"));
                row.put("Total Sales", resultSet.getInt("total_sales"));
                sales.add(row);
            }
        }

        return sales;
    }

}

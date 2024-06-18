package m_vasyliev.ukma.zlagoda_ais.dao;

import m_vasyliev.ukma.zlagoda_ais.model.Check;
import m_vasyliev.ukma.zlagoda_ais.dao.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CheckDAO {

    private Connection connection;

    public CheckDAO() {
        try {
            connection = DatabaseConnection.initializeDatabase();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Check> getAllChecks() throws SQLException {
        List<Check> checks = new ArrayList<>();
        String query = "SELECT * FROM SalesCheck";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Check check = new Check();
                check.setCheckNumber(resultSet.getString("check_number"));
                check.setEmployeeId(resultSet.getString("id_employee"));
                check.setCardNumber(resultSet.getString("card_number"));
                check.setPrintDate(resultSet.getString("print_date"));
                check.setSumTotal(resultSet.getDouble("sum_total"));
                check.setVat(resultSet.getDouble("vat"));
                checks.add(check);
            }
        }
        return checks;
    }

    public void saveCheck(Check check) {
        String query = "INSERT INTO SalesCheck (check_number, id_employee, card_number, print_date, sum_total, vat) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, check.getCheckNumber());
            preparedStatement.setString(2, check.getEmployeeId());
            preparedStatement.setString(3, check.getCardNumber());
            preparedStatement.setString(4, check.getPrintDate());
            preparedStatement.setDouble(5, check.getSumTotal());
            preparedStatement.setDouble(6, check.getVat());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

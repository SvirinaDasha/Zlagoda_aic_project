package m_vasyliev.ukma.zlagoda_ais.dao;

import m_vasyliev.ukma.zlagoda_ais.model.Check;
import m_vasyliev.ukma.zlagoda_ais.dao.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CheckDAO {

    private Connection connection;

    public CheckDAO() {
        try {
            connection = DatabaseConnection.initializeDatabase();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveCheck(Check check) {
        String query = "INSERT INTO SalesCheck (check_number, id_employee, card_number, print_date, sum_total, vat) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, check.getCheckNumber());
            preparedStatement.setString(2, check.getEmployeeId());
            preparedStatement.setString(3, check.getCardNumber());
            preparedStatement.setTimestamp(4, check.getPrintDate());
            preparedStatement.setDouble(5, check.getSumTotal());
            preparedStatement.setDouble(6, check.getVat());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

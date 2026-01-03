package m_vasyliev.ukma.zlagoda_ais.dao;

import m_vasyliev.ukma.zlagoda_ais.dto.CheckDTO;
import m_vasyliev.ukma.zlagoda_ais.model.Check;
import m_vasyliev.ukma.zlagoda_ais.dao.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<CheckDTO> getAllCheckDTOs() throws SQLException {
        List<CheckDTO> checkDTOs = new ArrayList<>();
        String query = "SELECT SalesCheck.check_number, SalesCheck.print_date, SalesCheck.sum_total, SalesCheck.vat, " +
                "Employee.empl_surname || ' ' || Employee.empl_name AS employee_full_name, " +
                "Customer_Card.card_number, " +
                "Customer_Card.cust_surname || ' ' || Customer_Card.cust_name AS customer_full_name " +
                "FROM SalesCheck " +
                "JOIN Employee ON SalesCheck.id_employee = Employee.id_employee " +
                "LEFT JOIN Customer_Card ON SalesCheck.card_number = Customer_Card.card_number";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                CheckDTO checkDTO = new CheckDTO();
                checkDTO.setCheckNumber(resultSet.getString("check_number"));
                checkDTO.setPrintDate(resultSet.getString("print_date"));
                checkDTO.setSumTotal(resultSet.getDouble("sum_total"));
                checkDTO.setVat(resultSet.getDouble("vat"));
                checkDTO.setEmployeeFullName(resultSet.getString("employee_full_name"));
                checkDTO.setCardNumber(resultSet.getString("card_number"));
                checkDTO.setCustomerFullName(resultSet.getString("customer_full_name"));
                checkDTOs.add(checkDTO);
            }
        }
        return checkDTOs;
    }

    public List<CheckDTO> getChecksByCashierAndPeriod(String cashierId, String startDate, String endDate) throws SQLException {
        List<CheckDTO> checks = new ArrayList<>();

        String sql = "SELECT c.check_number, CONCAT(e.empl_surname, ' ', e.empl_name) AS employee_full_name, c.card_number, c.print_date, c.sum_total, c.vat " +
                "FROM SalesCheck c " +
                "JOIN Employee e ON c.id_employee = e.id_employee " +
                "WHERE c.id_employee = ? AND c.print_date BETWEEN ? AND ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cashierId);
            statement.setString(2, startDate);
            statement.setString(3, endDate);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                CheckDTO check = new CheckDTO();
                check.setCheckNumber(resultSet.getString("check_number"));
                check.setEmployeeFullName(resultSet.getString("employee_full_name"));
                check.setCardNumber(resultSet.getString("card_number"));
                check.setPrintDate(resultSet.getString("print_date"));
                check.setSumTotal(resultSet.getDouble("sum_total"));
                check.setVat(resultSet.getDouble("vat"));
                checks.add(check);
            }
        }

        return checks;
    }

    public List<CheckDTO> getChecksByPeriod(String startDate, String endDate) throws SQLException {
        List<CheckDTO> checks = new ArrayList<>();

        String sql = "SELECT c.check_number, (e.empl_surname || ' ' || e.empl_name) AS employee_full_name, c.card_number, c.print_date, c.sum_total, c.vat " +
                "FROM SalesCheck c " +
                "JOIN Employee e ON c.id_employee = e.id_employee " +
                "WHERE c.print_date BETWEEN ? AND ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, startDate);
            statement.setString(2, endDate);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                CheckDTO check = new CheckDTO();
                check.setCheckNumber(resultSet.getString("check_number"));
                check.setEmployeeFullName(resultSet.getString("employee_full_name"));
                check.setCardNumber(resultSet.getString("card_number"));
                check.setPrintDate(resultSet.getString("print_date"));
                check.setSumTotal(resultSet.getDouble("sum_total"));
                check.setVat(resultSet.getDouble("vat"));
                checks.add(check);
            }
        }

        return checks;
    }

    public List<Map<String, Object>> getChecksReport(String cashierId, String startDate, String endDate) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT SalesCheck.check_number, Employee.empl_surname, Employee.empl_name, SalesCheck.card_number, SalesCheck.print_date, SalesCheck.sum_total, SalesCheck.vat " +
                "FROM SalesCheck " +
                "JOIN Employee ON SalesCheck.id_employee = Employee.id_employee " +
                "WHERE 1=1 ");

        if (cashierId != null && !cashierId.isEmpty()) {
            query.append(" AND SalesCheck.id_employee = ? ");
        }
        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            query.append(" AND SalesCheck.print_date BETWEEN ? AND ? ");
        }

        try (PreparedStatement statement = connection.prepareStatement(query.toString())) {
            int paramIndex = 1;
            if (cashierId != null && !cashierId.isEmpty()) {
                statement.setString(paramIndex++, cashierId);
            }
            if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
                statement.setString(paramIndex++, startDate);
                statement.setString(paramIndex++, endDate);
            }

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("Check Number", resultSet.getString("check_number"));
                row.put("Employee", resultSet.getString("empl_surname") + " " + resultSet.getString("empl_name"));
                row.put("Card Number", resultSet.getString("card_number"));
                row.put("Print Date", resultSet.getString("print_date"));
                row.put("Total Sum", resultSet.getDouble("sum_total"));
                row.put("VAT", resultSet.getDouble("vat"));
                results.add(row);
            }
        }
        return results;
    }

    public CheckDTO getCheckDTOByNumber(String checkNumber) throws SQLException {
        CheckDTO checkDTO = null;
        String query = "SELECT SalesCheck.check_number, SalesCheck.print_date, SalesCheck.sum_total, SalesCheck.vat, " +
                "(Employee.empl_surname || ' ' || Employee.empl_name) AS employee_full_name, " +
                "Customer_Card.card_number, " +
                "(Customer_Card.cust_surname || ' ' || Customer_Card.cust_name) AS customer_full_name " +
                "FROM SalesCheck " +
                "JOIN Employee ON SalesCheck.id_employee = Employee.id_employee " +
                "LEFT JOIN Customer_Card ON SalesCheck.card_number = Customer_Card.card_number " +
                "WHERE SalesCheck.check_number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, checkNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                checkDTO = new CheckDTO();
                checkDTO.setCheckNumber(resultSet.getString("check_number"));
                checkDTO.setPrintDate(resultSet.getString("print_date"));
                checkDTO.setSumTotal(resultSet.getDouble("sum_total"));
                checkDTO.setVat(resultSet.getDouble("vat"));
                checkDTO.setEmployeeFullName(resultSet.getString("employee_full_name"));
                checkDTO.setCardNumber(resultSet.getString("card_number"));
                checkDTO.setCustomerFullName(resultSet.getString("customer_full_name"));
            }
        }
        return checkDTO;
    }

    public List<CheckDTO> getFilteredChecks(String cashierId, String startDate, String endDate) throws SQLException {
        List<CheckDTO> checks = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT SalesCheck.check_number, SalesCheck.print_date, SalesCheck.sum_total, SalesCheck.vat, ")
                .append("Employee.empl_surname || ' ' || Employee.empl_name AS employee_full_name, ")
                .append("Customer_Card.card_number, ")
                .append("Customer_Card.cust_surname || ' ' || Customer_Card.cust_name AS customer_full_name ")
                .append("FROM SalesCheck ")
                .append("JOIN Employee ON SalesCheck.id_employee = Employee.id_employee ")
                .append("LEFT JOIN Customer_Card ON SalesCheck.card_number = Customer_Card.card_number ")
                .append("WHERE 1=1 ");

        if (cashierId != null && !cashierId.isEmpty()) {
            query.append("AND SalesCheck.id_employee = ? ");
        }
        if (startDate != null && !startDate.isEmpty()) {
            query.append("AND SalesCheck.print_date >= ? ");
        }
        if (endDate != null && !endDate.isEmpty()) {
            query.append("AND SalesCheck.print_date <= ? ");
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {
            int paramIndex = 1;
            if (cashierId != null && !cashierId.isEmpty()) {
                preparedStatement.setString(paramIndex++, cashierId);
            }
            if (startDate != null && !startDate.isEmpty()) {
                preparedStatement.setString(paramIndex++, startDate);
            }
            if (endDate != null && !endDate.isEmpty()) {
                preparedStatement.setString(paramIndex++, endDate);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CheckDTO checkDTO = new CheckDTO();
                checkDTO.setCheckNumber(resultSet.getString("check_number"));
                checkDTO.setPrintDate(resultSet.getString("print_date"));
                checkDTO.setSumTotal(resultSet.getDouble("sum_total"));
                checkDTO.setVat(resultSet.getDouble("vat"));
                checkDTO.setEmployeeFullName(resultSet.getString("employee_full_name"));
                checkDTO.setCardNumber(resultSet.getString("card_number"));
                checkDTO.setCustomerFullName(resultSet.getString("customer_full_name"));
                checks.add(checkDTO);
            }
        }

        return checks;
    }

    public void deleteCheck(String checkNumber) throws SQLException {
        String query = "DELETE FROM SalesCheck WHERE check_number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, checkNumber);
            preparedStatement.executeUpdate();
        }
    }

}


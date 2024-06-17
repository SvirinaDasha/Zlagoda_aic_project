package m_vasyliev.ukma.zlagoda_ais.dao;

import m_vasyliev.ukma.zlagoda_ais.model.Employee;
import m_vasyliev.ukma.zlagoda_ais.dao.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class EmployeeDAO {
    private Connection connection;

    public EmployeeDAO() {
        try {
            connection = DatabaseConnection.initializeDatabase();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Employee getEmployeeById(String id) {
        Employee employee = null;
        String query = "SELECT * FROM Employee WHERE id_employee = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                employee = new Employee();
                employee.setId(resultSet.getString("id_employee"));
                employee.setSurname(resultSet.getString("empl_surname"));
                employee.setName(resultSet.getString("empl_name"));
                employee.setPatronymic(resultSet.getString("empl_patronymic"));
                employee.setRole(resultSet.getString("empl_role"));
                employee.setSalary(resultSet.getBigDecimal("salary"));

                String date_of_birth = resultSet.getString("date_of_birth");
                employee.setDateOfBirth(new java.sql.Date(sdf.parse(date_of_birth).getTime()));

                String date_of_start = resultSet.getString("date_of_start");
                employee.setDateOfStart(new java.sql.Date(sdf.parse(date_of_start).getTime()));
                employee.setPhoneNumber(resultSet.getString("phone_number"));
                employee.setCity(resultSet.getString("city"));
                employee.setStreet(resultSet.getString("street"));
                employee.setZipCode(resultSet.getString("zip_code"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return employee;
    }

    public String getEmployeeNameById(String employeeId) {
        String query = "SELECT empl_surname, empl_name, empl_patronymic FROM Employee WHERE id_employee = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String surname = resultSet.getString("empl_surname");
                String name = resultSet.getString("empl_name");
                String patronymic = resultSet.getString("empl_patronymic");
                return surname + " " + name + " " + patronymic;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}


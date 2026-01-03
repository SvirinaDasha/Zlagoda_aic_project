package m_vasyliev.ukma.zlagoda_ais.dao;

import m_vasyliev.ukma.zlagoda_ais.model.Employee;
import m_vasyliev.ukma.zlagoda_ais.dao.DatabaseConnection;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EmployeeDAO {
    private Connection connection;

    public EmployeeDAO() {
        try {
            connection = DatabaseConnection.initializeDatabase();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String createId() {
        return UUID.randomUUID().toString();
    }

    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM Employee";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setIdEmployee(resultSet.getString("id_employee"));
                employee.setSurname(resultSet.getString("empl_surname"));
                employee.setName(resultSet.getString("empl_name"));
                employee.setPatronymic(resultSet.getString("empl_patronymic"));
                employee.setRole(resultSet.getString("empl_role"));
                employee.setSalary(resultSet.getDouble("salary"));
                employee.setPhoneNumber(resultSet.getString("phone_number"));
                employee.setDateOfBirth(resultSet.getString("date_of_birth"));
                employee.setDateOfStart(resultSet.getString("date_of_start"));
                employee.setCity(resultSet.getString("city"));
                employee.setStreet(resultSet.getString("street"));
                employee.setZipCode(resultSet.getString("zip_code"));
                System.out.println(employee.getSalary());
                employees.add(employee);
            }
        }
        return employees;
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
                employee.setIdEmployee(resultSet.getString("id_employee"));
                employee.setSurname(resultSet.getString("empl_surname"));
                employee.setName(resultSet.getString("empl_name"));
                employee.setPatronymic(resultSet.getString("empl_patronymic"));
                employee.setRole(resultSet.getString("empl_role"));
                employee.setSalary(resultSet.getDouble("salary"));

                String date_of_birth = resultSet.getString("date_of_birth");
                employee.setDateOfBirth(new java.sql.Date(sdf.parse(date_of_birth).getTime()).toString());

                String date_of_start = resultSet.getString("date_of_start");
                employee.setDateOfStart(new java.sql.Date(sdf.parse(date_of_start).getTime()).toString());
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

    public List<Employee> getAllCashiers() throws SQLException {
        List<Employee> cashiers = new ArrayList<>();
        String query = "SELECT * FROM Employee WHERE empl_role = 'Cashier'";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setIdEmployee(resultSet.getString("id_employee"));
                employee.setSurname(resultSet.getString("empl_surname"));
                employee.setName(resultSet.getString("empl_name"));
                employee.setPatronymic(resultSet.getString("empl_patronymic"));
                employee.setRole(resultSet.getString("empl_role"));
                employee.setPhoneNumber(resultSet.getString("phone_number"));
                employee.setCity(resultSet.getString("city"));
                employee.setStreet(resultSet.getString("street"));
                employee.setZipCode(resultSet.getString("zip_code"));
                employee.setDateOfBirth(resultSet.getString("date_of_birth"));
                employee.setDateOfStart(resultSet.getString("date_of_start"));
                cashiers.add(employee);
            }
        }
        return cashiers;
    }

    public void addEmployee(Employee employee) {
        String query = "INSERT INTO Employee (id_employee, empl_surname, empl_name, empl_patronymic, empl_role, salary, date_of_birth, date_of_start, phone_number, city, street, zip_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            String newId = createId();
            employee.setIdEmployee(newId);

            preparedStatement.setString(1, newId);
            preparedStatement.setString(2, employee.getSurname());
            preparedStatement.setString(3, employee.getName());
            preparedStatement.setString(4, employee.getPatronymic());
            preparedStatement.setString(5, employee.getRole());
            preparedStatement.setDouble(6, employee.getSalary());
            preparedStatement.setString(7, employee.getDateOfBirth());
            preparedStatement.setString(8, employee.getDateOfStart());
            preparedStatement.setString(9, employee.getPhoneNumber());
            preparedStatement.setString(10, employee.getCity());
            preparedStatement.setString(11, employee.getStreet());
            preparedStatement.setString(12, employee.getZipCode());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getallroles() {
        List<String> roles = new ArrayList<>();
        String query = "SELECT DISTINCT empl_role FROM Employee";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                roles.add(resultSet.getString("empl_role"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return roles;

    }


    public void updateEmployee(Employee employee) {
        String query = "UPDATE Employee SET empl_surname = ?, empl_name = ?, empl_patronymic = ?, empl_role = ?, salary = ?, date_of_birth = ?, date_of_start = ?, phone_number = ?, city = ?, street = ?, zip_code = ? WHERE id_employee = ?";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, employee.getSurname());
            preparedStatement.setString(2, employee.getName());
            preparedStatement.setString(3, employee.getPatronymic());
            preparedStatement.setString(4, employee.getRole());
            preparedStatement.setDouble(5, employee.getSalary());
            preparedStatement.setString(6, employee.getDateOfBirth());
            preparedStatement.setString(7, employee.getDateOfStart());
            preparedStatement.setString(8, employee.getPhoneNumber());
            preparedStatement.setString(9, employee.getCity());
            preparedStatement.setString(10, employee.getStreet());
            preparedStatement.setString(11, employee.getZipCode());
            preparedStatement.setString(12, employee.getIdEmployee());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEmployee(String employeeId) {
        String query = "DELETE FROM Employee WHERE id_employee = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, employeeId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Employee> getEmployeesByRole(String role) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM Employee WHERE empl_role = ? ORDER BY empl_surname";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, role);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setIdEmployee(resultSet.getString("id_employee"));
                employee.setSurname(resultSet.getString("empl_surname"));
                employee.setName(resultSet.getString("empl_name"));
                employee.setPatronymic(resultSet.getString("empl_patronymic"));
                employee.setRole(resultSet.getString("empl_role"));
                employee.setPhoneNumber(resultSet.getString("phone_number"));
                employee.setDateOfBirth(resultSet.getString("date_of_birth"));
                employee.setDateOfStart(resultSet.getString("date_of_start"));
                employee.setCity(resultSet.getString("city"));
                employee.setStreet(resultSet.getString("street"));
                employee.setZipCode(resultSet.getString("zip_code"));
                employees.add(employee);
            }
        }
        return employees;
    }

    public List<Employee> searchEmployeesBySurname(String surname) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT id_employee, empl_surname, empl_name, empl_patronymic, phone_number, city, street, zip_code FROM Employee WHERE empl_surname LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, surname + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setIdEmployee(resultSet.getString("id_employee"));
                employee.setSurname(resultSet.getString("empl_surname"));
                employee.setName(resultSet.getString("empl_name"));
                employee.setPatronymic(resultSet.getString("empl_patronymic"));
                employee.setPhoneNumber(resultSet.getString("phone_number"));
                employee.setCity(resultSet.getString("city"));
                employee.setStreet(resultSet.getString("street"));
                employee.setZipCode(resultSet.getString("zip_code"));
                employees.add(employee);
            }
        }
        return employees;
    }
}


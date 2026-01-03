package m_vasyliev.ukma.zlagoda_ais.dao;

import m_vasyliev.ukma.zlagoda_ais.model.User;
import m_vasyliev.ukma.zlagoda_ais.utils.PasswordUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection connection;

    public UserDAO() {
        try {
            connection = DatabaseConnection.initializeDatabase();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public User validateUser(String username, String password) {
        User user = null;
        String query = "SELECT Users.*, Employee.empl_role FROM Users " +
                "JOIN Employee ON Users.id_employee = Employee.id_employee WHERE Users.username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                if (PasswordUtils.checkPassword(password, storedPassword)) {
                    user = new User();
                    user.setId(resultSet.getInt("id_user"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(storedPassword);
                    user.setIdEmployee(resultSet.getString("id_employee"));
                    user.setRole(resultSet.getString("empl_role"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void addUser(User user) throws SQLException {
        String query = "INSERT INTO Users (username, password, id_employee) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getIdEmployee());
            preparedStatement.executeUpdate();
        }
    }

    public void updateUser(User user) throws SQLException {
        String query = "UPDATE Users SET username = ?, password = ?, id_employee = ? WHERE id_user = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getIdEmployee());
            preparedStatement.setInt(4, user.getId());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteUser(int id) throws SQLException {
        String query = "DELETE FROM Users WHERE id_user = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    public User getUserById(int id) throws SQLException {
        User user = null;
        String query = "SELECT Users.*, Employee.empl_role FROM Users " +
                "JOIN Employee ON Users.id_employee = Employee.id_employee WHERE Users.id_user = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id_user"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setIdEmployee(resultSet.getString("id_employee"));
                user.setRole(resultSet.getString("empl_role"));
            }
        }
        return user;
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT Users.*, Employee.empl_role FROM Users " +
                "LEFT JOIN Employee ON Users.id_employee = Employee.id_employee";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id_user"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setIdEmployee(resultSet.getString("id_employee"));
                String role = resultSet.getString("empl_role");
                user.setRole(role != null ? role : "Unknown");
                users.add(user);
            }
        }
        return users;
    }
}

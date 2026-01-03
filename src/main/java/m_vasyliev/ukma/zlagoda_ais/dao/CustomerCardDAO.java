package m_vasyliev.ukma.zlagoda_ais.dao;

import m_vasyliev.ukma.zlagoda_ais.model.CustomerCard;
import m_vasyliev.ukma.zlagoda_ais.dao.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomerCardDAO {
    private Connection connection;

    public CustomerCardDAO() {
        try {
            connection = DatabaseConnection.initializeDatabase();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String createId() {
        return UUID.randomUUID().toString();
    }

    public List<CustomerCard> getAllCustomerCards() {
        List<CustomerCard> customerCards = new ArrayList<>();
        String query = "SELECT * FROM Customer_Card ORDER BY cust_surname";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                CustomerCard card = new CustomerCard();
                card.setCardNumber(resultSet.getString("card_number"));
                card.setCustSurname(resultSet.getString("cust_surname"));
                card.setCustName(resultSet.getString("cust_name"));
                card.setCustPatronymic(resultSet.getString("cust_patronymic"));
                card.setPhoneNumber(resultSet.getString("phone_number"));
                card.setCity(resultSet.getString("city"));
                card.setStreet(resultSet.getString("street"));
                card.setZipCode(resultSet.getString("zip_code"));
                card.setPercent(resultSet.getInt("percent"));
                customerCards.add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerCards;
    }

    public CustomerCard getCustomerCardByCardNumber(String cardNumber) {
        CustomerCard customerCard = null;
        String query = "SELECT * FROM Customer_Card WHERE card_number = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, cardNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                customerCard = new CustomerCard();
                customerCard.setCardNumber(resultSet.getString("card_number"));
                customerCard.setCustSurname(resultSet.getString("cust_surname"));
                customerCard.setCustName(resultSet.getString("cust_name"));
                customerCard.setCustPatronymic(resultSet.getString("cust_patronymic"));
                customerCard.setPhoneNumber(resultSet.getString("phone_number"));
                customerCard.setCity(resultSet.getString("city"));
                customerCard.setStreet(resultSet.getString("street"));
                customerCard.setZipCode(resultSet.getString("zip_code"));
                customerCard.setPercent(resultSet.getInt("percent"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerCard;
    }

    public void addCustomerCard(CustomerCard customerCard) {
        String query = "INSERT INTO Customer_Card (card_number, cust_surname, cust_name, cust_patronymic, phone_number, city, street, zip_code, percent) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            String newId = createId();
            customerCard.setCardNumber(newId);

            preparedStatement.setString(1, newId);
            preparedStatement.setString(2, customerCard.getCustSurname());
            preparedStatement.setString(3, customerCard.getCustName());
            preparedStatement.setString(4, customerCard.getCustPatronymic());
            preparedStatement.setString(5, customerCard.getPhoneNumber());
            preparedStatement.setString(6, customerCard.getCity());
            preparedStatement.setString(7, customerCard.getStreet());
            preparedStatement.setString(8, customerCard.getZipCode());
            preparedStatement.setInt(9, customerCard.getPercent());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCustomerCard(CustomerCard customerCard) {
        String query = "UPDATE Customer_Card SET cust_surname = ?, cust_name = ?, cust_patronymic = ?, phone_number = ?, city = ?, street = ?, zip_code = ?, percent = ? WHERE card_number = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, customerCard.getCustSurname());
            preparedStatement.setString(2, customerCard.getCustName());
            preparedStatement.setString(3, customerCard.getCustPatronymic());
            preparedStatement.setString(4, customerCard.getPhoneNumber());
            preparedStatement.setString(5, customerCard.getCity());
            preparedStatement.setString(6, customerCard.getStreet());
            preparedStatement.setString(7, customerCard.getZipCode());
            preparedStatement.setInt(8, customerCard.getPercent());
            preparedStatement.setString(9, customerCard.getCardNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteCustomerCard(String cardNumber) {
        String query = "DELETE FROM Customer_Card WHERE card_number = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, cardNumber);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CustomerCard> searchCustomerCardsBySurname(String surname) {
        List<CustomerCard> customerCards = new ArrayList<>();
        String query = "SELECT * FROM Customer_Card WHERE cust_surname LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, surname + "%");
            System.out.println("Executing query: " + preparedStatement.toString()); // Логування SQL-запиту
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CustomerCard customerCard = new CustomerCard();
                customerCard.setCardNumber(resultSet.getString("card_number"));
                customerCard.setCustSurname(resultSet.getString("cust_surname"));
                customerCard.setCustName(resultSet.getString("cust_name"));
                customerCard.setCustPatronymic(resultSet.getString("cust_patronymic"));
                customerCard.setPhoneNumber(resultSet.getString("phone_number"));
                customerCard.setCity(resultSet.getString("city"));
                customerCard.setStreet(resultSet.getString("street"));
                customerCard.setZipCode(resultSet.getString("zip_code"));
                customerCard.setPercent(resultSet.getInt("percent"));
                customerCards.add(customerCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerCards;
    }

    public List<CustomerCard> searchCustomerCardsBySurnameAndPercent(String surname, int percent) {
        List<CustomerCard> customerCards = new ArrayList<>();
        String query = "SELECT * FROM Customer_Card WHERE cust_surname LIKE ? AND percent = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, surname + "%");
            preparedStatement.setInt(2, percent);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CustomerCard customerCard = new CustomerCard();
                customerCard.setCardNumber(resultSet.getString("card_number"));
                customerCard.setCustSurname(resultSet.getString("cust_surname"));
                customerCard.setCustName(resultSet.getString("cust_name"));
                customerCard.setCustPatronymic(resultSet.getString("cust_patronymic"));
                customerCard.setPhoneNumber(resultSet.getString("phone_number"));
                customerCard.setCity(resultSet.getString("city"));
                customerCard.setStreet(resultSet.getString("street"));
                customerCard.setZipCode(resultSet.getString("zip_code"));
                customerCard.setPercent(resultSet.getInt("percent"));
                customerCards.add(customerCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerCards;
    }

    public List<CustomerCard> getCustomerCardsByPercent(int percent) {
        List<CustomerCard> customerCards = new ArrayList<>();
        String query = "SELECT * FROM Customer_Card WHERE percent = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, percent);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CustomerCard customerCard = new CustomerCard();
                customerCard.setCardNumber(resultSet.getString("card_number"));
                customerCard.setCustSurname(resultSet.getString("cust_surname"));
                customerCard.setCustName(resultSet.getString("cust_name"));
                customerCard.setCustPatronymic(resultSet.getString("cust_patronymic"));
                customerCard.setPhoneNumber(resultSet.getString("phone_number"));
                customerCard.setCity(resultSet.getString("city"));
                customerCard.setStreet(resultSet.getString("street"));
                customerCard.setZipCode(resultSet.getString("zip_code"));
                customerCard.setPercent(resultSet.getInt("percent"));
                customerCards.add(customerCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerCards;
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

package m_vasyliev.ukma.zlagoda_ais.dao;

import m_vasyliev.ukma.zlagoda_ais.model.CustomerCard;
import m_vasyliev.ukma.zlagoda_ais.dao.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerCardDAO {
    private Connection connection;

    public CustomerCardDAO() {
        try {
            connection = DatabaseConnection.initializeDatabase();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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

    public CustomerCard getCustomerCardByNumber(String cardNumber) {
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

}

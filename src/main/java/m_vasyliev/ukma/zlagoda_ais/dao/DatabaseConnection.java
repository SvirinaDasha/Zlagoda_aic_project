package m_vasyliev.ukma.zlagoda_ais.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection initializeDatabase() throws SQLException, ClassNotFoundException {
        String url = "jdbc:sqlite:resources/zlagodaa.db";
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection(url);
    }
}

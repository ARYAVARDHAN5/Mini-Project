package bank.util;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:33078/bank_dbb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1234";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}

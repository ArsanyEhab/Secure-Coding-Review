import java.sql.*;
import java.util.Scanner;

public class VulnerableApp {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/users_db";
    private static final String USER = "root";
    private static final String PASS = "password123"; // Hardcoded password (vulnerable)

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (authenticateUser(username, password)) {
            System.out.println("Authentication successful!");
        } else {
            System.out.println("Authentication failed!");
        }
    }

    // Vulnerable method using raw SQL query (SQL injection risk)
    public static boolean authenticateUser(String username, String password) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            String query = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'"; // SQL Injection vulnerability
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            return resultSet.next();  // If a row is returned, authentication is successful
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}

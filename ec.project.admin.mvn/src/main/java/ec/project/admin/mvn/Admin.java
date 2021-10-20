package ec.project.admin.mvn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Admin {
	
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/test";
	static final String USER = "root";
	static final String PASS = "adam";
	

	public static void main(String[] args) throws Exception {
		
		createTable();
		createAdminUser();
	
	}

	public static void createTable() {
		System.out.println("DB connection");
		Connection connection = null;
		Statement statement = null;
		String sql;

		try {
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement();

			sql = "CREATE TABLE IF NOT EXISTS TOOL_USERS (ID INT AUTO_INCREMENT PRIMARY KEY, NAME VARCHAR(50), PASSWORD VARCHAR(200), SALT VARCHAR(50), ROLE INT)";
            statement.execute(sql);
 
			System.out.println("User table added");
		
			
		} catch (SQLException e) { // Handle errors for JDBC
			e.printStackTrace();
		} catch (Exception e) { 
			e.printStackTrace();
		} finally { // finally block used to close resources
			try {
				if (statement != null) statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		
	}
	
	private static void createAdminUser() {
		
		System.out.println("DB connection");
		Connection connection = null;
		Statement statement = null;
		PreparedStatement preparedStatement = null;
		SecurityGuard guard = new SecurityGuard();
		
		try {
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement();
			
			preparedStatement = connection.prepareStatement("INSERT INTO `tool_users` (name, password, salt, role) VALUES (?, ?, ?, ?)");

			List<String> hashSalt = guard.generateHash("cp630super");
			// just setting the class name
			preparedStatement.setString(1, "super");
			preparedStatement.setObject(2, hashSalt.get(0));
			preparedStatement.setObject(3, hashSalt.get(1));
			preparedStatement.setInt(4, 0);
			preparedStatement.executeUpdate();
			
			System.out.println("Super user added");
			
			
			
		} catch (SQLException e) { // Handle errors for JDBC
			e.printStackTrace();
		} catch (Exception e) { 
			e.printStackTrace();
		} finally { // finally block used to close resources
			try {
				if (statement != null) statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

	}

}

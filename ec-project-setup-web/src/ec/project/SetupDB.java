package ec.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SetupDB {
	
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/test";
	static final String USER = "root";
	static final String PASS = "adam";
	
	
	public SetupDB() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int createTables() {
		System.out.println("DB connection");
		Connection connection = null;
		Statement statement = null;
		String sql;
		int result = -1;

		try {
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement();

			
			try {
				sql = "DROP TABLE IF EXISTS `tool_data`";
				statement.execute(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			sql = "CREATE TABLE `tool_data` (id INT NOT NULL AUTO_INCREMENT, NAME VARCHAR(50), data LONGBLOB, PRIMARY KEY (id))";
			statement.execute(sql);		
			
			System.out.println("Tool data table added");

			try {
				sql = "DROP TABLE IF EXISTS `tool_model`";
				statement.execute(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			sql = "CREATE TABLE `tool_model` (id INT NOT NULL AUTO_INCREMENT, NAME VARCHAR(50), model MEDIUMBLOB, pca MEDIUMBLOB, PRIMARY KEY (id))";
			statement.execute(sql);	
			
			System.out.println("Tool model table added");
		
			
		} catch (SQLException e) { // Handle errors for JDBC
			e.printStackTrace();
			result = e.getErrorCode();
		} catch (Exception e) { 
			e.printStackTrace();
			result = 1;
		} finally { // finally block used to close resources
			try {
				if (statement != null) statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
				result = e.getErrorCode();
			}
			try {
				if (connection != null) connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
				result = se.getErrorCode();
			}
		}
		
		return result;
	}

}

package ec.project.data;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import ec.project.weka.InstanceQueryAdapter;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.DatabaseSaver;
import weka.experiment.InstanceQuery;

public class DataSaveDB {
	
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/test";
	static final String USER = "root";
	static final String PASS = "adam";
	
	private static void Save(Instances data, String tableName) {
		
        try {
			DatabaseSaver save = new DatabaseSaver();
	        save.setUrl(DB_URL);
	        save.setUser(USER);
	        save.setPassword(PASS);
	        save.setInstances(data);
	        save.setRelationForTableName(false);
	        save.setTruncate(true);
	        save.setTableName(tableName);
	        save.connectToDatabase();

			save.writeBatch();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Instances Load(String sqlStatement) {
		/*InstanceQuery query;
		try {
			query = new InstanceQuery();
			query.setDatabaseURL(DB_URL);
		    query.setUsername(USER);
		    query.setPassword(PASS);
			query.setQuery(sqlStatement);
			// You can declare that your data set is sparse
			// query.setSparseData(true);
			Instances data = query.retrieveInstances();
			
			return data;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;*/
		
		System.out.println("DB connection");
		Connection connection = null;
		Statement statement = null;
		PreparedStatement preparedStatement = null;
		InitialContext ctxt =null;
		
		try {
			//ctxt = new InitialContext();
			//DataSource ds = (DataSource) ctxt.lookup("java:/MySqlDS");
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from tool_data");
			
			InstanceQuery iq = new InstanceQuery();
			iq.retrieveInstances(iq, rs);
			
			return null;

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
    public static void main(String[] args) throws Exception {

		File dir = new File("../data");
		ArffLoader loader = new ArffLoader();
		loader.setSource(new File(dir+"/complete.arff"));
		Instances data = loader.getDataSet();
		
		Save(data, "tool_data");
		Instances loaded_data = Load("select * from tool_data");
		
		
		
      }
}


package dao;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class BD {
	
	private static Connection conn = null;
	public static String typeDB = null;
	
	private BD() {
		Properties prop = new Properties();
		
		try {
			
			prop.load(new FileReader("properties.database.prop"));
			
			typeDB = prop.getProperty("db");
			String driver = prop.getProperty("driver");
			String dsn = prop.getProperty("dsn");
			String user = prop.getProperty("user", "");
			String pass = prop.getProperty("pass", "");
			
			Class.forName(driver);
			
			conn = DriverManager.getConnection(dsn, user, pass);
			
		} catch (IOException  | SQLException | ClassNotFoundException e) {
		}
	}
	
	public static Connection open() {
		if(conn == null) {
			new BD();
		}
		return conn;
	}
	
	public static void close() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
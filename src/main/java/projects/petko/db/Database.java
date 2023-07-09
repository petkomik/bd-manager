package projects.petko.db;

import static projects.petko.general.Parameters.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;	

public class Database {
	protected Connection connection;
	protected Properties properties;
	
	public Database() {
		this.connect(ressourcesDir + databaseName);
		properties = new Properties();
		properties.setProperty("PRAGMA foreign_keys", "ON");
	}
	
	private void connect(String file) {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + file);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	protected void disconnect(){
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
package projects.petko.db;

import java.sql.SQLException;
import java.sql.Statement;

public class CreateDatabase extends Database {
	
	private void dropTable(String name) {
		try (Statement statement = connection.createStatement()) {
			String sql = "DROP TABLE IF EXISTS " + name;
			statement.executeUpdate(sql);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void createTables() {
		CreateDatabase createDB = new CreateDatabase();
		createDB.createPeopleTable();
		createDB.disconnect();
	}
	
	private void createPeopleTable() {
		dropTable("People");
		try(Statement stmt = connection.createStatement()) {
			String sql = "CREATE TABLE IF NOT EXISTS People ("
					+ "ID			INTEGER	PRIMARY KEY,"
					+ "FName		TEXT	NOT NULL,"
					+ "LName		TEXT 	NOT NULL,"
					+ "Nickname		TEXT	NOT NULL,"
					+ "Category		TEXT	NOT NULL,"
					+ "Subgroup		TEXT	NOT NULL,"
					+ "Birthday		TEXT	NOT NULL)";
			stmt.executeUpdate(sql);
			
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage() );
			e.printStackTrace();
			System.exit(0);		}
	}
}
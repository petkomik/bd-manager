package projects.petko.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import projects.petko.general.Parameters;
import projects.petko.io.CSVReader;

public class FillDatabase extends Database {
	
	private void clearTable(String name) {
		try {
			Statement statement = connection.createStatement();
			String sql = "DELETE FROM " + name + ";";
			statement.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void fillTable() {
		try {
			connection.setAutoCommit(false);
			fillPeople();
			connection.commit();
			connection.setAutoCommit(true);
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void fillPeople() {
		String filename = Parameters.ressourcesDir + Parameters.filenameCSV;
		CSVReader csvReader = new CSVReader(filename);
		List<String[]> tokens = csvReader.readFile();
		clearTable("People");
		PreparedStatement prest = null;	
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");


		
		try {
			prest = connection.prepareStatement("INSERT INTO People "
					+ "(ID, FName, LName, Nickname, Category, Subgroup, "
					+ "Birthday) "
					+ "VALUES (?,?,?,?,?,?,?)");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage() );
			e.printStackTrace();
			System.exit(0);
		}
		
		for(String[] thisLine : tokens) {
			try {
				prest.setInt(1,Integer.parseInt(thisLine[0]));
				prest.setString(2,thisLine[3]);
				prest.setString(3,thisLine[4]);
				prest.setString(4,thisLine[5]);
				prest.setString(5,thisLine[1]);
				prest.setString(6,thisLine[2]);
				prest.setString(7,LocalDate.parse(thisLine[6], dtf).format(dtf2));
				prest.executeUpdate();
			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage() );
				e.printStackTrace();
				System.exit(0);
			}
			
		}

		
	}
}
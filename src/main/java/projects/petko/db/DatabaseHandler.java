package projects.petko.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import com.opencsv.CSVWriter;

import projects.petko.data.Person;
import projects.petko.general.Parameters;


public class DatabaseHandler extends Database {
	private PreparedStatement pstm_Bewerbung;  // zum Einfuegen von Up Next


	public DatabaseHandler() {
		super();
	}

	public void disconnect() {
		try {
			if ((pstm_Bewerbung != null) && (!pstm_Bewerbung.isClosed())){
				pstm_Bewerbung.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		super.disconnect();
	}
	
	
	public Person getPersonWithID(int id) {
		
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	
			
		Person hehim = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM People WHERE ID=" + id;
		try (Statement stmt = connection.createStatement()) {
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				hehim = new Person(rs.getInt("ID"),
						rs.getString("Category"),
						rs.getString("Subgroup"),
						rs.getString("FName"),
						rs.getString("LName"),
						rs.getString("Nickname"),
						LocalDate.parse(rs.getString("Birthday"), formatter));
			}
			
		rs.close();
		} catch (SQLException e){
			System.err.println(e.getClass().getName() + ": " + e.getMessage() );
			e.printStackTrace();
	
		} 
		
		return hehim; // dummy return
	}
		
	public List<Person> getAllPeople() {
			
			List<Person> ppl = new ArrayList<Person>();
			ResultSet rs = null;
			try (Statement stmt = connection.createStatement();) {
				rs = stmt.executeQuery("SELECT * FROM People");
				while(rs.next()) {
					Person m = this.getPersonWithID(rs.getInt("ID"));
					ppl.add(m);
				}
				rs.close();
			} catch (SQLException e){
				System.err.println(e.getClass().getName() + ": " + e.getMessage() );
				e.printStackTrace();
	
			} 
			return ppl;
	}
	
	public String[] getUniqueFilterValues(String table, String filterColumn) {
			
		List<String> filterValues = new ArrayList<String>();
		String sql = "SELECT DISTINCT " + filterColumn + " FROM " + table
				+ " ORDER BY " + filterColumn;
		try (Statement stmt = connection.createStatement();) {
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()){
				String k = rs.getString(filterColumn);
				filterValues.add(k);
			}
			rs.close();
		} catch (SQLException e){
			System.err.println(e.getClass().getName() + ": " + e.getMessage() );
			e.printStackTrace();

		} 
		return  filterValues.toArray(new String[0]);
	}
	
	public List<Person> getSearchedPeople(String searchValue) {

		List<Person> searchedPpl = new ArrayList<Person>();

		String src = "%" + searchValue + "%";
		PreparedStatement prest = null;
		try {
			prest = connection.prepareStatement("SELECT * FROM People "
				+ "WHERE FName LIKE ? OR "
				+ "LName LIKE ? OR "
				+ "Nickname LIKE ? OR "
				+ "Birthday LIKE ?");
			
			prest.setString(1,src);
			prest.setString(2,src);
			prest.setString(3,src);
			prest.setString(4,src);

			ResultSet rs = prest.executeQuery();
			
			while(rs.next()){
				Person m = this.getPersonWithID(rs.getInt("ID"));
				searchedPpl.add(m);
			}
			rs.close();
		} catch (Exception e){
			System.err.println(e.getClass().getName() + ": " + e.getMessage() );
			e.printStackTrace();
			System.exit(0);
		}
		
		
		return searchedPpl; 
	}
	
	public List<Person> getFilteredPeople(String[] selectedFilters) {
		
		List<Person> filteredPpl = new ArrayList<Person>();

		for (int i = 0; i < selectedFilters.length; i++) {
			if ((selectedFilters[i] == null) || selectedFilters[i].equals("")) {
				selectedFilters[i] = "%";
			}
		}
	
		PreparedStatement prest = null;
		try {
			prest = connection.prepareStatement("SELECT * "
					+ "FROM People "
					+ "WHERE Category LIKE ? AND "
					+ "Subgroup LIKE ?");
			
			prest.setString(1,selectedFilters[0]);
			prest.setString(2,selectedFilters[1]);

			ResultSet rs = prest.executeQuery();
			
			while(rs.next()){
				Person m = this.getPersonWithID(rs.getInt("ID"));
				filteredPpl.add(m);
			}
			rs.close();
		} catch (Exception e){
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
		
		return filteredPpl; 
	}
	public List<Person> getNextSeven() {
		
		class DaysComparator implements Comparator<Person> {
			  
		    // override the compare() method
		    public int compare(Person p1, Person p2)
		    {
		        if (p1.getDaysTill() == p2.getDaysTill()) 
		            return 0;
		        else if (p1.getDaysTill() > p2.getDaysTill())
		            return 1;
		        else
		            return -1;
		    }
		}
		
		List<Person> nextUp = new ArrayList<Person>();
		ResultSet rs = null;
		try (Statement stmt = connection.createStatement();) {
			rs = stmt.executeQuery("SELECT * FROM People");
			while(rs.next()) {
				Person p = getPersonWithID(rs.getInt("ID"));
				nextUp.add(p);
		        Collections.sort(nextUp, new DaysComparator());		
			}
			rs.close();
		} catch (SQLException e){
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();

		} 
		return nextUp; // dummy return
	}
	
	public void addPerson() {
		PreparedStatement prest = null;		
		String sql = "INSERT INTO People (FName, LName, Nickname, Category, Subgroup, Birthday) VALUES (?,?,?,?,?,?)";

		try {
			prest = connection.prepareStatement(sql);
			prest.setString(1, "");
			prest.setString(2, "");
			prest.setString(3, "");
			prest.setString(4, "");
			prest.setString(5, "");
			prest.setString(6, "01-01-2001");
			prest.executeUpdate();
		} catch (SQLException e){
			System.err.println(e.getClass().getName() + ": " + e.getMessage() );
			e.printStackTrace();

		} 
	}
	
	public void delPerson(int id) {
		PreparedStatement prest = null;		
		String sql = "DELETE FROM People WHERE ID=?";
		
		try {
			prest = connection.prepareStatement(sql);
			prest.setInt(1, id);
			prest.executeUpdate();
		} catch (SQLException e){
			System.err.println(e.getClass().getName() + ": " + e.getMessage() );
			e.printStackTrace();

		} 

	}
	
	public void editPerson(int id, String info) {
		String[] split = info.split(";");

		
		PreparedStatement prest = null;		
		String sql = "UPDATE People "
				+ "SET FName = ?,"
				+ "LName = ?,"
				+ "Nickname = ?,"
				+ "Category = ?,"
				+ "Subgroup = ?,"
				+ "Birthday = ?"
				+ "WHERE ID = ?";

		try {
			prest = connection.prepareStatement(sql);
			prest.setString(1, split[0]);
			prest.setString(2, split[1]);
			prest.setString(3, split[2]);
			prest.setString(4, split[3]);
			prest.setString(5, split[4]);
			prest.setString(6, split[5]);
			prest.setInt(7, id);
			prest.executeUpdate();
		} catch (SQLException e){
			System.err.println(e.getClass().getName() + ": " + e.getMessage() );
			e.printStackTrace();

		} 

	}
	
	public void updateCSV() {
		CSVWriter writer;
		DateTimeFormatter dfs = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		try (Statement stmt = connection.createStatement();) { 
			FileOutputStream os = new FileOutputStream(Parameters.exportDir + "export-" + LocalDate.now().format(dfs) +  ".csv");
			os.write(0xef);
			os.write(0xbb);
			os.write(0xbf);
			writer = new CSVWriter(new OutputStreamWriter(os), ';', CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
			ResultSet rs = stmt.executeQuery("SELECT * FROM People");
			writer.writeAll(rs, true);
			writer.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}


	}

	
}




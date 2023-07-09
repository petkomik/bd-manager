package projects.petko.db;


public class DatabaseTester {
	
	public static void main(String[] args) {
		new CreateDatabase().createTables();
		new FillDatabase().fillTable();
	}
}
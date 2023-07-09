package projects.petko.general;

public class Parameters {
	
	public static final String stringSep = System.getProperty("file.separator");
	public static final String userDir = System.getProperty("user.dir");
	public static final String ressourcesDir = userDir + stringSep + "src" + stringSep + "main" + stringSep + "resources" + stringSep;
	public static final String exportDir = ressourcesDir + stringSep + "exports" + stringSep;


	public static final String databaseName = "birthdays.db";
	
	public static final String filenameCSV = "data.csv";
	
	public static final String iconSearch = "icon_search.png";
	public static final String iconClearSearch = "icon_clear_search.png";
	public static final String iconClearFilter = "icon_clear_filter.png";

	
}

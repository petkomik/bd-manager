package projects.petko.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class CSVReader {
	
	private String filename;
	private ArrayList<String[]> dbTokens;
	
	// intiate an instance with the desired file
	
	public CSVReader(String filename) {
		this.filename = filename;
		dbTokens = new ArrayList<String[]>();
	}
	
	private void parseLine(String line) {
		String[] tokens = line.split(";");
		// TODO string split error
		
		for(int i=0; i < tokens.length; i++) {
			if((tokens[i].length() > 0) && (tokens[i].charAt(0) == '"')) {
				tokens[i] = tokens[i].substring(1, tokens[i].length() - 1);
			}
		}
		
		dbTokens.add(tokens);
	}
	
	public List<String[]> readFile() {

		try(BufferedReader bufR = new BufferedReader(new InputStreamReader(
				new FileInputStream(this.filename), StandardCharsets.UTF_8))){
			String line = bufR.readLine();
			while ((line = bufR.readLine()) != null) {
				if (line.length() > 0) {
					parseLine(line);
				}
			}
			bufR.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.dbTokens;
	}
	
	
}

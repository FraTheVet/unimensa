package unimensa;

import java.sql.*;
import java.util.ArrayList;

public class Functionality {
	Connection database;
	PreparedStatement insert;
	PreparedStatement update;
	PreparedStatement delete;
	PreparedStatement deleteAll;
	PreparedStatement send;
	Statement read;
	ResultSet resultRead;

	/*
	 * add, remove, read queries gotta find a smart way for the signatures since
	 * we have different number of attributes. A solution could be a wise usage
	 * of arrays (see signatures) but we pass all strings, which sucks
	 * 
	 * Also contains what was in DBSync.java, for connecting to a DB
	 */
	// just an idea for the signature
	public int connect(String username, String password) {
		// connects to our UniMensa DB
		try {
			Class.forName("org.postgresql.Driver");
			database = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mensaDev",	username, password);
			// substitute jdbc:postgresql://localhost:5432/mensaDev with custom URI of your DB
			System.out.println("Connected to Mensa");
			database.setAutoCommit(true);
			System.out.println("Autocommit active");
			return 0;
		} catch (SQLException e) {
			System.out.println("DB is offline or Username/Password are wrong");
			return -1;
		} catch (ClassNotFoundException ex){
			System.out.println("Driver for Postgres not found");
			return -2;
		}
	}

	public Connection getConnection() {
		// getter
		return database;
	}

	public int setConnection(String URI, String uname, String pw) {
		// setter for custom DB Locations
		try {
			Class.forName("org.postgresql.Driver");
			database = DriverManager.getConnection(URI, uname, pw);
			System.out.println("Connected to " + URI);
			database.setAutoCommit(true);
			System.out.println("Autocommit active");
			return 0;
		} catch (SQLException e) {
			System.out.println("DB is offline or Username/Password are wrong");
			return -1;
		} catch (ClassNotFoundException ex){
			System.out.println("Driver for Postgres not found");
			return -2;
		}
	}

	public void insert(String tableName, String attributeNamesSeparatedByCommas,
			String[] attributeValues) {
		String insertTableSQL = "INSERT INTO " + tableName + "(location, capacity, tables) VALUES"
				+ "(?,?,?)";
		try {
			insert = database.prepareStatement(insertTableSQL);
			insert.setString(1, attributeValues[0]);
			insert.setInt(2, Integer.parseInt(attributeValues[1]));
			insert.setInt(3, Integer.parseInt(attributeValues[2]));
			System.out.println("Updating Insert query...");
			insert.executeUpdate(); // still not commit
			System.out.println("Updated. Committing Insert query...");
			database.commit(); // now commit
			System.out.println("Committed Insert query");
		} catch (SQLException e) {
			System.out.println("Connection lost while committing Insert query.");
		}
	}

	public void update(String tableName, String keyName, String keyValue, String updateName,
			String updateValue) {
		String updateTableSQL = "UPDATE " + tableName + " SET " + updateName + " = '" + updateValue
				+ "' WHERE " + keyName + " = '" + keyValue + "'";
		System.out.println(updateTableSQL);
		try {
			update = database.prepareStatement(updateTableSQL);
			System.out.println("Updating Update query...");
			update.executeUpdate(); // still not commit
			System.out.println("Updated. Committing Update query...");
			database.commit(); // now commit
			System.out.println("Committed Update query");
		} catch (SQLException e) {
			System.out.println("Connection lost while committing Update query.");
		}
	}

	public void delete(String tableName, String keyName, String keyValue) {
		String deleteTableSQL = "DELETE FROM " + tableName + " WHERE " + tableName + "." + keyName
				+ " = " + "'" + keyValue + "'";
		try {
			delete = database.prepareStatement(deleteTableSQL);
			System.out.println("Updating Delete query...");
			delete.executeUpdate(); // still not commit
			System.out.println("Updated. Committing Delete query...");
			database.commit(); // now commit
			System.out.println("Committed Delete query");
		} catch (SQLException e) {
			System.out.println("Connection lost while committing Delete query.");
		}
	}

	public void deleteAll(String tableName) {
		String deleteTableSQL = "DELETE FROM " + tableName;
		try {
			deleteAll = database.prepareStatement(deleteTableSQL);
			System.out.println("Updating DeleteAll query...");
			deleteAll.executeUpdate(); // still not commit
			System.out.println("Updated. Committing DeleteAll query...");
			database.commit(); // now commit
			System.out.println("Committed DeleteAll query");
		} catch (SQLException e) {
			System.out.println("Connection lost while executing DeleteAll query");
		}
	}
	
	public String[][] read(String tableName, String[] attributes) {
		String thingsToSeeSeparatedByCommas="";
		for (int i =0; i< attributes.length-1; i++){
			thingsToSeeSeparatedByCommas+=(attributes[i]+", ");
		}
		thingsToSeeSeparatedByCommas+=attributes[attributes.length-1];
		String readTableSQL = "SELECT " + thingsToSeeSeparatedByCommas + " FROM " + tableName;
		int columns = attributes.length;
		String[][] readString = null;
		try {
			read = database.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			resultRead = read.executeQuery(readTableSQL);
			resultRead.last();
			int rows = resultRead.getRow();
			resultRead.beforeFirst();
			readString = new String[rows+1][columns];
			int b = 1;
			readString[0]=attributes;
			while (resultRead.next()) {
				for (int a = 0; a < columns; a++) {
					readString[b][a] = resultRead.getString(attributes[a]);
				}
				b++;
			}
			System.out.println("Data read.");
		} catch (SQLException e) {
			System.out.println("Connection lost while committing Read query.");
		}
		return readString;
	}
	public String[][] read(String query) {
		int columns = 0;
		String[] components = query.split(" ");
		ArrayList<String> titles = new ArrayList<String>();
		int lastcomponent=0;
		for (int i = 0; i < components.length; i++) {
			if (components[i].toUpperCase().equals("SELECT")) {
				for (int j=i+1; j< components.length;j++){
					if (components[j].charAt(components[j].length()-1)==(44)){
						titles.add(components[j].substring(0, components[j].length()-1));
						columns++;
						lastcomponent=j+1;
					}
				}
				titles.add(components[lastcomponent]);
				columns++;
			}
		}
		String[] columnNames = new String[columns];
		for (int i=0; i< columns; i++){
			columnNames[i]=titles.get(i);
		}
		String[][] readString = null;
		try {
			read = database.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			resultRead = read.executeQuery(query);
			resultRead.last();
			int rows = resultRead.getRow();
			resultRead.beforeFirst();
			readString = new String[rows+1][columns];
			int b = 1;
			readString[0]=columnNames;
			while (resultRead.next()) {
				for (int a = 0; a < columns; a++) {
					readString[b][a] = resultRead.getString(columnNames[a]);
				}
				b++;
			}
			System.out.println("Data read.");
		} catch (SQLException e) {
			System.out.println("Connection lost while committing Read query.");
			e.printStackTrace();
		}
		return readString;
	}
}
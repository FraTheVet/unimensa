package unimensa;

public class MainWindow {
	/*
	 * main class, creates window with all sub-windows no functionalities like
	 * add data, remove data, etc... respect indentation o ve ammazzo scuoiati
	 * let's take the standard to comment what we add and alert everyone when
	 * you add a class and why pls <3 <3 <3 <3 <3 <3 <3 <3 <3 <3 <3 <3 <3 <3 <3
	 * <3 <3 <3
	 */
	public static void main(String[] args) {
		// Some rows of testing the Functionality class, pretty straightforward
		Functionality func = new Functionality();
		func.connect("postgres", "password"); // substitute with real UName & PW
		String[] values = { "Bolzano", "131", "145" };
		String[] values2 = { "Bressanone", "98", "70" };
		func.insert("unimensa", "location, tables, capacity", values);
		func.insert("unimensa", "location, tables, capacity", values2);
		func.update("unimensa", "location", "Bressanone", "capacity", "101");
		// func.delete("unimensa", "location", "Bolzano");
		// func.deleteAll("unimensa");
		String[][] readValues = func.read("unimensa", "location, tables, capacity");
		System.out.println("\nPrinting values:");
		// SUPER IMPORTANT - Keep this for loop as for a printer in the correct
		// format of Functionality.read method
		for (int l = 0; l < readValues.length; l++) {
			for (int i = 0; i < readValues[l].length; i++) {
				if (i > (readValues[l].length - 1)
						|| (i - (readValues[l].length - 1)) % readValues[l].length == 0) {
					System.out.println(readValues[l][i]);
				} else {
					System.out.print(readValues[l][i] + ", ");
				}
			}
		}
	}
}

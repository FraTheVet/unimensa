package unimensa;

public class MetaData {
	public static String username = "";
	public static String password = "";
	public static Functionality func = new Functionality();

	public static String[] unimensa() {
		return new String[] { "unimensa", "location", "tables", "capacity" };
	}

	public static String[] people() {
		return new String[] { "people", "universityID", "role", "location", "discount" };
	}

	public static String[] menuprice() {
		return new String[] { "menuprice", "discount", "fullmenu", "lightmenu", "extralightmenu" };
	}

	public static String[] employee() {
		return new String[] { "employee", "id", "contracttype", "firstname", "lastname",
				"dateofbirth", "address", "entryyear", "joblocation" };
	}

	public static String[] chef() {
		return new String[] { "chef", "employee.id", "licenceno" };
	}

	public static String[] staffmember() {
		return new String[] { "staffmember", "employee.id", "role" };
	}

	public static String[] dailymenu() {
		return new String[] { "dailymenu", "id", "dow", "wom", "night", "university.location" };
	}

	public static String[] dish() {
		return new String[] { "dish", "id", "course", "name", "veg" };
	}

	public static String[] ingredient() {
		return new String[] { "ingredient", "id", "name", "quantity" };
	}

	public static String[] distributor() {
		return new String[] { "distributor", "id", "typeofprovision" };
	}

	public static String[] has() {
		return new String[] { "has", "people.universityID", "menuprice.discount" };
	}

	public static String[] cater() {
		return new String[] { "cater", "people.universityID", "unimensa.location" };
	}

	public static String[] job() {
		return new String[] { "job", "unimensa.location", "employee.id" };
	}

	public static String[] lists() {
		return new String[] { "lists", "unimensa.location", "dailymenu.id" };
	}

	public static String[] contains() {
		return new String[] { "contains", "dailymenu.id", "dish.id" };
	}

	public static String[] provided() {
		return new String[] { "provided", "distributor.id", "dish.id" };
	}

	public static String[] made() {
		return new String[] { "made", "dish.id", "ingredient.id" };
	}
}

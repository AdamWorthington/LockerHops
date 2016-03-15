package com.lockerhops.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class Item {

	//Database information
	static final String url 		= DatabaseAccessors.url;
	static final String dbName 		= DatabaseAccessors.dbName;
	static final String username 	= DatabaseAccessors.username;
	static final String password 	= DatabaseAccessors.password;
	static final String driver 		= DatabaseAccessors.driver;
	static final String jdbcUrl 	= DatabaseAccessors.jdbcUrl;
	
	String 		restaurant;		//Name of the restaurant (limit 40 characters)
	String 		item;			//Name of the item (limit 45 characters)
	String 		description;	//Description of the item (limit 280 characters)
	double 		cost;			//Cost of item (Max: 9999.99, Min: 0.00)
	String 		category;		//Small granularity category (breakfast, lunch, dinner)
	String 		subCategory;	//High granularity category (food type)
	
	//These are not mandatory and therefore have defaults
	String[]	ingredients	= null;		//List of ingredients in this item (OPTIONAL)
	boolean 	glutenFree  = false;	//Is this item gluten free? (OPTIONAL)
	boolean 	vegetarian  = false;	//Is this item vegetarian? (OPTIONAL)
	boolean 	vegan		= false;	//Is this item vegan? (OPTIONAL)
	
	public Item(
			String 		restaurant,
			String 		item,
			String 		description,
			double 		cost,
			String 		category,
			String 		subCategory,
			String[]	ingredients,
			boolean 	glutenFree,
			boolean 	vegetarian,
			boolean 	vegan
		) {
		this.restaurant 	= restaurant;
		this.item 			= item;
		this.description 	= description;
		this.cost 			= cost;
		this.category 		= category;
		this.subCategory 	= subCategory;
		this.ingredients 	= ingredients;
		this.glutenFree 	= glutenFree;
		this.vegetarian 	= vegetarian;
		this.vegan 			= vegan;
	}

	public String getRestaurant() {
		return this.restaurant;
	}

	public String getItem() {
		return this.item;
	}

	public String getDescription() {
		return this.description;
	}

	public double getCost() {
		return this.cost;
	}

	public String getCategory() {
		return this.category;
	}

	public String getSubCategory() {
		return this.subCategory;
	}

	public String[] getIngredients() {
		return this.ingredients;
	}

	public boolean isGlutenFree() {
		return this.glutenFree;
	}

	public boolean isVegetarian() {
		return this.vegetarian;
	}

	public boolean isVegan() {
		return this.vegan;
	}
	
	/*
	 * Generic method for checking validity of string arguments
	 */
	public static boolean stringIsValid(String s) {
		if (s == null || s == "") return false;
		return true;
	}
	
	/*
	 * Add an item from a restaurant to the database
	 */
	public boolean addRestaurantItem() {
		//PERFORM ARGUMENT VALIDATION HERE
		if (!stringIsValid(this.restaurant)) {
			System.out.println("Invalid restaurant in addRestaurantItem");
		}
		if (!stringIsValid(this.item)) {
			System.out.println("Invalid item name in addRestaurantItem");
		}
		if (!stringIsValid(this.description)) {
			System.out.println("Invalid description in addRestaurantItem");
		}
		if (this.cost > 9999.99 || this.cost < 0) {
			System.out.println("Invalid cost in addRestaurantItem");
		}
		if (!stringIsValid(this.category)) {
			System.out.println("Invalid category in addRestaurantItem");
		}
		if (!stringIsValid(this.subCategory)) {
			System.out.println("Invalid subCategory in addRestaurantItem");
		}


		//The prepared statement for this order
		PreparedStatement	stmt	= null;

		//The connection to the database
		Connection			conn	= null;

		//The SQL query to update this order's information
		String query = "INSERT INTO Restaurant_Items " + 
				"(`Restaurant`, `Item`, `Description`, `Cost`, `Category`, `Sub-Category`, `Ingredients`, `Gluten-Free`, `Vegetarian`, `Vegan`) VALUES " + 
				"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

		//Check we can get the driver
		try {
			System.out.print("Checking driver: ");
			Class.forName(driver);
			System.out.println("SUCCESS");
		} 
		catch (ClassNotFoundException e) {
			System.out.println("Cannot find the driver in the classpath:    ");
			e.printStackTrace();
			return false;
		}

		try {
			//Acquire a connection using the specified driver
			System.out.print("Creating connection: ");
			conn = DriverManager.getConnection(jdbcUrl);
			System.out.println("SUCCESS");

			//Prepare the statement based on the given query
			System.out.print("Preparing statement: ");
			stmt = conn.prepareStatement(query);
			System.out.println("SUCCESS");

			//Add values to the prepared statement
			//Restaurant, Item, Description, Cost, Category, Sub-Category, Ingredients, Gluten-Free, Vegetarian, Vegan
			System.out.print("Setting statement values: ");
			stmt.setString(1, this.restaurant);
			System.out.print("1 ");

			stmt.setString(2, this.item);
			System.out.print("2 ");

			stmt.setString(3, this.description);
			System.out.print("3 ");

			stmt.setDouble(4, this.cost);
			System.out.print("4 ");

			stmt.setString(5, this.category);
			System.out.print("5 ");

			stmt.setString(6, this.subCategory);
			System.out.print("6 ");

			if (this.ingredients == null) {
				stmt.setString(7, null);
			}
			else {
				stmt.setString(7, String.join(",", this.ingredients));
			}
			System.out.print("7 ");

			int isGlutenFree = (this.glutenFree) ? 1 : 0;
			stmt.setInt(8, isGlutenFree);
			System.out.print("8 ");

			int isVegetarian = (this.vegetarian) ? 1 : 0;
			stmt.setInt(9, isVegetarian);
			System.out.print("9 ");

			int isVegan = (this.vegan) ? 1 : 0;
			stmt.setInt(10, isVegan);
			System.out.println("10");

			//Execute the statement to insert this order into the database
			System.out.print("Executing statement: ");
			int ret = stmt.executeUpdate();

			//
			if (ret != 0) {
				System.out.println("SUCCESS");
				return true;
			}
		}
		catch(SQLException e) {
			System.out.println("Unable to create and execute statement:    ");
			e.printStackTrace();
			return false;
		}

		//This is only reached if there was no value assigned to the insertion, meaning it failed, or an exception occured
		System.out.println("FAILURE");
		return false;
	}

	/*
	 * Returns all items that match to the values in the given parameter
	 * For example, if the parameter item passed in has values for Restaurant, Category, and Vegan, then all items
	 *  from that restaurant's category that are vegan will be returned.
	 *  
	 * Currently with the cost option you can only search for values that are less than a given cost.
	 * Currently not supporting searching by ingredients.  May need to change database to support this
	 */
	public ArrayList<Item> getRestaurantItems() {

		//The prepared statement for this order
		PreparedStatement	stmt	= null;

		//The connection to the database
		Connection			conn	= null;

		//The SQL query to update this order's information
		String query = "SELECT Restaurant, Item, Description, Cost, Category, Sub-Category, Ingredients, Gluten-Free, Vegetarian, Vegan "
				+ "FROM Restaurant_Items ";

		boolean first = true;
		String addWhere			= "WHERE ";
		String addRestaurant 	= "Restaurant=? ";
		String addItem 			= "Item=? ";
		String addCost 			= "Cost=? ";
		String addCostLess 		= "Cost<? ";
		String addCostGreater 	= "Cost>? ";
		String addCategory 		= "Category=? ";
		String addSubCategory 	= "Sub-Category=? ";
		String addIngredient 	= " ";		//May be added more than 1 time for multiple ingredients
		String addGlutenFree 	= "Gluten-Free=1 ";
		String addVegetarian 	= "Vegetarian=1 ";
		String addVegan 		= "Vegan=1 ";
		String ending			= ";";
		String and				= "AND ";

		if (stringIsValid(this.restaurant)) {
			System.out.println("Adding restaurant to query in getRestaurantItems");
			if (first) {
				query += addWhere;
				first = false;
			}
			else {
				query += and;
			}
			query += addRestaurant;
		}

		if (stringIsValid(this.item)) {
			System.out.println("Adding item to query in getRestaurantItems");
			if (first) {
				query += addWhere;
				first = false;
			}
			else {
				query += and;
			}
			query += addItem;
		}

		//CURRENTLY ONLY SUPPORTING SEARCHING FOR ITEMS WITH COST LESS THAN XYZ
		if (this.cost > 0) {
			System.out.println("Adding cost to query in getRestaurantItems");
			if (first) {
				query += addWhere;
				first = false;
			}
			else {
				query += and;
			}
			query += addCostLess;
		}

		if (stringIsValid(this.category)) {
			System.out.println("Adding category to query in getRestaurantItems");
			if (first) {
				query += addWhere;
				first = false;
			}
			else {
				query += and;
			}
			query += addCategory;
		}

		if (stringIsValid(this.subCategory)) {
			System.out.println("Adding subCategory to query in getRestaurantItems");
			if (first) {
				query += addWhere;
				first = false;
			}
			else {
				query += and;
			}
			query += addSubCategory;
		}

		//FIGURE OUT HOW TO DO INGREDIENTS HERE

		if (this.glutenFree) {
			System.out.println("Adding glutenFree to query in getRestaurantItems");
			if (first) {
				query += addWhere;
				first = false;
			}
			else {
				query += and;
			}
			query += addGlutenFree;
		}

		if (this.vegetarian) {
			System.out.println("Adding vegetarian to query in getRestaurantItems");
			if (first) {
				query += addWhere;
				first = false;
			}
			else {
				query += and;
			}
			query += addVegetarian;
		}

		if (this.vegan) {
			System.out.println("Adding vegan to query in getRestaurantItems");
			if (first) {
				query += addWhere;
				first = false;
			}
			else {
				query += and;
			}
			query += addVegan;
		}
		query += ending;

		System.out.println("Query: " + query);



		//Check we can get the driver
		try {
			System.out.print("Checking driver in getRestaurantItems: ");
			Class.forName(driver);
			System.out.println("SUCCESS");
		} 
		catch (ClassNotFoundException e) {
			System.out.println("Cannot find the driver in the classpath in getRestaurantItems:    ");
			e.printStackTrace();
			return null;
		}

		try {
			//Acquire a connection using the specified driver
			System.out.print("Creating connection in getRestaurantItems: ");
			conn = DriverManager.getConnection(jdbcUrl);
			System.out.println("SUCCESS");

			//Prepare the statement based on the given query
			System.out.print("Preparing statement in getRestaurantItems: ");
			stmt = conn.prepareStatement(query);
			System.out.println("SUCCESS");

			//Add values to the prepared statement
			int count = 1;
			//restaurant, item, cost, category, subCategory, ingredients, glutenFree, vegetarian, vegan
			System.out.print("Setting statement values in getRestaurantItems: ");
			if (stringIsValid(this.restaurant)) {
				stmt.setString(count, this.restaurant);
				System.out.print(count + " ");
				count++;
			}

			if (stringIsValid(this.item)) {
				stmt.setString(count, this.item);
				System.out.print(count + " ");
				count++;
			}

			if (this.cost > 0) {
				stmt.setDouble(count, this.cost);
				System.out.print(count + " ");
				count++;
			}

			if (stringIsValid(this.category)) {
				stmt.setString(count, this.category);
				System.out.print(count + " ");
				count++;
			}

			if (stringIsValid(this.subCategory)) {
				stmt.setString(count, this.subCategory);
				System.out.print(count + " ");
				count++;
			}

			//FIGURE OUT HOW TO DO INGREDIENTS HERE
			System.out.println();

			System.out.print("QUERY IS: " + stmt.toString());
			return null;

			/*
			//Execute the statement to insert this order into the database
			System.out.print("Executing statement in getRestaurantItems: ");
			ResultSet rs = stmt.executeQuery();

			//Item, Description, Cost, Category, Sub-Category, Ingredients, Gluten-Free, Vegetarian, Vegan
			ArrayList<Item> items = new ArrayList<Item>();
			String name, itemName, description, category, subCategory;
			double cost;
			String[] ingredients;
			while(rs.next()) {
				boolean glutenFree, vegetarian, vegan;

				//Retrieve values from ResultSet and build the array of Items for this restaurant
				name 		= rs.getString("Restaurant");
				itemName	= rs.getString("Item");
				description	= rs.getString("Description");
				cost 		= rs.getDouble("Cost");
				category 	= rs.getString("Category");
				subCategory = rs.getString("Sub-Category");

				Array ingrTemp = rs.getArray("Ingredients");
				ingredients = (String[]) ingrTemp.getArray();

				glutenFree	= (rs.getInt("") == 1) ? true : false;
				vegetarian 	= (rs.getInt("") == 1) ? true : false;
				vegan 		= (rs.getInt("") == 1) ? true : false;

				Item i = new Item(name, itemName, description, cost, category, subCategory, ingredients, glutenFree, vegetarian, vegan);
				items.add(i);
			}
			System.out.println("SUCCESS");
			return items;
			 */
		}
		catch(SQLException e) {
			System.out.println("Unable to create and execute statement in getRestaurantItems:    ");
			e.printStackTrace();
			return null;
		}
	}
}

package jumpit.lockereats.server;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONObject;

//TODO: figure out how to link order object and the entry in the database
//TODO: decide if worth it to move these methods to the order class (so can call in an object oriented style)
//TODO: decide if it is better to have 1 large orders table or multiple tables per restaurant
//TODO: should probably be using AWS CodeCommit & CodeDeploy instead of Github...
//TODO: if the server code runs 100% of the time on aws we should use a connection pool
//	  : would need to write that code (or find it if it already exists)
//TODO: clean up the query generation process
//    : turn the repetitive if statements into 1 method (need to pass by reference)

//TODO: DB table for orders:
//	-Only need 1 table that contains all restaurant orders (a query for a specific restaurant returns
//	 all possible orders from that restaurant which is essentially their menu)
//	-Order: Name, description, cost, category (breakfast, lunch, dinner, etc), subcategory (food type i.e.
//	 calzone, panini, pizza, salad, etc. Get these from the restaurant).
//	 -Maybe include ingredients (handle same way as items in order)
//	-Need some way to handle combos (just make them a separate order with their own "combo" category)
//	-Need to be able to handle additions (i.e. add ketchup for $.35, handle similarly to ^)
//	 -Category is "addition", subcategory is the type of food you can add this too (breakfast, lunch, dinner, etc)
//	-Need to be able to handle different sizes of item
//	-WILL PROBABLY END UP HAVING TO BE RESTAURANT DEPENDANT UNLESS A GOOD, RELATIVELY UNIVERSAL LAYOUT
//	 CAN BE FOUND AND EASILY USED


public class DatabaseAccessors {

	//Database information
	static final String url 		= "lockerhopsmain.c2fmvfgm3x77.us-east-1.rds.amazonaws.com:3306";
	static final String dbName 		= "LockerHopsMain";
	static final String username 	= "LockerHops";
	static final String password 	= "!25spoHrekcoL?9";
	static final String driver 		= "com.mysql.jdbc.Driver";
	static final String jdbcUrl 	= "jdbc:mysql://" + url + "/" + dbName + "?user=" + username + "&password=" + password;

	/*
	 * placeOrder
	 * updateTimePlacedInLocker
	 * updatePickupTime
	 * addRestaurantItem
	 * addRestaurant
	 * 
	 * TODO:
	 * -getRestaurantInfo
	 * -getRestaurantItems (with additional search terms)
	 * -
	 * -
	 * -
	 */

	/*
	 * Store the order in the database and updates this order's ID (as per identifier assigned by database)
	 * @param order: the order to be placed in the database
	 * Returns true if successfully entered into DB, false otherwise
	 */
	public static boolean placeOrder(Order order) {

		//If the order is not well formed we should not do anything with it
		if (!order.isWellFormed()) {
			return false;
		}

		//The prepared statement for this order
		PreparedStatement	stmt	= null;

		//The connection to the database
		Connection			conn	= null;

		//The SQL query that will place the order's values into the DB
		String				query	= "INSERT INTO Orders (`Restaurant`, `Cost`) VALUES (?, ?);";	
		String				query2	= "INSERT INTO Order_Items (`OrderID`, `Item`) VALUES (?, ?);";

		//Formats the array of items into a single string where each item is separated by a comma
		String				items	= String.join(",", order.items);

		//Load the driver for this class
		try {
			System.out.print("Checking driver in placeOrder: ");
			Class.forName(driver);
			System.out.println("SUCCESS");
		} 
		catch (ClassNotFoundException e) {
			System.out.println("Cannot find the driver in the classpath (placeOrder):    ");
			e.printStackTrace();
			return false;
		}

		try {
			//Acquire a connection using the specified driver
			System.out.print("Creating connection in placeOrder: ");
			conn = DriverManager.getConnection(jdbcUrl);
			System.out.println("SUCCESS");

			//RETURN_GENERATED_KEYS enables the execution of this statement to return the ID of the order (as in the DB)
			System.out.print("Preparing statement 1 in placeOrder: ");
			stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			System.out.println("SUCCESS");

			//Prepared statements are used to prevent SQL injection
			System.out.print("Setting statement 1 values in placeOrder: ");
			stmt.setString(1, order.getRestaurant());
			System.out.print("1 ");
			stmt.setDouble (2, order.getCost());
			System.out.println("2");

			//Execute the statement to insert this order into the database
			System.out.print("Executing statement 1: ");
			int ret = stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();


			int orderID = rs.getInt(1);

			if(orderID > 0) {
				//This allows us to link the order in the DB to the order object itself
				order.setID(orderID);
				System.out.println("SUCCESS (Order ID:" + orderID + ")");
			}
			else {
				return false;
			}

			//Prepare the queries for each item
			for (String item : order.items) {
				System.out.print("Preparing statement 2 in placeOrder: ");
				stmt = conn.prepareStatement(query2);
				System.out.println("SUCCESS");

				System.out.print("Setting statement 2 values in placeOrder: ");
				stmt.setInt(1, order.id);
				System.out.print("1 ");
				stmt.setString(2, item);
				System.out.println("2");

				System.out.print("Executing statement 2 (" + item + ") in placeOrder: ");
				ret = stmt.executeUpdate();

				if (ret <= 0) {
					System.out.println("FAILURE");
					return false;
				}
				System.out.println("SUCCESS");
			}
			stmt.close();
		}
		catch(SQLException e) {
			System.out.println("Unable to create and execute statement (placeOrder):    ");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/*
	 * Update the time an order was placed in the lockers
	 * @param order: the order entry to update.
	 * @param datetime: the datetime value when the item was handled
	 * Returns true if update was successful, false otherwise
	 */
	public static boolean updateTimePlacedInLocker(Order order, String datetime) {
		return updateOrderTime(order, datetime, 0);
	}

	/*
	 * Update the time an order was picked up
	 * @param order: the order entry to update.
	 * @param datetime: the datetime value when the item was handled
	 * Returns true if update was successful, false otherwise
	 */
	public static boolean updatePickupTime(Order order, String datetime) {
		return updateOrderTime(order, datetime, 1);
	}

	/*
	 * Update in the database the time an order
	 * @param order: the order entry to update.
	 * @param datetime: the datetime value when the item was handled
	 * @param type: 0 for time placed in locker, 1 for time picked up	IMPORTANT
	 * Returns true if update was successful, false otherwise
	 */
	private static boolean updateOrderTime(Order order, String datetime, int type) {
		//An order ID of -1 indicates it has not yet been inserted into the DB
		if (order.id == -1) {
			System.out.println("Order has not been placed in database yet");
			return false;
		}

		//If the order is not well formed we should not do anything with it
		if (!order.isWellFormed()) {
			return false;
		}

		//Validate datetime
		if (datetime == null || datetime == "") {
			System.out.println("Invalid DateTime in updateOrderTime (type: " + type + ")");
			return false;
		}

		//The prepared statement for this order
		PreparedStatement	stmt	= null;

		//The connection to the database
		Connection			conn	= null;

		//The SQL query to update this order's information
		String 				query;
		if (type == 0) {
			query = "UPDATE Orders SET TimePlacedInLocker = ? WHERE OrderID = ?;";
		}
		else if (type == 1) {
			query = "UPDATE Orders SET TimePickedUp = ? WHERE OrderID = ?;";
		}
		else {
			System.out.println("Invalid type for updateOrderTime (" + type + ")");
			return false;
		}

		//Check we can get the driver
		try {
			System.out.print("Checking driver in updateOrderTime (type: " + type + "): ");
			Class.forName(driver);
			System.out.println("SUCCESS");
		} 
		catch (ClassNotFoundException e) {
			System.out.println("Cannot find the driver in the classpath (updateOrderTime (type: " + type + ")):    ");
			e.printStackTrace();
			return false;
		}

		try {
			//Acquire a connection using the specified driver
			System.out.print("Creating connection in updateOrderTime (type: " + type + "): ");
			conn = DriverManager.getConnection(jdbcUrl);
			System.out.println("SUCCESS");

			//
			System.out.print("Preparing statement in updateOrderTime (type: " + type + "): ");
			stmt = conn.prepareStatement(query);
			System.out.println("SUCCESS");

			//Prepared statements are used to prevent SQL injection
			System.out.print("Setting statement values in updateOrderTime (type: " + type + "): ");
			stmt.setString(1,  datetime);
			System.out.print("1 ");
			stmt.setInt(2,  order.id);
			System.out.println("2");

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
			System.out.println("Unable to create and execute statement (updateOrderTime (type: " + type + ")):    ");
			e.printStackTrace();
			return false;
		}

		//This is only reached if there was no value assigned to the insertion, meaning it failed, or an exception occured
		System.out.println("FAILURE");
		return false;
	}

	/*
	 * Add an item from a restaurant to the database
	 */
	public boolean addRestaurantItem(
			String 		restaurant,		//Name of the restaurant (limit 40 characters)
			String 		item,			//Name of the item (limit 45 characters)
			String 		description,	//Description of the item (limit 280 characters)
			double 		cost,			//Cost of item (Max: 9999.99, Min: 0.00)
			String 		category,		//Small granularity category (breakfast, lunch, dinner)
			String 		subCategory,	//High granularity category (food type)
			String[]	ingredients,	//List of ingredients in this item (OPTIONAL)
			boolean 	glutenFree,		//Is this item gluten free? (OPTIONAL)
			boolean 	vegetarian,		//Is this item vegetarian? (OPTIONAL)
			boolean 	vegan			//Is this item vegan? (OPTIONAL)
			) {


		//PERFORM ARGUMENT VALIDATION HERE
		if (!stringIsValid(restaurant)) {
			System.out.println("Invalid restaurant in addRestaurantItem");
		}
		if (!stringIsValid(item)) {
			System.out.println("Invalid item name in addRestaurantItem");
		}
		if (!stringIsValid(description)) {
			System.out.println("Invalid description in addRestaurantItem");
		}
		if (cost > 9999.99 || cost < 0) {
			System.out.println("Invalid cost in addRestaurantItem");
		}
		if (!stringIsValid(category)) {
			System.out.println("Invalid category in addRestaurantItem");
		}
		if (!stringIsValid(subCategory)) {
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
			stmt.setString(1, restaurant);
			System.out.print("1 ");

			stmt.setString(2, item);
			System.out.print("2 ");

			stmt.setString(3, description);
			System.out.print("3 ");

			stmt.setDouble(4, cost);
			System.out.print("4 ");

			stmt.setString(5, category);
			System.out.print("5 ");

			stmt.setString(6, subCategory);
			System.out.print("6 ");

			if (ingredients == null) {
				stmt.setString(7, null);
			}
			else {
				stmt.setString(7, String.join(",", ingredients));
			}
			System.out.print("7 ");

			int isGlutenFree = (glutenFree) ? 1 : 0;
			stmt.setInt(8, isGlutenFree);
			System.out.print("8 ");

			int isVegetarian = (vegetarian) ? 1 : 0;
			stmt.setInt(9, isVegetarian);
			System.out.print("9 ");

			int isVegan = (vegan) ? 1 : 0;
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
	public static ArrayList<Item> getRestaurantItems(Item item) {

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

		if (stringIsValid(item.restaurant)) {
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

		if (stringIsValid(item.item)) {
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
		if (item.cost > 0) {
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

		if (stringIsValid(item.category)) {
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

		if (stringIsValid(item.subCategory)) {
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

		if (item.glutenFree) {
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

		if (item.vegetarian) {
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

		if (item.vegan) {
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
			if (stringIsValid(item.restaurant)) {
				stmt.setString(count, item.restaurant);
				System.out.print(count + " ");
				count++;
			}

			if (stringIsValid(item.item)) {
				stmt.setString(count, item.item);
				System.out.print(count + " ");
				count++;
			}

			if (item.cost > 0) {
				stmt.setDouble(count, item.cost);
				System.out.print(count + " ");
				count++;
			}

			if (stringIsValid(item.category)) {
				stmt.setString(count, item.category);
				System.out.print(count + " ");
				count++;
			}

			if (stringIsValid(item.subCategory)) {
				stmt.setString(count, item.subCategory);
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

	public static ArrayList<Restaurant> getRestaurants(Restaurant restaurant) {
		ArrayList<Restaurant> restaurants = null;

		//The prepared statement for this order
		PreparedStatement	stmt	= null;

		//The connection to the database
		Connection			conn	= null;

		//The SQL query to update this order's information
		String 				query = "SELECT Restaurant, Address, Phone_Number, Website, Type FROM Restaurants ";

		boolean first = true;
		String addWhere		= "WHERE ";
		String addName		= "Restaurant=? ";
		String addAddress	= "Address=? ";
		String addPhone		= "Phone_Number=? ";
		String addWebsite	= "Website=? ";
		String addType		= "Type=? ";
		String ending		= ";";
		String and			= "AND ";

		if (stringIsValid(restaurant.name)) {
			System.out.println("Adding name to query in getRestaurants");
			if (first) {
				query += addWhere;
				first = false;
			}
			else {
				query += and;
			}
			query += addName;
		}
		if (stringIsValid(restaurant.address)) {
			System.out.println("Adding address to query in getRestaurants");
			if (first) {
				query += addWhere;
				first = false;
			}
			else {
				query += and;
			}
			query += addAddress;
		}
		if (stringIsValid(restaurant.phone)) {
			System.out.println("Adding phone to query in getRestaurants");
			if (first) {
				query += addWhere;
				first = false;
			}
			else {
				query += and;
			}
			query += addPhone;
		}
		if (stringIsValid(restaurant.website)) {
			System.out.println("Adding website to query in getRestaurants");
			if (first) {
				query += addWhere;
				first = false;
			}
			else {
				query += and;
			}
			query += addWebsite;
		}
		if (stringIsValid(restaurant.type)) {
			System.out.println("Adding type to query in getRestaurants");
			if (first) {
				query += addWhere;
				first = false;
			}
			else {
				query += and;
			}
			query += addType;
		}
		query += ending;


		//Check we can get the driver
		try {
			System.out.print("Checking driver in getRestaurants: ");
			Class.forName(driver);
			System.out.println("SUCCESS");
		} 
		catch (ClassNotFoundException e) {
			System.out.println("Cannot find the driver in the classpath in getRestaurants:    ");
			e.printStackTrace();
			return null;
		}

		try {
			//Acquire a connection using the specified driver
			System.out.print("Creating connection in getRestaurants: ");
			conn = DriverManager.getConnection(jdbcUrl);
			System.out.println("SUCCESS");

			//Prepare the statement based on the given query
			System.out.print("Preparing statement in getRestaurants: ");
			stmt = conn.prepareStatement(query);
			System.out.println("SUCCESS");

			//Add values to the prepared statement
			int count = 1;
			//restaurant, item, cost, category, subCategory, ingredients, glutenFree, vegetarian, vegan
			System.out.print("Setting statement values in getRestaurants: ");
			if (stringIsValid(restaurant.name)) {
				stmt.setString(count, restaurant.name);
				System.out.print(count + " ");
				count++;
			}

			if (stringIsValid(restaurant.address)) {
				stmt.setString(count, restaurant.address);
				System.out.print(count + " ");
				count++;
			}

			if (stringIsValid(restaurant.phone)) {
				stmt.setString(count, restaurant.phone);
				System.out.print(count + " ");
				count++;
			}

			if (stringIsValid(restaurant.website)) {
				stmt.setString(count, restaurant.website);
				System.out.print(count + " ");
				count++;
			}
			
			if (stringIsValid(restaurant.type)) {
				stmt.setString(count, restaurant.type);
				System.out.print(count + " ");
				count++;
			}
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
			System.out.println("Unable to create and execute statement in getRestaurants:    ");
			e.printStackTrace();
			return null;
		}
	}

	public static boolean addRestaurant(String name, String address, String phone, String website, String type) {
		if (!stringIsValid(name)) {

		}
		if (!stringIsValid(address)) {
			System.out.println("Invalid address in addRestaurant (" + address + ")");
		}
		if (!stringIsValid(phone)) {
			System.out.println("Invalid phone in addRestaurant (" + phone + ")");
		}
		if (!stringIsValid(website)) {
			System.out.println("Invalid website in addRestaurant (" + website + ")");
		}
		if (!stringIsValid(type)) {
			System.out.println("Invalid type in addRestaurant (" + type + ")");
		}

		//The prepared statement for this order
		PreparedStatement	stmt	= null;

		//The connection to the database
		Connection			conn	= null;

		//The SQL query to update this order's information
		String 				query = "INSERT INTO Restaurants (`Restaurant`, `Address`, `Phone_Number`, `Website`, `Type`) VALUES " +
				"(?, ?, ?, ?, ?);";

		//Check we can get the driver
		try {
			System.out.print("Checking driver in addRestaurant: ");
			Class.forName(driver);
			System.out.println("SUCCESS");
		} 
		catch (ClassNotFoundException e) {
			System.out.println("Cannot find the driver in the classpath in addRestaurant:    ");
			e.printStackTrace();
			return false;
		}

		try {
			//Acquire a connection using the specified driver
			System.out.print("Creating connection in addRestaurant: ");
			conn = DriverManager.getConnection(jdbcUrl);
			System.out.println("SUCCESS");

			//Prepare the statement based on the given query
			System.out.print("Preparing statement in addRestaurant: ");
			stmt = conn.prepareStatement(query);
			System.out.println("SUCCESS");

			//Add values to the prepared statement
			System.out.print("Setting statement values in addRestaurant: ");
			stmt.setString(1, name);
			System.out.print("1 ");
			stmt.setString(2, address);
			System.out.print("2 ");
			stmt.setString(3, phone);
			System.out.print("3 ");
			stmt.setString(4, website);
			System.out.print("4 ");
			stmt.setString(5, type);
			System.out.println("5");

			//Execute the statement to insert this order into the database
			System.out.print("Executing statement in addRestaurant: ");
			int ret = stmt.executeUpdate();

			//
			if (ret != 0) {
				System.out.println("SUCCESS");
				return true;
			}
		}
		catch(SQLException e) {
			System.out.println("Unable to create and execute statement in addRestaurant:    ");
			e.printStackTrace();
			return false;
		}

		//This is only reached if there was no value assigned to the insertion, meaning it failed, or an exception occured
		System.out.println("FAILURE");
		return false;
	}

	
	
	
	
	
	/*
	 * Pass by reference way to modify the query. Currently not working
	 */
	public static String addToQuery(String arg, String toAdd, String query, boolean first) {
		if (stringIsValid(arg)) {
			System.out.println("Adding \"" + arg + "\" to query");
			if (first) {
				query += "WHERE ";
				first = false;
			}
			else {
				query += "AND ";
			}
			query += toAdd;
		}
		return query;
	}

	/*
	 * Generic method for checking validity of string arguments
	 */
	public static boolean stringIsValid(String s) {
		if (s == null || s == "") return false;
		return true;
	}

	public boolean databaseTemplate() {
		//PERFORM ARGUMENT VALIDATION HERE


		//The prepared statement for this order
		PreparedStatement	stmt	= null;

		//The connection to the database
		Connection			conn	= null;

		//The SQL query to update this order's information
		String 				query = "";

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
			System.out.print("Setting statement values: ");
			System.out.print("1 ");

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
}

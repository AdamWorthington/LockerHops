package jumpit.lockereats.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONObject;

//TODO: figure out how to link order object and the entry in the database
//TODO: decide if worth it to move these methods to the order class (so can call in an object oriented style)
//TODO: decide if it is better to have 1 large orders table or multiple tables per restaurant
//TODO: should probably be using AWS CodeCommit & CodeDeploy over Github...

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

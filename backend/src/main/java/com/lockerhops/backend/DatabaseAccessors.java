package com.lockerhops.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
	static final String url 		= "173.194.248.150:3306";
	static final String dbName 		= "LockerHops";
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

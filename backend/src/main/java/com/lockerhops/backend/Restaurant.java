package com.lockerhops.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Restaurant {
	
	//Database information
	static final String url 		= DatabaseAccessors.url;
	static final String dbName 		= DatabaseAccessors.dbName;
	static final String username 	= DatabaseAccessors.username;
	static final String password 	= DatabaseAccessors.password;
	static final String driver 		= DatabaseAccessors.driver;
	static final String jdbcUrl 	= DatabaseAccessors.jdbcUrl;
	
	String name;		//Name of the restaurant
	String address;		//Restaurant's address
	String phone;		//Restaurant's phone number
	String website;		//Restaurant's website (www.xyz.com)
	String type;		//Restaurant's type (American, mexican, pasta, pizza, etc)
	
	public Restaurant(String name, String address, String phone, String website, String type) {
		this.name 		= name;
		this.address 	= address;
		this.phone 		= phone;
		this.website 	= website;
		this.type 		= type;
	}

	public String getName() {
		return this.name;
	}

	public String getAddress() {
		return this.address;
	}

	public String getPhone() {
		return this.phone;
	}

	public String getWebsite() {
		return this.website;
	}

	public String getType() {
		return this.type;
	}
	
	/*
	 * Generic method for checking validity of string arguments
	 */
	public static boolean stringIsValid(String s) {
		if (s == null || s == "") return false;
		return true;
	}
	
	public ArrayList<Restaurant> getRestaurants() {
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

		if (stringIsValid(this.name)) {
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
		if (stringIsValid(this.address)) {
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
		if (stringIsValid(this.phone)) {
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
		if (stringIsValid(this.website)) {
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
		if (stringIsValid(this.type)) {
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
			if (stringIsValid(this.name)) {
				stmt.setString(count, this.name);
				System.out.print(count + " ");
				count++;
			}

			if (stringIsValid(this.address)) {
				stmt.setString(count, this.address);
				System.out.print(count + " ");
				count++;
			}

			if (stringIsValid(this.phone)) {
				stmt.setString(count, this.phone);
				System.out.print(count + " ");
				count++;
			}

			if (stringIsValid(this.website)) {
				stmt.setString(count, this.website);
				System.out.print(count + " ");
				count++;
			}
			
			if (stringIsValid(this.type)) {
				stmt.setString(count, this.type);
				System.out.print(count + " ");
				count++;
			}
			System.out.println();

			System.out.println("QUERY IS: " + stmt.toString());

			//Execute the statement to insert this order into the database
			System.out.print("Executing statement in getRestaurantItems: ");
			ResultSet rs = stmt.executeQuery();

			//Name, Address, Phone Number, Website, Type
			ArrayList<Restaurant> restaurantList = new ArrayList<Restaurant>();
			String name, address, phone, website, type;
			while(rs.next()) {
				//Retrieve values from ResultSet and build the array of restaurants
				name 	= rs.getString("Restaurant");
				address	= rs.getString("Address");
				phone	= rs.getString("Phone_Number");
				website = rs.getString("Website");
				type 	= rs.getString("Type");

				Restaurant r = new Restaurant(name, address, phone, website, type);
				restaurantList.add(r);
			}
			System.out.println("SUCCESS");
			return restaurantList;
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
}

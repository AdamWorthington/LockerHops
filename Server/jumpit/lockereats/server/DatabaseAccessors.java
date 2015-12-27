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


public class DatabaseAccessors {
	
	//Database information
	static final String url 		= "lockerhopsmain.c2fmvfgm3x77.us-east-1.rds.amazonaws.com:3306";
	static final String dbName 		= "LockerHopsMain";
	static final String username 	= "LockerHops";
	static final String password 	= "!25spoHrekcoL?9";
	static final String driver 		= "com.mysql.jdbc.Driver";
	static final String jdbcUrl 	= "jdbc:mysql://" + url + "/" + dbName + "?user=" + username + "&password=" + password;
	
	/*
	 * Store the order in the database
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
		String				query	= "INSERT INTO Orders (`Restaurant`, `Order`, `Cost`) VALUES (?, ?, ?);";		
		
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
			System.out.print("Preparing statement in placeOrder: ");
			stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			System.out.println("SUCCESS");
			
			//Prepared statements are used to prevent SQL injection
			System.out.print("Setting statement values in placeOrder: ");
			stmt.setString(1, order.getRestaurant());
			System.out.print("1 ");
			stmt.setString(2, items);
			System.out.print("2 ");
			stmt.setDouble (3, order.getCost());
			System.out.println("3");
			
			//Execute the statement to insert this order into the database
			System.out.print("Executing statement: ");
			int ret = stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();

			
			int orderID = rs.getInt(1);
			
			if(orderID > 0) {
			//This allows us to link the order in the DB to the order object itself
				order.setID(orderID);
				System.out.println("SUCCESS (Order ID:" + orderID + ")");
				return true;
			}
			
			stmt.close();
		}
		catch(SQLException e) {
			System.out.println("Unable to create and execute statement (placeOrder):    ");
		    e.printStackTrace();
		    return false;
		}
		
		//This is only reached if there was no value assigned to the insertion, meaning it failed, or an exception occured
		System.out.println("FAILURE");
		return false;
	}
	
	/*
	 * Update in the database the time an order
	 * @param order: the order entry to update.
	 * @param datetime: the datetime value when the item was handled
	 * @param type: 0 for time placed in locker, 1 for time picked up
	 * Returns true if update was successful, false otherwise
	 */
	public static boolean updateOrderTime(Order order, String datetime, int type) {
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
			query = "UPDATE Orders SET TimePlacedInLocker = ? WHERE Identifier = ?;";
		}
		else if (type == 1) {
			query = "UPDATE Orders SET TimePickedUp = ? WHERE Identifier = ?;";
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
}

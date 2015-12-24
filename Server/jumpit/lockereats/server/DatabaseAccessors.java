package jumpit.lockereats.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONObject;

//TODO: figure out how to link order object and the entry in the database
//TODO: decide if worth it to move these methods to the order class (so can call in an object oriented style)

public class DatabaseAccessors {
	
	static final String url 		= "lockerhopsmain.c2fmvfgm3x77.us-east-1.rds.amazonaws.com:3306";
	static final String username 	= "LockerHops";
	static final String password 	= "!25spoHrekcoL?9";
	static final String dbName 		= "LockerHopsMain";
	static final String driver 		= "com.mysql.jdbc.Driver";
	
	/*
	 * Store the order in the database
	 * @param order: the order to be placed in the database
	 * Returns true if successfully entered into DB, false otherwise
	 */
	public static boolean placeOrder(Order order) {
		
		PreparedStatement	stmt	= null;
		Connection			conn	= null;
		String				query	= "INSERT INTO Orders (`Restaurant`, `Order`, `Cost`) VALUES (?, ?, ?);";		
		String				items	= String.join(",", order.items);
		
		try {
		    System.out.print("Loading driver in placeOrder: ");
		    Class.forName("com.mysql.jdbc.Driver");
		    System.out.println("SUCCESS");
		} 
		catch (ClassNotFoundException e) {
		    throw new RuntimeException("Cannot find the driver in the classpath:    ", e);
		}
		
		try {
			System.out.print("Creating connection in placeOrder: ");
			conn = DriverManager.getConnection(driver);
			System.out.println("SUCCESS");
			
			System.out.print("Preparing statement in placeOrder: ");
			stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			System.out.println("SUCCESS");
			
			System.out.print("Setting statement values in placeOrder: ");
			stmt.setString(0, order.getRestaurant());
			System.out.print("0 ");
			stmt.setString(1, items);
			System.out.print("1 ");
			stmt.setFloat (2, order.cost);
			System.out.println("2");
			
			System.out.print("Executing statement: ");
			int ret = stmt.executeUpdate();
			
			if (ret >= 0) {
				order.id = ret;
				System.out.println("SUCCESS");
				return true;
			}
		}
		catch(SQLException e) {
			throw new RuntimeException("Unable to create and execute statement:    ", e);
		}
		
		System.out.println("FAILURE");
		return false;
	}
	
	/*
	 * Update in the database the time an order was placed in a locker
	 * @param order: the order entry to update.
	 * Returns true if update was successful, false otherwise
	 */
	public static boolean updateTimePlacedInLocker(Order order) {
		if (order.id == -1) {
			System.out.println("Order has not been placed in database yet");
			return false;
		}
		
		String query = "";
		
		return false;
	}

	/*
	 * Update in the database the time an order was picked up
	 * @param order: the order entry to update.
	 * Returns true if update was successful, false otherwise
	 */
	public static boolean updateTimePickedUp(Order order) {
		
		
		return false;
	}
}

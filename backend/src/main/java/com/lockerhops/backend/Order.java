package jumpit.lockereats.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Order {
	int			id;				//ID of the order (assigned by the database, -1 indicated unassigned)
	String		restaurant;		//Name of the restaurant to which this order is being placed
	String[]	items;			//Array of orders
	double		cost;			//Dollar/cent cost of order (minimum $00.00, maximum $9999.99)
	//If cost is ever going to be > 1,000,000 move this to BigDecimal

	//Database information
	static final String url 		= DatabaseAccessors.url;
	static final String dbName 		= DatabaseAccessors.dbName;
	static final String username 	= DatabaseAccessors.username;
	static final String password 	= DatabaseAccessors.password;
	static final String driver 		= DatabaseAccessors.driver;
	static final String jdbcUrl 	= DatabaseAccessors.jdbcUrl;

	public Order(String restaurant, String[] items, double cost) {
		this.id			= -1;
		this.restaurant = restaurant;
		this.items 		= items;
		this.cost 		= cost;
	}

	/*
	 * Indicates if the fields in the order contain values that can be correct
	 */
	public boolean isWellFormed() {
		if (this.restaurant == null || this.restaurant == "") {
			System.out.println("Order has bad restaurant");
			return false;
		}

		if (this.items == null) {
			System.out.println("Order has no items");
			return false;
		}

		for (String item : this.items) {
			if (item == null || item == "") {
				System.out.println("Order has malformed item");
				return false;
			}
		}

		if (this.cost <= 0.00) {
			System.out.println("Order has malformed cost (" + this.cost + ")");
			return false;
		}

		if (this.id < -1) {
			return false;
		}

		return true;
	}

	public String getRestaurant() {
		return this.restaurant;
	}

	public String[] getItems() {
		return this.items;
	}

	public double getCost() {
		return this.cost;
	}

	public int getID() {
		return this.id;
	}

	public void setID(int ID) {
		this.id = ID;
	}

	/*
	 * Generic method for checking validity of string arguments
	 */
	public static boolean stringIsValid(String s) {
		if (s == null || s == "") return false;
		return true;
	}

	/*
	 * Store the order in the database and updates this order's ID (as per identifier assigned by database)
	 * @param order: the order to be placed in the database
	 * Returns true if successfully entered into DB, false otherwise
	 */
	public boolean placeOrder() {

		//If the order is not well formed we should not do anything with it
		if (!this.isWellFormed()) {
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
		String				items	= String.join(",", this.items);

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
			stmt.setString(1, this.getRestaurant());
			System.out.print("1 ");
			stmt.setDouble (2, this.getCost());
			System.out.println("2");

			//Execute the statement to insert this order into the database
			System.out.print("Executing statement 1: ");
			int ret = stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();


			int orderID = rs.getInt(1);

			if(orderID > 0) {
				//This allows us to link the order in the DB to the order object itself
				this.setID(orderID);
				System.out.println("SUCCESS (Order ID:" + orderID + ")");
			}
			else {
				return false;
			}

			//Prepare the queries for each item
			for (String item : this.items) {
				System.out.print("Preparing statement 2 in placeOrder: ");
				stmt = conn.prepareStatement(query2);
				System.out.println("SUCCESS");

				System.out.print("Setting statement 2 values in placeOrder: ");
				stmt.setInt(1, this.id);
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
	public boolean updateTimePlacedInLocker(String datetime) {
		return updateOrderTime(this, datetime, 0);
	}

	/*
	 * Update the time an order was picked up
	 * @param order: the order entry to update.
	 * @param datetime: the datetime value when the item was handled
	 * Returns true if update was successful, false otherwise
	 */
	public boolean updatePickupTime(String datetime) {
		return updateOrderTime(this, datetime, 1);
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

	public static ArrayList<Order> getOrders(String restaurant) {
		//PERFORM ARGUMENT VALIDATION HERE
		if (!stringIsValid(restaurant)) {
			System.out.println("Invalid restaurant (" + restaurant + ") in getOrders");
			return null;
		}
			


		//The prepared statement for this order
		PreparedStatement stmt	= null;

		//The connection to the database
		Connection conn	= null;

		//The SQL query to update this order's information
		String 	query = "SELECT * " + 
						"FROM Orders " + 
						"INNER JOIN Order_Items ON Orders.OrderID = Order_Items.OrderID " + 
						"WHERE Restaurant = ? AND TimePickedUp IS NULL";

		//Check we can get the driver
		try {
			System.out.print("Checking driver: ");
			Class.forName(driver);
			System.out.println("SUCCESS");
		} 
		catch (ClassNotFoundException e) {
			System.out.println("Cannot find the driver in the classpath:    ");
			e.printStackTrace();
			return null;
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
			System.out.println("1");
			stmt.setString(1, restaurant);

			//Execute the statement to insert this order into the database
			System.out.print("Executing statement: ");
			ResultSet rs = stmt.executeQuery();
			
			ArrayList<Order> orderList = new ArrayList<Order>();

			//TODO: Figure out how to get orders and their respective items together out of the database
			while(rs.next()) {
				
			}
			if (orderList.size() != 0) {
				System.out.println("SUCCESS");
				return orderList;
			}
			else {
				System.out.println("FAILURE");
				return null;
			}
		}
		catch(SQLException e) {
			System.out.println("Unable to create and execute statement:    ");
			e.printStackTrace();
			return null;
		}
	}
}

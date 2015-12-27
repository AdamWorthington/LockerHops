package jumpit.lockereats.server;

public class Order {
	int			id;				//ID of the order (assigned by the database, -1 indicated unassigned)
	String		restaurant;		//Name of the restaurant to which this order is being placed
	String[]	items;			//Array of orders
	double		cost;			//Dollar/cent cost of order (minimum $00.00, maximum $9999.99)
								//If cost is ever going to be > 1,000,000 move this to BigDecimal
	
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
}

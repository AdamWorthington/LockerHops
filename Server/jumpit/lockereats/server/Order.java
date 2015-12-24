package jumpit.lockereats.server;

public class Order {
	int			id;				//ID of the order (assigned by the database, -1 indicated unassigned)
	String		restaurant;		//Name of the restaurant to which this order is being placed
	String[]	items;			//Array of orders
	float		cost;			//Dollar/cent cost of order (minimum $00.00, maximum $9999.99)
								//If cost is ever going to be > 1,000,000 move this to BigDecimal
	
	public Order(String restaurant, String[] items, float cost) {
		this.id			= -1;
		this.restaurant = restaurant;
		this.items 		= items;
		this.cost 		= cost;
	}
	
	public String getRestaurant() {
		return this.restaurant;
	}

	public String[] getItems() {
		return this.items;
	}

	public float getCost() {
		return this.cost;
	}
	
	public int getID() {
		return this.id;
	}
	
	public void setID(int ID) {
		this.id = ID;
	}
}

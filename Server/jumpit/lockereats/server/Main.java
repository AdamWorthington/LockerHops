package jumpit.lockereats.server;

public class Main {
	public static void main(String[] args) {
		/*if (testOrderPlacement()) {
			System.out.println("+----------------------------+");
			System.out.println("| ORDER PLACEMENT SUCCESSFUL |");
			System.out.println("+----------------------------+");
		}
		else {
			System.out.println("+-------------------------+");
			System.out.println("| ORDER PLACEMENT FAILURE |");
			System.out.println("+-------------------------+");
		}*/
		
		testGetRestaurants();
		
	}
	
	public static boolean testGetRestaurants() {
		Restaurant restaurant = new Restaurant("Vans", "123 Street Street", "777-777-7777", "www.vans.com", "American");
		DatabaseAccessors.getRestaurants(restaurant);
		
		return true;
	}
	
	public static boolean testGetRestaurantItems() {
		Item item = new Item("Vans", "Panini", "", 12.99, "Lunch", "American", null, true, true, true);
		DatabaseAccessors.getRestaurantItems(item);
		
		return true;
		
	}
	
	public static boolean testOrderPlacement() {
		String restaurant = "Vans2";
		String[] items = { "Panini", "Soda", "Mustard", "Ketchup" };
		double cost = 99.55;
		String datetime = "2015-12-27 02:37:55";	//Datetime: YYYY-MM-DD HH:MM:SS
		Order order = new Order(restaurant, items, cost);
		if (!DatabaseAccessors.placeOrder(order)) {
			return false;
		}
		if (!DatabaseAccessors.updateTimePlacedInLocker(order, datetime)) {
			return false;
		}
		if (!DatabaseAccessors.updatePickupTime(order, datetime)) {
			return false;
		}
		
		return true;
	}
	
	public static boolean testItemPlacement() {
		
		
		return false;
	}
	
	public static boolean testRestaurantPlacement() {
		
		
		return false;
	}
}

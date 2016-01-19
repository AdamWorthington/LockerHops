//MAIN IS CURRENTLY FOR TESTING PURPOSES ONLY!!!!

package jumpit.lockereats.server;

import java.util.ArrayList;

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
		}
		
		testAddRestaurant();
		
		if (testGetRestaurants()) {
			System.out.println("+---------------------------+");
			System.out.println("| RESTAURANT GET SUCCESSFUL |");
			System.out.println("+---------------------------+");
		}
		else {
			System.out.println("+------------------------+");
			System.out.println("| RESTAURANT GET FAILURE |");
			System.out.println("+------------------------+");
		}*/
		
	}
	
	public static boolean testAddRestaurant() {
		return Restaurant.addRestaurant("Vans", "123 Street Street", "777-777-7777", "www.vans.com", "American");
	}
	
	public static boolean testGetRestaurants() {
		Restaurant restaurant = new Restaurant("Vans", "123 Street Street", "777-777-7777", "www.vans.com", "American");
		ArrayList<Restaurant> restaurants = restaurant.getRestaurants();
		
		if (restaurants == null) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public static boolean testGetRestaurantItems() {
		Item item = new Item("Vans", "Panini", "", 12.99, "Lunch", "American", null, true, true, true);
		item.getRestaurantItems();
		
		return true;
	}
	
	public static boolean testOrderPlacement() {
		String restaurant = "Vans2";
		String[] items = { "Panini", "Soda", "Mustard", "Ketchup" };
		double cost = 99.55;
		String datetime = "2015-12-27 02:37:55";	//Datetime: YYYY-MM-DD HH:MM:SS
		Order order = new Order(restaurant, items, cost);
		if (!order.placeOrder()) {
			return false;
		}
		if (!order.updateTimePlacedInLocker(datetime)) {
			return false;
		}
		if (!order.updatePickupTime(datetime)) {
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

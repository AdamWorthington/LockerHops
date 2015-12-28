package jumpit.lockereats.server;

public class Main {
	public static void main(String[] args) {
		if (testOrderPlacement()) {
			System.out.println("+----------------------------+");
			System.out.println("| ORDER PLACEMENT SUCCESSFUL |");
			System.out.println("+----------------------------+");
		}
		else {
			System.out.println("+-------------------------+");
			System.out.println("| ORDER PLACEMENT FAILURE |");
			System.out.println("+-------------------------+");
		}
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
}

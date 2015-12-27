package jumpit.lockereats.server;

public class Main {
	public static void main(String[] args) {
		String restaurant = "Vans2";
		String[] items = { "Panini", "Soda", "Mustard", "Kechup" };
		double cost = 99.55;
		String datetime = "2015-12-27 02:37:55";
		Order order = new Order(restaurant, items, cost);
		DatabaseAccessors.placeOrder(order);
		DatabaseAccessors.updateOrderTime(order, datetime, 0);	//Datetime: YYYY-MM-DD HH:MM:SS
		DatabaseAccessors.updateOrderTime(order, datetime, 1);
	}
}

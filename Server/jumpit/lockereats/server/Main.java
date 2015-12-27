package jumpit.lockereats.server;

public class Main {
	public static void main(String[] args) {
		String restaurant = "Vans";
		String[] items = { "Panini", "Soda" };
		double cost = 5.55;
		Order order = new Order(restaurant, items, cost);
		DatabaseAccessors.placeOrder(order);
	}
}

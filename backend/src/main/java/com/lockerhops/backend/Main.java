//MAIN IS CURRENTLY FOR TESTING PURPOSES ONLY!!!!
/*

package com.lockerhops.backend;

import java.util.ArrayList;

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
		}

		if (testItemPlacement()) {
			System.out.println("+---------------------------+");
			System.out.println("| ITEM PLACEMENT SUCCESSFUL |");
			System.out.println("+---------------------------+");
		}
		else {
			System.out.println("+------------------------+");
			System.out.println("| ITEM PLACEMENT FAILURE |");
			System.out.println("+------------------------+");
		}
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
		Item item = new Item(0, "Vans", "Panini", "", 12.99, "Lunch", "American", null, true, true, true);
		item.getRestaurantItems();
		
		return true;
	}
	
	public static boolean testOrderPlacement() {
		String restaurant = "Vans2";
		int[] items = { 0, 1, 2, 3 };
		double cost = 99.55;
		String datetime = "2015-12-27 02:37:55";	//Datetime: YYYY-MM-DD HH:MM:SS
		Order order = new Order(restaurant, items, cost);
		if (order.placeOrder() == -1) {
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
		String 		restaurant = "Vans";
		String 		item = "Calzone";
		String 		description = "A tasty calzone";
		double 		cost = 9.99;
		String 		category = "American";
		String 		subCategory = "Delicious";
		String[]	ingredients = null;
		boolean 	glutenFree = false;
		boolean 	vegetarian = false;
		boolean 	vegan = false;

		Item food = new Item(0, restaurant, item, description, cost, category, subCategory, ingredients, glutenFree, vegetarian, vegan);

		return food.addRestaurantItem();
	}
	
	public static boolean testRestaurantPlacement() {
		
		
		return false;
	}
}
*/
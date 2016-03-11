package com.lockerhops.backend;




/*
 * Header file for Methods:
 * -Order
 *   -placeOrder:
 *     -Store the order in the database and updates this order's ID (as per identifier assigned by database)
 *     -@param order: the order to be placed in the database
 *     -Returns true if successfully entered into DB, false otherwise
 *   -updateTimePlacedInLocker:
 *     -Update the time an order was placed in the lockers
 *     -@param order: the order entry to update.
 *     -@param datetime: the datetime value when the item was handled
 *     -Returns true if update was successful, false otherwise
 *   -updatePickupTime:
 *     -Update the time an order was picked up
 *     -@param order: the order entry to update.
 *     -@param datetime: the datetime value when the item was handled
 *     -Returns true if update was successful, false otherwise
 *   -getOrders:
 *     -Get all orders from a restaurant that have not been picked up
 *     -@param restaurant: a string representing the name of the restaurant to get orders from
 *     -Returns an arraylist of order objects
 * -Item
 *   -addRestaurantItem:
 *     -Add a restaurant item to the database
 *     -See the method itself for what to pass it, there is a lot
 *   -getRestaurantItems:
 *     -Returns all items that match to the given Item object
 * -Restaurant
 *   -getRestaurants:
 *     -Returns an arraylist of restaurant objects that match the given restaurant object.
 *   -addRestaurant:
 *     -Add a restaurant to the database
 *     -@param name: 	a string representing the name of the restaurant
 *     -@param address: a string representing the address of the restaurant
 *     -@param phone: 	a string representing the phone number of the restaurant
 *     -@param website: a string representing the website URL of the restaurant
 *     -@param type: 	a string representing the type (American, Fast-Food, Chinese, etc) of the restaurant
 *     -Returns true or false dependant on database insertion success
 */




/**
 * Created by Scott on 2/24/2016.
 *
 * Functions to access the database
 *
 * These functions will retrieve information from the
 * SQL tables given a type of query and the information
 * required to fulfill that query
 *
 * SAMPLE FUNCTION FROM PREVIOUS PROJECT
 * Delete when you get the idea
 * The MyBean object is just to tell the calling API
 * function what happened. Makes for easier debugging later
 *
 * //Contact the database and store a single profile in it
     public static MyBean storeProfile(Profile input) {

         String url = Constants.DATABASE_URL;
         MyBean bean = new MyBean();
         String statement = "";
         int success = 0;

         try {
             // Load GoogleDriver class at runtime
             Class.forName(Constants.GOOGLE_DRIVER);

             // Open connection to database
             Connection conn = DriverManager.getConnection(url);
             try {
                 //Sanity check on argument
                 if (input == null) {
                     bean.setBool(false);
                     bean.setData("Sent Database a null Profile");
                     databaseError(Constants.DB_ERROR.BAD_INPUT_ERROR);
                     return bean;
                 }
                    else {
                     statement = "INSERT INTO profile (firstName, lastName, password, email, city, lat, lng, description)" +
                     " VALUES ('" +
                     input.getFirstName() + "', '" +
                     input.getLastName() + "', '" +
                     input.getPassword() + "', '" +
                     input.getEmail() + "', '" +
                     input.getCity() + "', " +
                     input.getLat() + ", " +
                     input.getLng() + ", '" +
                     input.getDescription() + "')";
                     PreparedStatement stmt = conn.prepareStatement(statement);

                     // Returns 1 on success, 0 on fail
                     success = stmt.executeUpdate();
                 }
             }
             finally {
                 // Always make sure to close database connection
                 conn.close();
             }
         }
         catch (Exception e){
            //Handle unsuccessful connection to database here
             e.printStackTrace();
         }

         if (success == 1) {
            bean.setBool(true);
            bean.setData("Successful insert");
         }
         else {
            databaseError(Constants.DB_ERROR.INSERT_ERROR);
            bean.setBool(false);
            bean.setData("Error: Inserting into database failed");
         }
         return bean;
    }
 *
 */


public class databaseConnection {
    //TODO
}

/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.lockerhops.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.NotFoundException;

import java.util.ArrayList;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 *
 * Functions will call database methods in
 * databaseConnection.java and will then manipulate the
 * data if needed. Functions will then return the information
 * to the frontend that calls these functions
 *
 */
@Api(
        name = "lockerHopsAPI",
        version = "v1",
        scopes = {Constants.EMAIL_SCOPE},
        clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID, Constants.IOS_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID},
        audiences = {Constants.ANDROID_AUDIENCE},
        namespace = @ApiNamespace(
                ownerDomain = "backend.lockerhops.com",
                ownerName = "backend.lockerhops.com",
                packagePath = ""
        )
)
public class LockerHopsAPI {
    //TODO: Require database methods for:
            //Simple Order fetch (i.e. getOrder(int ID) )

    //TODO: Confirm testing for methods:
        //Order.placeOrder
        //Order.updateTimePlaceInLocker
        //Order.updatePickupTime;
        //Order.getOrders
        //Item.addRestaurantItem
        //Item.getRestaurantItems
        //Restaurant.getAllRestaurants
        //Restaurant.addRestaurant

    /*
     * METHODS FOR ORDER
     */

    //Will attempt to place an order with the given parameters. Returns a MyBean with myBoolean set to true on success and false on failure
    @ApiMethod(name = "Order.placeOrder", httpMethod = "get")
    public MyBean placeOrder(@Named("id") Integer id, @Named("restaurant") String restaurant, @Named("items") int[] items, @Named("cost") Double cost) throws NotFoundException {
        MyBean ret = new MyBean();
        try {
            Order insert = new Order(id, restaurant, items, cost);
            ret.setMyInt(insert.placeOrder());
        } catch (Exception e) {

        }
        return ret;
    }

    //TODO
    //Will attempt to update the time an order was placed into a locker. Returns a MyBean with myBoolean set to true on success and false on failure
    @ApiMethod(name = "Order.updateTimePlaceInLocker")
    public MyBean lockerTimeUpdate(@Named("orderID") Integer id, @Named("DateTime") String datetime) {
        MyBean ret = new MyBean();
        //TODO: Requires simple order fetch method
        //Order update = Order.getOrder(id);
        //ret.setMyBoolean(update.updateTimePlacedInLocker(datetime));
        return null;
    }

    //TODO
    //Will attempt to update the time an order was picked up from a locker. Returns a MyBean with myBoolean set to true on success and false on failure
    @ApiMethod(name = "Order.updatePickupTime")
    public MyBean pickupTimeUpdate(@Named("orderID") Integer id, @Named("DateTime") String datetime) {
        MyBean ret = new MyBean();
        //TODO: Requires simple order fetch method
        //Order update = Order.getOrder(id);
        //ret.setMyBoolean(update.updatePickupTime(datetime));
        return null;
    }

    //Attempts to retrieve all orders for the given restaurant that have not been picked up
    @ApiMethod(name = "Order.getOrders")
    public ArrayList<Order> getOrders(@Named("Restaurant") String restaurant) {
        ArrayList<Order> ret = new ArrayList<Order>();
        try {
            ret = Order.getOrders(restaurant);
        }
        catch (Exception e) {

        }
        return ret;
    }

    /*
     * METHODS FOR ITEM
     */

    //Attempts to add an item with the given parameters to a restaurant's menu. Returns a MyBean with myBoolean set to true on success and false on failure
    @ApiMethod(name = "Item.addRestaurantItem")
    public MyBean addRestaurantItem(@Named("restaurant") String restaurant, @Named("item") String item,
                                    @Named("description") String description, @Named("cost") Double cost,
                                    @Named("category") String category, @Named("subCategory") String subCategory,
                                    @Nullable @Named("ingredients") String[] ingredients, @Nullable @Named("isGlutenFree") Boolean isGlutenFree,
                                    @Nullable @Named("isVegetarian") Boolean isVegetarian, @Nullable @Named("isVegan") Boolean isVegan) {
        MyBean ret = new MyBean();
        try {
            Item insert = new Item(0, restaurant, item, description, cost, category, subCategory, ingredients, isGlutenFree, isVegetarian, isVegan);
            ret.setMyBoolean(insert.addRestaurantItem());
        }
        catch (Exception e) {

        }
        return ret;
    }

    //Will return an ArrayList<Item> of items that match the given parameters item parameters.
    //For example, calling the method with only a restaurant name will return all items for that restaurant
    //If cost is input, it will return all items LESS than the input cost
    @ApiMethod(name = "Item.getRestaurantItems")
    public ArrayList<Item> getRestaurantItems(@Named("restaurant") String restaurant, @Nullable @Named("item") String item,
                                              @Nullable @Named("description") String description, @Nullable @Named("cost") Double cost,
                                              @Nullable @Named("category") String category, @Nullable @Named("subCategory") String subCategory,
                                              @Nullable @Named("ingredients") String[] ingredients, @Nullable @Named("isGlutenFree") Boolean isGlutenFree,
                                              @Nullable @Named("isVegetarian") Boolean isVegetarian, @Nullable @Named("isVegan") Boolean isVegan) {
        ArrayList<Item> ret = new ArrayList<Item>();
        try {
            Item search = new Item(0, restaurant, item, description, cost, category, subCategory, ingredients, isGlutenFree, isVegetarian, isVegan);
            ret = search.getRestaurantItems();
        }
        catch (Exception e) {

        }
        return ret;
    }

    /*
     * METHODS FOR RESTAURANT
     */

    //Much like Item.getRestaurantItems, this method will search for restaurants matching the given parameters
    @ApiMethod(name = "Restaurant.getAllRestaurants")
    public ArrayList<Restaurant> getRestaurants(@Nullable @Named("name") String name, @Nullable @Named("address") String address,
                                 @Nullable @Named("phone") String phone, @Nullable @Named("website") String website,
                                 @Nullable @Named("type") String type) {
        ArrayList<Restaurant> ret = new ArrayList<Restaurant>();
        try {
            Restaurant search = new Restaurant(name, address, phone, website, type);
            ret = search.getRestaurants();
        }
        catch (Exception e) {

        }
        return ret;
    }

    //Will attempt to create a new restaurant with the given parameters. Returns a MyBean with myBoolean set to true on success and false on failure
    //P.S. thanks for changing up the format for creation, Dan. Keeps me on my toes -.-
    @ApiMethod(name = "Restaurant.addRestaurant")
    public MyBean restaurantCreate(@Named("name") String name, @Named("address") String address,
                                   @Named("phone") String phone, @Named("website") String website,
                                   @Named("type") String type) {
        MyBean ret = new MyBean();
        try {
            ret.setMyBoolean(Restaurant.addRestaurant(name, address, phone, website, type));
        }
        catch (Exception e) {

        }
        return ret;
    }
}

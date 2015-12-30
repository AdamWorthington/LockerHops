package jumpit.lockereats.Core;

import java.util.ArrayList;

import jumpit.lockereats.Model.Order;
import jumpit.lockereats.Model.Restaurant;
import jumpit.lockereats.Model.StoreItem;
import jumpit.lockereats.Model.StoreMenu;

/**
 * Created by cdwil on 12/22/2015.
 */
public class Singleton
{
    private static Singleton instance;


    private ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
    private Order order;
    private boolean hasSavedCredentials;

    public boolean getHasSavedCredentials()
    {
        return hasSavedCredentials;
    }

    public static void tryInitInstance()
    {
        if (instance == null)
        {
            // Create the instance
            instance = new Singleton();
        }
    }

    public static Singleton getInstance()
    {
        tryInitInstance();
        // Return the instance
        return instance;
    }

    private Singleton()
    {
        Restaurant rr = new Restaurant("papa johns", null, null, null);
        order = new Order();
        ArrayList<StoreItem> items = new ArrayList<StoreItem>();
        items.add(new StoreItem("food", 5));
        items.add(new StoreItem("food", 5));
        items.add(new StoreItem("food", 5));
        items.add(new StoreItem("food", 5));
        items.add(new StoreItem("food", 5));
        items.add(new StoreItem("food", 5));
        items.add(new StoreItem("food", 5));
        items.add(new StoreItem("food", 5));
        items.add(new StoreItem("food", 5));
        items.add(new StoreItem("food", 5));
        rr.setStoreMenu(new StoreMenu(items));
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);
        restaurants.add(rr);

        hasSavedCredentials = false;
    }


    public ArrayList<Restaurant> getRestaurants()
    {
        return restaurants;
    }

    public Restaurant getRestaurantByPos(int position)
    {
        if(position > restaurants.size() - 1 || position < 0)
            return null;

        return restaurants.get(position);
    }

    public Order getOrder()
    {
        return order;
    }
}

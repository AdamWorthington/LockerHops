package jumpit.lockereats.Core;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Objects;

import jumpit.lockereats.Model.Order;
import jumpit.lockereats.Model.Restaurant;
import jumpit.lockereats.Model.StoreCategory;
import jumpit.lockereats.Model.StoreItem;
import jumpit.lockereats.Model.StoreMenu;
import jumpit.lockereats.Tasks.GetRestaurantsTask;

/**
 * Created by cdwil on 12/22/2015.
 */
public class Singleton
{
    private static Singleton instance;

    private static GoogleApiClient mGoogleApiClient = null;

    private ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
    private ArrayList<Order> cart = new ArrayList<>();

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

    public static void trySetGoogleClient(GoogleApiClient thisClient)
    {
        if(mGoogleApiClient == null)
        {
            mGoogleApiClient = thisClient;
        }
    }

    public static GoogleApiClient getmGoogleApiClient()
    {
        return mGoogleApiClient;
    }

    private Singleton()
    {
        Restaurant rr = new Restaurant("Von's Dough Shack", null, null, null);
        ArrayList<Object> items = new ArrayList<Object>();
        items.add(new StoreItem("burrito", 5, "description", 0));
        items.add(new StoreItem("sandwich", 5, "description", 1));
        items.add(new StoreItem("soup", 5, "description", 2));
        items.add(new StoreItem("chicken", 5, "description", 3));
        items.add(new StoreItem("pork", 5, "description", 4));
        items.add(new StoreItem("hamburger", 5, "description", 5));
        items.add(new StoreItem("pizza", 5, "description", 6));
        items.add(new StoreItem("pineapple", 5, "description", 7));
        items.add(new StoreItem("drink", 5, "description", 8));
        items.add(new StoreItem("chips", 5, "description", 9));
        StoreCategory cat1 = new StoreCategory("Hot 'n Spicy");
        cat1.setChildItemList(items);
        StoreCategory cat2 = new StoreCategory("Suburban Delites");
        cat2.setChildItemList(items);
        StoreCategory cat3 = new StoreCategory("Adam's Delectables");
        cat3.setChildItemList(items);
        StoreCategory cat4 = new StoreCategory("Russian Station");
        cat4.setChildItemList(items);
        ArrayList<StoreCategory> cats = new ArrayList<StoreCategory>();
        cats.add(cat1);
        cats.add(cat2);
        cats.add(cat3);
        cats.add(cat4);
        rr.setStoreMenu(new StoreMenu(cats, rr));
        restaurants.add(rr);
    }

    public ArrayList<Restaurant> getRestaurants()
    {
        if(restaurants == null)
        {
            new GetRestaurantsTask().execute();
        }
        return restaurants;
    }

    public Restaurant getRestaurantByPos(int position)
    {
        if(position > restaurants.size() - 1 || position < 0)
            return null;

        return restaurants.get(position);
    }

    public void addToCart(Order thisOrder)
    {
        cart.add(thisOrder);
    }

    public void addToCart(int position, Order thisOrder)
    {
        cart.add(position, thisOrder);
    }

    public void removeFromCart(Order thisOrder)
    {
        cart.remove(thisOrder);
    }

    public void removeFromCartAt(int position)
    {
        cart.remove(position);
    }

    public ArrayList<Order> getCart()
    {
        return cart;
    }

    public Order getOrderFromCart(int position)
    {
        return cart.get(position);
    }
}

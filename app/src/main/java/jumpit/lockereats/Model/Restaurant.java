package jumpit.lockereats.Model;

import android.graphics.Bitmap;
import android.location.Location;

import java.util.Dictionary;

/**
 * Created by cdwil on 12/8/2015.
 */
public class Restaurant
{
    private int restaurantId;
    public int getRestaurantId() { return restaurantId; }

    /*
    Icon of this restuarant
     */
    private Bitmap icon;
    public Bitmap getIcon() { return icon; }

    /*
    Holds the formal name of the restuarant
     */
    private String name;
    public String getName()
    {
        return name;
    }

    /*
    GPS location of the store
     */
    private Location storeLocation;
    public Location getStoreLocation()
    {
        return storeLocation;
    }

    /*
    Holds the street address of restaurant
     */
    private String streetAddress;
    public String getStreetAddress()
    {
        return streetAddress;
    }

    /*
    Standard hours that the store is open
     */
    private Dictionary<String, String> hoursOfOperation;
    public Dictionary<String, String> getHoursOfOperation()
    {
        return hoursOfOperation;
    }

    private StoreMenu storeMenu;
    public StoreMenu getStoreMenu()
    {
        return storeMenu;
    }
    public void setStoreMenu(StoreMenu m) { storeMenu = m; }

    public Restaurant(String name, Location storeLocation, Dictionary<String, String> hoursOfOperation, StoreMenu storeMenu)
    {
        this.name = name;
        this.storeLocation = storeLocation;
        this.hoursOfOperation = hoursOfOperation;
        this.storeMenu = storeMenu;
        this.restaurantId = 1;
    }
}

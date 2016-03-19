package jumpit.lockereats.Model;

import android.graphics.Bitmap;
import android.location.Location;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

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
    Phone number of the restaurant
     */
    private String phone;
    public String getPhone()
    {
        return phone;
    }

    /*
    Web address of the restaurant
     */
    private String website;
    public String getWebsite()
    {
        return website;
    }

    /*
    Categorical type of restaurant
     */
    private String type;
    public String getType()
    {
        return type;
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

    public Restaurant(String name, String address, String phone, String website, String type)
    {
        this.name = name;
        this.streetAddress = address;
        this.phone = phone;
        this.website = website;
        this.type = type;
    }

    public static ArrayList<Restaurant> convert(List<com.lockerhops.backend.lockerHopsAPI.model.Restaurant> from)
    {
        ArrayList<Restaurant> to = new ArrayList<>();
        for(com.lockerhops.backend.lockerHopsAPI.model.Restaurant r : from)
        {
            String name = r.getName();
            String address = r.getAddress();
            String phone = r.getPhone();
            String website = r.getWebsite();
            String type = r.getType();

            Restaurant copy = new Restaurant(name, address, phone, website, type);
            to.add(copy);
        }

        return to;
    }
}

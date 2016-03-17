package jumpit.lockereats.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by cdwil on 12/28/2015.
 */
public class Order implements Parcelable
{
    private int quantity;
    public int getQuantity()
    {
        return quantity;
    }
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    private StoreItem item;
    public StoreItem getItem()
    {
        return item;
    }

    private ArrayList<FoodItemOption> options;
    public ArrayList<FoodItemOption> getOptions()
    {
        return options;
    }

    public Order(int quantity, StoreItem item, ArrayList<FoodItemOption> options)
    {
        this.quantity = quantity;
        this.item = item;
        this.options = options;
    }

    public double calculatePrice()
    {
        double basePrice = item.getPrice();
        for(FoodItemOption option : options)
        {
            for(OptionItem oi : option.getOptions())
            {
                if(oi.getIsSelected())
                    basePrice += oi.getPrice();
            }
        }

        return basePrice * quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.quantity);
        out.writeParcelable(this.item, 0);
        out.writeTypedList(this.options);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    // constructor that takes a Parcel and makes StoreItem out of it
    private Order(Parcel in) {
        this.quantity = in.readInt();
        this.item = in.readParcelable(StoreItem.class.getClassLoader());
        this.options = in.createTypedArrayList(FoodItemOption.CREATOR);
    }
}

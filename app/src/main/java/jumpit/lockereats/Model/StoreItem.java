package jumpit.lockereats.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cdwil on 12/24/2015.
 */
public class StoreItem implements Parcelable
{
    private String name;
    public String getName()
    {
        return name;
    }

    private double price;
    public double getPrice()
    {
        return price;
    }

    private String description;
    public String getDescription()
    {
        return  description;
    }

    private int id;
    public int getId()
    {
        return id;
    }

    public StoreItem(String name, float price, String description, int id)
    {
        this.name = name;
        this.price = price;
        this.description = description;
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeDouble(price);
        out.writeString(description);
        out.writeInt(id);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<StoreItem> CREATOR = new Parcelable.Creator<StoreItem>() {
        public StoreItem createFromParcel(Parcel in) {
            return new StoreItem(in);
        }

        public StoreItem[] newArray(int size) {
            return new StoreItem[size];
        }
    };

    // constructor that takes a Parcel and makes StoreItem out of it
    private StoreItem(Parcel in) {
        this.name = in.readString();
        this.price = in.readDouble();
        this.description = in.readString();
        this.id = in.readInt();
    }
}

package jumpit.lockereats.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by cdwil on 2/8/2016.
 */
public class OptionItem implements Parcelable
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

    private boolean isSelected;
    public boolean getIsSelected() { return isSelected; }
    public void setIsSelected(boolean state)
    {
        isSelected = state;
    }

    public OptionItem(String name, double price)
    {
        this.name = name;
        this.price = price;
        this.isSelected = false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.name);
        out.writeDouble(this.price);
        out.writeValue(this.isSelected);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<OptionItem> CREATOR = new Parcelable.Creator<OptionItem>() {
        public OptionItem createFromParcel(Parcel in) {
            return new OptionItem(in);
        }

        public OptionItem[] newArray(int size) {
            return new OptionItem[size];
        }
    };

    // constructor that takes a Parcel and makes OptionItem out of it
    private OptionItem(Parcel in) {
        this.name = in.readString();
        this.price = in.readDouble();
        this.isSelected = (Boolean) in.readValue(null);
    }
}

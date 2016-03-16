package jumpit.lockereats.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by cdwil on 1/31/2016.
 */
public class FoodItemOption implements Parcelable
{
    private String label;
    public String getLabel()
    {
        return label;
    }

    private boolean canChoose;
    public boolean getCanChoose()
    {
        return canChoose;
    }

    private int chooseLimit;
    public int getChooseLimit()
    {
        return chooseLimit;
    }

    private int chooseMinimum;
    public int getChooseMinimum()
    {
        return chooseMinimum;
    }

    private ArrayList<OptionItem> options;
    public ArrayList<OptionItem> getOptions()
    {
        return options;
    }

    private int choiceCount;
    public int getChoiceCount()
    {
        return choiceCount;
    }
    public void incrementChoiceCount()
    {
        choiceCount++;
    }

    public void decrementChoiceCount()
    {
        choiceCount--;
    }

    public FoodItemOption(String label, boolean canChoose, int chooseLimit, int chooseMinimum, List<String> optionNames, List<Double> optionPrices)
    {
        this.label = label;
        this.canChoose = canChoose;
        this.chooseLimit = chooseLimit;
        this.chooseMinimum = chooseMinimum;
        this.choiceCount = 0;

        options = new ArrayList<>();
        for(int i = 0; i < optionNames.size(); i++)
            options.add(new OptionItem(optionNames.get(i), optionPrices.get(i).doubleValue()));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.choiceCount);
        out.writeInt(this.chooseLimit);
        out.writeInt(this.chooseMinimum);
        out.writeValue(this.canChoose);
        out.writeString(this.label);
        out.writeTypedList(this.options);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<FoodItemOption> CREATOR = new Parcelable.Creator<FoodItemOption>() {
        public FoodItemOption createFromParcel(Parcel in) {
            return new FoodItemOption(in);
        }

        public FoodItemOption[] newArray(int size) {
            return new FoodItemOption[size];
        }
    };

    // constructor that takes a Parcel and makes StoreItem out of it
    private FoodItemOption(Parcel in) {
        this.choiceCount = in.readInt();
        this.chooseLimit = in.readInt();
        this.chooseMinimum = in.readInt();
        this.canChoose = (Boolean) in.readValue(null);
        this.label = in.readString();
        this.options = in.createTypedArrayList(OptionItem.CREATOR);
    }
}

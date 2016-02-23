package jumpit.lockereats.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by cdwil on 1/31/2016.
 */
public class FoodItemOption
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
}

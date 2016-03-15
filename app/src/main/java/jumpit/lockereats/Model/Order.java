package jumpit.lockereats.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by cdwil on 12/28/2015.
 */
public class Order
{
    private int quantity;
    public int getQuantity()
    {
        return quantity;
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
}

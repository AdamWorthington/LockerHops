package jumpit.lockereats.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by cdwil on 12/28/2015.
 */
public class Order
{
    private Date orderDate;
    public Date getOrderDate()
    {
        return orderDate;
    }

    private HashMap<StoreItem, Integer> orderItems;
    public HashMap<StoreItem, Integer> getOrderItems()
    {
        return orderItems;
    }

    private Restaurant source;
    public Restaurant getSource()
    {
        return source;
    }

    public Order(Restaurant source)
    {
        this.source = source;
        orderItems = new HashMap<>();
    }

    public int addToOrder(StoreItem thisItem)
    {
        int count = orderItems.containsKey(thisItem) ? orderItems.get(thisItem) : 0;
        orderItems.put(thisItem, count + 1);

        return count + 1;
    }

    public int removeFromOrder(StoreItem thisItem)
    {
        int count = orderItems.containsKey(thisItem) ? orderItems.get(thisItem) : 0;

        if(count == 0)
            return 0;
        else if(count - 1 == 0)
            orderItems.remove(thisItem);
        else
            orderItems.put(thisItem, count - 1);

        return count - 1;
    }

    public int getCountOfItem(StoreItem thisItem)
    {
        return orderItems.containsKey(thisItem) ? orderItems.get(thisItem) : 0;
    }
}

package jumpit.lockereats.Model;

import java.util.ArrayList;

/**
 * Created by cdwil on 12/31/2015.
 */
public class Profile
{
    private String name;
    public String getName()
    {
        return name;
    }

    private ArrayList<Order> orderHistory;
    public ArrayList<Order> getOrderHistory()
    {
        return orderHistory;
    }

    public Profile(String name)
    {
        this.name = name;
    }
}

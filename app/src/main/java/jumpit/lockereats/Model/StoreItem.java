package jumpit.lockereats.Model;

/**
 * Created by cdwil on 12/24/2015.
 */
public class StoreItem
{
    private String name;
    public String getName()
    {
        return name;
    }

    private float price;
    public float getPrice()
    {
        return price;
    }

    public StoreItem(String name, float price)
    {
        this.name = name;
        this.price = price;
    }
}

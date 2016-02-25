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
}

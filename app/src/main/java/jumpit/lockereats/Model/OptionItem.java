package jumpit.lockereats.Model;

/**
 * Created by cdwil on 2/8/2016.
 */
public class OptionItem
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
}

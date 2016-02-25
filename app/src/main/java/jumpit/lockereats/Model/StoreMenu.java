package jumpit.lockereats.Model;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cdwil on 12/8/2015.
 */
public class StoreMenu
{
    private ArrayList<StoreCategory> categories;
    public ArrayList<StoreCategory> getCategories()
    {
        return categories;
    }

    private Restaurant parent;
    public Restaurant getParent()
    {
        return parent;
    }

    public StoreMenu(ArrayList<StoreCategory> items, Restaurant parent)
    {
        this.parent = parent;
        categories = items;
    }
}

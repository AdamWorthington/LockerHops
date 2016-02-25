package jumpit.lockereats.Model;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdwil on 1/18/2016.
 */
public class StoreCategory implements ParentObject
{
    private String category;
    public String getCategory()
    {
        return category;
    }

    private List<Object> storeItems;

    public StoreCategory(String category)
    {
        this.category = category;
    }

    @Override
    public void setChildObjectList(List<Object> list)
    {
        storeItems = list;
    }

    @Override
    public List<Object> getChildObjectList()
    {
        return storeItems;
    }
}

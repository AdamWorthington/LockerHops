package jumpit.lockereats.Model;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdwil on 1/18/2016.
 */
public class StoreCategory implements ParentListItem
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

    public void setChildItemList(List<Object> list)
    {
        storeItems = list;
    }

    @Override
    public List<Object> getChildItemList()
    {
        return storeItems;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

}

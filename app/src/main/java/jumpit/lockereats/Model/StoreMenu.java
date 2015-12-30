package jumpit.lockereats.Model;

import java.util.ArrayList;

/**
 * Created by cdwil on 12/8/2015.
 */
public class StoreMenu
{
    private ArrayList<StoreItem> menu;
    public ArrayList<StoreItem> getMenu()
    {
        return menu;
    }

    public StoreItem getMenuItemByPos(int position)
    {
        if(position > menu.size() - 1 || position < 0)
            return null;

        return menu.get(position);
    }

    public StoreMenu(ArrayList<StoreItem> menu)
    {
        this.menu = menu;
    }
}

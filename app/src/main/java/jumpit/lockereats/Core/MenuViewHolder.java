package jumpit.lockereats.Core;

import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import jumpit.lockereats.R;

/**
 * Created by cdwil on 12/28/2015.
 */
public class MenuViewHolder extends RecyclerView.ViewHolder
{
    protected TextView vMenuItemName;
    protected TextView vMenuItemPrice;
    protected TextView vMenuItemCount;
    protected ImageButton vAddOrder;
    protected ImageButton vRemoveOrder;

    public MenuViewHolder(View v)
    {
        super(v);
        vMenuItemName = (TextView) v.findViewById(R.id.menuItemName);
        vMenuItemPrice = (TextView) v.findViewById(R.id.menuItemPrice);
        vMenuItemCount = (TextView) v.findViewById(R.id.menuItemCount);
        vAddOrder = (ImageButton) v.findViewById(R.id.menuAddOrder);
        vRemoveOrder = (ImageButton) v.findViewById(R.id.menuRemoveOrder);
    }
}

package jumpit.lockereats.Core;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import jumpit.lockereats.Model.Restaurant;
import jumpit.lockereats.Model.StoreItem;
import jumpit.lockereats.R;

/**
 * Created by cdwil on 12/17/2015.
 */
public class MenuArrayAdapter extends RecyclerView.Adapter<MenuViewHolder>
{
    private final List<StoreItem> values;
    private NumberFormat format = NumberFormat.getCurrencyInstance();


    public MenuArrayAdapter(List<StoreItem> values)
    {
        this.values = values;
    }

    @Override
    public int getItemCount()
    {
          return values.size();
    }

    @Override
    public void onBindViewHolder(MenuViewHolder menuViewHolder, int i)
    {
        StoreItem v = values.get(i);
        menuViewHolder.vMenuItemName.setText(v.getName());
        menuViewHolder.vMenuItemPrice.setText(format.format(v.getPrice()));
        int count = Singleton.getInstance().getOrder().getCountOfItem(v);
        menuViewHolder.vMenuItemCount.setText(String.valueOf(count));
        menuViewHolder.itemView.setTag(i); //Set position as tag
    }

    @Override

    public MenuViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
         View itemView = LayoutInflater.
                     from(viewGroup.getContext()).
                     inflate(R.layout.layout_menu_item, viewGroup, false);

         return new MenuViewHolder(itemView);
    }

}


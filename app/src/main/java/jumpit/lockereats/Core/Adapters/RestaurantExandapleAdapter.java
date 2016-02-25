package jumpit.lockereats.Core.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.text.NumberFormat;
import java.util.List;

import jumpit.lockereats.Controller.CustomizeItem;
import jumpit.lockereats.Model.StoreCategory;
import jumpit.lockereats.Model.StoreItem;
import jumpit.lockereats.R;

/**
 * Created by cdwil on 1/18/2016.
 */
public class RestaurantExandapleAdapter extends ExpandableRecyclerAdapter<RestaurantCategoryViewHolder, RestaurantItemViewHolder>
{
    private final List<ParentObject> parents;
    private Context context;

    @Override
    public RestaurantCategoryViewHolder onCreateParentViewHolder(ViewGroup viewGroup)
    {
        View view = LayoutInflater.
                from(viewGroup.getContext()).inflate(R.layout.list_item_restaurant_category, viewGroup, false);
        return new RestaurantCategoryViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(RestaurantCategoryViewHolder restaurantCategoryViewHolder, int i, Object o)
    {
        StoreCategory item = (StoreCategory)o;
        restaurantCategoryViewHolder.TitleTextView.setText(item.getCategory());
    }

    @Override
    public RestaurantItemViewHolder onCreateChildViewHolder(ViewGroup viewGroup)
    {
        View view = LayoutInflater.
                from(viewGroup.getContext()).inflate(R.layout.list_item_restaurant_item, viewGroup, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass along the Database id of the view's object
                customizeItem((int)v.getTag());
            }
        });

        return new RestaurantItemViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(RestaurantItemViewHolder restaurantItemViewHolder, int i, Object o)
    {
        StoreItem item = (StoreItem)o;
        restaurantItemViewHolder.ItemTitleTextView.setText(item.getName());
        restaurantItemViewHolder.ItemDescriptionTextView.setText(item.getDescription());
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String moneyString = formatter.format(item.getPrice());
        restaurantItemViewHolder.ItemPriceTextView.setText(moneyString);
        restaurantItemViewHolder.itemView.setTag(item.getId());
    }

    public RestaurantExandapleAdapter(Context c, List<ParentObject> parents)
    {
        super(c, parents);

        this.parents = parents;
        this.context = c;
    }

    private void customizeItem(int id)
    {
        Intent customizeIntent = new Intent(context, CustomizeItem.class);
        customizeIntent.putExtra("Id", id);
        context.startActivity(customizeIntent);
    }
}

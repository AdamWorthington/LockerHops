package jumpit.lockereats.Core.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;

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
    private final List<ParentListItem> parents;
    private Context context;

    @Override
    public RestaurantCategoryViewHolder onCreateParentViewHolder(ViewGroup viewGroup)
    {
        View view = LayoutInflater.
                from(viewGroup.getContext()).inflate(R.layout.list_item_restaurant_category, viewGroup, false);
        return new RestaurantCategoryViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(RestaurantCategoryViewHolder restaurantCategoryViewHolder, int i, ParentListItem o)
    {
        StoreCategory cat = (StoreCategory)o;
        restaurantCategoryViewHolder.TitleTextView.setText(cat.getCategory());
        restaurantCategoryViewHolder.category = cat;
    }

    @Override
    public RestaurantItemViewHolder onCreateChildViewHolder(ViewGroup viewGroup)
    {
        View view = LayoutInflater.
                from(viewGroup.getContext()).inflate(R.layout.list_item_restaurant_item, viewGroup, false);

        return new RestaurantItemViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(final RestaurantItemViewHolder restaurantItemViewHolder, int i, Object o)
    {
        final StoreItem item = (StoreItem)o;
        restaurantItemViewHolder.ItemTitleTextView.setText(item.getName());
        restaurantItemViewHolder.ItemDescriptionTextView.setText(item.getDescription());
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String moneyString = formatter.format(item.getPrice());
        restaurantItemViewHolder.ItemPriceTextView.setText(moneyString);
        restaurantItemViewHolder.ItemRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass along the Database id of the view's object
                customizeItem(item);
            }
        });

    }

    public RestaurantExandapleAdapter(Context c, List<ParentListItem> parents)
    {
        super(parents);

        this.parents = parents;
        this.context = c;
    }

    private void customizeItem(StoreItem thisItem)
    {
        Intent customizeIntent = new Intent(context, CustomizeItem.class);
        customizeIntent.putExtra("IsEditMode", false);
        customizeIntent.putExtra("ItemParcel", thisItem);
        context.startActivity(customizeIntent);
    }
}

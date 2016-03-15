package jumpit.lockereats.Core.Adapters;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;

import jumpit.lockereats.Model.StoreCategory;
import jumpit.lockereats.R;

/**
 * Created by cdwil on 1/18/2016.
 */
public class RestaurantItemViewHolder extends ChildViewHolder
{
    public TextView ItemTitleTextView;
    public TextView ItemDescriptionTextView;
    public TextView ItemPriceTextView;
    public RelativeLayout ItemRelativeLayout;

    public RestaurantItemViewHolder(View itemView)
    {
        super(itemView);
        ItemTitleTextView = (TextView) itemView.findViewById(R.id.list_item_restaurant_item_name);
        ItemDescriptionTextView = (TextView) itemView.findViewById(R.id.list_item_restaurant_item_description);
        ItemPriceTextView = (TextView) itemView.findViewById(R.id.list_item_restaurant_item_price);
        ItemRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.list_item_restaurant_item_layout);
    }
}
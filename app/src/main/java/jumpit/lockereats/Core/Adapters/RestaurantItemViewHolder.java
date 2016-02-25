package jumpit.lockereats.Core.Adapters;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import jumpit.lockereats.R;

/**
 * Created by cdwil on 1/18/2016.
 */
public class RestaurantItemViewHolder extends ChildViewHolder
{
    public TextView ItemTitleTextView;
    public TextView ItemDescriptionTextView;
    public TextView ItemPriceTextView;

    public RestaurantItemViewHolder(View itemView)
    {
        super(itemView);
        ItemTitleTextView = (TextView) itemView.findViewById(R.id.list_item_resetaurant_item_name);
        ItemDescriptionTextView = (TextView) itemView.findViewById(R.id.list_item_restaurant_item_description);
        ItemPriceTextView = (TextView) itemView.findViewById(R.id.list_item_restaurant_item_price);
    }
}
package jumpit.lockereats.Core.Adapters;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;

import jumpit.lockereats.Model.StoreCategory;
import jumpit.lockereats.R;

/**
 * Created by cdwil on 1/18/2016.
 */
public class RestaurantCategoryViewHolder extends ParentViewHolder
{
    public TextView TitleTextView;
    public StoreCategory category;

    public RestaurantCategoryViewHolder(View itemView)
    {
        super(itemView);
        TitleTextView = (TextView) itemView.findViewById(R.id.list_item_restaurant_category);
    }
}

package jumpit.lockereats.Core.Adapters;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import jumpit.lockereats.R;

/**
 * Created by cdwil on 1/18/2016.
 */
public class CustomizeItemViewHolder extends ChildViewHolder
{
    public TextView ItemCategoryTextView;
    public TextView ItemChooseIndicatorTextView;
    public LinearLayout ItemListOptions;

    public CustomizeItemViewHolder(View itemView)
    {
        super(itemView);
        ItemCategoryTextView = (TextView) itemView.findViewById(R.id.list_item_category);
        ItemChooseIndicatorTextView = (TextView) itemView.findViewById(R.id.list_item_choose_indicator);
        ItemListOptions = (LinearLayout) itemView.findViewById(R.id.list_options);
    }
}
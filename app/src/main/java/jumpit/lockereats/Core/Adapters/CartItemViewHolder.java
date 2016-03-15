package jumpit.lockereats.Core.Adapters;

import android.media.Image;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;

import jumpit.lockereats.R;

/**
 * Created by cdwil on 1/18/2016.
 */
public class CartItemViewHolder extends ChildViewHolder
{
    public TextView ItemTitleTextView;
    public TextView ItemQuantityTextView;
    public TextView ItemPriceTextView;
    public ImageButton ItemRemoveButton;

    public CartItemViewHolder(View itemView)
    {
        super(itemView);
        ItemTitleTextView = (TextView) itemView.findViewById(R.id.item_name);
        ItemQuantityTextView = (TextView) itemView.findViewById(R.id.item_qty);
        ItemPriceTextView = (TextView) itemView.findViewById(R.id.item_price);
        ItemRemoveButton = (ImageButton) itemView.findViewById(R.id.remove_item);
    }
}
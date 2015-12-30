package jumpit.lockereats.Core;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import jumpit.lockereats.Controller.RestaurantMenu;
import jumpit.lockereats.R;

/**
 * Created by cdwil on 12/28/2015.
 */
public class RestaurantViewHolder extends RecyclerView.ViewHolder
{
    protected TextView vRestaurantName;
    protected TextView vRestaurantLocation;
    protected ImageView vRestaurantImage;

    public RestaurantViewHolder(View v)
    {
        super(v);
        vRestaurantName = (TextView) v.findViewById(R.id.menuNameView);
        vRestaurantLocation = (TextView) v.findViewById(R.id.menuLocationView);
        vRestaurantImage = (ImageView) v.findViewById(R.id.menuImageView);
    }
}

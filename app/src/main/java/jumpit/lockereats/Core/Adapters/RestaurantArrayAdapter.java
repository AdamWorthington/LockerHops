package jumpit.lockereats.Core.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import jumpit.lockereats.Controller.RestaurantMenu;
import jumpit.lockereats.Model.Restaurant;
import jumpit.lockereats.R;

/**
 * Created by cdwil on 12/17/2015.
 */
public class RestaurantArrayAdapter extends RecyclerView.Adapter<RestaurantViewHolder>
{
    private final List<Restaurant> values;
    private Context context;

    public RestaurantArrayAdapter(Context context, List<Restaurant> values)
    {
        this.values = values;
        this.context = context;
    }

    @Override
    public int getItemCount()
    {
          return values.size();
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder restaurantViewHolder, int i)
    {
        Restaurant v = values.get(i);
        restaurantViewHolder.vRestaurantName.setText(v.getName());
        //restaurantViewHolder.vRestaurantLocation.setText(v.getStoreLocation().toString());
        restaurantViewHolder.vRestaurantLocation.setText("empty");
    }

    @Override

    public RestaurantViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i)
    {
         View itemView = LayoutInflater.
                     from(viewGroup.getContext()).
                     inflate(R.layout.layout_home_restaurant_item, viewGroup, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // item clicked
                navigateToMenu(i);
            }
        });

         return new RestaurantViewHolder(itemView);
    }


    private void navigateToMenu(int restaurantPos)
    {
        Intent menuIntent = new Intent(context, RestaurantMenu.class);
        menuIntent.putExtra("RestaurantPosition", restaurantPos);
        context.startActivity(menuIntent);
    }
}


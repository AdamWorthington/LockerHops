package jumpit.lockereats.Core;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import jumpit.lockereats.Model.Restaurant;
import jumpit.lockereats.R;

/**
 * Created by cdwil on 12/17/2015.
 */
public class MediaArrayAdapter extends ArrayAdapter<Restaurant>
{
    private final Context context;
    private final ArrayList<Restaurant> values;

    public MediaArrayAdapter(Context context, int resource, ArrayList<Restaurant> objects)
    {
        super(context, resource, objects);

        values = objects;

        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.layout_home_restaurant_item, parent, false);

        Restaurant thisRestaurant = values.get(position);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.menuImageView);
        imageView.setImageBitmap(thisRestaurant.getIcon());

        TextView nameView = (TextView) rowView.findViewById(R.id.menuNameView);
        nameView.setText(thisRestaurant.getName());

        TextView locationView = (TextView) rowView.findViewById(R.id.menuLocationView);
        locationView.setText(thisRestaurant.getStreetAddress());

        return rowView;
    }

}


package jumpit.lockereats.Core.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import jumpit.lockereats.Model.OptionItem;
import jumpit.lockereats.R;

/**
 * Created by cdwil on 2/8/2016.
 */
public class CustomizeOptionArrayAdapter extends ArrayAdapter<OptionItem>
{
    private final Context context;
    private List<OptionItem> items;

    public CustomizeOptionArrayAdapter(Context context, int textViewResourceId)
    {
        super(context, textViewResourceId);
        this.context = context;
    }

    public CustomizeOptionArrayAdapter(Context context, int resource, List<OptionItem> items)
    {
        super(context, resource, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.layout_item_config_option_checked, parent, false);

        OptionItem thisItem = items.get(position);
        TextView nameView = (TextView) rowView.findViewById(R.id.config_option_name);
        nameView.setText(thisItem.getName());

        TextView priceView = (TextView) rowView.findViewById(R.id.config_option_price);
        priceView.setText(String.valueOf(thisItem.getPrice()));

        return rowView;
    }

}

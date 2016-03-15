package jumpit.lockereats.Core.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import jumpit.lockereats.Core.Singleton;
import jumpit.lockereats.Model.FoodItemOption;
import jumpit.lockereats.Model.OptionItem;
import jumpit.lockereats.Model.Order;
import jumpit.lockereats.R;

/**
 * Created by cdwil on 12/17/2015.
 */
public class CartArrayAdapter extends RecyclerView.Adapter<CartItemViewHolder>
{
    private List<Order> values;
    private int itemLayout;

    public CartArrayAdapter(List<Order> values, int itemLayout)
    {
        this.values = values;
        this.itemLayout = itemLayout;
    }

    @Override
    public CartItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int resource)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(itemLayout, viewGroup, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CartItemViewHolder holder, final int position)
    {
        Order order = values.get(position);

        holder.ItemTitleTextView.setText(order.getItem().getName());
        holder.ItemQuantityTextView.setTextColor(order.getQuantity());

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String priceString = formatter.format(order.calculatePrice());
        holder.ItemPriceTextView.setText(priceString);

        holder.ItemRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFromCart(values.get(position));
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, values.size());
            }
        });

    }

    private void removeFromCart(Order thisOrder)
    {
        Singleton.getInstance().removeFromCart(thisOrder);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public List<Order> getValues()
    {
        return values;
    }
}


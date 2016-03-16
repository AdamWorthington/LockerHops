package jumpit.lockereats.Core.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import jumpit.lockereats.Controller.Cart;
import jumpit.lockereats.Controller.CustomizeItem;
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
    public static final int RESULT_CUSTOMIZE_ITEM = 2144;
    private List<Order> values;
    private int itemLayout;
    private Activity context;

    private List<PropertyChangeListener> listener = new ArrayList<PropertyChangeListener>();

    public CartArrayAdapter(List<Order> values, int itemLayout, Activity context)
    {
        this.values = values;
        this.itemLayout = itemLayout;
        this.context = context;
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
        holder.ItemQuantityTextView.setText("x" + order.getQuantity());

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String priceString = formatter.format(order.calculatePrice());
        holder.ItemPriceTextView.setText(priceString);

        holder.ItemRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFromCart(values.get(position), position);

            }
        });

        holder.ItemRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editItemInCart(values.get(position), position);
            }
        });
    }

    private void removeFromCart(Order thisOrder, int position)
    {
        Singleton.getInstance().removeFromCart(thisOrder);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, values.size());
        notifyListeners("values");
    }

    private void editItemInCart(Order thisOrder, int position)
    {
        Intent customizeIntent = new Intent(context, CustomizeItem.class);
        customizeIntent.putExtra("IsEditMode", true);
        customizeIntent.putExtra("Order", thisOrder);
        customizeIntent.putExtra("OrderPos", position);
        context.startActivityForResult(customizeIntent, RESULT_CUSTOMIZE_ITEM);
    }

    private void notifyListeners(String property) {
        for (PropertyChangeListener name : listener) {
            name.propertyChange(new PropertyChangeEvent(this, property, null, null));
        }
    }

    public void addChangeListener(PropertyChangeListener newListener) {
        listener.add(newListener);
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


package jumpit.lockereats.Controller;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import jumpit.lockereats.Core.Singleton;
import jumpit.lockereats.Model.Order;
import jumpit.lockereats.Model.StoreItem;
import jumpit.lockereats.R;

public class Cart extends AppCompatActivity
{
    private float subtotal = 0.0f;
    private int totalItemsOrdered = 0;
    private final float processingfee = 0.75f;
    private final float salesTax = .07f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Order cart = Singleton.getInstance().getOrder();
        HashMap<StoreItem, Integer> items = cart.getOrderItems();
        ArrayList<StoreItem> keys = new ArrayList<>(items.keySet());
        ArrayList<Integer> vals = new ArrayList<>(items.values());
        NumberFormat format = NumberFormat.getCurrencyInstance();

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < keys.size(); i++)
        {
            StoreItem current = keys.get(i);
            Integer count = vals.get(i);
            totalItemsOrdered += count.intValue();
            subtotal += (current.getPrice() * count);

            String name = current.getName();
            String price = format.format(current.getPrice());
            String num = String.valueOf(count);
            sb.append(price);
            sb.append(" ");
            sb.append(name);
            sb.append(" ");
            sb.append("x");
            sb.append(num);
            sb.append("     ");
            sb.append(price);
            sb.append('\n');
        }

        TextView cartSubtotal = (TextView) findViewById(R.id.cart_subtotal);
        cartSubtotal.setText(format.format(subtotal));

        TextView checkoutItems = (TextView) findViewById(R.id.checkout_items);
        checkoutItems.setText(sb.toString());

        TextView processFee = (TextView) findViewById(R.id.process_fee);
        processFee.setText(format.format(processingfee));

        TextView taxFee = (TextView) findViewById(R.id.sales_tax);
        taxFee.setText(format.format(salesTax*subtotal));

        TextView cartTotal = (TextView) findViewById(R.id.cart_total);
        cartTotal.setText(format.format((subtotal * salesTax) + subtotal + processingfee));

        TextView cartItems = (TextView) findViewById(R.id.cart_items);
        cartItems.setText(String.valueOf(totalItemsOrdered) + " Items");
    }

    public void onCheckout(View v)
    {

    }
}

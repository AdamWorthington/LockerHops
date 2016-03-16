package jumpit.lockereats.Controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import jumpit.lockereats.Core.Adapters.CartArrayAdapter;
import jumpit.lockereats.Core.Adapters.CustomizeItemArrayAdapter;
import jumpit.lockereats.Core.Singleton;
import jumpit.lockereats.Model.Order;
import jumpit.lockereats.Model.StoreItem;
import jumpit.lockereats.R;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

public class Cart extends AppCompatActivity implements PropertyChangeListener
{
    private float subtotal = 0.0f;
    private int totalItemsOrdered = 0;
    private final float processingfee = 0.75f;
    private final float salesTax = .07f;
    private CartArrayAdapter contentAdapter;
    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)

            .clientId("<YOUR_CLIENT_ID>");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.cartToolbar);
        myToolbar.setTitle("Order Summary");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<Order> cart = Singleton.getInstance().getCart();
        contentAdapter = new CartArrayAdapter(cart, R.layout.layout_cart_item, this);
        contentAdapter.addChangeListener(this);
        RecyclerView cartRecyclerView = (RecyclerView) findViewById(R.id.cart_list);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        cartRecyclerView.setLayoutManager(llm);
        cartRecyclerView.setAdapter(contentAdapter);

        configureViews();

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void configureViews()
    {
        ArrayList<Order> cart = Singleton.getInstance().getCart();
        subtotal = 0;
        totalItemsOrdered = 0;
        for(Order o : cart)
        {
            totalItemsOrdered += o.getQuantity();
            subtotal += o.calculatePrice();
        }

        NumberFormat format = NumberFormat.getCurrencyInstance();

        TextView cartSubtotal = (TextView) findViewById(R.id.cart_subtotal);
        cartSubtotal.setText(format.format(subtotal));

        TextView processFee = (TextView) findViewById(R.id.process_fee);
        processFee.setText(format.format(processingfee));

        TextView taxFee = (TextView) findViewById(R.id.sales_tax);
        taxFee.setText(format.format(salesTax*subtotal));

        TextView cartTotal = (TextView) findViewById(R.id.cart_total);
        cartTotal.setText(format.format((subtotal * salesTax) + subtotal + processingfee));

        TextView cartItems = (TextView) findViewById(R.id.cart_items);
        cartItems.setText(String.valueOf(totalItemsOrdered) + " Items");
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    public void onCheckout(View v)
    {
        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to
        //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
        //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
        //     later via calls from your server.

        /*Order cart = Singleton.getInstance().getOrder();
        PayPalPayment payment = new PayPalPayment(new BigDecimal((subtotal * salesTax) + subtotal + processingfee), "USD", cart.getSource().getName(),
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, 0);*/
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
        {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED)
        {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
        {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
        else if (resultCode == CartArrayAdapter.RESULT_CUSTOMIZE_ITEM)
        {
            Order returnOrder = data.getParcelableExtra("Order");
            int orderPos = data.getIntExtra("OrderPos", 0);

            Singleton.getInstance().removeFromCartAt(orderPos);
            Singleton.getInstance().addToCart(orderPos, returnOrder);
            configureViews();
            contentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent event)
    {
        configureViews();
    }
}

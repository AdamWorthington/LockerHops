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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jumpit.lockereats.Core.Adapters.CustomizeItemArrayAdapter;
import jumpit.lockereats.Core.Singleton;
import jumpit.lockereats.Model.FoodItemOption;
import jumpit.lockereats.Model.Order;
import jumpit.lockereats.Model.StoreItem;
import jumpit.lockereats.R;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

public class CustomizeItem extends AppCompatActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_item);

        /*Recieve the intent from the restaurant menu and get selected item*/
        Intent intent = getIntent();
        int id = intent.getIntExtra("Id", 0);

        /*Submit a query to recieve information concerning ingredients, etc*/
        List<String> optionNames = new ArrayList<String>();
        optionNames.add("zesty");
        optionNames.add("bbq");
        optionNames.add("garlic");
        optionNames.add("habanero");
        optionNames.add("regular");
        List<Double> optionPrices = new ArrayList<Double>();
        optionPrices.add(12.0);
        optionPrices.add(10.0);
        optionPrices.add(80.0);
        optionPrices.add(7.0);
        optionPrices.add(4.0);

        FoodItemOption option = new FoodItemOption("Sauce", true, 0, 1, optionNames, optionPrices);
        FoodItemOption option2 = new FoodItemOption("Dip", true, 0, 1, optionNames, optionPrices);
        FoodItemOption option3 = new FoodItemOption("Crust Type", false, 1, 1, optionNames, optionPrices);
        ArrayList<FoodItemOption> options = new ArrayList<>();
        options.add(option);
        options.add(option2);
        options.add(option3);

        CustomizeItemArrayAdapter listAdapter = new CustomizeItemArrayAdapter(options, R.layout.layout_list_item_config_item);
        RecyclerView configRecyclerView = (RecyclerView) findViewById(R.id.itemConfigRecyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        configRecyclerView.setLayoutManager(llm);
        configRecyclerView.setAdapter(listAdapter);
    }
}

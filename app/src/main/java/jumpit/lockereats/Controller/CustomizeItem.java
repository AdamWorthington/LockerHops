package jumpit.lockereats.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import jumpit.lockereats.Core.Adapters.CartArrayAdapter;
import jumpit.lockereats.Core.Adapters.CustomizeItemArrayAdapter;
import jumpit.lockereats.Core.Singleton;
import jumpit.lockereats.Model.FoodItemOption;
import jumpit.lockereats.Model.Order;
import jumpit.lockereats.Model.StoreItem;
import jumpit.lockereats.R;

public class CustomizeItem extends AppCompatActivity implements PropertyChangeListener
{
    private CustomizeItemArrayAdapter contentAdapter = null;
    private double total = 0f;
    private int quantity = 1;
    private StoreItem item;
    private Order order;
    private int orderPos;
    private LinearLayoutManager llm = null;
    private RecyclerView configRecyclerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_item);

        /*Recieve the intent from the restaurant menu and get selected item*/
        Intent intent = getIntent();
        boolean isEditMode = intent.getBooleanExtra("IsEditMode", false);
        if(isEditMode)
            createEditLayout(intent);
        else
            createNormalLayout(intent);
    }

    private void createEditLayout(Intent intent)
    {
        order = intent.getParcelableExtra("Order");
        orderPos = intent.getIntExtra("OrderPos", 0);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.customizeItemToolbar);
        myToolbar.setTitle(order.getItem().getName());
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner spinner = (Spinner) findViewById(R.id.orderQuantity);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.orderquantity_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setSelection(order.getQuantity() - 1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onSpinnerChanged(parent, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button addButton = (Button) findViewById(R.id.addToCartButton);
        addButton.setText("Update Cart");
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCart(v);
            }
        });

        contentAdapter = new CustomizeItemArrayAdapter(order.getOptions(), R.layout.layout_list_item_config_item);
        configRecyclerView = (RecyclerView) findViewById(R.id.itemConfigRecyclerView);
        llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        configRecyclerView.setLayoutManager(llm);
        configRecyclerView.setAdapter(contentAdapter);

        contentAdapter.addChangeListener(this);
    }

    private void createNormalLayout(Intent intent)
    {
        item = intent.getParcelableExtra("ItemParcel");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.customizeItemToolbar);
        myToolbar.setTitle(item.getName());
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner spinner = (Spinner) findViewById(R.id.orderQuantity);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.orderquantity_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View  view, int position, long id) {
                onSpinnerChanged(parent, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

        contentAdapter = new CustomizeItemArrayAdapter(options, R.layout.layout_list_item_config_item);
        configRecyclerView = (RecyclerView) findViewById(R.id.itemConfigRecyclerView);
        llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        configRecyclerView.setLayoutManager(llm);
        configRecyclerView.setAdapter(contentAdapter);

        contentAdapter.addChangeListener(this);
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

    private void navigateBack()
    {
        this.finish();
    }

    private void onSpinnerChanged(AdapterView<?> parent, int position)
    {
        //The array stored by the adapter view is a string representation of 1-10
        String selection = (String)parent.getItemAtPosition(position);
        int value = Integer.valueOf(selection);
        quantity = value;

        calculateTotal();
    }

    @Override
    public void propertyChange(PropertyChangeEvent event)
    {
        total = (double)event.getNewValue();

        String moneyString = NumberFormat.getCurrencyInstance().format(total * quantity);
        TextView totalTextView = (TextView) findViewById(R.id.itemSubtotal);
        totalTextView.setText(moneyString);
    }

    private void calculateTotal()
    {
        String moneyString = NumberFormat.getCurrencyInstance().format(total * quantity);
        TextView totalTextView = (TextView) findViewById(R.id.itemSubtotal);
        totalTextView.setText(moneyString);
    }

    public void onAddToCart(View v)
    {
        int invalidPos = contentAdapter.validateForm();
        if(invalidPos != -1)
        {
            configRecyclerView.smoothScrollToPosition(invalidPos);
        }
        else
        {
            /* We are clear to add the order to the cart */
            Order thisOrder = new Order(quantity, item, new ArrayList<FoodItemOption>(contentAdapter.getValues()));
            Singleton.getInstance().addToCart(thisOrder);
            this.finish();
        }
    }

    private void updateCart(View v)
    {
        Intent resultData = new Intent();
        resultData.putExtra("Order", order);
        resultData.putExtra("OrderPos", orderPos);
        setResult(CartArrayAdapter.RESULT_CUSTOMIZE_ITEM, resultData);
        finish();
    }
}

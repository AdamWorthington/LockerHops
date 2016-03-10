package jumpit.lockereats.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import jumpit.lockereats.Core.Adapters.CustomizeItemArrayAdapter;
import jumpit.lockereats.Model.FoodItemOption;
import jumpit.lockereats.R;

public class CustomizeItem extends AppCompatActivity implements PropertyChangeListener
{
    private CustomizeItemArrayAdapter contentAdapter = null;
    private double total = 0f;
    private int quantity = 1;
    private LinearLayoutManager llm;
    private RecyclerView configRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_item);

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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onSpinnerChanged(parent, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

        contentAdapter = new CustomizeItemArrayAdapter(options, R.layout.layout_list_item_config_item);
        configRecyclerView = (RecyclerView) findViewById(R.id.itemConfigRecyclerView);
        llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        configRecyclerView.setLayoutManager(llm);
        configRecyclerView.setAdapter(contentAdapter);

        contentAdapter.addChangeListener(this);
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
        }
    }
}

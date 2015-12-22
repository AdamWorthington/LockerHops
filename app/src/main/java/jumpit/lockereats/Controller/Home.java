package jumpit.lockereats.Controller;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.List;

import jumpit.lockereats.Core.MediaArrayAdapter;
import jumpit.lockereats.Model.Restaurant;
import jumpit.lockereats.R;

public class Home extends AppCompatActivity
{
    /*
    Collection of all restaurants that LockerEats serves.
     */
    private ArrayList<Restaurant> restaurants;

    /*
    Grid view representation of the restaurants
     */
    private GridView restaurantsGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        restaurants = new ArrayList<>();
        restaurants.add(new Restaurant("test", null, null, null));

        /*
        Hook up the grid with the restaurants data structure list and populate view.
         */
        restaurantsGrid = (GridView)findViewById(R.id.restuarantGridView);
        MediaArrayAdapter gridAdapter = new MediaArrayAdapter(this,
                R.layout.layout_home_restaurant_item, restaurants);
        restaurantsGrid.setAdapter(gridAdapter);
        restaurantsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}

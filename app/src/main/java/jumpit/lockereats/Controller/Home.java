package jumpit.lockereats.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import jumpit.lockereats.Core.RestaurantArrayAdapter;
import jumpit.lockereats.Core.Singleton;
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

        Toolbar myToolbar = (Toolbar) findViewById(R.id.home_toolbar);
        myToolbar.inflateMenu(R.menu.menu_home);
        setSupportActionBar(myToolbar);

        /*Get the list of restaurants*/
        restaurants = Singleton.getInstance().getRestaurants();


        /*
        Hook up the grid with the restaurants data structure list and populate view.
         */
        RecyclerView listMenu = (RecyclerView) findViewById(R.id.restuarantGridView);
        listMenu.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listMenu.setLayoutManager(llm);
        RestaurantArrayAdapter listAdapter = new RestaurantArrayAdapter(this, restaurants);
        listMenu.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_restaurant_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

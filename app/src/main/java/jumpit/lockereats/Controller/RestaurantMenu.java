package jumpit.lockereats.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jumpit.lockereats.Core.MenuArrayAdapter;
import jumpit.lockereats.Core.Singleton;
import jumpit.lockereats.Model.Restaurant;
import jumpit.lockereats.Model.StoreItem;
import jumpit.lockereats.Model.StoreMenu;
import jumpit.lockereats.R;

public class RestaurantMenu extends AppCompatActivity
{
    private Restaurant curRestaurant = null;
    private StoreMenu curMenu = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);

        /*Recieve the intent from the main menu and get selected restaurant*/
        Intent intent = getIntent();
        int position = intent.getIntExtra("RestaurantPosition", 0);
        curRestaurant = Singleton.getInstance().getRestaurantByPos(position);

        curMenu = curRestaurant.getStoreMenu();

        Toolbar toolbar = (Toolbar) findViewById(R.id.menu_toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout ct = (CollapsingToolbarLayout) findViewById(R.id.menu_collapsing_bar);
        ct.setTitle(curRestaurant.getName());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_cart);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigateToCart();
            }
        });

        RecyclerView listMenu = (RecyclerView) findViewById(R.id.menuListView);
        listMenu.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listMenu.setLayoutManager(llm);
        MenuArrayAdapter listAdapter = new MenuArrayAdapter(curMenu.getMenu());
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

    public void NavigateToCart()
    {
        Intent cartIntent = new Intent(this, Cart.class);
        startActivity(cartIntent);
    }

    public void AddToOrder(View view)
    {
        LinearLayout parent = (LinearLayout) view.getParent().getParent().getParent().getParent();
        int position = (int)parent.getTag();
        StoreItem thisItem = curMenu.getMenuItemByPos(position);

        int newCount = Singleton.getInstance().getOrder().addToOrder(thisItem);
        ((View) view.getParent()).findViewById(R.id.menuItemCount);
        TextView countView = (TextView)parent.findViewById(R.id.menuItemCount);
        countView.setText(String.valueOf(newCount));
    }

    public void RemoveFromOrder(View view)
    {
        LinearLayout parent = (LinearLayout) view.getParent().getParent().getParent().getParent();
        int position = (int)parent.getTag();
        StoreItem thisItem = curMenu.getMenuItemByPos(position);

        int newCount = Singleton.getInstance().getOrder().removeFromOrder(thisItem);
        ((View) view.getParent()).findViewById(R.id.menuItemCount);
        TextView countView = (TextView)parent.findViewById(R.id.menuItemCount);
        countView.setText(String.valueOf(newCount));
    }
}

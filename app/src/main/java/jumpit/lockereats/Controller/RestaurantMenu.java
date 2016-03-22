package jumpit.lockereats.Controller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.lockerhops.backend.lockerHopsAPI.LockerHopsAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jumpit.lockereats.Core.Adapters.RestaurantArrayAdapter;
import jumpit.lockereats.Core.Adapters.RestaurantExandapleAdapter;
import jumpit.lockereats.Core.Singleton;
import jumpit.lockereats.Model.Restaurant;
import jumpit.lockereats.Model.StoreCategory;
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

        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.vonslogo);
        ImageView logoView = (ImageView) findViewById(R.id.backdrop);
        logoView.setImageBitmap(logo);

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
        ArrayList<StoreCategory> categories = curMenu.getCategories();
        List<ParentListItem> categoriesCasted = new ArrayList<>();
        for(StoreCategory sc : categories)
            categoriesCasted.add(sc);
        RestaurantExandapleAdapter menuAdapter = new RestaurantExandapleAdapter(this, categoriesCasted);
        listMenu.setAdapter(menuAdapter);
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
        /*LinearLayout parent = (LinearLayout) view.getParent().getParent().getParent().getParent();
        int position = (int)parent.getTag();
        StoreItem thisItem = curMenu.getMenuItemByPos(position);

        int newCount = Singleton.getInstance().getOrder().addToOrder(thisItem);
        ((View) view.getParent()).findViewById(R.id.menuItemCount);
        TextView countView = (TextView)parent.findViewById(R.id.menuItemCount);
        countView.setText(String.valueOf(newCount));*/
    }

    public void RemoveFromOrder(View view)
    {
        /*LinearLayout parent = (LinearLayout) view.getParent().getParent().getParent().getParent();
        int position = (int)parent.getTag();
        StoreItem thisItem = curMenu.getMenuItemByPos(position);

        int newCount = Singleton.getInstance().getOrder().removeFromOrder(thisItem);
        ((View) view.getParent()).findViewById(R.id.menuItemCount);
        TextView countView = (TextView)parent.findViewById(R.id.menuItemCount);
        countView.setText(String.valueOf(newCount));*/
    }

    private class GetMenuTask extends AsyncTask<Object, Void, List<Restaurant>>
    {
        private LockerHopsAPI myApiService = null;

        private ProgressDialog dialog = new ProgressDialog(RestaurantMenu.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Fetching menu");
            this.dialog.show();
        }

        @Override
        protected List<jumpit.lockereats.Model.Restaurant> doInBackground(Object... params)
        {
            if(myApiService == null)
            {  // Only do this once
                LockerHopsAPI.Builder builder = new LockerHopsAPI.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                        .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                // end options for devappserver

                myApiService = builder.build();
            }

            try {
                List<com.lockerhops.backend.lockerHopsAPI.model.Restaurant> restaurants = myApiService.restaurant().getAllRestaurants().execute().getItems();
                return jumpit.lockereats.Model.Restaurant.convert(restaurants);
            } catch (IOException e) {
                Log.d("Fetch Restaurant", e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<jumpit.lockereats.Model.Restaurant> result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if(result != null)
            {
                listAdapter = new RestaurantArrayAdapter(RestaurantMenu.this, result);
                listMenu.setAdapter(listAdapter);
            }
            else
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RestaurantMenu.this);

                // set title

                alertDialogBuilder.setTitle("An error occured");

                // set dialog message
                alertDialogBuilder
                        .setMessage("We were unable to fetch the menu. Close the app and try again later.")
                        .setPositiveButton("Okay", null);

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        }
    }
}

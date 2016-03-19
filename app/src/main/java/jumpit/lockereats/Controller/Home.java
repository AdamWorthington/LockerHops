package jumpit.lockereats.Controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.GridView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.lockerhops.backend.lockerHopsAPI.LockerHopsAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jumpit.lockereats.Core.Adapters.RestaurantArrayAdapter;
import jumpit.lockereats.Core.Singleton;
import jumpit.lockereats.Model.Restaurant;
import jumpit.lockereats.R;

public class Home extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
{
    /*
    Collection of all restaurants that LockerEats serves.
     */
    private ArrayList<Restaurant> restaurants;

    /*
    Grid view representation of the restaurants
     */
    private GridView restaurantsGrid;

    private GoogleApiClient mGoogleApiClient;
    private RestaurantArrayAdapter listAdapter;
    private LinearLayoutManager llm;
    private RecyclerView listMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.home_toolbar);
        myToolbar.inflateMenu(R.menu.menu_home);
        setSupportActionBar(myToolbar);

        /*Get the list of restaurants*/
        new GetRestaurantsTask().execute();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        /*
        Hook up the grid with the restaurants data structure list and populate view.
         */
        listMenu = (RecyclerView) findViewById(R.id.restuarantGridView);
        listMenu.setHasFixedSize(true);
        llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listMenu.setLayoutManager(llm);
    }

    /*Google API calls failed when logging in*/
    public void onConnectionFailed(ConnectionResult r)
    {
        //Suppress..
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
        else if(id == R.id.action_logout)
        {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout()
    {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if(status.isSuccess())
                            handleLogoutSuccess();
                        else
                            handleLogoutFail();
                    }
                });
    }

    private void handleLogoutSuccess()
    {
        Intent welcomeIntent = new Intent(this, Welcome.class);
        welcomeIntent.putExtra("LogoutSuccess", true);
        startActivity(welcomeIntent);
        finish();
    }

    private void handleLogoutFail()
    {

    }

    private class GetRestaurantsTask extends AsyncTask<Object, Void, List<Restaurant>>
    {
        private LockerHopsAPI myApiService = null;

        private ProgressDialog dialog = new ProgressDialog(Home.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Fetching restaurants nearby");
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
                listAdapter = new RestaurantArrayAdapter(Home.this, result);
                listMenu.setAdapter(listAdapter);
            }
            else
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Home.this);

                // set title

                alertDialogBuilder.setTitle("An error occured");

                // set dialog message
                alertDialogBuilder
                        .setMessage("We were unable to fetch any restaurants. Close the app and try again later.")
                        .setPositiveButton("Okay", null);

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        }
    }

}

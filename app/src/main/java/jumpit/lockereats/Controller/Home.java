package jumpit.lockereats.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;

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
        restaurants = Singleton.getInstance().getRestaurants();

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
        RecyclerView listMenu = (RecyclerView) findViewById(R.id.restuarantGridView);
        listMenu.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listMenu.setLayoutManager(llm);
        RestaurantArrayAdapter listAdapter = new RestaurantArrayAdapter(this, restaurants);
        listMenu.setAdapter(listAdapter);
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
}

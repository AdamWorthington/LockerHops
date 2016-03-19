package jumpit.lockereats.Tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.lockerhops.backend.lockerHopsAPI.LockerHopsAPI;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import jumpit.lockereats.Model.Restaurant;

/**
 * Created by cdwil on 3/18/2016.
 */
public class GetRestaurantsTask extends AsyncTask<Object, Void, List<jumpit.lockereats.Model.Restaurant>>
{
    private static LockerHopsAPI myApiService = null;

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
    }
}


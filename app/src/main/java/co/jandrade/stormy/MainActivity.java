package co.jandrade.stormy;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity implements LocationProvider.LocationCallback{
    public static String TAG = MainActivity.class.getSimpleName();
    private CurrentWeather mCurrentWeather;
    private LocationProvider mLocationProvider;
    private double mCurrentLatitude;
    private double mCurrentLongitude;

    @InjectView(R.id.timeLabel) TextView mTimeLabel;
    @InjectView(R.id.temperatureLabel) TextView mTemperatureLabel;
    @InjectView(R.id.humidityValue) TextView mHumidityValue;
    @InjectView(R.id.precipValue) TextView mPrecipValue;
    @InjectView(R.id.summaryLabel) TextView mSummaryLabel;
    @InjectView(R.id.iconImageView) ImageView mIconImageView;
    @InjectView(R.id.refreshImageView) ImageView mRefreshImageView;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast();
            }
        });
        mLocationProvider = new LocationProvider(this,this);
        mLocationProvider.connect();
        Log.d(TAG,"lati"+mCurrentLatitude);
        getForecast();


    }
    @Override
    protected void onResume(){
        super.onResume();
        mLocationProvider.connect();
        Log.d(TAG, "lati" + mCurrentLatitude);
        getForecast();
    }
    protected void onPause(){
        super.onPause();
        mLocationProvider.disconnect();
    }

    private void getForecast() {
        String baseUrl = getString(R.string.base_url);
        String apiKey = getString(R.string.api_key);
        //String latitude = getString(R.string.latitude);
        //String longitude = getString(R.string.longitude);
        String latitude = String.valueOf(mCurrentLatitude);
        String longitude = String.valueOf(mCurrentLongitude);

        String url = baseUrl+apiKey+"/"+latitude+","+longitude;
        Log.d(TAG,"forcast call..."+url);

        if(isNetworkAvailable()) {
            toggleRefresh();
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = client.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.d(TAG, request.url().toString(), e);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });

                    try {
                        String jsonData = response.body().string();

                        if (response.isSuccessful()) {
                            mCurrentWeather = getCurrentDetails(jsonData);
                            //send message to main thread
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    updateDisplay();
                                }
                            });


                        } else {
                            alertUserAboutError();
                        }

                    } catch (IOException e) {
                        Log.e(TAG, "fail...", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "fail...json", e);
                    }
                }
            });

        }else{
            Toast.makeText(this, "Network is unavailable", Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefresh() {
        if(mProgressBar.getVisibility()==View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        }else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        mTemperatureLabel.setText(mCurrentWeather.getTemperature()+"");
        mTimeLabel.setText("At "+mCurrentWeather.getFormattedTime()+" it will be:");
        mHumidityValue.setText(mCurrentWeather.getHuminity()+"");
        mPrecipValue.setText(mCurrentWeather.getPrecipChance() + " %");
        mSummaryLabel.setText(mCurrentWeather.getSummary());
        Drawable drawable = getResources().getDrawable(mCurrentWeather.getIconId());
        mIconImageView.setImageDrawable(drawable);
    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject currently = forecast.getJSONObject("currently");

        String icon = currently.getString("icon");
        long time = currently.getLong("time");
        double humidity = currently.getDouble("humidity");
        double precipChance = currently.getDouble("precipProbability");
        double temperature = currently.getDouble("temperature");
        String summary = currently.getString("summary");

        CurrentWeather currentWeather = new CurrentWeather();

        currentWeather.setHuminity(humidity);
        currentWeather.setIcon(icon);
        currentWeather.setPrecipChance(precipChance);
        currentWeather.setTemperature(temperature);
        currentWeather.setSummary(summary);
        currentWeather.setTime(time);
        currentWeather.setTimeZone(timezone);

        Log.d(TAG,currentWeather.getFormattedTime());
        return currentWeather;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        //requires permission ACCESS_NETWORK_STATE
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //try catch
        boolean isAvailable = false;
        //Log.d(TAG,String.valueOf(networkInfo.isConnected()));
        // if there is network and is connected
        if(networkInfo!=null && networkInfo.isConnected()){
            isAvailable = true;
        }

        return isAvailable;
    }

    private void alertUserAboutError() {
        //instance
        AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        //show alert, getFragmentManager() ready for us to use
        alertDialogFragment.show(getFragmentManager(),"weather_api_error");
    }


    @Override
    public void handleNewLocation(Location location) {
        Log.d(TAG, "handling new loc..."+location.toString());
        mCurrentLatitude = location.getLatitude();
        mCurrentLongitude = location.getLongitude();
        getForecast();
    }
}

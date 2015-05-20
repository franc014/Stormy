package co.jandrade.stormy.ui;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import co.jandrade.stormy.LocationProvider;
import co.jandrade.stormy.R;
import co.jandrade.stormy.weather.Current;
import co.jandrade.stormy.weather.Day;
import co.jandrade.stormy.weather.Forecast;
import co.jandrade.stormy.weather.Hour;


public class MainActivity extends ActionBarActivity implements LocationProvider.LocationCallback {
    public static final String HOURLY_FORECAST = "HORLY_FORECAST";;
    public static String TAG = MainActivity.class.getSimpleName();
    public static final String DAILY_FORECAST = "DAILY_FORECAST";
    private Forecast mForecast;
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
        Log.d(TAG, "lati" + mCurrentLatitude);
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
        Log.d(TAG, "forcast call..." + url);

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
                            mForecast = parseForecastDetails(jsonData);
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
        mTemperatureLabel.setText(mForecast.getCurrent().getTemperature()+"");
        mTimeLabel.setText("At "+ mForecast.getCurrent().getFormattedTime()+" it will be:");
        mHumidityValue.setText(mForecast.getCurrent().getHuminity()+"");
        mPrecipValue.setText(mForecast.getCurrent().getPrecipChance() + " %");
        mSummaryLabel.setText(mForecast.getCurrent().getSummary());
        Drawable drawable = getResources().getDrawable(mForecast.getCurrent().getIconId());
        mIconImageView.setImageDrawable(drawable);
    }

    private Forecast parseForecastDetails(String jsonData) throws JSONException{
        Forecast forecast = new Forecast();
        forecast.setCurrent(getCurrentDetails(jsonData));
        forecast.setHours(getHourlyDetails(jsonData));
        forecast.setDays(getDailyDetails(jsonData));
        return forecast;
    }

    private List<Day> getDailyDetails(String jsonData) throws JSONException{

        JSONObject forecast = new JSONObject(jsonData);
        String timeZone = forecast.getString("timezone");
        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");
        Log.d(TAG, data.toString());
        List<Day> days = new ArrayList<>();
        for(int i=0;i< data.length();i++){
            Day day = new Day();
            JSONObject jsonDay = data.getJSONObject(i);
            day.setTime(jsonDay.getLong("time"));
            day.setSummary(jsonDay.getString("summary"));
            day.setIcon(jsonDay.getString("icon"));
            day.setTemperatureMax(jsonDay.getDouble("temperatureMax"));
            day.setTimeZone(timeZone);
            days.add(day);
        }
        return days;
    }

    private List<Hour> getHourlyDetails(String jsonData) throws JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timeZone = forecast.getString("timezone");
        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        List<Hour> hours = new ArrayList<>();

        for(int i=0;i<data.length();i++){
            Hour hour = new Hour();
            JSONObject jsonHour = data.getJSONObject(i);
            hour.setIcon(jsonHour.getString("icon"));
            hour.setSummary(jsonHour.getString("summary"));
            hour.setTime(jsonHour.getLong("time"));
            hour.setTimeZone(timeZone);
            hour.setTemperature(jsonHour.getDouble("temperature"));
            hours.add(hour);
        }

        return hours;
    }


    private Current getCurrentDetails(String jsonData) throws JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject currently = forecast.getJSONObject("currently");

        String icon = currently.getString("icon");
        long time = currently.getLong("time");
        double humidity = currently.getDouble("humidity");
        double precipChance = currently.getDouble("precipProbability");
        double temperature = currently.getDouble("temperature");
        String summary = currently.getString("summary");

        Current current = new Current();

        current.setHuminity(humidity);
        current.setIcon(icon);
        current.setPrecipChance(precipChance);
        current.setTemperature(temperature);
        current.setSummary(summary);
        current.setTime(time);
        current.setTimeZone(timezone);

        Log.d(TAG, current.getFormattedTime());
        return current;
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
    @OnClick (R.id.dailyButton)
    public void startDailyActivity(View view){
        Intent intent = new Intent(this, DailyForecastActivity.class);
        //putting Array List of Parcelable data
        intent.putParcelableArrayListExtra(DAILY_FORECAST, (ArrayList<? extends Parcelable>) mForecast.getDays());
        //intent.putExtra(DAILY_FORECAST, (Parcelable) mForecast.getDays());
        startActivity(intent);
    }
    @OnClick (R.id.horlyButton)
    public void startHourlyActivity(View view){
        Intent intent = new Intent(this, HourlyForecastActivity.class);
        intent.putParcelableArrayListExtra(HOURLY_FORECAST, (ArrayList<? extends Parcelable>) mForecast.getHours());
        startActivity(intent);
    }
}

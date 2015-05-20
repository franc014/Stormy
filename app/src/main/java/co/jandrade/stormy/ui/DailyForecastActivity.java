package co.jandrade.stormy.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import co.jandrade.stormy.R;
import co.jandrade.stormy.adapters.DayAdapter;
import co.jandrade.stormy.weather.Day;

public class DailyForecastActivity extends ListActivity {
    private static final String TAG = DailyForecastActivity.class.getSimpleName();
    private List<Day> mDays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);

        Intent intent = getIntent();
        //getting data from MainActivity.
        //Using this method to get the ArrayList of data, not the array
        mDays = intent.getParcelableArrayListExtra(MainActivity.DAILY_FORECAST);
        //Parcelable[]parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);

        //mDays = Arrays.asList(Arrays.copyOf(parcelables,parcelables.length,Day[].class));
        //mDays = Arrays.asList(parcelables.toArray());
        /*String[] daysOfTheWeek = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, daysOfTheWeek);
        setListAdapter(arrayAdapter);*/
        DayAdapter dayAdapter = new DayAdapter(this,mDays);
        setListAdapter(dayAdapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);


        String dayOfTheWeek = mDays.get(position).getDayOfTheWeek();
        String conditions = mDays.get(position).getSummary();
        String highTemp = mDays.get(position).getTemperatureMax()+"";
        String message = String.format("On %s the high will be %s and it will be %s",
                dayOfTheWeek,highTemp,conditions
                );
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }
}

package co.jandrade.stormy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.jandrade.stormy.R;
import co.jandrade.stormy.adapters.HourAdapter;
import co.jandrade.stormy.weather.Hour;

public class HourlyForecastActivity extends ActionBarActivity {

    private List<Hour> mHours;
    @InjectView(R.id.recyclerView) RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecast);
        ButterKnife.inject(this);
        Intent intent = getIntent();
        mHours = intent.getParcelableArrayListExtra(MainActivity.HOURLY_FORECAST);

        HourAdapter hourAdapter = new HourAdapter(this,mHours);
        mRecyclerView.setAdapter(hourAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }


}

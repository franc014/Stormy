package co.jandrade.stormy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.jandrade.stormy.R;
import co.jandrade.stormy.weather.Hour;

/**
 * Created by Juan Francisco Andrade on 5/18/15.
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {
     private List<Hour> mHours;
    private Context mContext;

    public HourAdapter(Context context,List<Hour> hourList) {
        mContext = context;
        mHours = hourList;
    }

    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.hourly_list_item, viewGroup, false);
        HourViewHolder viewHolder = new HourViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HourViewHolder hourViewHolder, int i) {
        hourViewHolder.bindHour(mHours.get(i));
    }

    @Override
    public int getItemCount() {
        return mHours.size();
    }

    public class HourViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @InjectView(R.id.timeLabel) TextView mTimeLabel;
        @InjectView(R.id.summaryLabel) TextView mSummaryLabel;
        @InjectView(R.id.iconImageView) ImageView mIconView;
        @InjectView(R.id.temperatureLabel) TextView mTemperatureLabel;
        public HourViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
            itemView.setOnClickListener(this);
        }

        public void bindHour(Hour hour){
            mTimeLabel.setText(hour.getFormattedTime());
            mSummaryLabel.setText(hour.getSummary());
            mIconView.setImageResource(hour.getIconId());
            mTemperatureLabel.setText(hour.getTemperature() + "");
        }

        @Override
        public void onClick(View v) {
            String time = mTimeLabel.getText().toString();
            String conditions = mSummaryLabel.getText().toString();
            String temperature = mTemperatureLabel.getText().toString();
            String message = String.format("At %s the temperature will be %s and it will be %s",
                    time,temperature,conditions
            );
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }
    }

}

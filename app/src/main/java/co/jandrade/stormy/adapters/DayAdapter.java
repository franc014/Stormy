package co.jandrade.stormy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


import co.jandrade.stormy.R;
import co.jandrade.stormy.weather.Day;

/**
 * Created by Juan Francisco Andrade on 5/14/15.
 */
public class DayAdapter extends BaseAdapter {
    private Context mContext;
    private List<Day> mDays;


    public DayAdapter(Context context, List<Day> days) {
        mContext = context;
        mDays = days;
    }

    @Override
    public int getCount() {
        return mDays.size();
    }

    @Override
    public Object getItem(int position) {
        return mDays.get(position);
    }

    @Override
    public long getItemId(int position) {
        //tag items for easy reference. won't use it
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            //brand new
            convertView = LayoutInflater.from(mContext).inflate(R.layout.daily_list_item,null);
            holder = new ViewHolder();
            //initializing the elements in the holder
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.iconImageView);
            holder.temperatureLabel = (TextView) convertView.findViewById(R.id.temperatureLabel);
            holder.dayLabel = (TextView) convertView.findViewById(R.id.dayNameLabel);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        Day day = mDays.get(position);
        //setting the holder data
        holder.iconImageView.setImageResource(day.getIconId());
        holder.temperatureLabel.setText(day.getTemperatureMax()+"");

        if(position==0){
            holder.dayLabel.setText("Today");
        }else {
            holder.dayLabel.setText(day.getDayOfTheWeek());
        }
        return convertView;
    }

    private static class ViewHolder{
        ImageView iconImageView; //public as default
        TextView temperatureLabel;
        TextView dayLabel;

    }
}

package co.jandrade.stormy.weather;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.SimpleFormatter;

/**
 * Created by Juan Francisco Andrade on 5/11/15.
 */
public class Hour implements Parcelable {


    private long mTime;
    private String mSummary;
    private String mIcon;
    private String mTimeZone;
    private double mTemperature;

    public Hour() {
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getIcon() {
        return mIcon;
    }

    public int getIconId(){
        return Forecast.getIconId(mIcon);
    }

    public String getFormattedTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("H a");
        formatter.setTimeZone(TimeZone.getTimeZone(mTimeZone));
        Date date = new Date(mTime*1000);
        return formatter.format(date);
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mTime);
        dest.writeString(mSummary);
        dest.writeString(mIcon);
        dest.writeDouble(mTemperature);
        dest.writeString(mTimeZone);
    }

    private Hour(Parcel in){
        mTime = in.readLong();
        mSummary = in.readString();
        mIcon = in.readString();
        mTemperature = in.readDouble();
        mTimeZone = in.readString();
    }

    public static final Creator CREATOR = new Creator<Hour>() {
        @Override
        public Hour createFromParcel(Parcel source) {
            return new Hour(source);
        }

        @Override
        public Hour[] newArray(int size) {
            return new Hour[size];
        }
    };

    public int getTemperature() {
        return (int) Math.round(mTemperature);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }
}

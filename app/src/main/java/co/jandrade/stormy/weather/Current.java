package co.jandrade.stormy.weather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Juan Francisco Andarde on 4/27/15.
 */
public class Current {
    private String mIcon;
    private long mTime;
    private double mTemperature;
    private double mHuminity;
    private double mPrecipChance;
    private String mSummary;
    private String mtimeZone;

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }
    public int getIconId(){
        return Forecast.getIconId(mIcon);
    }

    public Long getTime() {
        return mTime;
    }

    public String getFormattedTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(mtimeZone));
        Date date = new Date(mTime * 1000);
        String timeString = formatter.format(date);
        return  timeString;
    }

    public void setTime(Long time) {
        mTime = time;
    }

    public int getTemperature() {

        return (int)Math.round(mTemperature);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public double getHuminity() {
        return mHuminity;
    }

    public void setHuminity(double huminity) {
        mHuminity = huminity;
    }

    public int getPrecipChance() {

        return (int)Math.round(mPrecipChance)*100;
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getTimeZone() {
        return mtimeZone;
    }

    public void setTimeZone(String timeZone) {
        mtimeZone = timeZone;
    }
}

package co.jandrade.stormy.weather;

import java.util.List;

import co.jandrade.stormy.R;

/**
 * Created by Juan Francisco Andrade on 5/11/15.
 */
public class Forecast {
    private Current mCurrent;
    private List<Hour> mHours;
    private List<Day> mDays;

    public Current getCurrent() {
        return mCurrent;
    }

    public void setCurrent(Current current) {
        mCurrent = current;
    }

    public List<Hour> getHours() {
        return mHours;
    }

    public void setHours(List<Hour> hours) {
        mHours = hours;
    }

    public List<Day> getDays() {
        return mDays;
    }

    public void setDays(List<Day> days) {
        mDays = days;
    }

    public static int getIconId(String icon){
        int iconId = R.mipmap.clear_day;

        if (icon.equals("clear-day")) {
            iconId = R.mipmap.clear_day;
        }
        else if (icon.equals("clear-night")) {
            iconId = R.mipmap.clear_night;
        }
        else if (icon.equals("rain")) {
            iconId = R.mipmap.rain;
        }
        else if (icon.equals("snow")) {
            iconId = R.mipmap.snow;
        }
        else if (icon.equals("sleet")) {
            iconId = R.mipmap.sleet;
        }
        else if (icon.equals("wind")) {
            iconId = R.mipmap.wind;
        }
        else if (icon.equals("fog")) {
            iconId = R.mipmap.fog;
        }
        else if (icon.equals("cloudy")) {
            iconId = R.mipmap.cloudy;
        }
        else if (icon.equals("partly-cloudy-day")) {
            iconId = R.mipmap.partly_cloudy;
        }
        else if (icon.equals("partly-cloudy-night")) {
            iconId = R.mipmap.cloudy_night;
        }
        return iconId;
    }


}

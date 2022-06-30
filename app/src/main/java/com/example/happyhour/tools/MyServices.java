package com.example.happyhour.tools;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.happyhour.objects.MyTime;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

public class MyServices {
    private static MyServices _instance = null;
    private final int LEN_MSG_SHORT = 15;
    private Context context;
    private Toast toast;
    private View viewToast;
    private final String LOG_TAG = "MyLog";



    private MyServices(Context context) {
        this.context = context.getApplicationContext();
        this.toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
        toast.getView().getBackground().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.DARKEN);
        ((TextView) toast.getView().findViewById(android.R.id.message)).setTextColor(Color.parseColor("#FF577F"));
    }

    public static void initHelper(Context context) {
        if (_instance == null) {
            _instance = new MyServices(context);
        }
    }

    public static MyServices getInstance(){
        return _instance;
    }

    public void makeToast(String msg){
        if(msg.length() < LEN_MSG_SHORT)
            toast.makeText(context, "", Toast.LENGTH_SHORT);
        else
            toast.makeText(context, "", Toast.LENGTH_LONG);
        toast.setText(msg);
        toast.show();
    }

    public  void toLog(String msg){
        Log.d(LOG_TAG , msg);
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        public interface Callback_time {
            void get_input_time(MyTime time);
        }
        private Callback_time callback_time;

        public TimePickerFragment setCallback_time(Callback_time callback_time) {
            this.callback_time = callback_time;
            return this;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if(this.callback_time != null){
                this.callback_time.get_input_time(new MyTime(hourOfDay , minute));
            }
        }
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        public interface Callback_date {
            void get_input_date(Date date);
        }
        private Callback_date callback_date;

        public DatePickerFragment setCallback_date(Callback_date callback_date) {
            this.callback_date = callback_date;
            return this;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            if(this.callback_date != null){
                final Calendar c = Calendar.getInstance();
                c.set(year, month, day , 0, 0 ,0);
                this.callback_date.get_input_date(c.getTime());
            }
        }
    }
    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p1;

    }
}
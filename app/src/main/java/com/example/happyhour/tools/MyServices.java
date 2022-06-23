package com.example.happyhour.tools;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.happyhour.objects.MyTime;

public class MyServices {
    private static MyServices _instance = null;
    private Context context;
    private Toast toast;
    private View viewToast;
    private final String LOG_TAG = "MyLog";



    private MyServices(Context context) {
        this.context = context.getApplicationContext();
        this.toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
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

}

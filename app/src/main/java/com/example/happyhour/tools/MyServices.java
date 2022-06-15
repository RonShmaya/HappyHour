package com.example.happyhour.tools;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MyServices {
    private static MyServices _instance = null;
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
        toast.setText(msg);
        toast.show();
    }

    public  void toLog(String msg){
        Log.d(LOG_TAG , msg);
    }

}

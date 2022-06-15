package com.example.happyhour;

import android.app.Application;

import com.example.happyhour.tools.MyDB;
import com.example.happyhour.tools.MyServices;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MyDB.getInstance();
        MyServices.initHelper(this);

    }


}

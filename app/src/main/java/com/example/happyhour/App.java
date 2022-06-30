package com.example.happyhour;

import android.app.Application;

import com.example.happyhour.tools.MyDB;
import com.example.happyhour.tools.MyServices;
import com.example.happyhour.tools.MyStorage;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MyDB.getInstance();
        MyStorage.getInstance();
        MyServices.initHelper(this);

    }



}

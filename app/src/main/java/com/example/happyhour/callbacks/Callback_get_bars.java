package com.example.happyhour.callbacks;

import com.example.happyhour.objects.Bar;

import java.util.HashMap;

public interface Callback_get_bars extends Callback_DB {
   void get_bars(HashMap<String, Bar> bars);
   void get_bar(Bar bar);
}

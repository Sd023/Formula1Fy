package com.sdapps.formula1fy.core;

import android.content.Context;
import android.util.Log;


public class Commons {

    private Context context;

    public Commons(Context context){
        this.context = context;

    }

    public void print(String data){
        Log.d(this.getClass().getName(),data);
    }
}

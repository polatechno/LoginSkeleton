package com.polatechno.loginskeleton.common;

import android.util.Log;


public class LogManager {
    private static final String TAG = "TAG";

    public static void print(String message) {

        Log.d("DEBUGGER", message);
    }
}

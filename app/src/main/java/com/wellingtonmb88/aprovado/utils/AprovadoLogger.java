package com.wellingtonmb88.aprovado.utils;

import android.util.Log;

import com.wellingtonmb88.aprovado.BuildConfig;

public abstract class AprovadoLogger {

    private static String TAG = "AprovadoLogger";

    public static void d(String msg) {

        if(BuildConfig.DEBUG){
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg) {

        if(BuildConfig.DEBUG){
            Log.e(TAG, msg);
        }
    }

    public static void i(String msg) {

        if(BuildConfig.DEBUG){
            Log.i(TAG, msg);
        }
    }

    public static void w(String msg) {

        if(BuildConfig.DEBUG){
            Log.w(TAG, msg);
        }
    }
}

package com.wellingtonmb88.aprovado.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.wellingtonmb88.aprovado.AppApplication;

public abstract class DriveApiPreferences {

    private static final String DRIVE_API_PREFERENCES = "DRIVE_API_PREFERENCES";
    private static final String DRIVE_API_CONNECTED = "DRIVE_API_CONNECTED";
    private static final String DRIVE_API_CONNECTION_DENIED = "DRIVE_API_CONNECTION_DENIED";
    private static final String UPLOAD_PERIOD_TYPE = "UPLOAD_PERIOD_TYPE";
    private static final String UPLOAD_CONNECTIVITY_TYPE = "UPLOAD_CONNECTIVITY_TYPE";
    private static final String LAST_TIME_UPLOADED = "LAST_UPLOAD_TIME";
    private static final String UPLOADED_FILE_ID = "UPLOADED_FILE_ID";

    public static boolean isDriveApiConnectionDenied() {
        SharedPreferences sharedPreferences = AppApplication.getAppContext().getSharedPreferences(DRIVE_API_PREFERENCES, Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean(DRIVE_API_CONNECTION_DENIED, false);
    }

    public static void setDriveApiConnectionDenied(boolean denied) {
        SharedPreferences sharedPreferences = AppApplication.getAppContext().getSharedPreferences(DRIVE_API_PREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(DRIVE_API_CONNECTION_DENIED, denied);
        editor.apply();
    }

    public static boolean isDriveApiConnected() {
        SharedPreferences sharedPreferences = AppApplication.getAppContext().getSharedPreferences(DRIVE_API_PREFERENCES, Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean(DRIVE_API_CONNECTED, false);
    }

    public static void setDriveApiConnected(boolean connected) {
        SharedPreferences sharedPreferences = AppApplication.getAppContext().getSharedPreferences(DRIVE_API_PREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(DRIVE_API_CONNECTED, connected);
        editor.apply();
    }

    public static void saveUploadPeriodType(int periodTyping) {
        SharedPreferences sharedPreferences = AppApplication.getAppContext().getSharedPreferences(DRIVE_API_PREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(UPLOAD_PERIOD_TYPE, periodTyping);
        editor.apply();
    }

    public static int getUploadPeriodType() {
        SharedPreferences sharedPreferences = AppApplication.getAppContext().getSharedPreferences(DRIVE_API_PREFERENCES, Context.MODE_PRIVATE);

        return sharedPreferences.getInt(UPLOAD_PERIOD_TYPE, 0);
    }

    public static void saveUploadConnectivityType(int connectivityTyping) {
        SharedPreferences sharedPreferences = AppApplication.getAppContext().getSharedPreferences(DRIVE_API_PREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(UPLOAD_CONNECTIVITY_TYPE, connectivityTyping);
        editor.apply();
    }

    public static int getUploadConnectivityType() {
        SharedPreferences sharedPreferences = AppApplication.getAppContext().getSharedPreferences(DRIVE_API_PREFERENCES, Context.MODE_PRIVATE);

        return sharedPreferences.getInt(UPLOAD_CONNECTIVITY_TYPE, 0);
    }

    public static void saveLastTimeUploaded() {
        SharedPreferences sharedPreferences = AppApplication.getAppContext().getSharedPreferences(DRIVE_API_PREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(LAST_TIME_UPLOADED, System.currentTimeMillis());
        editor.apply();
    }

    public static int getLastTimeUploaded() {
        SharedPreferences sharedPreferences = AppApplication.getAppContext().getSharedPreferences(DRIVE_API_PREFERENCES, Context.MODE_PRIVATE);

        return sharedPreferences.getInt(LAST_TIME_UPLOADED, 0);
    }

    public static void saveUploadedFileId(String uploadedFileId) {
        SharedPreferences sharedPreferences = AppApplication.getAppContext().getSharedPreferences(DRIVE_API_PREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(UPLOADED_FILE_ID, uploadedFileId);
        editor.apply();
    }

    public static String getUploadedFileId() {
        SharedPreferences sharedPreferences = AppApplication.getAppContext().getSharedPreferences(DRIVE_API_PREFERENCES, Context.MODE_PRIVATE);

        return sharedPreferences.getString(UPLOADED_FILE_ID, null);
    }


    public interface PeriodTyping {
        int dayly = 0;
        int weekly = 1;
        int monthly = 2;
    }

    public interface ConnectivityTyping {
        int wifi = 0;
        int mobileData = 1;
    }

}

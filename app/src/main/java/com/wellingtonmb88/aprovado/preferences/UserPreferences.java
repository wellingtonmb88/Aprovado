package com.wellingtonmb88.aprovado.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.wellingtonmb88.aprovado.entity.User;

public abstract class UserPreferences {

    private static final String USER_NAME = "USER_NAME";
    private static final String USER_EMAIL = "USER_EMAIL";
    private static final String USER_PICTURE_URI = "USER_PICTURE_URI";

    private static final String USER_PREFERENCES = "USER_PREFERENCES";

    @SuppressLint("CommitPrefEdits")
    public static void saveUserToPreferences(Context context, User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        if (user != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(USER_NAME, user.getName());
            editor.putString(USER_EMAIL, user.getEmail());
            editor.putString(USER_PICTURE_URI, user.getPictureUri());
            editor.apply();
        }else{
            sharedPreferences.edit().clear().apply();
        }

    }

    public static User requestUserFromPreferences(Context context) {
        User user = null;
        if (context != null) {

            SharedPreferences sharedPreferences = context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
            String name = sharedPreferences.getString(USER_NAME, null);
            String email = sharedPreferences.getString(USER_EMAIL, null);
            String pictureUri = sharedPreferences.getString(USER_PICTURE_URI, null);
            user = new User(name, email, pictureUri);
        }
        return user;
    }
}

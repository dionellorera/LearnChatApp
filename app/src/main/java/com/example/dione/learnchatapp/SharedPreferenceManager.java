package com.example.dione.learnchatapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dione on 01/09/2016.
 */
public class SharedPreferenceManager {
    private Context mContext;
    private SharedPreferences mSharedpreferences;
    private final String MY_PREFERENCE_TAG = "my_preferences";
    public SharedPreferenceManager(Context mContext){
        this.mContext = mContext;
        mSharedpreferences = mContext.getSharedPreferences(MY_PREFERENCE_TAG, Context.MODE_PRIVATE);
    }

    public void saveStringPreferences(String key, String value){
        SharedPreferences.Editor editor = mSharedpreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringPreferences(String key, String defaultValue){
        return mSharedpreferences.getString(key, defaultValue);
    }

    public void clearSharedPreference(){
        mSharedpreferences.edit().clear().apply();

    }
}

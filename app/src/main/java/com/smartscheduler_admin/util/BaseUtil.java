package com.smartscheduler_admin.util;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

public class BaseUtil {
    public static String PREF_NAME="SmartScheduler_PREFERENCE_MANAGER";
    public static final String DEVICE_TOKEN = "device_token";
    public static final String LOGIN_WITH = "login_with";
    public static final String EMAIL = "email";
    public static final String MOBILE_NUMBER = "mobile_number";
    private Context context;
    //private SharedPreferences preferences;

    public BaseUtil(Context context)
    {
        this.context=context;
        //preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
    }

    public void ClearPreferences()
    {
        String DeviceToken = getDeviceToken();
        context.getSharedPreferences(PREF_NAME,MODE_PRIVATE).edit().clear().apply();
        SetDeviceToken(DeviceToken);
    }

    public static void hideKeyboard(@NonNull Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void SetDeviceToken(String tokenId){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DEVICE_TOKEN,tokenId);
        editor.apply();
    }

    public String getDeviceToken(){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        return preferences.getString(DEVICE_TOKEN,"");
    }

    public void SetEmail(String email){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EMAIL,email);
        editor.apply();
    }

    public String getEmail(){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        return preferences.getString(EMAIL,"");
    }

    public void SetMobileNumber(String mobile){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MOBILE_NUMBER,mobile);
        editor.apply();
    }

    public String getMobileNumber(){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        return preferences.getString(MOBILE_NUMBER,"");
    }
}

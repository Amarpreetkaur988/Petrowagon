package com.omninos.petrowagon.SharePreference;

import android.content.Context;
import android.content.SharedPreferences;
import com.omninos.petrowagon.ModelClass.LoginRegisterModel;
import com.google.gson.Gson;

public class AppPrefrences {

    private static AppPrefrences appPreference;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public AppPrefrences(Context context) {
        sharedPreferences = context.getSharedPreferences("PetroWagon", Context.MODE_PRIVATE);
    }

    public static AppPrefrences init(Context context) {
        if (appPreference == null) {
            appPreference = new AppPrefrences(context);
        }
        return appPreference;
    }

    public void clearAppPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void saveString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {

        return sharedPreferences.getString(key, "");
    }

    public void saveLoginDetail(LoginRegisterModel loginRegisterModel) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppConstants.LOGIN_DETAIL, gson.toJson(loginRegisterModel));
        editor.apply();
    }

    public LoginRegisterModel getLoginDetail() {
        Gson gson = new Gson();
        return gson.fromJson(sharedPreferences.getString(AppConstants.LOGIN_DETAIL, ""), LoginRegisterModel.class);
    }

    public void setSettings(String key, boolean value ){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }
    public boolean getSettings(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public boolean isUserFirstTime(){
        return sharedPreferences.getBoolean("isUserFirstTime",true);
    }
    public void setUserLoginTime() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isUserFirstTime",false);
        editor.apply();
    }
}

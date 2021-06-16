package com.omninos.petrowagon.SharePreference;

import android.app.Application;
import android.content.Context;


public class App extends Application {

    private Context context;

    private static AppPrefrences appPreference;

    private static SingltonPojo sinltonPojo;

    public static App appInstance;

    public static App getInstance()
    {
        return appInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        appPreference = AppPrefrences.init(context);
        sinltonPojo=new SingltonPojo();
    }

    public static AppPrefrences getAppPreference() {
        return appPreference;
    }


    public static SingltonPojo getSinltonPojo() {
        return sinltonPojo;
    }

}
package com.samalapsy.githubjavadevelopers;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;


public class GlobalClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
    }
}

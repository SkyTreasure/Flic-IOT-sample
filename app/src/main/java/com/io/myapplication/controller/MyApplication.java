package com.io.myapplication.controller;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import io.flic.lib.FlicManager;


/**
 * Created by akash on 8/8/16.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FlicManager.setAppCredentials("9e020d3f-a049-4515-946b-4039c4fd9c82", "90c58b35-8e94-4e60-9f09-d086e3f63ebf", "Interaction One");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}


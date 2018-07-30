package com.example.user.keepit;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;

public class KeepItApp extends Application {
    private static final String DUMMY_ADS_ID = "ca-app-pub-3940256099942544/6300978111";
    @Override
    public void onCreate(){
        super.onCreate();
        MobileAds.initialize(this,DUMMY_ADS_ID);

    }
}

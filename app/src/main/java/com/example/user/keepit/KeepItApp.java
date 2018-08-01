package com.example.user.keepit;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;

import static com.example.user.keepit.utils.Constants.DUMMY_ADS_ID;

/**
 * Solution found here:
 * https://code.tutsplus.com/tutorials/how-to-monetize-your-android-apps-with-admob--cms-29255
 */
public class KeepItApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, DUMMY_ADS_ID);

    }
}

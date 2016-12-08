package com.peacockweb.billsplitter;

import android.app.Application;
import android.content.Context;

/**
 * Created by Andrew on 5/11/2016.
 */
public class MyApp extends Application {

    public static final String SHARED_PREF_KEY = "shared_pref_key";

    public static Context mContext;

    public MyApp() {}

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;

    }
}

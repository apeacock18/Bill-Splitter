package com.peacockweb.billsplitter;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;

/**
 * Created by Andrew on 5/11/2016.
 */
public class MyApp extends Application {

    public MyApp() {}

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
            .applicationId("SmKujk7VXA7gQcUNz6hHjbPWpk1jF0Wtp1RPZ71Z")
            .clientKey("g1gsXIi0t2Hk1maTsl5lXGbEaqLMlIQE8MludaDW")
            .server("https://parseapi.back4app.com/") // The trailing slash is important.
            .build()
        );
    }
}

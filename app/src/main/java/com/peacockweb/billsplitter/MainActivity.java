package com.peacockweb.billsplitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.parse.Parse;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(new Parse.Configuration.Builder(getApplication().getApplicationContext())
                        .applicationId("SmKujk7VXA7gQcUNz6hHjbPWpk1jF0Wtp1RPZ71Z")
                        .clientKey("g1gsXIi0t2Hk1maTsl5lXGbEaqLMlIQE8MludaDW")
                        .server("https://parseapi.back4app.com/") // The trailing slash is important.

                        .build()
        );

        Intent intent = new Intent(this, HeaderPage.class);
        finish();
        startActivity(intent);
    }
}

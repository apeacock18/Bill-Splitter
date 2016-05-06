package com.peacockweb.billsplitter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.Parse;

public class HeaderPage extends AppCompatActivity {

    public static Activity hp;
    public static boolean startedFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!startedFlag) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_header_page);
            startedFlag = true;
            hp = this;

            Parse.initialize(new Parse.Configuration.Builder(getApplication().getApplicationContext())
                            .applicationId("SmKujk7VXA7gQcUNz6hHjbPWpk1jF0Wtp1RPZ71Z")
                            .clientKey("g1gsXIi0t2Hk1maTsl5lXGbEaqLMlIQE8MludaDW")
                            .server("https://parseapi.back4app.com/") // The trailing slash is important.

                            .build()
            );
        }
    }

    @Override
    protected  void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            startedFlag = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_header_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void signUpClick(View view) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    public void signInClick(View view) {
        Intent intent = new Intent(this, SignIn.class);
        startActivity(intent);
    }

    public void testClick(View view) {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }
}

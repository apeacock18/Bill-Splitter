package com.peacockweb.billsplitter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.peacockweb.billsplitter.util.NetworkManager;
import com.peacockweb.billsplitter.util.VariableManager;

import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(VariableManager.SHARED_PREF_KEY, Context.MODE_PRIVATE);
        String token = prefs.getString("token", "DNE");
        if (!token.equals("DNE")) {
            NetworkManager.loginWithToken(token, this);
        }
        else {
            Intent intent = new Intent(this, HeaderPage.class);
            startActivity(intent);
            finish();
        }
    }

}

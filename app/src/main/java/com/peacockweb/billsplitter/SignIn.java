package com.peacockweb.billsplitter;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.parse.FunctionCallback;
import com.parse.LogInCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
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

    public void SIbackClick(View view) {
        Intent intent = new Intent(this, HeaderPage.class);
        startActivity(intent);
    }

    public void SIdoneClick(View view) {

        EditText username = (EditText) findViewById(R.id.SIemail);
        EditText password = (EditText) findViewById(R.id.SIpassword);

        HashMap<String, String> params = new HashMap<>();
        params.put("username", username.getText().toString());
        params.put("password", get_SHA_512_SecurePassword(password.getText().toString(), ""));
        ParseCloud.callFunctionInBackground("login", params, new FunctionCallback<String>() {
            public void done(String id, ParseException e) {
                if (e == null) {
                    System.out.println(id);
                    Intent intent = new Intent(getApplicationContext(), HomePage.class);
                    finish();
                    startActivity(intent);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public String get_SHA_512_SecurePassword(String passwordToHash, String salt)
    {
        String generatedPassword = null;
        try {

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(passwordToHash.concat(salt).getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}

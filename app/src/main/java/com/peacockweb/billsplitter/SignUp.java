package com.peacockweb.billsplitter;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.parse.FunctionCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void signUpBack(View view) {
        Intent intent = new Intent(this, AddBill.class);
        startActivity(intent);
    }

    public void signUpDone(View view) {
        EditText fullName = (EditText) findViewById(R.id.SUfullName);
        EditText password = (EditText) findViewById(R.id.SUpassword);
        EditText email = (EditText) findViewById(R.id.SUemail);
        EditText phone = (EditText) findViewById(R.id.SUphone);
        EditText username = (EditText) findViewById((R.id.usernameText));

        ParseObject user = new ParseObject("Users");

        String str = fullName.getText().toString();
        String[] splited = str.split("\\s+");
/*        user.put("fName", splited[0]);
        user.put("lName", splited[1]);
        user.put("username", username.getText().toString());
        user.put("email", email.getText().toString());
        user.put("password", get_SHA_512_SecurePassword(password.toString(), ""));
        user.put("phoneNumber", phone.getText().toString());*/

        HashMap<String, String> params = new HashMap<>();
        params.put("fName", splited[0]);
        params.put("lName", splited[1]);
        params.put("email", email.getText().toString());
        params.put("phoneNumber", phone.getText().toString());
        params.put("username", username.getText().toString());
        params.put("password", get_SHA_512_SecurePassword(password.getText().toString(), ""));
        ParseCloud.callFunctionInBackground("create", params, new FunctionCallback<String>() {
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

        user.saveInBackground();

        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
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

package com.peacockweb.billsplitter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

    EditText fullName;
    EditText password;
    EditText email;
    EditText phone;
    EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fullName = (EditText) findViewById(R.id.SUfullName);
        password = (EditText) findViewById(R.id.SUpassword);
        email = (EditText) findViewById(R.id.SUemail);
        phone = (EditText) findViewById(R.id.SUphone);
        username = (EditText) findViewById((R.id.usernameText));

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!isValidEmailAddress(email.getText().toString()) && !email.getText().toString().equals("")) {
                        email.setError("Invalid email address");
                    } else {
                        email.setError(null);
                    }
                }
            }
        });

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    username.setError(null);
                }
            }
        });

        fullName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    fullName.setError(null);
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    password.setError(null);
                }
            }
        });
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
        finish();
        HeaderPage.getInstance().finish();
        Intent intent = new Intent(this, HeaderPage.class);
        startActivity(intent);
    }

    public void signUpDone(View view) {
        if (FieldsAreValid()) {
            String str = fullName.getText().toString();
            String[] splited = str.split("\\s+");

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();

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
                        HeaderPage.getInstance().finish();
                        startActivity(intent);
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
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

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public boolean FieldsAreValid() {
        boolean isValid = true;
        if (username.getText().toString().equals(null) || username.getText().toString().equals("")) {
            username.setError("Required field");
            isValid = false;
        }
        if (password.getText().toString().equals(null) || password.getText().toString().equals("")) {
            password.setError("Required field");
            isValid = false;
        }
        if (email.getText().toString().equals(null) || email.getText().toString().equals("")) {
            email.setError("Required field");
            isValid = false;
        }
        if (fullName.getText().toString().equals(null) || fullName.getText().toString().equals("")) {
            fullName.setError("Required field");
            isValid = false;
        }
        return isValid;
    }
}

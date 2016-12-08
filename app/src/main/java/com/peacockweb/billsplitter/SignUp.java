package com.peacockweb.billsplitter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.peacockweb.billsplitter.util.NetworkManager;

public class SignUp extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText password;
    EditText email;
    EditText phone;
    EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstName = (EditText) findViewById(R.id.su_first_name);
        lastName = (EditText) findViewById(R.id.su_last_name);
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

        firstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    firstName.setError(null);
                }
            }
        });

        lastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    lastName.setError(null);
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

            // Clear token from shared prefs before continuing
            SharedPreferences prefs = getSharedPreferences(MyApp.SHARED_PREF_KEY, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();

            NetworkManager.signUp(username.getText().toString(), password.getText().toString(), firstName.getText().toString(),
                    lastName.getText().toString(), email.getText().toString(), phone.getText().toString(), this);
        }
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
        if (firstName.getText().toString().equals(null) || firstName.getText().toString().equals("")) {
            firstName.setError("Required field");
            isValid = false;
        }
        if (lastName.getText().toString().equals(null) || lastName.getText().toString().equals("")) {
            lastName.setError("Required field");
            isValid = false;
        }
        return isValid;
    }
}

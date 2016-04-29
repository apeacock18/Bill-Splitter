package com.peacockweb.billsplitter;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

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

        ParseUser user = new ParseUser();
        user.setUsername(fullName.getText().toString());
        user.setPassword(password.getText().toString());
        user.setEmail(email.getText().toString());
        user.put("phone", phone.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {

                } else {
                    e.printStackTrace();
                    Snackbar.make(findViewById(R.id.SUfullName), e.getMessage(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                }
            }
        });

        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
    }
}

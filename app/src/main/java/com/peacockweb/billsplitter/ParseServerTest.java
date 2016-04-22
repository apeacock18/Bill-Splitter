package com.peacockweb.billsplitter;

import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class ParseServerTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse_server_test);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_parse_server_test, menu);
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

    public void submitClick(View view) {
        ParseObject obj = new ParseObject("BillMember");
        obj.put("memberName", "David");
        obj.put("amountPaid", 42);

        //obj.put("description", ((EditText) (findViewById(R.id.transactionDescrip))).getText().toString());
        obj.saveInBackground();
    }

    public void updateClick(View view) {
        final TextView text = (TextView) findViewById(R.id.updateText);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("BillMember");
        query.whereEqualTo("memberName", "Chris");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    int i = object.getInt("amountPaid");
                    String s = String.valueOf(i);
                    text.setText(s);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}

package com.peacockweb.billsplitter;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListPopupWindow;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.peacockweb.billsplitter.util.PopupListWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddBill extends AppCompatActivity
implements AdapterView.OnItemClickListener {

    ArrayList namesList;
    EditText usernameText;
    ListPopupWindow listPopupWindow;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
/*        myToolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back_arrow));
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
            }
        });*/
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

/*        usernameText = (EditText) findViewById(
                R.id.billAddPersonText);*/

        listPopupWindow = new ListPopupWindow(
                AddBill.this);

        namesList = new ArrayList();

        adapter = new ArrayAdapter(AddBill.this, R.layout.list_item, namesList);
        listPopupWindow.setAdapter(adapter);

        listPopupWindow.setAnchorView(usernameText);
        listPopupWindow.setWidth(200);
        listPopupWindow.setHeight(300);

        listPopupWindow.setModal(true);
        listPopupWindow.setOnItemClickListener(
                AddBill.this);

/*        usernameText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listPopupWindow.show();
            }
        });*/

        EditText etValue = (EditText) findViewById(R.id.SIpassword);
        etValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Fires right before text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Fires right after the text has changed
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
                query.whereEqualTo("username", s.toString());
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> names, ParseException e) {
                        if (e == null) {
                            if (!names.isEmpty()) {
                                Log.d("usernames", names.size() + " found in database!");
                                adapter.clear();
                                for (ParseObject obj : names) {
                                    adapter.add(obj.getString("username"));
                                }
                                listPopupWindow.show();
                            /*String[] parseNames = names.toArray(new String[names.size()]);
                            adapter.addAll(Arrays.copyOf(parseNames, parseNames.length, String[].class));*/
                            }
                        } else {
                            Log.d("usernames", "Error: " + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        usernameText.setText((String) namesList.get(position));
        listPopupWindow.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tool_bar_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_favorite)
        {
/*            Intent intent = new Intent(getApplicationContext(), HomePage.class);
            startActivity(intent);*/
            Snackbar.make(findViewById(R.id.editText10), "Successfully created bill!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

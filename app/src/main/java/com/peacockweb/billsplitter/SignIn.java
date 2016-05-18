package com.peacockweb.billsplitter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.peacockweb.billsplitter.util.TinyDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SignIn extends AppCompatActivity {

    public static ParseObject user;
    TinyDB tinyDB;
    ArrayList<String> knownUserUsernames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        tinyDB = new TinyDB(this);
        knownUserUsernames = new ArrayList();
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
        HeaderPage.getInstance().finish();
        finish();
        Intent intent = new Intent(this, HeaderPage.class);
        startActivity(intent);
    }

    public void SIdoneClick(View view) {

        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();

        final EditText username = (EditText) findViewById(R.id.SIemail);
        final EditText password = (EditText) findViewById(R.id.SIpassword);

        // Login user to Parse through cloud code
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username.getText().toString());
        params.put("password", get_SHA_512_SecurePassword(password.getText().toString() + username.getText().toString(), ""));
        ParseCloud.callFunctionInBackground("login", params, new FunctionCallback<String>() {
            public void done(String id, ParseException e) {
                if (e == null) {
                    // Run if user login is successful
                    tinyDB.putString("userId", id);

                    // Pull the current Parse user
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
                    query.whereEqualTo("username", username.getText().toString());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> list, ParseException e) {
                            if (e == null) {
                                user = list.get(0);;
                                // Query Parse to store local group data
                                if (user.getList("groups") != null) {
                                    final ArrayList groups = new ArrayList<>();
                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
                                    // Pull all groups owned by current user
                                    query.whereContainedIn("objectId", user.getList("groups"));
                                    query.findInBackground(new FindCallback<ParseObject>() {
                                        public void done(List<ParseObject> list, ParseException e) {
                                            if (e == null) {
                                                final List<ParseObject> parseGroups = list;
                                                // Cycle through each of user's groups
                                                for (ParseObject obj : list) {
                                                    // Pull all members from each group
                                                    List members = obj.getList("members");
                                                    for (Object str : members) {
                                                        // Add each userId to "known users"
                                                        if (!knownUserUsernames.contains(str.toString())) {
                                                            knownUserUsernames.add(str.toString());
                                                        }
                                                    }
                                                }

                                                // Pull all known user objects from parse
                                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
                                                query.whereContainedIn("objectId", knownUserUsernames);
                                                query.findInBackground(new FindCallback<ParseObject>() {
                                                    public void done(List<ParseObject> users, ParseException e) {
                                                        if (e == null) {

                                                            ArrayList groups = new ArrayList();
                                                            ArrayList knownMembers = new ArrayList();

                                                            for (ParseObject parseGroup : parseGroups) {
                                                                ArrayList<GroupMember> mems = new ArrayList();
                                                                for (ParseObject parseUser : users) {
                                                                    if (parseGroup.getList("members").contains(parseUser.getObjectId())) {
                                                                        mems.add(new GroupMember(parseUser.getString("name"), parseUser.getString("username"), parseUser.getObjectId()));
                                                                    }
                                                                }
                                                                groups.add(new Group(mems, parseGroup.getString("name"), parseGroup.getObjectId()));
                                                            }

                                                            for (ParseObject parseUser : users) {
                                                                knownMembers.add(new GroupMember(
                                                                        parseUser.getString("name"),
                                                                        parseUser.getString("username"),
                                                                        parseUser.getObjectId()
                                                                ));
                                                            }

                                                            tinyDB.putListObject("groupList", groups);
                                                            tinyDB.putListObject("knownMembers", knownMembers);
                                                            String userGroup = user.getString("currentGroup");
                                                            for (Object g : groups) {
                                                                if (((Group)g).groupId.equals(userGroup)) {
                                                                    tinyDB.putObject("currentGroup", g);
                                                                }
                                                            }
                                                          /*  for (ParseObject g : parseGroups) {
                                                                if (g.getObjectId().equals(userGroup)) {
                                                                    JSONArray jsonarray = g.getJSONArray("status");
                                                                    for (int i = 0; i < jsonarray.length(); i++) {
                                                                        try {
                                                                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                                            String payee = jsonobject.getString();
                                                                            String date = jsonobject.getString("date");
                                                                            double amount = jsonobject.getDouble("amount");
                                                                            String description = jsonobject.getString("description");
                                                                        }
                                                                        catch (JSONException ex) {

                                                                        }
                                                                    }
                                                                }
                                                            }*/

                                                            Intent intent = new Intent(getApplicationContext(), HomePage.class);
                                                            finish();
                                                            HeaderPage.getInstance().finish();
                                                            startActivity(intent);
                                                        }
                                                        else {
                                                            Log.d("members", "Error: " + e.getMessage());
                                                        }
                                                    }
                                                });

                                            } else {
                                                Log.d("users", "Error: " + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            } else {
                                Log.d("user", "Error: " + e.getMessage());
                            }
                        }
                    });

                } else {
                    Log.d("login", "Error: " + e.getMessage());
                    if (e.getMessage().equals("0") || e.getMessage().equals("1")) {
                        Snackbar.make(findViewById(R.id.SIdone), "Invalid username/password", Snackbar.LENGTH_LONG)
                                .setAction("Action", null)
                                .show();
                    }
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

    public void initLocalSettings() {

        if (user.getList("groups") != null) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
            query.whereContainedIn("objectId", user.getList("groups"));
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> list, ParseException e) {
                    if (e == null) {
                        if (list.isEmpty()) {
                            System.out.println("Whoop, whoop!!! The group wasn't found!!!");
                        }
                        ArrayList groups = new ArrayList<>();
                        for (ParseObject obj : list) {
                            List members = obj.getList("members");
                            ArrayList<GroupMember> mems = new ArrayList();
                            for (Object str : members) {
                                mems.add(new GroupMember(str.toString()));
                            }
                            groups.add(new Group(mems, obj.getString("name"), obj.getObjectId()));
                        }
                        tinyDB.putListObject("groupList", groups);
                    } else {
                        Log.d("score", "Error: " + e.getMessage());
                    }
                }
            });
        }
    }
}

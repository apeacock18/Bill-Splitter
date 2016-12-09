package com.peacockweb.billsplitter.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.peacockweb.billsplitter.Group;
import com.peacockweb.billsplitter.HeaderPage;
import com.peacockweb.billsplitter.HomePage;
import com.peacockweb.billsplitter.MyApp;
import com.peacockweb.billsplitter.SignIn;
import com.peacockweb.billsplitter.Status;
import com.peacockweb.billsplitter.Transaction;
import com.peacockweb.billsplitter.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by apeacock on 12/6/16.
 */
public class NetworkManager {

    public interface NetworkCallBackCallBack {
        void response(String response);
    }

    public static void runRequest(final String urlFrag, final HashMap<String, String> params, final NetworkCallBackCallBack callBack) {

        RequestQueue queue = Volley.newRequestQueue(MyApp.mContext);
        String url ="http://10.0.2.2:8000/";

        StringRequest request = new StringRequest(Request.Method.POST, url + urlFrag, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.response(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley Error", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };
        queue.add(request);
    }

    public static boolean logIn(String username, String password, final Context mContext) {
        // Clear any stale tokens from shared prefs
        SharedPreferences prefs = mContext.getSharedPreferences(MyApp.SHARED_PREF_KEY, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        // Authenticate user login and retrieve token and user id
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        //params.put("password", get_SHA_512_SecurePassword(password, username));

        NetworkManager.runRequest("login/", params, new NetworkManager.NetworkCallBackCallBack() {
            @Override
            public void response(String response) {
                Log.d("Response", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.has("Error")) {
                        JSONObject error = obj.getJSONObject("Error");
                        Log.d("Django Error 1", error.toString());
                        Integer errorCode = error.getInt("Code");
                        if (errorCode == 1) {
                            Toast toast = new Toast(mContext);
                            toast.setText("Invalid username/password");
                            toast.show();
                        }
                    } else {
                        // Store auth token in shared prefs
                        String token = obj.getString("token");
                        editor.putString("token", token);
                        editor.apply();
                        LoadUserData(obj);
                        Intent intent = new Intent(mContext, HomePage.class);
                        mContext.startActivity(intent);
                        HeaderPage.getInstance().finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return true;
    }

    public static void LoadUserData(JSONObject response) throws JSONException {
        String username = response.getString("username");
        String fName = response.getString("firstName");
        String lName = response.getString("lastName");
        String email = response.getString("email");
        String token = response.getString("token");
        String phoneNumber = response.getString("phoneNumber");
        String userID = Integer.toString(response.getInt("id"));
        VariableManager.Init(username, fName, lName, email, token, phoneNumber, userID);
        //VariableManager.addUser(new User(username, userID, fName, lName, email, phoneNumber));

        HashMap<String, String> params = new HashMap();
        params.put("token", VariableManager.getToken());
        params.put("userId", VariableManager.getUserId());

        NetworkManager.runRequest("group/info/", params, new NetworkManager.NetworkCallBackCallBack() {
            @Override
            public void response(String response) {
                Log.d("Response", response);
                try {
                    Object json = new JSONTokener(response).nextValue();
                    if (json instanceof JSONObject) {
                        JSONObject error = (JSONObject) json;
                        Log.d("Django Error 2", error.toString());
                    } else {
                        JSONArray groupArray = (JSONArray) json;
                        for (int i = 0; i < groupArray.length(); i++) {
                            JSONObject group = groupArray.getJSONObject(i);

                            JSONArray statusArray = group.getJSONArray("status");
                            ArrayList<Status> statuses = new ArrayList<>();
                            for (int j = 0; j < statusArray.length(); j++) {
                                JSONObject status = statusArray.getJSONObject(j);
                                statuses.add(new Status(status));
                            }

                            JSONArray transactionArray = group.getJSONArray("transactions");
                            ArrayList<Transaction> transactions = new ArrayList<>();
                            for (int j = 0; j < transactionArray.length(); j++) {
                                JSONObject tObj = transactionArray.getJSONObject(j);
                                Transaction transaction = new Transaction(tObj);
                                transactions.add(transaction);
                            }

                            String groupId = Integer.toString(group.getInt("id"));
                            String name = group.getString("name");

                            JSONArray membersArray = group.getJSONArray("members");
                            Set<Integer> members = new HashSet<>();
                            for (int j = 0; j < membersArray.length(); j++) {
                                members.add(membersArray.getInt(j));
                            }

                            ArrayList<Integer> memIds = new ArrayList<>();
                            VariableManager.userIds = memIds;
                            memIds.addAll(members);

                            String memberString = "[";
                            for (int j = 0; j < memIds.size(); j++) {
                                if (j != 0) {
                                    memberString += ", ";
                                }
                                memberString += Integer.toString(memIds.get(j));
                            }
                            memberString += "]";
                            Log.d("Array String", memberString);

                            HashMap map = new HashMap<>();
                            map.put("token", VariableManager.getToken());
                            map.put("userIds", memberString);

                            NetworkManager.runRequest("person/info/", map, new NetworkCallBackCallBack() {
                                @Override
                                public void response(String response) {
                                    try {
                                        Object json = new JSONTokener(response).nextValue();
                                        if (json instanceof JSONObject) {
                                            JSONObject error = (JSONObject) json;
                                            Log.d("Django Error 3", error.toString());
                                        } else {
                                            JSONArray userArray = (JSONArray) json;
                                            for (int i = 0; i < userArray.length(); i++) {
                                                JSONObject obj = userArray.getJSONObject(i);

                                                VariableManager.addUser(new User(
                                                        obj.getString("username"),
                                                        Integer.toString(obj.getInt("id")),
                                                        obj.getString("first_name"),
                                                        obj.getString("last_name"),
                                                        obj.getString("email"),
                                                        obj.getString("phoneNumber")
                                                ));
                                            }

                                            Log.d("Name Log", VariableManager.findUserNameById("1"));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            Group _group = new Group(members, name, groupId, statuses, transactions);
                            VariableManager.addGroup(_group);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static boolean signUp(String username, String password, String firstName, String lastName, String email, String phoneNumber, final Context mContext) {
        // Clear any stale tokens from shared prefs
        SharedPreferences prefs = mContext.getSharedPreferences(MyApp.SHARED_PREF_KEY, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        // Authenticate user login and retrieve token and user id
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("email", email);
        params.put("phonNumber", phoneNumber);
        params.put("firstName", firstName);
        params.put("lastName", lastName);
        //params.put("password", get_SHA_512_SecurePassword(password, username));

        NetworkManager.runRequest("person/create/", params, new NetworkManager.NetworkCallBackCallBack() {
            @Override
            public void response(String response) {
                Log.d("Response", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.has("Error")) {
                        JSONObject error = obj.getJSONObject("Error");
                        Log.d("Django Error 1", error.toString());
                    } else {
                        // Store auth token in shared prefs
                        String token = obj.getString("token");
                        editor.putString("token", token);
                        editor.apply();
                        createNewUser(obj);
                        Intent intent = new Intent(mContext, HomePage.class);
                        mContext.startActivity(intent);
                        HeaderPage.getInstance().finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return true;
    }

    public static void createNewUser(JSONObject response) throws JSONException {
        String username = response.getString("username");
        String fName = response.getString("firstName");
        String lName = response.getString("lastName");
        String email = response.getString("email");
        String token = response.getString("token");
        String phoneNumber = response.getString("phoneNumber");
        String userID = Integer.toString(response.getInt("id"));
        VariableManager.Init(username, fName, lName, email, token, phoneNumber, userID);
    }

    public static String get_SHA_512_SecurePassword(String passwordToHash, String salt)
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

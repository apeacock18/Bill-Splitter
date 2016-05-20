package com.peacockweb.billsplitter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by apeacock on 5/17/16.
 */
public class Status {

    public String id = "";
    public ArrayList<HashMap<String, Object>> data = new ArrayList<>();

    public Status() {
        id = "";
    }

    public Status(String jsonString) throws JSONException {
        JSONObject json = new JSONObject(jsonString);
        System.out.println(jsonString);
        id = json.getString("id");
        JSONArray array = json.getJSONArray("data");
        for (int i = 0; i < array.length(); i++) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("recipient", array.getJSONObject(i).getString("recipient"));
            hashMap.put("amount", array.getJSONObject(i).getDouble("amount"));
            data.add(hashMap);
            System.out.println(i + ": " + array.getJSONObject(i).toString());
        }
    }

    public Double getAmountByRecipient(String userId) {
        for(HashMap<String, Object> map : data) {
            if(map.get("recipient").equals(userId)) {
                return (Double) map.get("amount");
            }
        }
        return null;
    }

    @Override
    public String toString() {
        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("id", id);
        jsonMap.put("data", data);
        JSONObject json = new JSONObject(jsonMap);
        return json.toString();
    }

}

package com.peacockweb.billsplitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by apeacock on 5/17/16.
 */
public class Status {

    public String id = "";
    public ArrayList<HashMap<String, Object>> data = new ArrayList<>();

    public Status() {
        id = "";
    }

    public Status(JSONObject json) throws JSONException {
        id = json.getString("id");
        JSONArray array = json.getJSONArray("data");
        for (int i = 0; i < array.length(); i++) {
            HashMap<String, Object> hashMap = new HashMap<>();
            System.out.println("Recipient-Amount Pair: " + array.getJSONObject(i));
            hashMap.put("recipient", array.getJSONObject(i).getString("recipient"));
            Object obj = array.getJSONObject(i).get("amount");
            Double amount = 0.0;
            if (obj == null) {
                amount = 0.0;
            }
            else {
                amount = array.getJSONObject(i).getDouble("amount");
            }

            hashMap.put("amount", amount);
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

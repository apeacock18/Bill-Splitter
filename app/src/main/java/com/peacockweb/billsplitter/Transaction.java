package com.peacockweb.billsplitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Andrew on 5/13/2016.
 */
public class Transaction {
    public String payee;
    public HashMap<String, Double> split;
    public Double amount;
    public String desc;
    public String date;

/*    public Transaction(String name, Double amount, ) {

    }*/

    public Transaction(String jsonString) throws JSONException {
        JSONObject json = new JSONObject(jsonString);
        payee = json.getString("payee");
        amount = json.getDouble("amount");
        desc = json.getString("description");
        date = json.getString("date");
        split = new HashMap<>();

        JSONObject splitJson = json.getJSONObject("split");
        Iterator<?> keys = splitJson.keys();
        while( keys.hasNext() ){
            String key = (String) keys.next();
            Double value = splitJson.getDouble(key);
            split.put(key, value);
        }
    }

    public String getRecipientsStatement() {
        String str = "";
        int i = 0;
        for (String name : split.keySet()) {
            if (!name.equals(VariableManager.userId)) {
                if (i == 0) {
                    str += VariableManager.getNameFromID(name);
                } else {
                    str += ", " + VariableManager.getNameFromID(name);
                }
                i++;
            }
        }
        return str;
    }

    public String getPayerStatement() {
        String str = "";
        str += VariableManager.getNameFromID(payee) + " paid $" + String.format("%.2f", amount);
        return str;
    }
}

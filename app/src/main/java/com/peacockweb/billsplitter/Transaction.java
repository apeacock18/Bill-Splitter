package com.peacockweb.billsplitter;

import com.peacockweb.billsplitter.util.VariableManager;

import org.json.JSONException;
import org.json.JSONObject;

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


    public Transaction(JSONObject json) throws JSONException {
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
            if (!name.equals(VariableManager.getUserId())) {
                if (i == 0) {
                    str += name;
                } else {
                    str += ", " + name;
                }
                i++;
            }
        }
        return str;
    }

    public String getPayerStatement() {
        String str = "";
        str += VariableManager.findUserNameById(payee) + " paid $" + String.format("%.2f", amount);
        return str;
    }
}

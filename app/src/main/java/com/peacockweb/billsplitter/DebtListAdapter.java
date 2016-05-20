package com.peacockweb.billsplitter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.peacockweb.billsplitter.util.TinyDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by apeacock on 5/18/16.
 */
public class DebtListAdapter extends ArrayAdapter {
    private final Context context;
    private final List<HashMap<String, Object>> data;
    private final ArrayList<String> memberIds;

    public DebtListAdapter(Context context, List<HashMap<String, Object>> data) {
        super(context, -1, data);
        this.context = context;
        this.data = data;
        TinyDB tinyDB = new TinyDB(context);
        memberIds = tinyDB.getListString("memberIds");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.debt_list_item, parent, false);

        TextView payer = (TextView) rowView.findViewById(R.id.debtPayer);
        TextView debtAmount = (TextView) rowView.findViewById(R.id.debtAmount);

        payer.setText(VariableManager.getNameFromID(data.get(position).get("recipient").toString()));
        Double amount = (Double) (data.get(position).get("amount"));
        if (amount < 0) {
            debtAmount.setText("$" + String.format("%.2f", Math.abs(amount)));
            debtAmount.setTextColor(Color.parseColor("#4CAF50"));
        }
        else {
            debtAmount.setText("- $" + String.format("%.2f", Math.abs(amount)));
            debtAmount.setTextColor(Color.parseColor("#F44336"));
        }
        return rowView;
    }
}

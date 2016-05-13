package com.peacockweb.billsplitter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 5/13/2016.
 */
class PayerListAdapter extends ArrayAdapter {
    private final Context context;
    private final ArrayList<GroupMember> values;

    public PayerListAdapter(Context context, ArrayList<GroupMember> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.payer_list_item, parent, false);

        TextView payerName = (TextView) rowView.findViewById(R.id.payerNameText);
        TextView splitPercent = (TextView) rowView.findViewById(R.id.payerSplitPercentText);

        payerName.setText(values.get(position).getName());
        double split = 100/values.size();
        DecimalFormat df = new DecimalFormat("#.##");
        String str = String.valueOf(df.format(split));
        splitPercent.setText(str + "%");

        return rowView;
    }
}


package com.peacockweb.billsplitter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PaymentsListAdapter extends ArrayAdapter {
    private final Context context;
    private final ArrayList<PaymentSummary> values;

    public PaymentsListAdapter(Context context, ArrayList<PaymentSummary> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.main_page_list_item, parent, false);

        TextView payer = (TextView) rowView.findViewById(R.id.summaryItemPayer);
        TextView description = (TextView) rowView.findViewById(R.id.summaryItemDescription);
        TextView recipients = (TextView) rowView.findViewById(R.id.summaryItemReceivers);
        TextView date = (TextView) rowView.findViewById(R.id.summaryItemDate);

        payer.setText(values.get(position).getPayerStatement());
        description.setText(values.get(position).description);
        recipients.setText(values.get(position).getRecipientsStatement());
        date.setText(values.get(position).date);


        return rowView;
    }
}


package com.peacockweb.billsplitter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Andrew on 5/8/2016.
 */
public class GroupListAdapter extends ArrayAdapter<Group> {
    public GroupListAdapter(Context context, ArrayList<Group> groups) {
        super(context, 0, groups);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Group group = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.group_item, parent, false);
        }
        // Lookup view for data population
        TextView groupName = (TextView) convertView.findViewById(R.id.groupName);
        TextView groupMembers = (TextView) convertView.findViewById(R.id.groupMembers);
        // Populate the data into the template view using the data object
        groupName.setText(group.name);
        //groupMembers.setText(group.getMemberNames());
        // Return the completed view to render on screen
        return convertView;
    }
}

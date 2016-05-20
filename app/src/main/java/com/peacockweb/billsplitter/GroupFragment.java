package com.peacockweb.billsplitter;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.peacockweb.billsplitter.util.TinyDB;

import java.util.ArrayList;


public class GroupFragment extends Fragment {

    public static ArrayList groupsData;
    public static GroupListAdapter groupAdapter;
    private View view;

    public GroupFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        groupsData = VariableManager.groups;
        System.out.println(groupsData.size() + " groups contained in adapter");
        Log.d("MyApp", " groups contained in adapter");
        groupAdapter = new GroupListAdapter(getContext(), groupsData);
        Button currentGroup = (Button) getActivity().findViewById(R.id.currentGroupButton);

        if (VariableManager.currentGroup != null) {
            currentGroup.setText(VariableManager.currentGroup.name);
        }

        ListView listView1 = (ListView) view.findViewById(R.id.groupsList);
        listView1.setAdapter(groupAdapter);
        listView1.setOnItemClickListener(mMessageClickedHandler);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            Button currentGroup = (Button) getActivity().findViewById(R.id.currentGroupButton);
            Group group = (Group) groupsData.get(position);
            currentGroup.setText(group.name);
            VariableManager.currentGroup = group;
            ParseObject user = VariableManager.user;
            user.put("currentGroup", group.groupId);
            user.saveInBackground();
            System.out.println(groupsData.size() + " groups contained in adapter");
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.group_fragment, container, false);
        return view;
    }
}

package com.peacockweb.billsplitter;

import android.support.annotation.RequiresPermission;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class AddGroup extends AppCompatActivity implements View.OnClickListener, AddGroupMemberDialog.NoticeDialogListener {

    LinearLayout layoutOfPopup;
    PopupWindow popupMessage;
    EditText popupButton;
    Button insidePopupButton;
    TextView popupText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
    }

    public void onCreateGroupClick(View view) {
        EditText groupName = (EditText) findViewById(R.id.newGroupName);
        EditText groupMembers = (EditText) findViewById(R.id.addGroupMembers);

        popupButton = (EditText) findViewById(R.id.addGroupMembers);
        popupButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //popupMessage.showAsDropDown(popupButton, 0, 0);
        DialogFragment newFragment = new AddGroupMemberDialog();
        newFragment.show(getSupportFragmentManager(), "groupMember");
    }

    @Override
    public void onDialogPositiveClick(String username) {
        EditText text = (EditText) findViewById(R.id.addGroupMembers);
        text.setText(username);
    }
}

package com.peacockweb.billsplitter.util;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListPopupWindow;

import com.peacockweb.billsplitter.R;

public class PopupListWindow extends Activity
        implements OnItemClickListener {

    String[] products={"Camera", "Laptop", "Watch","Smartphone",
            "Television"};
    EditText usernameText;
    ListPopupWindow listPopupWindow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_list_window);
        /*usernameText = (EditText) findViewById(
                R.id.editText5);

        listPopupWindow = new ListPopupWindow(
                PopupListWindow.this);
        listPopupWindow.setAdapter(
                new ArrayAdapter(PopupListWindow.this,
                R.layout.list_item, products));

        listPopupWindow.setAnchorView(usernameText);
        listPopupWindow.setWidth(300);
        listPopupWindow.setHeight(400);

        listPopupWindow.setModal(true);
        listPopupWindow.setOnItemClickListener(
                PopupListWindow.this);

        usernameText.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                listPopupWindow.show();
            }
        });*/
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        usernameText.setText(products[position]);
        listPopupWindow.dismiss();
    }
}

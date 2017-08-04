package com.qm.customview_qmlibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class CustomviewActivity extends AppCompatActivity {
    private AutoCompleteTextView autotv_customlib;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customview);
        autotv_customlib = (AutoCompleteTextView) findViewById(R.id.autotv_customlib);
        String [] arr={"aa","aab","aac"};
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arr);
        autotv_customlib.setAdapter(arrayAdapter);
        autotv_customlib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** 点击时，弹出下拉框*/
                autotv_customlib.showDropDown();
            }
        });
    }
}

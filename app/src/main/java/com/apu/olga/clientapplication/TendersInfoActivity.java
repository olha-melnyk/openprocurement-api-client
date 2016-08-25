package com.apu.olga.clientapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TendersInfoActivity extends AppCompatActivity {

    private static final String TAG = TendersInfoActivity.class.getSimpleName();

    private ArrayList tenderId;
    TextView textId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenders_info);

        tenderId = getIntent().getStringArrayListExtra("list");
        Log.i(TAG, "Tender ID: " + tenderId);

        textId = (TextView) findViewById(R.id.textView);
        textId.setText(tenderId.toString());


    }
}

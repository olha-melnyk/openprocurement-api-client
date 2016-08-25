package com.apu.olga.clientapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TendersInfoActivity extends AppCompatActivity {

    private static final String TAG = TendersInfoActivity.class.getSimpleName();
    public static String LOG_TAG = "my_log_tender";
    private ArrayList tenderId;
    TextView textId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenders_info);

        tenderId = getIntent().getStringArrayListExtra("list");
        Log.i(TAG, "Tender ID: " + tenderId);

        textId = (TextView) findViewById(R.id.textView);
        new RetrieveTender().execute();
    }

    class RetrieveTender extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... urls) {

                try {
                    for (final Object idTen : tenderId) {

                        URL urlTenderInfo = new URL(Constants.API_KEY + Constants.API_URL + "/" + idTen);
                        Log.i(TAG, "URL:" + urlTenderInfo);

                        HttpURLConnection tenderInfoConnection = (HttpURLConnection) urlTenderInfo.openConnection();
                        tenderInfoConnection.setRequestMethod("GET");
                        tenderInfoConnection.connect();
                        try {
                            BufferedReader bufferedReaderTender = new BufferedReader(new InputStreamReader(tenderInfoConnection.getInputStream()));
                            StringBuilder stringBuilderTender = new StringBuilder();
                            String line;
                            while ((line = bufferedReaderTender.readLine()) != null) {
                                stringBuilderTender.append(line).append("\n");
                            }
                            bufferedReaderTender.close();
                            return stringBuilderTender.toString();
                        } finally {
                            tenderInfoConnection.disconnect();
                        }
                    }
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                    return null;
                }
            return null;
        }

        protected void onPostExecute(String tenderId) {
            super.onPostExecute(tenderId);

            if (tenderId == null) {
                tenderId = "THERE WAS AN ERROR";
            }
            JSONObject dataJsonObj = null;
            try {
                dataJsonObj = new JSONObject(tenderId);
                JSONArray dataTenders = dataJsonObj.getJSONArray("data");
                for (int i = 0; i < dataTenders.length(); i++) {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            textId.setText(tenderId);
        }
    }
}

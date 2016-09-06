package com.apu.olga.clientapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static String LOG_TAG = "my_log";
    TextView responseView;
    ProgressBar progressBar;
    Button tenderBtn;
    String id;
    ArrayList<String> list = new ArrayList<String>();

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        responseView = (TextView) findViewById(R.id.responseView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        new RetrieveFeedTask().execute();
        tenderBtn = (Button) findViewById(R.id.tenderBtn);
        tenderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TendersInfoActivity.class);
                intent.putExtra("list", list);
                startActivity(intent);
            }
        });

    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            responseView.setText("");
        }

        @Override
        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL(Constants.API_KEY + Constants.API_URL);
                Log.i(LOG_TAG, "URL" + url);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }

        }

        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            progressBar.setVisibility(View.VISIBLE);
            responseView.setText("");

            if (strJson == null) {
                strJson = "THERE WAS AN ERROR";
            }
            JSONObject dataJsonObj = null;
            try {
                dataJsonObj = new JSONObject(strJson);
                JSONArray dataTenders = dataJsonObj.getJSONArray("data");
                for (int i = 0; i < dataTenders.length(); i++) {
                    JSONObject tenderId = dataTenders.getJSONObject(i);
                    id = tenderId.getString("id");
                    Log.i(LOG_TAG, "Id:" + id);
                    String data = tenderId.getString("dateModified");
                    Log.i(LOG_TAG, "Data:" + data);
                    list.add(id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.GONE);
            responseView.setText(strJson);
        }
    }
}



package com.apu.olga.clientapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static String LOG_TAG = "my_log";
    TextView responseView;
    ProgressBar progressBar;
    static final String API_KEY = "https://api-sandbox.openprocurement.org";
    static final String API_URL = "/api/2.3/tenders";

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        responseView = (TextView) findViewById(R.id.responseView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        new RetrieveFeedTask().execute();
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            responseView.setText("");
        }

        @Override
        protected String doInBackground(Void... urls) {

            try {
                URL url = new URL(API_KEY + API_URL);
                Log.i(TAG, "URL" + url);
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
                }
                finally{
                    urlConnection.disconnect();
                }
            }  catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            Log.d(LOG_TAG, strJson);

            if (strJson == null) {
                strJson = "THERE WAS AN ERROR";
            }
            progressBar.setVisibility(View.GONE);
            responseView.setText(strJson);

            JSONObject dataJsonObj = null;
            try {
                dataJsonObj = new JSONObject(strJson);
                JSONArray dataTenders = dataJsonObj.getJSONArray("data");
                for (int i = 0; i < dataTenders.length(); i++) {
                    JSONObject tenderId = dataTenders.getJSONObject(i);

                    String id = tenderId.getString("id");
                    String data = tenderId.getString("dateModified");
                    Log.d(LOG_TAG, "ID: " + id);
                    Log.d(LOG_TAG, "DateModified: " + data);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

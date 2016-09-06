package com.apu.olga.clientapplication;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.apu.olga.clientapplication.model.TenderItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class TendersInfoActivity extends AppCompatActivity {

    private static final String TAG_URL = "auctionUrl";
    private static final String TAG_PERIOD = "awardPeriod";

    private static final String TAG = TendersInfoActivity.class.getSimpleName();
    public static String LOG_TAG = "my_log_tender";
    private ArrayList<String> tenderIdArray;
    ListView listView;
    ProgressBar progressBar;
    ArrayList<TenderItem> tenders = new ArrayList<TenderItem>();
    TendersListAdapter adapter;
    URL urlTenderInfo;
    ArrayList<String> allTenders = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenders_info);


        tenderIdArray = getIntent().getStringArrayListExtra("list");
        Log.i(TAG, "Tender ID: " + tenderIdArray);

        progressBar = (ProgressBar) findViewById(R.id.progress);

        adapter = new TendersListAdapter(this,tenders);
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        new RetrieveTender().execute();
    }

    class RetrieveTender extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(Void... urls) {

            try {
                for (final Object idTen : tenderIdArray) {
                    urlTenderInfo = new URL(Constants.API_KEY + Constants.API_URL + "/" + idTen);
                    Log.i(LOG_TAG, "Taaaa:" + urlTenderInfo);
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
                        allTenders.add(stringBuilderTender.toString());

                    } finally {
                        tenderInfoConnection.disconnect();
                    }
                    Log.i(TAG, "AllTenders:" + allTenders);
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
            }
            return allTenders.toString();
        }

        protected void onPostExecute(String jsonStr) {
            super.onPostExecute(jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray tenders = jsonObj.getJSONArray(TAG_URL);

                    for (int i = 0; i < tenders.length(); i++) {
                        JSONObject obj = tenders.getJSONObject(i);

                        String url = obj.getString(TAG_URL);
                        Log.i(LOG_TAG, "Taaa:" + url);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
                tenders.add(new TenderItem(jsonStr));
                listView.invalidate();
            }
        }
    }
}
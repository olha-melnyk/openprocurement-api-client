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

        protected void onPostExecute(String all) {
            super.onPostExecute(all);
            if (all == null) {
                all = "THERE WAS AN ERROR";
            }
            JSONObject dataJsonObj = null;
            try {
                dataJsonObj = new JSONObject(all);
                JSONArray tenders = dataJsonObj.getJSONArray(all);
                JSONArray dataTenderInfo = dataJsonObj.getJSONArray("");

                for (int i = 0; i < dataTenderInfo.length(); i++) {
                    JSONObject tenderId = tenders.getJSONObject(i);
                    if(tenderId == null) continue;

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.GONE);
            tenders.add(new TenderItem(all));
            listView.invalidate();
        }
    }
}
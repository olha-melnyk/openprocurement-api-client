package com.apu.olga.clientapplication;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

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
    private ArrayList tenderIdArry;
    ListView listView;
    ArrayList<TenderItem> tenders = new ArrayList<TenderItem>();
    TendersListAdapter adapter;
    URL urlTenderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenders_info);

        tenderIdArry = getIntent().getStringArrayListExtra("list");
        Log.i(TAG, "Tender ID: " + tenderIdArry);

        fillData();
        new RetrieveTender().execute();
        adapter = new TendersListAdapter(this,tenders);
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
    }

    void fillData() {
        for (final Object idTen : tenderIdArry) {

            String  urlTenderInfo = Constants.API_KEY + Constants.API_URL + idTen;
            tenders.add(new TenderItem(urlTenderInfo));

        }

    }

    class RetrieveTender extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... urls) {

            try {
                for (final Object idTen : tenderIdArry) {

                    urlTenderInfo = new URL(Constants.API_KEY + Constants.API_URL + "/" + idTen);
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
                    JSONObject Id = dataTenders.getJSONObject(i);
                    String id = Id.getString("contactPoint");
                    Log.i(LOG_TAG, "Id:" + id);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
package com.apu.olga.clientapplication.firebase;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.apu.olga.clientapplication.Constants;
import com.apu.olga.clientapplication.R;
import com.apu.olga.clientapplication.TendersInfoActivity;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class FirebaseActivity extends AppCompatActivity {

    private static final String TAG = FirebaseActivity.class.getSimpleName();

    private List<Tenders> mTenders = new LinkedList<>();
    LocalActivityManager mlam;
    private Firebase mRef;
    private String mTenderId;
    private String mTenderTitle;

    private FirebaseTendersListAdapter mTenderListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

        mTenderId = Constants.FIREBASE_URL + "/tender1";
        ListView listView = (ListView) findViewById(R.id.list);
        mTenderListAdapter = new FirebaseTendersListAdapter(this, mTenders);
        listView.setAdapter(mTenderListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mTenderId != null) {
                    Tenders tender =  (Tenders) mTenderListAdapter.getItem(position);
                    Intent intent = new Intent(view.getContext(), TendersInfoActivity.class);
                    intent.putExtra("user_id", tender.getId());
                    startActivity(intent);
                } else {
                    Toast.makeText(FirebaseActivity.this, "No User ID", Toast.LENGTH_LONG).show();
                }
            }
        });
        Firebase.setAndroidContext(this);
        loadUsers();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void loadUsers() {
        final Firebase firebase = new Firebase(Constants.FIREBASE_URL).child("tenders");
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot usersSnapshot) {
                for (DataSnapshot userSnapshot : usersSnapshot.getChildren()) {
                    Tenders tender = userSnapshot.getValue(Tenders.class);
                    tender.setId(userSnapshot.getKey());
                    mTenders.add(tender);
                    Log.i(TAG, "Tender:" + tender + " " + tender.getId());
                }
                mTenderListAdapter.update();
                Log.i(TAG, "data changed " + usersSnapshot.getValue());
                Log.i(TAG, "data changed Num of tender" + usersSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.i(TAG, "cancelled");
            }
        });
    }
}

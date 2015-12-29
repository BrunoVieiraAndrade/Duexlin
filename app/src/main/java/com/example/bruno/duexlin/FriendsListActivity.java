package com.example.bruno.duexlin;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class FriendsListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}

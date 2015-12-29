package com.example.bruno.duexlin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

import java.util.Properties;

public class SuccessActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Profile profile = intent.getParcelableExtra("profile");

        ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.profilePic);
        TextView userNameTextView = (TextView) findViewById(R.id.welcomeTextView);
        Button button = (Button) findViewById(R.id.duelarButton);
        Button logoutButton = (Button) findViewById(R.id.logoutbutton);

        profilePictureView.setCropped(true);
        profilePictureView.setPresetSize(ProfilePictureView.LARGE);
        profilePictureView.setProfileId(profile.getId());


        userNameTextView.setText("Bem vindo " + profile.getFirstName() + "!");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FriendsListActivity.class));
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

    }

}

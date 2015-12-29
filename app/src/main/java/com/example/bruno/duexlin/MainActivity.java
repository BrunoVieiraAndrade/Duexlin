package com.example.bruno.duexlin;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends ActionBarActivity {

    CallbackManager callbackManager;
    ProfileTracker mProfileTracker;
    AccessTokenTracker accessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                updateWithToken(newAccessToken);
            }
        };

        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);

        getProjectHash();

        ImageView duexlinLogoImageView = (ImageView) findViewById(R.id.logo_image_view);
        duexlinLogoImageView.setImageResource(R.drawable.duexlin_logo);
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setText("Fazer login com Facebook");
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            Intent intent = new Intent(getApplicationContext(), SuccessActivity.class);
                            intent.putExtra("profile", profile2);
                            startActivity(intent);
                            mProfileTracker.stopTracking();
                        }
                    };
                    mProfileTracker.startTracking();
                }
                else {
                    Profile profile = Profile.getCurrentProfile();
                    Intent intent = new Intent(getApplicationContext(), SuccessActivity.class);
                    intent.putExtra("profile", profile);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getApplicationContext(), "Login cancelado " , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getApplicationContext(), "Erro no login - " + exception.getMessage(), Toast.LENGTH_LONG).show();
            }

        });

    }

    private void updateWithToken(AccessToken currentAccessToken) {

        if (currentAccessToken != null) {

            Profile profile = Profile.getCurrentProfile();
            Intent intent = new Intent(getApplicationContext(), SuccessActivity.class);
            intent.putExtra("profile", profile);
            startActivity(intent);
            finish();

        }

    }


    private void getProjectHash(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.bruno.duexlin",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

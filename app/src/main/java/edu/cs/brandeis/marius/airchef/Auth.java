package edu.cs.brandeis.marius.airchef;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.StormpathConfiguration;
import com.stormpath.sdk.models.StormpathError;
import com.stormpath.sdk.models.UserProfile;
import com.stormpath.sdk.ui.StormpathLoginActivity;
import com.stormpath.sdk.ui.StormpathLoginConfig;

public class Auth extends AppCompatActivity {

    public static final int REQUEST_LOGIN = 422;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Initialize Stormpath
        StormpathConfiguration stormpathConfiguration = new StormpathConfiguration.Builder()
                .baseUrl("http://homecooked-server.herokuapp.com/")
                .build();
        Stormpath.init(this, stormpathConfiguration);

        navigateToLogin();

    }

    private void navigateToLogin() {
        Intent loginIntent = new Intent(this, StormpathLoginActivity.class);

        loginIntent.putExtras(new StormpathLoginConfig.Builder()
                .autoLoginAfterRegister(true)
                .setIcon(R.mipmap.ic_launcher)
                .create());

        startActivityForResult(loginIntent, REQUEST_LOGIN);
    }

    private void navigateToHome() {
        startActivity(new Intent(this, ExploreMealsActivity.class));
        // !!! Put SharedPreference code here
        Stormpath.getUserProfile(new StormpathCallback<UserProfile>() {
            @Override
            public void onSuccess(UserProfile userProfile) {
                Log.d("app", userProfile.getEmail());
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Auth.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("email",userProfile.getEmail());
                editor.putString("name",userProfile.getFullName());
                editor.apply();
            }

            @Override
            public void onFailure(StormpathError error) {
            }
        });


        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_LOGIN:
                if (resultCode == Activity.RESULT_OK) {
                    // we are logged in so, let's navigate to home
                    navigateToHome();
                } else {
                    // looks like the user couldn't login and gave up by pressing the back button
                    finish();
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

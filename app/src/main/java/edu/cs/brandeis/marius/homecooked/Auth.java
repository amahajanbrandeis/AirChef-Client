package edu.cs.brandeis.marius.homecooked;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathConfiguration;
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
                .setIcon(R.mipmap.ic_launcher)
                .create());

        startActivityForResult(loginIntent, REQUEST_LOGIN);
    }

    private void navigateToHome() {
        startActivity(new Intent(this, MainActivity.class));
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

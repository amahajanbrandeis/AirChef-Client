package edu.cs.brandeis.marius.airchef;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.StormpathError;
import com.stormpath.sdk.models.UserProfile;
import com.stormpath.sdk.ui.StormpathLoginActivity;

import org.w3c.dom.Text;

public class myProfileActivity extends AppCompatActivity {
    String userEmail;
    String userName;
    TextView profileName;
    TextView profileEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userEmail = preferences.getString("email", "");
        userName = preferences.getString("name", "");

        setupViews();
        setupDrawer();
    }

    private void setupViews() {
        profileName = (TextView) findViewById(R.id.profileName);
        profileEmail = (TextView) findViewById(R.id.profileEmail);

        profileName.setText(userName);
        profileEmail.setText(userEmail);
    }

    private void setupDrawer() {
        new DrawerBuilder()
                .withActivity(this)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("My Profile").withIdentifier(1).withSelectable(false),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Explore Meals").withIdentifier(2),
                        new PrimaryDrawerItem().withName("My Meals").withIdentifier(3),
                        new PrimaryDrawerItem().withName("Purchased Meals").withIdentifier(4))
                .addStickyDrawerItems(
                        new PrimaryDrawerItem().withName("Logout").withIdentifier(5)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
//                                intent = new Intent(ExploreMealsActivity.this, myProfileActivity.class);
                            } else if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(myProfileActivity.this, ExploreMealsActivity.class);
                            } else if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(myProfileActivity.this, MyMealsActivity.class);
                            } else if (drawerItem.getIdentifier() == 4) {
                                intent = new Intent(myProfileActivity.this, PurchasedMealsActivity.class);
                            } else if (drawerItem.getIdentifier() == 5) {
                                Stormpath.logout();
                                intent = new Intent(myProfileActivity.this, StormpathLoginActivity.class);
                            }
                            if (intent != null) {
                                myProfileActivity.this.startActivity(intent);
                            }
                        }

                        return false;
                    }
                })
                .withSelectedItem(1)
                .build();
    }
}

package edu.cs.brandeis.marius.airchef;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

public class myProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String email = preferences.getString("email", "");
        if(!email.equalsIgnoreCase(""))
        {
            Log.d("app", email);
        }

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

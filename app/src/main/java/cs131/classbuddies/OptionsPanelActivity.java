package cs131.classbuddies;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class OptionsPanelActivity extends AppCompatActivity
{
    CBApp classbuddiesApp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Create page layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_panel);
        classbuddiesApp = (CBApp) getApplicationContext();


        // LISTENER: menu button
        ImageView menuIcon = findViewById(R.id.menu_bars);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        // LISTENER: Home
        final RelativeLayout homeTab = findViewById(R.id.home_tab);
        homeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActivities(MapActivity.class);
            }
        });


        // LISTENER: Profile
        final RelativeLayout profileTab = findViewById(R.id.profile_tab);
        profileTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switchActivities(ProfileActivity.class);
            }
        });


        // LISTENER BuddiesList
        final RelativeLayout buddiesTab = findViewById(R.id.buddies_tab);
        buddiesTab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switchActivities(BuddyListActivity.class);
            }
        });


        // LISTENER: ClassChat
        final RelativeLayout classChatTab = findViewById(R.id.classchat_tab);
        classChatTab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switchActivities(ChatActivity.class);
            }
        });


        // LISTENER: Meet Up
        final RelativeLayout meetUpTab = findViewById(R.id.meetup_tab);
        meetUpTab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
             switchActivities(MeetUpActivity.class);
            }
        });


        // LISTENER: Search
        final RelativeLayout searchTab = findViewById(R.id.search_tab);
        searchTab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switchActivities(SearchActivity.class);
            }
        });


        // LISTENER: Settings tab
        final RelativeLayout settingsTab = findViewById(R.id.settings_tab);
        settingsTab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switchActivities(SettingsActivity.class);
            }
        });


        // Listener for Sign Out tab
        final RelativeLayout signOutTab = findViewById(R.id.signout_tab);
        signOutTab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(OptionsPanelActivity.this, R.style.myDialog));
                builder.setTitle("Logout")
                        .setMessage("Are you sure you wish to sign out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing.
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Class<?> returnTo = classbuddiesApp.getLastActivity();
        startActivity(new Intent(OptionsPanelActivity.this, returnTo));
        finish();
    }

    private void switchActivities(Class<?> thatActivity){
        startActivity(new Intent(OptionsPanelActivity.this, thatActivity));
        finish();
    }
}
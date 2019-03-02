package cs131.classbuddies;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class SettingsActivity extends AppCompatActivity
{
    CBApp classbuddiesApp;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Get Application data
        classbuddiesApp = (CBApp) getApplicationContext();
        user = classbuddiesApp.getUser();


        // UI Handles
        final ImageView menuIcon              = findViewById(R.id.menu_bars);
        final RelativeLayout aboutTab         = findViewById(R.id.about_tab);
        final RelativeLayout reportBugsTab    = findViewById(R.id.reportbugs_tab);
        final RelativeLayout deleteProfileTab = findViewById(R.id.deleteprofile_tab);


        //LISTENER: Menu icon
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classbuddiesApp.setLastActivity(SettingsActivity.class);
                startActivity(new Intent(SettingsActivity.this, OptionsPanelActivity.class));
                finish();
            }
        });

        // LISTENER: About
        aboutTab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                classbuddiesApp.setLastActivity(SettingsActivity.class);
                startActivity(new Intent(SettingsActivity.this, AboutActivity.class));
                finish();
            }
        });


        // LISTENER: Report Bugs
        reportBugsTab.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://athena.ecs.csus.edu/~classbud/reportBug.html"));
                        startActivity(viewIntent);
                    }
                });


        // LISTENER: Deactivate Profile
        deleteProfileTab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(SettingsActivity.this, R.style.myDialog));
                builder.setTitle("YOU CAN NOT UNDO THIS ACTION!")
                        .setMessage("Are you sure you want to deactivate your account?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("DIALOG:", "Yes");

                                // Deactivate account
                                Server.deleteUserAccount(classbuddiesApp.getUser());

                                // Logout user
                                classbuddiesApp.setUser(null);
                                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("DIALOG:", "No");
                                // do nothing.
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, MapActivity.class);
        startActivity(intent);
        finish();
    }

}
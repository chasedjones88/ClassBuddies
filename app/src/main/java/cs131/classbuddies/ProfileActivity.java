package cs131.classbuddies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProfileActivity extends AppCompatActivity {
    CBApp       classbuddiesApp;
    Button      editButton;
    ImageView   menuButton;
    TextView    userName;
    TextView    userMajor;
    ListView    lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create page layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get Application data
        classbuddiesApp  = (CBApp) getApplicationContext();
        User currentUser = classbuddiesApp.getUser();

        // UI Handles
        menuButton  = findViewById(R.id.menu_bar_icon);
        editButton  = findViewById(R.id.class_field);
        lv          = findViewById(R.id.list_view);
        userName    = findViewById(R.id.name);
        userMajor   = findViewById(R.id.major);


        // Get User Information, replace empty data with 'N/A'
        String name      = ( currentUser.getName().isEmpty() ) ? "N/A" : currentUser.getName();
        String major     = ( currentUser.getMajor().isEmpty() ) ? "N/A" : currentUser.getMajor();
        String classes   =   currentUser.getClasses();

        // Populate UI with User data
        userName.setText(name);
        userMajor.setText(major);

        // Build list of classes from class string
        List<String> classesList = new ArrayList<>();
        String[] classList = classes.split(",");
        classesList.addAll(Arrays.asList(classList));

        // Add 'N/A' if the user has no classes.
        if (classes.isEmpty()) classesList.add("N/A");

        // Populate list view and deactivate clicking
        lv.setAdapter(new ArrayAdapter<> (ProfileActivity.this, android.R.layout.simple_list_item_1, classesList));
        lv.setEnabled(false);



        // LISTENER: Menu button
        menuButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                classbuddiesApp.setLastActivity(ProfileActivity.class);
                Intent intent = new Intent(ProfileActivity.this, OptionsPanelActivity.class);
                startActivity(intent);
                finish();
            }
        });


        // LISTENER: Edit Profile button
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                classbuddiesApp.setLastActivity(ProfileActivity.class);
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileActivity.this, classbuddiesApp.getLastActivity ());
        startActivity(intent);
        finish();
    }

}

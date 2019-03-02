package cs131.classbuddies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BuddyListActivity extends AppCompatActivity
{
    CBApp classbuddiesApp;

    // UI elements
    LinearLayout layout;
    ImageView menuButton;

    // Variables
    @SuppressLint("UseSparseArrays")     // SparseArrays are slower than HashMaps
    Map<Integer,User> buddyMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddy_list);

        // Force background color to slate_grey 
        View root = findViewById(R.id.background).getRootView();
        root.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.slate_grey));


        // Get application data
        classbuddiesApp = (CBApp) getApplicationContext();
        User thisUser = classbuddiesApp.getUser();
        
        
        // UI Handles
        menuButton = findViewById(R.id.menu_bars);
        ListView lv = findViewById(R.id.list_view);

        
        // Populate buddy list
        List<String> buddiesList = new ArrayList<> ();
        ArrayAdapter<String> adapter;

        int[] friendIDs = Server.getUsersFriends(thisUser.getUserID());

        if (friendIDs.length == 0) {
            // Show empty list message, deactivate clicking on items.
            buddiesList.add("Your buddies list looks lonely, add a buddy!");
            lv.setEnabled(false);
        }
        else {
            for (int friendID : friendIDs) {

                // Get each friend and place them in the buddies list
                User friend = Server.getUserByID(friendID);

                // Add friend if it exists
                if (friend!=null) {
                    //Store all friends objects inline with the view list for indexing purposes.
                    buddyMap.put(buddyMap.size(), friend);
                    buddiesList.add(friend.getName());
                }
            }
        }
        // Populate ListView with friends
        adapter = new ArrayAdapter<> (BuddyListActivity.this, android.R.layout.simple_list_item_1, buddiesList);
        lv.setAdapter(adapter);



        // LISTENER: ListView OnItemClick
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Store clicked friend to Application
                classbuddiesApp.setClickedUser(buddyMap.get(i));

                // Switch activities to show friend profile
                classbuddiesApp.setLastActivity(BuddyListActivity.class);
                startActivity(new Intent(BuddyListActivity.this, BuddyProfileActivity.class));
                finish();
            }
        });



        // LISTENER: Menu button
        menuButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                classbuddiesApp.setLastActivity(BuddyListActivity.class);
                startActivity(new Intent(BuddyListActivity.this, OptionsPanelActivity.class));
                finish();
            }
        });

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BuddyListActivity.this, MapActivity.class);
        startActivity(intent);
        finish();
    }

}
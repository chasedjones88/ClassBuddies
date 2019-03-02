package cs131.classbuddies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MeetUpDescription extends AppCompatActivity {
    CBApp       classbuddiesApp;
    // UI
    TextView    eventName;
    TextView    eventClass;
    TextView    eventTime;
    TextView    eventLocation;
    TextView    eventDesc;
    Button      buttonGroup;
    Button      buttonDeleteGroup;
    ImageView   buttonMenu;
    ListView    lv;
    // Variables
    @SuppressLint("UseSparseArrays")    // HashMaps are faster
    Map<Integer,User> memberMap = new HashMap<>();
    Group       thisGroup;
    User        user;
    Boolean     userInGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_date);

        // Get Global Application data
        classbuddiesApp = (CBApp) getApplicationContext();
        thisGroup       = classbuddiesApp.getGroup();
        user            = classbuddiesApp.getUser();


        // Get handles for all UI objects
        eventName           = findViewById(R.id.textEventName);
        eventClass          = findViewById(R.id.textEventClass);
        eventTime           = findViewById(R.id.textEventTime);
        eventLocation       = findViewById(R.id.textEventLocation);
        eventDesc           = findViewById(R.id.textEventDesc);
        buttonGroup         = findViewById(R.id.buttonEventJoin);
        buttonMenu          = findViewById(R.id.threebar_options);
        buttonDeleteGroup   = findViewById(R.id.buttonDeleteGroup);
        lv                  = findViewById(R.id.members_list);


        // Make dates human readable
        SimpleDateFormat humanReadable = new SimpleDateFormat("LLL dd, yyyy hh:mm aa", Locale.US);
        String dateString = "Starts:\t\t" +
                            humanReadable.format(thisGroup.getStartTime()) +
                            "\n  Ends:\t\t" +
                            humanReadable.format(thisGroup.getEndTime());


        // Populate UI elements with Group details
        eventName.setText(thisGroup.getGroupName());
        eventClass.setText(thisGroup.getGroupClass());
        eventDesc.setText(thisGroup.getGroupDesc());
        eventTime.setText(dateString);
        eventLocation.setText(thisGroup.getLocationDesc());


        // Populate event members list view
        populateListView();


        // If user is group creator, show remove group button
        if (user.getUserID() == thisGroup.getOwnerID()) {
            buttonDeleteGroup.setVisibility(View.VISIBLE);

            //LISTENER: WHEN VISIBLE create OnClickListener for Remove Group button
            buttonDeleteGroup.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View view) {
                    Server.deactivateGroup(thisGroup.getID());
                    startActivity (new Intent (MeetUpDescription.this, MapActivity.class));
                    finish();
                }
            });
        }


        // Modify UI according to user-to-group relationship
        userInGroup = false;
        buttonGroup.setText(R.string.joinGroup);

        int[] userGroups = user.getGroups();
        if (userGroups.length > 0) {
            for (int i : userGroups) {
                if (i == thisGroup.getID ( )) {
                    userInGroup = true;
                    buttonGroup.setText(R.string.leaveGroup);
                }
            }
        }



        // LISTENER: Join/Leave Group button
        buttonGroup.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                if (userInGroup) {
                    boolean response = Server.removeUserFromGroup(user.getUserID(),thisGroup.getID());
                    buttonGroup.setText(R.string.joinGroup);
                    Log.d("REMOVE_GROUP: ", String.valueOf(response));
                }
                else {
                    int response = Server.addUserToGroup(user.getUserID(),thisGroup.getID());
                    buttonGroup.setText(R.string.leaveGroup);
                    Log.d("ADD_GROUP: ", String.valueOf(response));
                }

                userInGroup = !userInGroup;

                // Update user's groups for map markers
                user.setGroups(Server.getUsersGroups(user.getUserID()));

                // refresh group member list
                populateListView();
            }
        });


        // LISTENER: Menu Button
        buttonMenu.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                classbuddiesApp.setLastActivity(MeetUpDescription.class);
                startActivity (new Intent (MeetUpDescription.this, OptionsPanelActivity.class));
                finish();
            }
        });



        // LISTENER: list view onClick
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Get selected user from list view
                User selected = memberMap.get(i);

                // Avoid user seeing buddy profile for themselves.
                if (selected.getUserID () == user.getUserID ()) {
                    classbuddiesApp.setLastActivity (MeetUpDescription.class);
                    startActivity (new Intent (MeetUpDescription.this, ProfileActivity.class));
                    finish();
                }
                else {

                    // set selected user at application level
                    classbuddiesApp.setClickedUser(selected);

                    // Switch activities to show friend details
                    classbuddiesApp.setLastActivity(MeetUpDescription.class);
                    startActivity(new Intent(MeetUpDescription.this, BuddyProfileActivity.class));
                    finish();
                }
            }
        });

    }

    // Function that refreshes group member data and repopulates the list view.
    public void populateListView() {

        memberMap.clear();                          // clear map(i,User) for refresh

        List<String> memberList = new ArrayList<> ();
        ArrayAdapter<String> adapter;

        int groupCreatorID = classbuddiesApp.getGroup().getOwnerID();
        int[] groupUserList = Server.getGroupUserList(thisGroup.getID());

        if (groupUserList.length == 0) {
            memberList.add("No one is in this group.");
            lv.setEnabled(false);
        }
        else
        {
            for (int i = 0; i < groupUserList.length; i++) {

                // Get each friend and place them in the buddies list
                User member = Server.getUserByID(groupUserList[i]);
                String memberName = "";

                // Handle bad return data from Server class
                if (member!=null) memberName = member.getName();

                // Note if the member is also the group creator
                if (groupUserList[i]==groupCreatorID) memberName += "  (creator)";

                //Store all friends objects inline with the view list for indexing purposes.
                memberMap.put(i, member);
                memberList.add(memberName);
            }
        }
        adapter = new ArrayAdapter<> (MeetUpDescription.this, android.R.layout.simple_list_item_1, memberList);
        lv.setAdapter(adapter);
    }



    @Override
    public void onBackPressed() {
        startActivity (new Intent (MeetUpDescription.this, classbuddiesApp.getLastActivity()));
        finish();
    }
}

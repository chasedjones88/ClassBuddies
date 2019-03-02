package cs131.classbuddies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BuddyProfileActivity extends AppCompatActivity {
    // Application Level
    CBApp classbuddiesApp;
    User selectedUser;
    User currentUser;

    // UI components
    ImageView menuButton;
    RelativeLayout send_message;
    RelativeLayout alterBuddy;
    ImageView alterBuddyIcon;
    TextView alterBuddyText;

    // Activity variables
    boolean areFriends = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_buddy_profile);
        classbuddiesApp = (CBApp) getApplicationContext ( );

        // Get application level data
        currentUser = classbuddiesApp.getUser();
        selectedUser = classbuddiesApp.getClickedUser();


        // Get UI Handles
        menuButton = findViewById (R.id.menu_bar_icon);
        send_message = findViewById (R.id.message_buddy);
        alterBuddy = findViewById (R.id.alter_buddy);
        alterBuddyIcon = findViewById(R.id.alter_buddy_icon);
        alterBuddyText = findViewById(R.id.alter_buddy_text);
        TextView buddyName = findViewById (R.id.name);
        TextView buddyMajor = findViewById (R.id.major);
        ListView lv = findViewById (R.id.list_view);


        // Check if user and buddy are friends
        int[] userFriends = currentUser.getFriends();
        for(int friendID : userFriends){
            if (friendID == selectedUser.getUserID()) {
                areFriends = true;
            }
        }


        // Set UI based on friend status
        setAddRemoveBuddy(areFriends);


        // Populate UI with selectedUser data
        List<String> classesList = new ArrayList<> ( );

        String name = selectedUser.getName ( );
        String major = selectedUser.getMajor ( );
        String classes = selectedUser.getClasses ( );

        String[] classList = classes.split (",");
        classesList.addAll (Arrays.asList (classList));

        // Populate default values
        buddyName.setText ("N/A");
        buddyMajor.setText ("N/A");
        if (classes.length() == 0) classesList.add ("N/A");

        // Poulate data-driven values
        if (name != null) buddyName.setText (name);
        if (major != null) buddyMajor.setText (major);

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<> (BuddyProfileActivity.this, android.R.layout.simple_list_item_1, classesList);
        lv.setAdapter (adapter);
        lv.setEnabled(false);


        // If selected is self
        if(selectedUser.getUserID() == currentUser.getUserID()){
            AlphaAnimation alpha = new AlphaAnimation(0.5F, 0.5F);
            alpha.setDuration(0); // Make animation instant
            alpha.setFillAfter(true); // Tell it to persist after the animation ends
            // And then on your layout
            alterBuddy.startAnimation(alpha);
            alterBuddy.setEnabled(false);
        }

        // Create listener for menu button
        menuButton.setOnClickListener (new View.OnClickListener ( ) {
            public void onClick(View v) {
                classbuddiesApp.setLastActivity (BuddyProfileActivity.class);
                Intent intent = new Intent (BuddyProfileActivity.this, OptionsPanelActivity.class);
                startActivity (intent);
                finish ( );
            }
        });



        // Create listener for send_message button
        send_message.setOnClickListener (new View.OnClickListener ( ) {
            public void onClick(View v) {

                int currentUserID = currentUser.getUserID ( );
                int buddyUserID = selectedUser.getUserID ( );

                if (currentUserID != buddyUserID) {
                    Chat friendChat = Server.getPrivateChat (currentUserID, buddyUserID);

                    if (friendChat == null) {
                        String chatDesc = "Buddy: " + currentUser.getName ( ) + " and " + selectedUser.getName();
                        friendChat = Server.createChat (currentUserID, buddyUserID, chatDesc);
                    }

                    classbuddiesApp.setChat(friendChat);
                    classbuddiesApp.setLastActivity (BuddyProfileActivity.class);
                    startActivity (new Intent (BuddyProfileActivity.this, ChatWindowActivity.class));
                    finish ( );
                }
                else {
                    showError(R.string.selfchat);

                }

            }
        });



        // Create OnClick for Alter Buddy
        alterBuddy.setOnClickListener (new View.OnClickListener ( ) {
                                           public void onClick(View v) {
                                               // If we are friends, remove friend

                                               if (areFriends) {
                                                   if (Server.removeFriendForUser(currentUser.getUserID(), selectedUser.getUserID())) {
                                                       areFriends = false;
                                                       setAddRemoveBuddy (false);
                                                   }
                                               } else {
                                                   if (Server.addFriendForUser(currentUser.getUserID(), selectedUser.getUserID(), "no note") > -2) {
                                                       areFriends = true;
                                                       setAddRemoveBuddy(true);
                                                   }
                                               }
                                               // Update user at application level
                                               currentUser.setFriends(Server.getUsersFriends(currentUser.getUserID()));
                                               classbuddiesApp.setUser(currentUser);
                                           }
        });
    }


    private void showError(int errorType) {

        Toast.makeText(classbuddiesApp, errorType, Toast.LENGTH_SHORT).show();
    }



    private void setAddRemoveBuddy(boolean isFriend) {
        if (isFriend) {
            // Since we're friends, populate screen with options to remove friends
            alterBuddyIcon.setImageResource(R.drawable.delete_icon);
            alterBuddyText.setText(R.string.removeBuddy);
        }
        else {
            // Since we're NOT friends, populate screen with options to add friend
            alterBuddyIcon.setImageResource(R.drawable.new_buddy_icon);
            alterBuddyText.setText(R.string.addBuddy);
        }
    }


    @Override
    public void onBackPressed() {
        Class<?> lastActivity = classbuddiesApp.getLastActivity();
        Intent intent = new Intent(BuddyProfileActivity.this, lastActivity);
        startActivity(intent);
        finish();
    }
}

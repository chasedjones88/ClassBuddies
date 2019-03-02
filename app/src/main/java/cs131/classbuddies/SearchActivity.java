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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchActivity extends AppCompatActivity
{
    CBApp                   classbuddiesApp;
    // UI
    SearchView              searchView;
    TextView                resultsText;
    Search                  previousSearch;
    ImageView               menuButton;
    RadioButton             rbGroup, rbUsers;
    // Variables
    User                    currentUser;
    ListView                listView;
    List<String>            searchList;
    @SuppressLint("UseSparseArrays")    // HashMaps are faster
    Map<Integer, Integer>   resultsMap = new HashMap<>();
    ArrayAdapter<String>    adapter;
    boolean                 usersChecked  = false;
    boolean                 groupsChecked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        // Get Application Data
        classbuddiesApp = (CBApp) getApplicationContext();
        currentUser     = classbuddiesApp.getUser();
        previousSearch  = classbuddiesApp.getSearch();


        // Get UI Handles
        listView        = findViewById(R.id.list_view);
        resultsText     = findViewById(R.id.resultsText);
        menuButton      = findViewById(R.id.menu_bars);
        rbGroup         = findViewById(R.id.search_by_groups);
        rbUsers         = findViewById(R.id.search_by_users);
        searchView      = findViewById(R.id.search_view);


        // Set defaults
        searchView.setIconifiedByDefault(false);
        searchList = new ArrayList<> ();
        View root = findViewById(R.id.background).getRootView();
        root.setBackgroundColor(ContextCompat.getColor(classbuddiesApp, R.color.slate_grey)); // Force background color to slate-grey


        // Redisplay a previous search (if available)
        if (previousSearch != null) {
            // Reset Activity values
            groupsChecked = previousSearch.isGroupSelected();
            usersChecked  = previousSearch.isUsersSelected();

            // Reset Radio Buttons
            rbGroup.setChecked(groupsChecked);
            rbUsers.setChecked(usersChecked);

            // Restore searchList and resultMap relationship
            resultsMap = previousSearch.getResultsMap();
            searchList = previousSearch.getSearchList();

            // Replace Search Query Text
            searchView.setQuery(previousSearch.getSearchText(), false);

            // Repopulate list view
            listView.setAdapter(new ArrayAdapter<> (SearchActivity.this, android.R.layout.simple_list_item_1, searchList));

            // Reset results banner
            resultsText.setText(R.string.results);

        }



        // LISTENER: Search Query
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchList.clear();
                resultsMap.clear();
                query = query.replaceAll("[^a-zA-Z0-9\\s]", "");
                if(usersChecked)
                {
                    int[] userIDs = Server.searchUsers(query);
                    for (int userID : userIDs) {
                        // Filter current user from search results
                        if (userID != currentUser.getUserID ( )) {
                            // Get each friend and place them in the buddies list
                            User user = Server.getUserByID (userID);
                            String usersName =  (user != null) ? user.getName() : "";
                            //Store all users objects inline with the view list for indexing purposes.
                            resultsMap.put (resultsMap.size(), userID);
                            searchList.add (usersName);
                        }
                    }
                }
                if(groupsChecked)
                {
                    int[] groupIDs = Server.searchGroups(query);
                    for (int groupID : groupIDs) {
                        Group group = Server.getGroupByID (groupID);
                        String groupName = (group != null) ? group.getGroupName ( ) : "";
                        if (groupName.length ( ) > 23) {
                            groupName = groupName.substring (0, 20) + "...";
                        }
                        resultsMap.put (resultsMap.size ( ), groupID);
                        searchList.add (groupName);
                    }
                }
                if(!usersChecked && !groupsChecked)
                    Toast.makeText(classbuddiesApp, "Please select a filter", Toast.LENGTH_SHORT).show();
                else if(searchList.isEmpty())
                    Toast.makeText(classbuddiesApp, "Sorry, nothing found", Toast.LENGTH_SHORT).show();

                resultsText.setText(R.string.results);
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        adapter = new ArrayAdapter<> (SearchActivity.this, android.R.layout.simple_list_item_1, searchList);
        listView.setAdapter(adapter);



        // LISTENER: list view OnClick
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // create copy of search
                Search search = new Search(searchView.getQuery().toString(), usersChecked, groupsChecked, searchList, resultsMap);

                if(usersChecked)
                {
                    // Set search for next time screen is opened
                    classbuddiesApp.setSearch(search);
                    classbuddiesApp.setClickedUser(new User(resultsMap.get(i)));
                    classbuddiesApp.setLastActivity(SearchActivity.class);
                    startActivity(new Intent(SearchActivity.this, BuddyProfileActivity.class));
                    finish();

                }
                if(groupsChecked)
                {
                    // Set search for next time screen is opened
                    classbuddiesApp.setSearch(search);
                    classbuddiesApp.setGroup(Server.getGroupByID(resultsMap.get(i)));
                    classbuddiesApp.setLastActivity(SearchActivity.class);
                    startActivity (new Intent (SearchActivity.this, MeetUpDescription.class));
                    finish ();
                }
            }
        });



        // LISTENER: Menu button
        menuButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                classbuddiesApp.setLastActivity(SearchActivity.class);
                Intent intent = new Intent(SearchActivity.this, OptionsPanelActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }



    // FUNCTION called when any radio button is selected
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        groupsChecked = usersChecked = false;

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.search_by_users:
                if (checked)
                    Toast.makeText(classbuddiesApp, "Let's search for users!", Toast.LENGTH_SHORT).show();
                    usersChecked = true;
                    break;
            case R.id.search_by_groups:
                if (checked)
                    Toast.makeText(classbuddiesApp, "Let's search for groups!", Toast.LENGTH_SHORT).show();
                    groupsChecked = true;
                    break;
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SearchActivity.this, MapActivity.class);
        startActivity(intent);
        finish();
    }


}
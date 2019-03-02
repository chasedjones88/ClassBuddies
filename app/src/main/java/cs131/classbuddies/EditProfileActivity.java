package cs131.classbuddies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {
    CBApp        classbuddiesApp;
    User         currentUser;
    // UI
    ImageView    menuButton;
    Button       addButton;
    Button       deleteButton;
    TextView     userName;
    EditText     userMajor;
    EditText     textClass;
    ListView     lv;
    // Variables
    ArrayAdapter<String> adapter;
    List<String> classesList;
    String       nameBefore;
    String       nameAfter;
    String       majorBefore;
    String       majorAfter;
    String       classesBefore;
    String       classesAfter;
    String[]     classList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create page layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Get Application data
        classbuddiesApp = (CBApp) getApplicationContext();
        currentUser = classbuddiesApp.getUser();


        // Get UI Handles
        userName        = findViewById(R.id.name);
        userMajor       = findViewById(R.id.major);
        textClass       = findViewById(R.id.textClass);
        addButton       = findViewById(R.id.buttonAddClass);
        deleteButton    = findViewById(R.id.buttonDeleteClass);
        menuButton      = findViewById(R.id.menu_bar_icon);
        lv              = findViewById(R.id.list_view);


        // Build UI Data
        nameBefore      = ( currentUser.getName().isEmpty() ) ? "N/A" : currentUser.getName();
        majorBefore     = ( currentUser.getMajor().isEmpty() ) ? "N/A" : currentUser.getMajor();
        classesBefore   = currentUser.getClasses();

        // Build class list
        classesList = new ArrayList<> ();
        classList       = classesBefore.split(",");
        classesList.addAll(Arrays.asList(classList));


        // Populate UI with data
        userName.setText(nameBefore);
        userMajor.setText(majorBefore);

        if (classesList.isEmpty()) {
            classesList.add ("N/A");
        }
        adapter = new ArrayAdapter<> (EditProfileActivity.this, android.R.layout.simple_list_item_1, classesList);
        lv.setAdapter(adapter);



        // LISTENER: ListView On Click
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String className = lv.getAdapter().getItem(i).toString();
                if (!className.equals("N/A")) textClass.setText(className);
            }
        });



        // LISTENER: Menu Button
        menuButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                classbuddiesApp.setLastActivity(EditProfileActivity.class);
                Intent intent = new Intent(EditProfileActivity.this, OptionsPanelActivity.class);
                startActivity(intent);
                finish();
            }
        });



        // LISTENER: Save button
        Button saveButton = findViewById(R.id.save);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Grab user's information after editing
                nameAfter    = userName.getText().toString();
                majorAfter   = userMajor.getText().toString();
                classesAfter = classesList.toString();
                classesAfter = classesAfter.replaceAll("[^a-zA-Z0-9,]", "");

                // If user clicks the save button without making any new changes
                if (nameBefore.equals(nameAfter) && majorBefore.equals(majorAfter) && classesBefore.equals(classesAfter)) {
                    Toast.makeText(classbuddiesApp, "No New Changes", Toast.LENGTH_SHORT).show();
                }
                else {
                     // Save new changes by setting fields to new values
                     currentUser.setMajor(userMajor.getText().toString());
                     currentUser.setName(userName.getText().toString());
                     currentUser.setClasses(classesAfter);
                     Server.updateUser(currentUser);
                     Toast.makeText(EditProfileActivity.this, "Changes Saved", Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                     startActivity(intent);
                     finish();
                }
            }
        });



        // LISTENER: Add class button
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                // Get new class, clean string
                String newClass = textClass.getText().toString();
                newClass = newClass.replaceAll("[^a-zA-Z0-9]", "");
                newClass = newClass.toUpperCase();

                boolean duplicate = false;
                // If user tries to add a class that already exists in their class list

                for(int i = 0; i < classesList.size(); i++){
                    if (classesList.get(i).equals(newClass))
                    {
                        Toast.makeText(EditProfileActivity.this, "Already in list", Toast.LENGTH_SHORT).show();
                        duplicate = true;
                    }
                }

                // As long as the class desired is not already in the list
                if(!duplicate) {
                    // If user tries to add an empty string/class or a class that contains more than one space
                    if (newClass.isEmpty() || newClass.contains("  "))
                    {
                        Toast.makeText(EditProfileActivity.this, "Cannot add blank space", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        classesList.add(newClass);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(EditProfileActivity.this, "Class added", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });


        // LISTENER: Delete Class button
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                // Get class to delete, clean string
                String deleteClass = textClass.getText().toString();
                deleteClass = deleteClass.replaceAll("[^a-zA-Z0-9]", "");
                deleteClass = deleteClass.toUpperCase();

                // Loop through class list and remove the class entered by user if a match is found
                boolean match = false;
                for(int i = 0; i < classesList.size(); i++){
                    if (classesList.get(i).equals(deleteClass))
                    {
                        classesList.remove(deleteClass);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(EditProfileActivity.this, "Class Deleted", Toast.LENGTH_SHORT).show();
                        match = true;
                    }
                }
                // If a match is not found
                if(!match)
                {
                    Toast.makeText(EditProfileActivity.this, "Unknown Selected Class", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditProfileActivity.this, classbuddiesApp.getLastActivity());
        startActivity(intent);
        finish();
    }

}

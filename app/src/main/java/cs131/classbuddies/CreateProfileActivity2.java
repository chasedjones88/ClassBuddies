package cs131.classbuddies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CreateProfileActivity2 extends AppCompatActivity
{
    CBApp       classbuddiesApp;
    // UI
    ImageView   buttonBack;
    ImageView   buttonNext;
    ImageView   buttonAddClass;
    EditText    textClass;
    EditText    textMajor;
    TextView    textClassList;
    // Variables
    User        user;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Create page layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile2);


        // Get application data
        classbuddiesApp = (CBApp) getApplicationContext();
        user = classbuddiesApp.getUser();


        // Create handles for necessary UI elements
        buttonBack      = findViewById(R.id.buttonBack);
        buttonNext      = findViewById(R.id.buttonNext);
        buttonAddClass  = findViewById(R.id.buttonAddClass);
        textClass       = findViewById(R.id.textClass);
        textClassList   = findViewById(R.id.textClassList);
        textMajor       = findViewById(R.id.textMajor);


        // LISTENER: Back button. On press, reject creating a profile, go to home page
        buttonBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(CreateProfileActivity2.this, MapActivity.class));
                finish();
            }
        });


        // LISTENER: Next button
        buttonNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Get class string from UI, strip banner/spaces, store in user
                String classes = textClassList.getText().toString();
                classes = classes.replace("Your Classes:", "");
                classes = classes.replaceAll(" ", "");
                user.setClasses(classes);

                // Get Major, store in user
                String major = textMajor.getText().toString();
                user.setMajor(major);

                // Update user in database
                if (!Server.updateUser(user)) {
                    Log.d("USER UPDATE: ", "failed");
                }

                // Update user in App
                classbuddiesApp.setUser(user);

                startActivity(new Intent(CreateProfileActivity2.this, MapActivity.class));
                finish();
            }
        });


        // LISTENER: Add Class button
        buttonAddClass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                // Get new class, clean string, insert to UI textView.
                String newClass = textClass.getText().toString();
                newClass = newClass.replaceAll("[^a-zA-Z0-9]", "");
                newClass = newClass.toUpperCase();
                boolean duplicate = false;

                String classList = textClassList.getText().toString();

                // If user tries to add a class that already exists in their class list
                if(classList.contains(newClass) && !newClass.isEmpty())
                {
                    Toast.makeText(CreateProfileActivity2.this, "Already in list", Toast.LENGTH_SHORT).show();
                    duplicate = true;
                }

                // As long as the class desired is not already in the list
                if(!duplicate)
                {
                    // If user tries to add an empty string/class
                    if ( newClass.isEmpty() ||  (newClass.equals(" ")) )
                    {
                        Toast.makeText(CreateProfileActivity2.this, "Cannot add blank space", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        // Check if the list is currently empty (with default UI text). Add class to list.
                        if (classList.contains("Example:")) {
                            classList = "Your Classes: " + newClass;
                            Toast.makeText(CreateProfileActivity2.this, "Class added", Toast.LENGTH_SHORT).show();
                        } else {
                            classList += ", " + newClass;
                            Toast.makeText(CreateProfileActivity2.this, "Class added", Toast.LENGTH_SHORT).show();
                        }
                        textClassList.setText(classList);
                    }
                }
            }
        });


    }
}


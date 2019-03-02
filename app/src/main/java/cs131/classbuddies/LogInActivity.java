package cs131.classbuddies;

import android.support.design.widget.Snackbar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class LogInActivity extends AppCompatActivity {
    CBApp       classbuddiesApp;
    EditText    textName;
    EditText    textPass;
    ImageView   log_in;
    ImageView   new_profile;
    User        user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Create page layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_menu);

        // get parent class reference
        classbuddiesApp = (CBApp) getApplicationContext();

        // UI handles
        textName    = findViewById(R.id.name_field);
        textPass    = findViewById(R.id.pass_field);
        log_in      = findViewById(R.id.img_log_in);
        new_profile = findViewById(R.id.img_create_account);

        // Create input filter for username
        textName.setFilters( new InputFilter[] { filter});



        // LISTENER: Log in button
        log_in.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String username = textName.getText().toString();
                String password = textPass.getText().toString();

                // IF USERNAME < 3, ERROR
                if (username.length() < 3) {
                    // Show error for user length
                    showError(R.string.user_too_short);
                }
                // IF PASSWORD < 6, ERROR
                else if (password.length() < 6) {
                    // Show error for password length
                    showError(R.string.pass_too_short);
                }
                else {
                    // TRY AUTHENTICATION
                    if (authenticateLogin(username, password)) {

                        if (user!=null) {
                            classbuddiesApp.setUser(user);
                        }
                        else {
                            // Show general error since user is null
                            showError(R.string.error);
                        }
                        // Create UI fork if the users classes and major are empty --- DO NOT FINISH() LogInActivity.
                        String classes = user.getClasses();
                        String major = user.getMajor();

                        if ( (classes.length()==0) && (major.length()==0) ){
                            startActivity(new Intent(LogInActivity.this, CreateProfileActivity1.class));
                        }
                        else {
                            startActivity(new Intent(LogInActivity.this, MapActivity.class));
                        }

                    }
                }
            }
        });


        // LISTENER: Create Profile button
        new_profile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, CreateAccountActivity.class));
            }
        });
    }



    // Function to check authentication, report error if any
    private boolean authenticateLogin(String username, String password){
        boolean response = false;
        int userID = Server.userEmailConfirmed(username, Hash.hashString(password));

        Log.d("Authenticate Response: ", String.valueOf(userID));

        // attempt authentication, react to response type
        switch(userID) {
            case -1:
                showError(R.string.email_notif);
                break;
            case 0:
                showError(R.string.invalid);
                break;
            case -9:
                showError(R.string.error);
                break;
            default:
                user = new User(userID);
                response = true;
        }

        return response;
    }


    // Function to display errors
    private void showError(int errorType) {
        Snackbar.make(findViewById(R.id.coord_layout), errorType, Snackbar.LENGTH_LONG).show();
    }


    // Input filter to clear non-alphanumeric characters
    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (!Character.isLetterOrDigit(source.charAt(i)) && !Character.isSpaceChar(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }
    };


    @Override
    public void onBackPressed() {
        // Closes the app
        finish();
    }

}

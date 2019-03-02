package cs131.classbuddies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class CreateAccountActivity extends AppCompatActivity implements AppCompatCallback {
    CBApp       classbuddiesApp;
    EditText    textUsername;
    EditText    textPassword;
    EditText    textEmail;
    TextView    errorText;
    ImageView   account_create_next;
    ImageView   create_account_back;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        // Create page layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // get parent class reference
        classbuddiesApp = (CBApp) getApplicationContext();

        // UI handles
        textUsername        = findViewById(R.id.username);
        textPassword        = findViewById(R.id.password);
        textEmail           = findViewById(R.id.email);
        errorText           = findViewById(R.id.textError);
        account_create_next = findViewById(R.id.account_create_next);
        create_account_back = findViewById(R.id.create_account_back);



        // LISTENER: Create account button
        account_create_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = textUsername.getText().toString();
                String password = textPassword.getText().toString();
                String email    = textEmail.getText().toString().toLowerCase();

                if (!isEmailAddress(email)) {
                    errorText.setText(R.string.invalid_email);
                }
                else if (username.length() < 4) {
                    errorText.setText(R.string.user_too_short);
                }
                else if (password.length() < 6) {
                    errorText.setText(R.string.pass_too_short);
                }
                else {
                    errorText.setText(null);
                    String hashedPassword = Hash.hashString(password);

                    // Create Account.
                    int response = Server.userCreate(username, hashedPassword,email);
                    if (response == 0) {
                        // Store user data for verification purposes
                        User temp = new User();
                        temp.setName(username);
                        temp.setPWhash(hashedPassword);
                        classbuddiesApp.setUser(temp);

                        startActivity(new Intent(CreateAccountActivity.this, CreateAccountActivity2.class));
                        finish();
                    }
                    else {
                        int err = R.string.error;                           // Default error
                        if (response == -1) err = R.string.user_exists;     // User exists error
                        if (response == -2) err = R.string.email_exists;    // Email exists error
                         errorText.setText(err);

                        Log.d("USER CREATE ERROR", String.valueOf(response));
                    }
                }
            }
        });



        // LISTENER: Back button
        create_account_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to Login screen
                finish();
            }
        });
    }

    // Function that verifies email address format
    protected boolean isEmailAddress(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}




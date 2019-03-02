package cs131.classbuddies;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CreateAccountActivity2 extends AppCompatActivity {
    CBApp       classbuddiesApp;
    // UI
    ImageView   backButton;
    ImageView   nextButton;
    TextView    lowerText;
    TextView    upperText;
    // Variables
    String      username;
    String      hashedPassword;


    // Create page layout
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        // Create page layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account2);

        // Get Application data
        classbuddiesApp = (CBApp) getApplicationContext();

        // UI Handles
        backButton = findViewById(R.id.back_button);
        nextButton = findViewById(R.id.next_button);
        lowerText = findViewById(R.id.warnSpamBox);
        upperText = findViewById(R.id.check_email_text);

        // Retrieve data from last page, remove partial user class from global CBApp
        User temp = classbuddiesApp.getUser();
        username = temp.getName();
        hashedPassword = temp.getPWhash();
        classbuddiesApp.setUser(null);

        // Fade Next button until email is activated
        nextButton.setAlpha(0.5f);


        // Check for email confirmation timer. Checks every 3 seconds.
        new CountDownTimer(1000000, 3000) {
            @Override
            public void onTick(long l) {
                int response = Server.userEmailConfirmed(username, hashedPassword);
                if (response > 1) {
                    // The email account is authorized
                    this.cancel();
                    accountAuthorized();
                }
            }

            @Override
            public void onFinish() {
                // Restart timer if timer runs out
                this.start();
            }
        }.start();



        // LISTENER: Back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateAccountActivity2.this, CreateAccountActivity.class));
                finish();
            }
        });

    }


    // Function to reconfigure UI when email is confirmed.
    private void accountAuthorized(){
        // Remove Back button, change text
        backButton.setVisibility(View.INVISIBLE);
        upperText.setText(R.string.email_verified);
        lowerText.setText(null);

        // Make Next button clickable
        nextButton.setAlpha(1.0f);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateAccountActivity2.this, LogInActivity.class));
                finish();
            }
        });
    }
}

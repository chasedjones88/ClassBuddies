package cs131.classbuddies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class CreateProfileActivity1 extends AppCompatActivity
{
    ImageView buttonYes;
    ImageView buttonNo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Create page layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile1);


        // Get UI Handles
        buttonYes = findViewById(R.id.button_yes);
        buttonNo = findViewById(R.id.button_no);


        // LISTENER: Yes button
        buttonYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(CreateProfileActivity1.this, CreateProfileActivity2.class));
                finish();
            }
        });


        // LISTENER: No button
        buttonNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(CreateProfileActivity1.this, MapActivity.class));
                finish();
            }
        });

    }
}

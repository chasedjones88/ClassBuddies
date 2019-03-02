package cs131.classbuddies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;


public class AboutActivity extends AppCompatActivity {
    CBApp classbuddiesApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_about);
        classbuddiesApp = (CBApp) getApplicationContext();


        // LISTENER: Menu icon
        ImageView menuIcon = findViewById(R.id.menu_bars);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classbuddiesApp.setLastActivity(AboutActivity.class);
                Intent intent = new Intent(AboutActivity.this, OptionsPanelActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AboutActivity.this, classbuddiesApp.getLastActivity());
        startActivity(intent);
        finish();
    }

}


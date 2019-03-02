package cs131.classbuddies;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;


public class MeetUpActivity extends AppCompatActivity {
    CBApp       classbuddiesApp;
    Context     mContext;

    // UI elements
    EditText    eventName, className, locationDesc, groupDesc, startHr, startMin, endHr, endMin;
    Spinner     spinMonth, spinDay, spinYear, spinStart12hr, spinEnd12hr;
    Button      nextButton, cancelButton;

    // Variables
    Date        dateStart = null, dateEnd = null;
    String      strName, strClass, strDesc, strStart, strEnd, strDate, strLocation;
    Double      latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create page layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meet);

        // Get application variables
        classbuddiesApp = (CBApp) getApplicationContext();
        mContext        = this.getApplicationContext();


        // Create handles for necessary UI elements
        eventName       = findViewById(R.id.editTextName);
        className       = findViewById(R.id.editTextClass);
        startHr         = findViewById(R.id.editTextStartHr);
        startMin        = findViewById(R.id.editTextStartMin);
        endHr           = findViewById(R.id.editTextEndHr);
        endMin          = findViewById(R.id.editTextEndMin);
        locationDesc    = findViewById(R.id.editTextLocationDesc);
        groupDesc       = findViewById(R.id.editTextDescription);
        spinMonth       = findViewById(R.id.spinMonth);
        spinDay         = findViewById(R.id.spinDay);
        spinYear        = findViewById(R.id.spinYear);
        spinStart12hr   = findViewById(R.id.spinStart12Hr);
        spinEnd12hr     = findViewById(R.id.spinEnd12Hr);
        nextButton      = findViewById(R.id.buttonPickLocation);
        cancelButton    = findViewById(R.id.buttonCancel);



        // Check for Android Location permission. Deactivate button if false.
        nextButton.setEnabled(checkLocationPermission());


        // Get current time-date values
        Calendar now     = Calendar.getInstance();
        int currentMonth = now.get(Calendar.MONTH);
        int currentDay   = now.get(Calendar.DAY_OF_MONTH);
        int currentYear  = now.get(Calendar.YEAR);
        int currentHour  = now.get(Calendar.HOUR_OF_DAY);
        int currentMin   = now.get(Calendar.MINUTE);
        int current12hr  = now.get(Calendar.AM_PM);

        ArrayAdapter<String> spinnerArrayAdapter;

        // UPDATE MONTH SPINNER: Limit to 3 months
        String[] months     = getResources().getStringArray(R.array.month_array);
        String[] useMonths  = new String[3];

        for (int i=0;i<3;i++) {
            useMonths[i] = months[( (currentMonth+i) % 12 )];
        }
        spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, useMonths);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMonth.setAdapter(spinnerArrayAdapter);


        // UPDATE YEAR SPINNER: Limit to current year, or next year depending on month (does current month + 2 go into next year?)
        String[] displayYears;
        if (  (currentMonth==11) || (currentMonth==12) ) {
            displayYears = new String[]{ String.valueOf(currentYear), String.valueOf(currentYear + 1) };
        }
        else {
            displayYears = new String[]{ String.valueOf(currentYear) };
        }
        spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, displayYears);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinYear.setAdapter(spinnerArrayAdapter);


        // SET DATE VALUES
        int startHour = ((currentHour % 12) == 0) ? 12 : currentHour % 12;               // Swap 24hr format for 12hr
        int endHour   = (startHour == 12) ? 1 : startHour + 1;                           // End hour = startHour + 1 in 12hr format
        int end12Hour = (endHour == 12) ? (current12hr + 1) % 1 : current12hr;           // Flip AM/PM selection if necessary (AM=0, PM=1)

        startHr.setText(String.valueOf(startHour));
        startMin.setText(String.format(Locale.US, "%02d", currentMin));
        spinStart12hr.setSelection(current12hr);

        endHr.setText(String.valueOf(endHour));
        endMin.setText(String.format(Locale.US, "%02d", currentMin));
        spinEnd12hr.setSelection(end12Hour);

        // Month already selected at index 0 on spinner
        spinDay.setSelection( currentDay - 1 );                                          // current day with 0 index on spinner
        // Year already selected at index 0 on spinner



        // LISTENER: Next button
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // List of encountered errors
                LinkedList<Integer> errorList = new LinkedList<>();


                // Get values from UI
                strName         = eventName.getText().toString();
                strClass        = className.getText().toString();
                strDesc         = groupDesc.getText().toString();
                strLocation     = locationDesc.getText().toString();


                // Require Values
                if (strName.length() == 0)  errorList.add(R.string.errEventName);
                if (strClass.length() == 0) errorList.add(R.string.errEventClass);
                if (strLocation.length() == 0) errorList.add(R.string.errEventLocation);
                if (strDesc.length() == 0)  errorList.add(R.string.errEventDesc);


                // Set date as formatted string
                strDate = spinMonth.getSelectedItem().toString() + " " +
                            spinDay.getSelectedItem().toString() + ", " +
                           spinYear.getSelectedItem().toString();               // Formatted Date: LLL dd, yyyy



                // Change date format to yyyy-mm-dd, errors if out of date range (ex: Nov 31st doesn't exist w/ 30 days in Nov)
                try {
                    SimpleDateFormat selectedDate = new SimpleDateFormat("LLL dd, yyyy", Locale.US);
                    selectedDate.setLenient(false);
                    SimpleDateFormat dateConversion = new SimpleDateFormat ("yyyy-MM-dd", Locale.US);
                    dateConversion.setLenient(false);
                    strDate = dateConversion.format(selectedDate.parse(strDate));
                } catch (ParseException e) {
                    Log.d("Date Conversion Error", e.toString());
                    errorList.add(R.string.invalidDate);
                } catch (IllegalArgumentException e) {
                    Log.d("Illegal Argument", e.toString());
                    errorList.add(R.string.invalidDate);
                }


                // Check for valid hours [1-12]
                int sHour = Integer.parseInt(startHr.getText().toString());
                int eHour = Integer.parseInt(endHr.getText().toString());

                if ( (sHour < 1) || (eHour < 1) || (sHour > 12) || (eHour > 12) ) {
                    errorList.add(R.string.invalidHour);
                }


                // Check for valid minutes [0-59]
                int sMin = Integer.parseInt(startMin.getText().toString());
                int eMin = Integer.parseInt(endMin.getText().toString());

                if ( (sMin < 0) || (eMin < 0) || (sMin > 59) || (eMin > 59) ) {
                    errorList.add(R.string.invalidMin);
                }


                // Set start and end times to strings
                strStart = startHr.getText().toString() + ":" +
                          startMin.getText().toString() + " " +
                     spinStart12hr.getSelectedItem().toString();        // Formatted Start time ( hh:mm aa)

                strEnd =     endHr.getText().toString() + ":" +
                            endMin.getText().toString() + " " +
                       spinEnd12hr.getSelectedItem().toString();          // Formatted End time ( hh:mm aa )



                // Set start and end dates to Date objects
                try {
                    dateStart = new SimpleDateFormat ("yyyy-MM-dd hh:mm aa", Locale.US).parse(strDate + " " + strStart);
                    dateEnd   = new SimpleDateFormat ("yyyy-MM-dd hh:mm aa", Locale.US).parse(strDate + " " + strEnd);
                } catch (ParseException e) {
                    // Something unexpected went wrong.
                    Log.d("Date Conversion Error", e.toString());
                    errorList.add(R.string.error);
                }

                Calendar cal = Calendar.getInstance();

                // If the times ( hh:mm ) are equal pull back end time by 1 minute. ( Maximum 24 hours )
                if (dateEnd.equals(dateStart)) {
                    cal.setTime(dateEnd);
                    cal.add(Calendar.MINUTE, -1);
                    dateEnd = cal.getTime();
                }

                // If end time falls on next day, end date must be incremented to next day.
                if (dateEnd.before(dateStart)) {
                    cal.setTime (dateEnd);
                    cal.add (Calendar.DATE, 1);
                    dateEnd = cal.getTime();
                }

                // If end time has already occurred, flag Back to the Future
                if (dateEnd.compareTo(new Date()) < 0) {
                    errorList.add(R.string.backToTheFuture);
                }


                if (errorList.isEmpty()) {
                    startActivityForResult(new Intent(MeetUpActivity.this, MeetUpMapActivity.class), 0);
                }
                else {
                    while (!errorList.isEmpty()) {
                        // Display all errors
                        showError(errorList.remove());
                    }
                }
            }
        });


        // LISTENER: Cancel Button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MeetUpActivity.this, MapActivity.class));
                finish();

            }
        });


        // LISTENER: Menu button
        final ImageView menuButton = findViewById(R.id.threebar_options);
        menuButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                classbuddiesApp.setLastActivity(MeetUpActivity.class);
                startActivity(new Intent(MeetUpActivity.this, OptionsPanelActivity.class));
                finish();
            }
        });

    }


    // Shows error using R.string.[name]
    private void showError(int errorType) {
        Toast.makeText(classbuddiesApp, errorType, Toast.LENGTH_SHORT).show();
    }


    // MANAGE ANDROID LOCATION PERMISSIONS
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MeetUpActivity.this, R.style.myDialog));
                builder.setTitle("Can we access your location?")
                        .setMessage("This helps us show you nearby study groups, and allows you to create new groups. You can always turn it on by opening this screen again later.")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MeetUpActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { } // Do nothing. No permission granted.
                        })
                        .create()
                        .show();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    // Results from the meetup location selection activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode==RESULT_OK) {

            // Retrieve map coordinates from previous activity
            LatLng lat_lng = classbuddiesApp.getLatLng();
            latitude = lat_lng.latitude;
            longitude = lat_lng.longitude;


            // If returned location is valid, process group creation
            if ((latitude >= 38.553234 && latitude <= 38.566321) && (longitude >= -121.431466 && longitude <= -121.417819)) {
                int groupID = Server.groupCreate(strName, strClass, strDesc, strLocation, latitude, longitude, classbuddiesApp.getUser().getUserID(), dateStart, dateEnd);
                // If group creation is successful, add creator to group, refresh application values, and load group description activity
                if (groupID > 1) {
                    Server.addUserToGroup(classbuddiesApp.getUser().getUserID(), groupID);
                    classbuddiesApp.setUser(new User(classbuddiesApp.getUser().getUserID()));
                    classbuddiesApp.setGroup(Server.getGroupByID(groupID));
                    startActivity(new Intent(MeetUpActivity.this, MeetUpDescription.class));
                    finish();
                }
                else {
                    // Group creation was unsuccessful
                    showError(R.string.error);
                }

            } else {
                // ERROR: Coordinates provided were out of map bounds
                new AlertDialog.Builder(mContext)
                        .setTitle("Error with Location")
                        .setMessage("We could not get your location, or you are outside school bounds. Please try again.")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MeetUpActivity.this, MapActivity.class);
        startActivity(intent);
        finish();
    }

}




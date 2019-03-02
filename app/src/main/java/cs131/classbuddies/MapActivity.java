package cs131.classbuddies;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    CBApp classbuddiesApp;
    User user;

    private GoogleMap mMap;
    private HashMap<Marker, Integer> markerList = new HashMap<> ( );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_map);

        classbuddiesApp = (CBApp) getApplicationContext();
        user = classbuddiesApp.getUser();


        // reset Application level variables on "home" screen
        classbuddiesApp.resetVariables();


        // Get Map
        if (mMap == null) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById (R.id.map);
            mapFragment.getMapAsync (this);
        }

        // Handle Android Permissions for location data
        checkLocationPermission();

        // Create listener for menu button
        final Button menuButton = findViewById (R.id.menu_button);
        menuButton.setOnClickListener (new View.OnClickListener ( ) {
            public void onClick(View v) {
                classbuddiesApp.setLastActivity(MapActivity.class);
                startActivity (new Intent (MapActivity.this, OptionsPanelActivity.class));
                finish();
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Set bounds for map
        LatLng upperLeftBound = new LatLng (38.553234, -121.431466);
        LatLng lowerRightBound = new LatLng (38.566321, -121.417819);
        LatLngBounds sacStateBounds = new LatLngBounds (upperLeftBound, lowerRightBound);

        mMap.setLatLngBoundsForCameraTarget (sacStateBounds);
        mMap.setMinZoomPreference (15.25f);
        mMap.moveCamera (CameraUpdateFactory.newLatLngZoom (new LatLng (38.5597775, -121.4246425), 15.25f));


        // Populate Map with Groups
        placeMapMarkers();

        mMap.setOnInfoWindowClickListener (new GoogleMap.OnInfoWindowClickListener ( ) {
            @Override
            public void onInfoWindowClick(Marker marker) {
                int groupID = markerList.get (marker);
                Group selected = Server.getGroupByID (groupID);
                classbuddiesApp.setGroup (selected);
                startActivity (new Intent (MapActivity.this, MeetUpDescription.class));
                finish();
            }
        });
    }


    private void placeMapMarkers(){

        mMap.clear();

        Group[] groupList = Server.getAllGroups ( );

        if (groupList != null) {
            for (Group x : groupList) {

                // Get group description and truncate to 23 chars if necessary
                String groupName = x.getGroupName();
                if (groupName.length() > 23) {
                    groupName = groupName.substring (0, 20) + "...";
                }


                // If user belongs to this group, change marker color to blue;
                float markerColor = BitmapDescriptorFactory.HUE_RED;

                int[] userGroups = user.getGroups ( );
                for (int g : userGroups) {
                    if (g == x.getID()) {
                        markerColor = BitmapDescriptorFactory.HUE_BLUE;
                    }
                }


                // Place pin on map
                LatLng pinPosition = new LatLng (x.getCoordLat(), x.getCoordLong());
                MarkerOptions options = new MarkerOptions().position (pinPosition);

                options.title(groupName);

                options.icon (BitmapDescriptorFactory.defaultMarker (markerColor));
                Marker thisMarker = mMap.addMarker (options);
                markerList.put (thisMarker, x.getID());
            }
        }
    }


    // Handle Android App Permissions for Location Data
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale (this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder builder = new AlertDialog.Builder (new ContextThemeWrapper (MapActivity.this, R.style.myDialog));
                builder.setTitle ("Can we access your location?")
                        .setMessage ("This helps us show you nearby study groups, and allows you to create new groups. You can always turn it on by opening the Meet Up screen later.")
                        .setPositiveButton ("Okay", new DialogInterface.OnClickListener ( ) {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions (MapActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .setNegativeButton ("No", new DialogInterface.OnClickListener ( ) {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            } // Do nothing. No permission granted.
                        })
                        .create()
                        .show();
            }
            else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions (this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void onRestart() {
        // Invokes map redraw on activity restart
        super.onRestart();
        placeMapMarkers();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MapActivity.this, R.style.myDialog));

        builder.setTitle("This action will log you out.");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Close dialog and finish() to Login page.
                dialog.dismiss();
                finish();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

}
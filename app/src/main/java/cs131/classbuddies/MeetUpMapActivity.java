package cs131.classbuddies;

import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MeetUpMapActivity extends FragmentActivity implements OnMapReadyCallback{
    private CBApp         classbuddiesApp;
    private Marker        marker;
    private LatLngBounds  sacStateBounds;

    private GoogleMap     mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_up_map);
        classbuddiesApp = (CBApp) getApplicationContext();


        // Set Sac State Map Bounds
        LatLng upperLeftBound = new LatLng(38.553234, -121.431466);
        LatLng lowerRightBound = new LatLng(38.566321, -121.417819);
        sacStateBounds = new LatLngBounds(upperLeftBound, lowerRightBound);


        // Get Map
        if (mMap == null) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MeetUpMapActivity.this, R.style.myDialog));
        builder.setTitle("Where will the event be held?")
                .setMessage("Tap to set the event location.")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create()
                .show();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setLatLngBoundsForCameraTarget(sacStateBounds);
        mMap.setMinZoomPreference(15.25f);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(38.5597775, -121.4246425), 15.25f));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                final LatLng lat_lng_obj = latLng;

                // Add marker to map, move camera to just below marker
                double lat = latLng.latitude;
                double lng = latLng.longitude;

                if (sacStateBounds.contains(new LatLng(lat, lng))){

                    marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .alpha(1.0f)
                    );

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(lat-(0.0004), lng))
                            .zoom(18)
                            .tilt(mMap.getCameraPosition().tilt)
                            .bearing(mMap.getCameraPosition().bearing)
                            .build();

                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                    // Prompt user if position is okay.
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MeetUpMapActivity.this, R.style.myDialog));
                    builder.setTitle("Is this spot okay?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    classbuddiesApp.setLatLng(lat_lng_obj);
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    marker.remove();
                                }
                            })
                            .create()
                            .show();
                }
                else {
                    // Map marker position not within Sac State bounds
                    showError(R.string.MapOOB);
                }
            }
        });

    }


    private void showError(int errorType) {
        Toast.makeText(classbuddiesApp, errorType, Toast.LENGTH_SHORT).show();
    }

}

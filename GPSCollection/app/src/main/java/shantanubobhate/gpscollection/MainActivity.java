package shantanubobhate.gpscollection;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private TextView textViewLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private static final int LARGE_INTERVAL = 60000;    // 60 seconds
    private static final int SMALL_INTERVAL = 15000;    // 15 seconds

    private FusedLocationProviderApi mFusedLocationProvider = LocationServices.FusedLocationApi;

    private Double mLatitude;
    private Double mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewLocation = (TextView) findViewById(R.id.textViewLocation);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)           // Want to call the location-based services...
                // ... api from google play services
                .addConnectionCallbacks(this)           // Methods for Connection Callbacks
                .addOnConnectionFailedListener(this)    // Listener for Connection Failure
                .build();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LARGE_INTERVAL);
        mLocationRequest.setFastestInterval(SMALL_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY); // Can decide option
    }

    // Function I defined
    private void requestLocationUpdates() {
        // Need to check if permissions have been accepted by the user
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    /* Functions that need to be implemented for Connection Callbacks */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        requestLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    /* ****************************************************************/

    /* Function that needs to be implemented for On Connection Listener */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed.", Toast.LENGTH_SHORT).show();
        // mGoogleApiClient.connect();
    }
    /* ******************************************************************/

    /* Function that needs to be implemented for Location Listener */
    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, "Location Changed", Toast.LENGTH_SHORT).show();
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        textViewLocation.setText("Works");
        textViewLocation.setText(String.valueOf(mLatitude) + " : " + String.valueOf(mLongitude));
    }
    /* *************************************************************/

    /* Need to manage the API connections over the lifecycle of the App to conserve battery */
    @Override
    protected void onStart() {
        super.onStart();
        // Connect the Client
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start Location Updates
        if (mGoogleApiClient.isConnected()) {
            requestLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Suspend Location Services
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Disconnect the Client
        mGoogleApiClient.disconnect();
    }
    /* **************************************************************************************/
}

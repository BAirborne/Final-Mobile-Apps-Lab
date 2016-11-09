package farrowc.gpsrunner;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    final static int MY_PERMISSIONS_REQUEST_READ_LOCATION = 1;
    private LocationManager locationManager;
    Marker marker;
    LatLng defaultPos;

    private String bestProvider = null;

    private final android.location.LocationListener locationListener = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            String str = "Lat:" + latitude + "\nLong:" + longitude;
            Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
            // locationManager.removeUpdates(MapsActivity.this);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            String str = "status changed to " + status + "!";
            Toast.makeText(MainActivity.this, provider + str, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            String str = "Provider " + provider + " enabled!";
            Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            String str = "Provider " + provider + " disabled!";
            Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //	obtain	reference	to	LocationManager
        String svcName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(svcName);
        //	Set	location	criteria
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);



        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                String str = "Explanation needed: Please I need to determine your location";
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_LOCATION);
                String str = "No explanation needed: thanks.";
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            }
        }

        Location bestLocation = null;

        try
        {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // getting GPS status
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            Log.d("GPS", "GPS is enabled - " + isGPSEnabled);

            // getting network status
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Log.d("Network", "Network is enabled - " + isGPSEnabled);

            if (!isGPSEnabled && !isNetworkEnabled)
            {
                Log.d("Provider", "Network is enabled - " + isGPSEnabled);
            }
            else
            {
                boolean canGetLocation = true;
                if (isNetworkEnabled)
                {
                    bestProvider = LocationManager.NETWORK_PROVIDER;
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, (long) 100, 4f, locationListener);
                    Log.d("Provider", "Starting Network listener");
                    if (locationManager != null)
                    {
                        bestLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (bestLocation != null)
                        {
                            defaultPos = new LatLng(bestLocation.getLatitude(), bestLocation.getLongitude());
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled)
                {
                    if (bestLocation == null)
                    {
                        bestProvider = LocationManager.GPS_PROVIDER;
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, (long) 100, 4f, locationListener);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null)
                        {
                            bestLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (bestLocation != null)
                            {
                                defaultPos = new LatLng(bestLocation.getLatitude(), bestLocation.getLongitude());
                            }
                        }
                    }
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                String str = "Explanation needed: Please I need to determine your location";
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_LOCATION);
                String str = "No explanation needed: thanks.";
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                String str = "Explanation needed: Please I need to determine your location";
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_LOCATION);
                String str = "No explanation needed: thanks.";
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            }
        }

        locationManager.requestLocationUpdates(bestProvider, (long) 2000, 10f, locationListener);
    }

    public void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                String str = "Explanation needed: Please I need to determine your location";
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_LOCATION);
                String str = "No explanation needed: thanks.";
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            }
        }

        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setIndoorEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);

        if (defaultPos != null) {
            mMap.addMarker(new MarkerOptions().position(defaultPos).title("Default Position"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultPos));
        }
    }
}

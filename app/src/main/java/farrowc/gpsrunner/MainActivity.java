package farrowc.gpsrunner;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
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
    Location finish;
    Location lastLocation;
    Marker finishMarker;
    double distanceToTravel;
    double distanceTravelled;
    long startTime;

    private String bestProvider = null;

    private final android.location.LocationListener locationListener = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (mMap != null) {
                marker.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude())));
            }

            if (finish != null) {
                finish.setAltitude(location.getAltitude());
                distanceTravelled += lastLocation.distanceTo(location);
                if (location.distanceTo(finish) < 10) {
                    finish = null;
                    finishMarker.remove();
                    Intent intent = new Intent(MainActivity.this,LocationReachedActivity.class);
                    intent.putExtra("MinimumDistance",distanceToTravel);
                    intent.putExtra("DistanceTravelled",distanceTravelled);
                    intent.putExtra("Time",(System.nanoTime()-startTime)/1000);
                    //Toast.makeText(MainActivity.this, "You Won", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }
            // locationManager.removeUpdates(MapsActivity.this);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
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
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                String str = "Explanation needed: Please I need to determine your location";
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_LOCATION);
                String str = "No explanation needed: thanks.";
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            }
        }

        //	Obtain	reference	to	location	provider
        List<String> providers = locationManager.getProviders(true);
        bestProvider = providers.get(1);
        Location l = locationManager.getLastKnownLocation(bestProvider);
        Location bestLocation = l;
        lastLocation = l;
        locationManager.requestLocationUpdates(bestProvider, (long) 100, 4f, locationListener);

        if (bestLocation != null) {
            defaultPos = new LatLng(bestLocation.getLatitude(), bestLocation.getLongitude());
            Log.d("Location", "Using previous location");
        } else {
            locationManager.requestSingleUpdate(bestProvider, locationListener, Looper.myLooper());
            Log.d("Location", "Requesting new location");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                String str = "Explanation needed: Please I need to determine your location";
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_LOCATION);
                String str = "No explanation needed: thanks.";
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            }
        }

        locationManager.requestLocationUpdates(bestProvider, (long) 100, 4f, locationListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                String str = "Explanation needed: Please I need to determine your location";
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
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

        if (defaultPos != null) {
            marker = mMap.addMarker(new MarkerOptions().position(defaultPos).title("Player"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultPos));
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                if (finishMarker == null) {
                    finishMarker = mMap.addMarker(new MarkerOptions().position(point).title("Finish"));
                    finish = new Location(bestProvider);
                    finish.setLatitude(point.latitude);
                    finish.setLongitude(point.longitude);

                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                            String str = "Explanation needed: Please I need to determine your location";
                            Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                        } else {
                            // No explanation needed, we can request the permission.
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_READ_LOCATION);
                            String str = "No explanation needed: thanks.";
                            Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                        }
                    }

                    finish.setAltitude(locationManager.getLastKnownLocation(bestProvider).getAltitude());
                    distanceToTravel = locationManager.getLastKnownLocation(bestProvider).distanceTo(finish);
                    distanceTravelled = 0;
                    startTime = System.nanoTime();
                }else{
                    finishMarker.setPosition(point);
                    finish.setLatitude(point.latitude);
                    finish.setLongitude(point.longitude);
                }
            }
        });
    }
}

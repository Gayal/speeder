package test.siaxe.speedeo;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.github.anastr.speedviewlib.SpeedView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.tbruyelle.rxpermissions2.RxPermissions;

import static test.siaxe.speedeo.util.DATA_VALUE_CHANGING;
import static test.siaxe.speedeo.util.LATITUDE;
import static test.siaxe.speedeo.util.LONGITUDE;
import static test.siaxe.speedeo.util.SPEED;

public class Speedmeteractivity extends BaseActivity {

    private static final String TAG = Speedmeteractivity.class.getSimpleName();

    private LocationManager locationManager;
    private android.location.LocationListener myLocationListener;
    private boolean isFirstTime = true;
    private Location myLocation;
    TextView textViewSpeed, textViewkmh;

    private FirebaseAnalytics mFirebaseAnalytics;
    int speed;
    private SpeedView speedView;
    NotificationManager mNotificationManager;


    private static final String[] PERMISSIONS =
            {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speedmeteractivity);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        askLocationPermission();

        Typeface roborolight = Typeface.createFromAsset(this.getAssets(), "fonts/RobotoCondensed-Light.ttf");

        textViewSpeed = (TextView) findViewById(R.id.textViewSpeed);
        speedView = (SpeedView) findViewById(R.id.speedView);

        textViewSpeed.setTextSize(92);
        textViewSpeed.setTypeface(roborolight);

        textViewkmh = (TextView) findViewById(R.id.textViewkmh);
        textViewkmh.setTextSize(42);
        textViewkmh.setTypeface(roborolight);

        Button goNextButton = findViewById(R.id.button2);
        goNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Speedmeteractivity.this, DetailActivity.class));
            }
        });

    }

    private void askLocationPermission() {

        new RxPermissions(this)
                .request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        // All requested permissions are granted

//                        check location service
                        getCurrentLocation();


                    } else {
                        // At least one permission is denied

                        Toast.makeText(this, "Location Permission error", Toast.LENGTH_LONG).show();


                    }
                });

    }

    public void getCurrentLocation() {

        String serviceString = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(serviceString);

        myLocationListener = new android.location.LocationListener() {

            public void onLocationChanged(Location locationListener) {

                System.out.println("onLocationChanged " + locationListener.getLatitude() + "--> || -->" + locationListener.getLongitude());
                LONGITUDE = locationListener.getLongitude();
                LATITUDE = locationListener.getLatitude();



                try {
                    speedView.speedTo(Math.round(locationListener.getSpeed() * 18 / 5));
                    myLocation = locationListener;
                    SPEED = Math.round(locationListener.getSpeed() * 18 / 5);
                    sendBroadcastMeaasge(DATA_VALUE_CHANGING,String.valueOf(locationListener.getLongitude()),String.valueOf(locationListener.getLatitude()),String.valueOf(Math.round(locationListener.getSpeed() * 18 / 5)));
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            public void onProviderDisabled(String provider) {

            }

            public void onProviderEnabled(String provider) {
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            System.out.println("permission");
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 250, 0, myLocationListener);

    }

    private  void sendBroadcastMeaasge(String status, String longitude, String latitude,String speed) {
        Intent localIntent = new Intent("test.siaxe.speedeo");
        //localIntent.putExtra("fileKey", fileKey);
        localIntent.putExtra("result", status);
        localIntent.putExtra("longitude", longitude);
        localIntent.putExtra("latitude", latitude);
        localIntent.putExtra("speed", speed);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

}

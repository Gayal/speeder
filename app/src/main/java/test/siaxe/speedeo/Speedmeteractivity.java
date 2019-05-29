package test.siaxe.speedeo;

import android.Manifest;

import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.widget.TextViewCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

public class Speedmeteractivity extends BaseActivity {

    private LocationManager locationManager;
    private android.location.LocationListener myLocationListener;
    private boolean isFirstTime =true;
    private Location myLocation;
    TextView textViewSpeed,textViewkmh;
    int speed;
    NotificationManager mNotificationManager;

    private static final String[] PERMISSIONS =
            {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speedmeteractivity);

        askLocationPermission();

        Typeface roborolight = Typeface.createFromAsset(this.getAssets(), "fonts/RobotoCondensed-Light.ttf");

        textViewSpeed = (TextView) findViewById(R.id.textViewSpeed);

        textViewSpeed.setTextSize(92);
        textViewSpeed.setTypeface(roborolight);

        textViewkmh = (TextView) findViewById(R.id.textViewkmh);
        textViewkmh.setTextSize(42);
        textViewkmh.setTypeface(roborolight);

//        textViewSpeed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Speedmeteractivity.this,DisatanceMeterActivity.class));
//            }
//        });


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

                        Toast.makeText(this,"Location Permission error",Toast.LENGTH_LONG).show();


                    }
                });

    }


    public void getCurrentLocation() {

        String serviceString = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(serviceString);

        myLocationListener = new android.location.LocationListener() {

            public void onLocationChanged(Location locationListener) {

                System.out.println("onLocationChanged " + locationListener.getLatitude() + "--> || -->" + locationListener.getLongitude());

                textViewSpeed.setText(""+Math.round(locationListener.getSpeed()*18/5));
                myLocation = locationListener;

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
        }else{
            System.out.println("permission");
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 250, 0, myLocationListener);

    }
}

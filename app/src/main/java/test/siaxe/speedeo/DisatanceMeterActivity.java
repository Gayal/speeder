package test.siaxe.speedeo;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.TextViewCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;


public class DisatanceMeterActivity extends AppCompatActivity {

    LinearLayout.LayoutParams li;
    TextView textViewDuration;
    private LocationManager locationManager;
    private LocationRequest mLocationRequest;
    private android.location.LocationListener myLocationListener;
    float calculatedAllDistance = 0;
    long startTime = 0;
    float[] locationResults, locationResultsForNotify;
    float calculatedDistance;
    private LatLng driverLatLngNewPosition, driverLatLngOldPosition;
    TextView textViewDisatnce;
    Location location;
    private boolean isFirstTime = true;


    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            textViewDuration.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disatance_meter);

        setlayouts();

    }

    private void startDistanceMeter() {

        String serviceString = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(serviceString);

        myLocationListener = new android.location.LocationListener() {

            @Override
            public void onLocationChanged(Location locationListener) {

                driverLatLngNewPosition = new LatLng(locationListener.getLatitude(), locationListener.getLongitude());
                if (isFirstTime) {
                    driverLatLngOldPosition = new LatLng(locationListener.getLatitude(), locationListener.getLongitude());
                    isFirstTime = false;
                }
                calculatedAllDistance += calculateDistance(driverLatLngOldPosition, driverLatLngNewPosition);
                textViewDisatnce.setText(""+round(calculatedAllDistance,2));
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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 250, 0, myLocationListener);


    }

    public float calculateDistance(LatLng latLngOldPosition, LatLng latLngNewPosition) {

        //locationResults{} is result array that return correct distance
        locationResults = new float[1];

        try {

            Location.distanceBetween(latLngOldPosition.latitude, latLngOldPosition.longitude, latLngNewPosition.latitude, latLngNewPosition.longitude, locationResults);


            if (locationResults[0] > 1) {
                calculatedDistance = locationResults[0];
                driverLatLngOldPosition = latLngNewPosition;
            } else {
                calculatedDistance = 0;
            }

        } catch (IllegalArgumentException e) {

            System.out.println("calculateDistance exception" + e);
        }

        return calculatedDistance;
    }

    private void startCoutdown() {
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);

        startDistanceMeter();

    }

    private void stopCounddown() {
        timerHandler.removeCallbacks(timerRunnable);

    }




    private void setlayouts() {

        Typeface roborolight = Typeface.createFromAsset(this.getAssets(), "fonts/RobotoCondensed-Light.ttf");

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        UISetup.deviceHeight = displaymetrics.heightPixels;
        UISetup.deviceWidth = displaymetrics.widthPixels;


        textViewDuration = (TextView) findViewById(R.id.textView_duration);
        li = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        li.topMargin = UISetup.sizeBaseOnHeight(35);
        li.leftMargin = UISetup.sizeBaseOnWidth(950);
        textViewDuration.setTextSize(25);
        textViewDuration.setLayoutParams(li);
        textViewDuration.setTypeface(roborolight);

        textViewDisatnce = (TextView) findViewById(R.id.textView_disatnce);
        li = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        li.topMargin = UISetup.sizeBaseOnHeight(650);
        li.gravity = Gravity.CENTER;
        textViewDisatnce.setLayoutParams(li);
        textViewDisatnce.setTypeface(roborolight);
        textViewDisatnce.setTextSize(92);

        LinearLayout linearBottomButton = (LinearLayout) findViewById(R.id.linear_bottomButton);

        Button buttonStart = (Button) findViewById(R.id.buttonStart);
        li = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        li.topMargin = UISetup.sizeBaseOnHeight(400);
        li.leftMargin = UISetup.sizeBaseOnWidth(100);
        buttonStart.setLayoutParams(li);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCoutdown();

            }
        });

        Button buttonStop = (Button) findViewById(R.id.buttonStop);
        li = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        li.topMargin = UISetup.sizeBaseOnHeight(400);
        li.leftMargin = UISetup.sizeBaseOnWidth(400);
        buttonStop.setLayoutParams(li);
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopCounddown();
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}

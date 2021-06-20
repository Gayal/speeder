package test.siaxe.speedeo;

import android.content.ContentResolver;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {

    private long SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_splash);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        startActivity(new Intent(SplashActivity.this,Speedmeteractivity.class));
        finish();




       /* new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if(checkLocationStatus()){
                    startActivity(new Intent(SplashActivity.this,Speedmeteractivity.class));
                    finish();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                    builder.setMessage("No location service. \ndo you want go to settings to enable location?")
                            .setCancelable(false)
                            .setTitle("Location service error!")
                            .setNegativeButton("NO",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Toast.makeText(SplashActivity.this, "Exit", Toast.LENGTH_SHORT).show();
                                           System .exit(0);
                                        }
                                    })
                            .setPositiveButton("YES",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            startActivity(myIntent);
                                        }
                                    });


                    builder.show();
                }
            }
        }, SPLASH_TIME_OUT);*/



    }



    private Boolean checkLocationStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver,
                LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }

}

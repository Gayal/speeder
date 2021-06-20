package test.siaxe.speedeo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import static test.siaxe.speedeo.util.DATA_VALUE_CHANGING;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();
    AdView adView;
    private InterstitialAd mInterstitialAd;
    private boolean isAddClosed = false;

    private BroadcastReceiver myLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String result = intent.getStringExtra("result");
            String longitude = intent.getStringExtra("longitude");
            String latitude = intent.getStringExtra("latitude");
            String speed = intent.getStringExtra("speed");

            if(result.equals(DATA_VALUE_CHANGING)){
                ((TextView) findViewById(R.id.textView5)).setText(String.valueOf(speed)+" km/h");
                ((TextView) findViewById(R.id.textView6)).setText(String.valueOf(longitude));
                ((TextView) findViewById(R.id.textView7)).setText(String.valueOf(latitude));
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        adView = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                mInterstitialAd = null;
                isAddClosed=true;

                Log.d(TAG, "onAdDismissedFullScreenContent: ");

            }
        };

//       test --> ca-app-pub-3940256099942544/1033173712
//       original --> ca-app-pub-8437553700245733/1755515087
        InterstitialAd.load(this, "ca-app-pub-8437553700245733/1755515087",
                adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                interstitialAd.setFullScreenContentCallback(fullScreenContentCallback);
                Log.i(TAG, "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i(TAG, loadAdError.getMessage());
                mInterstitialAd = null;
                isAddClosed = true;
                Log.i(TAG, "onAdFailedToLoad");
            }
        });

    }

    @Override
    public void onBackPressed() {

        if (mInterstitialAd != null) {
            if (isAddClosed) {
                super.onBackPressed();
            } else {
                mInterstitialAd.show(DetailActivity.this);
            }

        } else {
            super.onBackPressed();
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter("test.siaxe.speedeo");
        LocalBroadcastManager.getInstance(this).registerReceiver(myLocalBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myLocalBroadcastReceiver);
    }
}
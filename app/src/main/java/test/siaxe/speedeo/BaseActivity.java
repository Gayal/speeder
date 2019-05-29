package test.siaxe.speedeo;

import android.location.Address;
import android.location.Location;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;
import com.patloew.rxlocation.RxLocation;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();


    private RxLocation rxLocation;
    private LocationRequest locationRequest;

    private Disposable realTimeLocationDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createLocationRexSettings();
    }

    private void createLocationRexSettings() {

        rxLocation = new RxLocation(this);

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000); // 1 second


    }


    private void startLocationRefresh() {

//        u need to check this if this is not going to dispose in Onpause()
        /*if(realTimeLocationDisposable != null){

        }*/

        realTimeLocationDisposable = rxLocation.settings().checkAndHandleResolution(locationRequest)
                .flatMapObservable(this::getAddressObservable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onAddressUpdate, throwable -> Log.e("MainPresenter", "Error fetching location/address updates", throwable));

    }

    private void onAddressUpdate(Address address) {
    }


    private Observable<Address> getAddressObservable(boolean success) {
        if (success) {
            return rxLocation.location().updates(locationRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(this::onLocationUpdate)
                    .flatMap(this::getAddressFromLocation);

        } else {
            this.onLocationSettingsUnsuccessful();

            return rxLocation.location().lastLocation()
                    .doOnSuccess(this::onLocationUpdate)
                    .flatMapObservable(this::getAddressFromLocation);
        }
    }

    private void onLocationUpdate(Location location) {
    }

    private Observable<Address> getAddressFromLocation(Location location) {
        return rxLocation.geocoding().fromLocation(location).toObservable()
                .subscribeOn(Schedulers.io());
    }

    public void onLocationSettingsUnsuccessful() {

        Log.d(TAG, "onLocationSettingsUnsuccessful: ");
    }


    @Override
    protected void onPause() {
        super.onPause();

        safelyDispose(realTimeLocationDisposable);

    }


    @Override
    protected void onPostResume() {
        super.onPostResume();

        startLocationRefresh();
    }

    private void safelyDispose(Disposable... disposables) {
        for (Disposable subscription : disposables) {
            if (subscription != null && !subscription.isDisposed()) {
                subscription.dispose();
            }
        }


    }
}

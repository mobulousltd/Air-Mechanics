package mobulous12.airmechanics;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.iid.FirebaseInstanceId;

import mobulous12.airmechanics.addressfetcher.Constants;
import mobulous12.airmechanics.addressfetcher.GeocodeAddressIntentService;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.utils.NetworkConnectionCheck;

public class SplashTestAct extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,
        ResultCallback<LocationSettingsResult>
{
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest locationRequest;
    int REQUEST_CHECK_SETTINGS = 100;
    Location mLastLocation;

    private static long SPLASH_TIME_OUT = 3000;
    AddressResultReceiver mResultReceiver;
    private static final int REQUEST_CODE_LOCATION = 000;
    Handler handler = new Handler();
    Runnable runnable=new Runnable()
    {
        public void run(){
            if (!new NetworkConnectionCheck(getApplicationContext()).isConnect())
            {
                Toast.makeText(getApplicationContext(), "Internet Disconnected", Toast.LENGTH_SHORT).show();
                handler.postDelayed(this, 6000);
            }
            else
            {
                loadGoogleApi();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_test);
        handler.postDelayed(runnable, 1000);
    }

    private void loadHome()
    {
        Intent intent = new Intent(SplashTestAct.this, GeocodeAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.FETCH_TYPE_EXTRA, Constants.USE_ADDRESS_LOCATION);
        intent.putExtra(Constants.LOCATION_LATITUDE_DATA_EXTRA, Double.parseDouble(SharedPreferenceWriter.getInstance(SplashTestAct.this).getString(SPreferenceKey.LATITUDE)));
        intent.putExtra(Constants.LOCATION_LONGITUDE_DATA_EXTRA, Double.parseDouble(SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.LONGITUDE)));
        startService(intent);
        registerDeviceForToken();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = null;
                if (SharedPreferenceWriter.getInstance(SplashTestAct.this).getBoolean(SPreferenceKey.LOGINKEY))
                {
                    if (SharedPreferenceWriter.getInstance(getApplicationContext()).getBoolean(SPreferenceKey.SERVICE_PROVIDER_LOGIN))
                    {
                        intent = new Intent(SplashTestAct.this, HomeActivityServicePro.class);
                    }
                    else
                    {
                        intent = new Intent(SplashTestAct.this, HomeActivity.class);
                    }
                }
                else
                {
                    intent = new Intent(SplashTestAct.this, RoleSelectionActivity.class);
                }
                Log.e("Save Device Token", "-->" +SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.DEVICETOKEN));
                Log.e("Save Token", "-->" +SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.TOKEN));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    //getting device token
    private void registerDeviceForToken() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                SharedPreferenceWriter mPreference = SharedPreferenceWriter.getInstance(getApplicationContext());
                try {
                    if (mPreference.getString(SPreferenceKey.DEVICETOKEN).isEmpty())
                    {
                        String token = FirebaseInstanceId.getInstance().getToken();
                        Log.e("Generated Device Token", "-->" + token);
                        if (token == null)
                        {
                            registerDeviceForToken();
                        }
                        else
                        {
                            mPreference.writeStringValue(SPreferenceKey.DEVICETOKEN, token);
                        }
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                super.run();
            }
        };
        thread.start();
    }

    //method to load google api
    public void loadGoogleApi()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(SplashTestAct.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(SplashTestAct.this)
                .addOnConnectionFailedListener(SplashTestAct.this).build();
        mGoogleApiClient.connect();
        createLocationRequest();
        mResultReceiver = new AddressResultReceiver(null);
    }

    //method to create location request and set its priorities
    private void createLocationRequest()
    {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(5 * 1000);
    }
    private void loadLoaction()
    {
        if(checkrequestLocPermission())
        {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation == null)
            {
                createLocationRequest();
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, SplashTestAct.this);

            } else
            {
                Log.i("Load Location", ""+mLastLocation.getLatitude()+":"+mLastLocation.getLongitude());
                SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.LATITUDE, "" + mLastLocation.getLatitude());
                SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.LONGITUDE, "" + mLastLocation.getLongitude());
                loadHome();
            }
        }
        else
        {
            requestPermission();
        }
    }

    //Google api client connection start
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //initialize the pending result and locationRequest
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );

        result.setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (location != null)
        {
            Log.i("Location Changed", ""+mLastLocation.getLatitude()+":"+mLastLocation.getLongitude());
            SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.LATITUDE, "" + mLastLocation.getLatitude());
            SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.LONGITUDE, "" + mLastLocation.getLongitude());
        }
        else
        {
            loadGoogleApi();
            Toast.makeText(SplashTestAct.this, "Gps is still disabled.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if(mGoogleApiClient!=null)
        {
            mGoogleApiClient.connect();
        }
    }
    //Google api client connection ends


    //implement resultCallback
    // overridden method called OnResult for gps result
    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // NO need to show the dialog;
                loadLoaction();
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(SplashTestAct.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    //failed to show
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }



    //GPS permission start
    public boolean checkrequestLocPermission()
    {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    private void requestPermission()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION))
        {
            Toast.makeText(SplashTestAct.this, "Current Location needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // success!
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    return;
                }
                else
                {
                    mGoogleApiClient.connect();
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if(mLastLocation==null)
                    {
                        createLocationRequest();
                    }
                    else
                    {
                        Log.i("on request Location", ""+mLastLocation.getLatitude()+":"+mLastLocation.getLongitude());
                        SharedPreferenceWriter.getInstance(this.getApplicationContext()).writeStringValue(SPreferenceKey.LATITUDE, "" + mLastLocation.getLatitude());
                        SharedPreferenceWriter.getInstance(this.getApplicationContext()).writeStringValue(SPreferenceKey.LONGITUDE, "" + mLastLocation.getLongitude());
                        loadHome();
                    }
                }
            }
            else
            {
                requestPermission();
                Toast.makeText(SplashTestAct.this, "Location Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //GPS permission ends
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {

            if (resultCode == RESULT_OK) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        loadLoaction();
                        SPLASH_TIME_OUT=100;
                    }
                }, 3000);
                Toast.makeText(getApplicationContext(), "GPS is enabled now.", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(getApplicationContext(), "GPS is not enabled", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(runnable);
        finish();
    }

    //Address Fetcher
    class AddressResultReceiver extends ResultReceiver
    {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultCode == Constants.SUCCESS_RESULT) {
                final Address address = resultData.getParcelable(Constants.RESULT_ADDRESS);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferenceWriter.getInstance(SplashTestAct.this).writeStringValue(SPreferenceKey.ADDRESS, resultData.getString(Constants.RESULT_DATA_KEY));
                    }
                });
            }
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }
    }
}

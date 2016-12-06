package mobulous12.airmechanics;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.iid.FirebaseInstanceId;

import mobulous12.airmechanics.addressfetcher.Constants;
import mobulous12.airmechanics.addressfetcher.GeocodeAddressIntentService;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.utils.NetworkConnectionCheck;

public class SplashActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
{
    AddressResultReceiver mResultReceiver;
    private static final int REQUEST_CODE_LOCATION = 000;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest; AlertDialog.Builder builder;AlertDialog alert;
    Location mLastLocation;
    private static long SPLASH_TIME_OUT = 3000;
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
        setContentView(R.layout.activity_splash);
        if (!new NetworkConnectionCheck(getApplicationContext()).isConnect())
        {
            Toast.makeText(getApplicationContext(), "Internet Disconnected", Toast.LENGTH_SHORT).show();
            handler.postDelayed(runnable, 1000);
        }
        else
        {
            loadGoogleApi();
        }
    }
    public void loadGoogleApi()
    {
        if(!checkforgps())
        {
            buildAlertMessageNoGps();
        }
        else
        {
            if(mGoogleApiClient==null)
            {
                mGoogleApiClient = new GoogleApiClient.Builder(SplashActivity.this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(SplashActivity.this)
                        .addOnConnectionFailedListener(SplashActivity.this).build();
                createLocationRequest();
//            locationChecker(mGoogleApiClient, SplashActivity.this);
            }

            mResultReceiver = new AddressResultReceiver(null);
        }

    }
    public static void locationChecker(GoogleApiClient mGoogleApiClient, final Activity activity) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult( activity, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }
    private void loadHome()
    {
        Intent intent = new Intent(SplashActivity.this, GeocodeAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.FETCH_TYPE_EXTRA, Constants.USE_ADDRESS_LOCATION);
        intent.putExtra(Constants.LOCATION_LATITUDE_DATA_EXTRA, Double.parseDouble(SharedPreferenceWriter.getInstance(SplashActivity.this).getString(SPreferenceKey.LATITUDE)));
        intent.putExtra(Constants.LOCATION_LONGITUDE_DATA_EXTRA, Double.parseDouble(SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.LONGITUDE)));
        startService(intent);
        registerDeviceForToken();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = null;
                if (SharedPreferenceWriter.getInstance(SplashActivity.this).getBoolean(SPreferenceKey.LOGINKEY))
                {
                    if (SharedPreferenceWriter.getInstance(getApplicationContext()).getBoolean(SPreferenceKey.SERVICE_PROVIDER_LOGIN))
                    {
                        intent = new Intent(SplashActivity.this, HomeActivityServicePro.class);
                    }
                    else
                    {
                        intent = new Intent(SplashActivity.this, HomeActivity.class);
                    }
                }
                else
                {
                    intent = new Intent(SplashActivity.this, RoleSelectionActivity.class);
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
    private  boolean checkforgps()
    {
        LocationManager locationManager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
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


    //alert dialog for gps
    private void buildAlertMessageNoGps()
    {
        builder = new AlertDialog.Builder(SplashActivity.this);//Enable to getLocation
        builder.setMessage(getString(R.string.gpsdisabled))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.settings), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 01);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id)
                    {
                        finish();
                        dialog.cancel();
                    }
                });
        alert = builder.create();
        alert.show();
    }

    //location
    @Override
    protected void onStart() {
        super.onStart();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    protected void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(2000);
        Log.i("MainAct", "createLocationRequest");
    }
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
            Toast.makeText(SplashActivity.this, "Current Location needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_SHORT).show();
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
    public void onConnected(Bundle bundle) {
        Log.i("MainAct", "onConnected");
        if(checkrequestLocPermission())
        {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation == null)
            {
                createLocationRequest();
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, SplashActivity.this);

            } else
            {
                Log.i("Location", ""+mLastLocation.getLatitude()+":"+mLastLocation.getLongitude());
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

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (location != null)
        {
            Log.i("Location", ""+mLastLocation.getLatitude()+":"+mLastLocation.getLongitude());
            SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.LATITUDE, "" + mLastLocation.getLatitude());
            SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.LONGITUDE, "" + mLastLocation.getLongitude());
        } else {
            Toast.makeText(SplashActivity.this, "Gps is still disabled.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        if(mGoogleApiClient!=null)
        {
            mGoogleApiClient.connect();
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
                        Log.i("Location", ""+mLastLocation.getLatitude()+":"+mLastLocation.getLongitude());
                        SharedPreferenceWriter.getInstance(this.getApplicationContext()).writeStringValue(SPreferenceKey.LATITUDE, "" + mLastLocation.getLatitude());
                        SharedPreferenceWriter.getInstance(this.getApplicationContext()).writeStringValue(SPreferenceKey.LONGITUDE, "" + mLastLocation.getLongitude());
                        loadHome();
                    }

                }
            }
            else
            {
                requestPermission();
                Toast.makeText(SplashActivity.this, "Location Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadGoogleApi();

//        handler.postDelayed(runnable, 1000);
    }
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
                        SharedPreferenceWriter.getInstance(SplashActivity.this).writeStringValue(SPreferenceKey.ADDRESS, resultData.getString(Constants.RESULT_DATA_KEY));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(runnable);
        finish();
    }
}

package mobulous12.airmechanics.customer.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import mobulous12.airmechanics.addressfetcher.Constants;
import mobulous12.airmechanics.addressfetcher.GeocodeAddressIntentService;
import mobulous12.airmechanics.R;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;

public class AddressActivity extends AppCompatActivity implements  OnMapReadyCallback {

    GoogleMap googlemap;
    PlaceAutocompleteFragment autocompleteFragment;
    AddressResultReceiver mResultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_address);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.address_map);
        mapFragment.getMapAsync(this);
        autocompleteFragment = (PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.place_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
              addMarker(place.getLatLng(), ""+place.getAddress());
            }

            @Override
            public void onError(Status status) {

            }
        });
        autocompleteFragment.setHint("Find Location");
        mResultReceiver = new AddressResultReceiver(null);
    }

    private void addMarker(LatLng latLng, String address)
    {
        googlemap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(address);
        googlemap.addMarker(markerOptions);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(14)
                .build();
        googlemap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        googlemap = googleMap;
        if (googleMap != null)
        {
            googleMap.getMapType();
            if (ActivityCompat.checkSelfPermission(AddressActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(AddressActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                return;
            }
            googleMap.setMyLocationEnabled(true);
            LatLng latLng=new LatLng(Double.parseDouble(SharedPreferenceWriter.getInstance(AddressActivity.this).getString(SPreferenceKey.LATITUDE))
                    , Double.parseDouble(SharedPreferenceWriter.getInstance(AddressActivity.this).getString(SPreferenceKey.LONGITUDE)));
            addMarker(latLng, SharedPreferenceWriter.getInstance(AddressActivity.this).getString(SPreferenceKey.ADDRESS));
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng)
                {
                    Intent intent = new Intent(AddressActivity.this, GeocodeAddressIntentService.class);
                    intent.putExtra(Constants.RECEIVER, mResultReceiver);
                    intent.putExtra(Constants.FETCH_TYPE_EXTRA, Constants.USE_ADDRESS_LOCATION);
                    intent.putExtra(Constants.LOCATION_LATITUDE_DATA_EXTRA, latLng.latitude);
                    intent.putExtra(Constants.LOCATION_LONGITUDE_DATA_EXTRA, latLng.longitude);
                    startService(intent);
                }
            });
        }
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
                        LatLng latLng=new LatLng(address.getLatitude(), address.getLongitude());
                        addMarker(latLng, resultData.getString(Constants.RESULT_DATA_KEY));
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

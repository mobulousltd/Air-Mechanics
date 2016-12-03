package mobulous12.airmechanics;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;

public class MapActivityToPickAddress extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private TextView cancel_pickAddress,done_pickAddress;
    AddressResultReceiver mResultReceiver;
    private final String TAG ="PICKADDRESS";
    private String address = "", selectedLat = "", selectedLong = "";
    private LatLng latLng;
    private String addressText = "";
    private ImageView backArrow_PickAddress;
    private TextView headername_PickAddress;
    private static final int mapRequest = 111;
    private TextView locationTextView;
    private TextView attributionsTextView;
    private FloatingActionButton fab;
    private GoogleMap mgoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_map_to_pick_address);
//   for Header
        headername_PickAddress = (TextView) findViewById(R.id.headername_pickAddress);
//   for footer
//        fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(this);

        findViewById(R.id.textView_Cancel_mapToPickAddress).setOnClickListener(this);
        findViewById(R.id.back_pickAddress).setOnClickListener(this);
        findViewById(R.id.textView_Done_mapToPickAddress).setOnClickListener(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.address_map);
        mapFragment.getMapAsync(this);
        //  search in placeautocompletefrag
        PlaceAutocompleteFragment placeAutocompleteFrag = (PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        placeAutocompleteFrag.setOnPlaceSelectedListener(new PlaceSelectionListener()
        {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName());
                addMarker(place.getLatLng(), ""+place.getAddress());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
                Toast.makeText(getApplicationContext(), "Place selection failed: " + status.getStatusMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        placeAutocompleteFrag.setHint("Find Location");
        mResultReceiver = new AddressResultReceiver(null);
    }

    private void addMarker(LatLng latLng, String address)
    {
        mgoogleMap.clear();
        selectedLat=String.valueOf(latLng.latitude);
        selectedLong=String.valueOf(latLng.longitude);
        addressText=address;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(address);
        mgoogleMap.addMarker(markerOptions);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(15)
                .build();
        mgoogleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_pickAddress:
                finish();
                break;

            case R.id.textView_Cancel_mapToPickAddress:
                finish();
                break;


//            case R.id.fab:
//             inSearchAddress();
//                break;

            case R.id.textView_Done_mapToPickAddress:

                Intent in = new Intent();
                if (addressText != null && !addressText.isEmpty()) {
                    in.putExtra("lat",selectedLat);
                    in.putExtra("long",selectedLong);
                    in.putExtra("address", addressText);
                }
                setResult(RESULT_OK, in);
                finish();

                break;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        if (googleMap != null)
        {
            mgoogleMap=googleMap;
            googleMap.getMapType();
            if (ActivityCompat.checkSelfPermission(MapActivityToPickAddress.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(MapActivityToPickAddress.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                return;
            }
            googleMap.setMyLocationEnabled(true);

            LatLng latLng;
            if(!getIntent().getStringExtra("lat").isEmpty() && !getIntent().getStringExtra("lng").isEmpty())
            {
                latLng = new LatLng(Double.parseDouble(getIntent().getStringExtra("lat")),Double.parseDouble(getIntent().getStringExtra("lng")));
            }
           else {

                latLng=new LatLng(Double.parseDouble(SharedPreferenceWriter.getInstance(MapActivityToPickAddress.this).getString(SPreferenceKey.LATITUDE))
                        , Double.parseDouble(SharedPreferenceWriter.getInstance(MapActivityToPickAddress.this).getString(SPreferenceKey.LONGITUDE)));
            }
            addMarker(latLng,getIntent().getStringExtra("address"));

//
//            addMarker(latLng, SharedPreferenceWriter.getInstance(MapActivityToPickAddress.this).getString(SPreferenceKey.ADDRESS));
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng)
                {
                    Intent intent = new Intent(MapActivityToPickAddress.this, GeocodeAddressIntentService.class);
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

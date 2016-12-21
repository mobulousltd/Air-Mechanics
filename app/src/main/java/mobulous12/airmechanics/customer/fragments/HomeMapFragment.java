package mobulous12.airmechanics.customer.fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.ServiceProviderBean;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.customer.activities.ServiceProviderActivity;
import mobulous12.airmechanics.customer.activities.ServiceProviderDetailActivity;
import mobulous12.airmechanics.customer.activities.VerificationActivity;
import mobulous12.airmechanics.customer.adapters.MyInfoWindowAdapter;
import mobulous12.airmechanics.customer.adapters.SearchListAdapter;
import mobulous12.airmechanics.databinding.HomeMapFragmentBinding;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;


public class HomeMapFragment extends Fragment implements OnMapReadyCallback , ApiListener, GoogleMap.InfoWindowAdapter{
    GoogleMap googlemap;
    boolean search=false;
    HashMap<Marker, ServiceProviderBean> sphashmap=new HashMap<Marker, ServiceProviderBean>( );
    private View view;
    private EditText et_search;
    private SearchView searchView_Home;
    private ArrayList<ServiceProviderBean> spArrayList;
    ArrayList<ServiceProviderBean> arrayList;
    private RecyclerView recView_SPList;
    private SearchListAdapter searchListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null)
        {
            try
            {
                HomeMapFragmentBinding binding=DataBindingUtil.inflate(inflater, R.layout.home_map_fragment, container, false);
                view = binding.getRoot();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                ViewGroup parent = (ViewGroup) view.getParent();
                if (parent != null) parent.removeView(view);
                HomeMapFragmentBinding binding=DataBindingUtil.inflate(inflater, R.layout.home_map_fragment, container, false);
                view = binding.getRoot();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        ((HomeActivity) getActivity()).setToolbarTitle(getResources().getString(R.string.headername_home));
        ((HomeActivity) getActivity()).setNavigationIcon();
        ((HomeActivity) getActivity()).toolbarVisible();


//        et_search = (EditText) view.findViewById(R.id.editText_search_home);
//        et_search.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-Regular.ttf"));
//        et_search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                searchByTextService();
//            }
//        });

        recView_SPList = (RecyclerView) view.findViewById(R.id.recView_SPList);
        searchView_Home = (SearchView) view.findViewById(R.id.searchView_Home);
        searchView_Home.setQueryHint("Search Service Providers..");

    /*Searching serviceProviders by text in Home Map*/
        int et_id = searchView_Home.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText editText = (EditText) searchView_Home.findViewById(et_id);

        int searchPlateId = searchView_Home.getContext().getResources()
                .getIdentifier("android:id/search_plate", null, null);
        View searchPlateView = searchView_Home.findViewById(searchPlateId);
        if (searchPlateView != null) {
            searchPlateView.setBackgroundColor(Color.WHITE);
        }

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(search)
                {
                    search=false;
                    view.findViewById(R.id.search_rv).setVisibility(View.GONE);
                    recView_SPList.setVisibility(View.GONE);
                }
                else
                {
                    search=true;
                    view.findViewById(R.id.search_rv).setVisibility(View.VISIBLE);
                    recView_SPList.setVisibility(View.VISIBLE);
                    setDefaultRecyclerView();
                }
            }
        });

        searchView_Home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!query.isEmpty())
                {
                    searchByTextService();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.isEmpty())
                {
                    searchByTextService();
                }
                else
                {
                    spArrayList=arrayList;
                    searchListAdapter.setArraList(spArrayList);
                    view.findViewById(R.id.search_rv).setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        setDefaultRecyclerView();

        searchListAdapter.onItemClickListener(new SearchListAdapter.MyListener() {
            @Override
            public void onItemClick(int position, View v)
            {
                ServiceProviderBean bean=spArrayList.get(position);
                if(bean.getId().isEmpty())
                {

                    Intent intent = new Intent(getActivity(), ServiceProviderActivity.class);
                    intent.putExtra("bean", bean);
                    getActivity().startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(getActivity(), ServiceProviderDetailActivity.class);
                    intent.putExtra("bean", bean);
                    getActivity().startActivity(intent);

                }

                view.findViewById(R.id.search_rv).setVisibility(View.GONE);
            }
        });
//        view.findViewById(R.id.search_rv).setVisibility(View.GONE);
//
//        view.findViewById(R.id.layout).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity();
//                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                if(search)
//                {
//                    search=false;
//                    view.findViewById(R.id.search_rv).setVisibility(View.GONE);
//                }
//                else
//                {
//                    search=true;
//                    view.findViewById(R.id.search_rv).setVisibility(View.VISIBLE);
//                }
//            }
//        });
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.home_map);
        mapFragment.getMapAsync(this);
        serviceProviderList();// Listing of ServiceProviders as Markers in MAP

        view.findViewById(R.id.search_rv).setVisibility(View.GONE);
        return view;
    }

    private void setDefaultRecyclerView() {
        arrayList = new ArrayList<ServiceProviderBean>();
        spArrayList=arrayList;
        ServiceProviderBean serviceproviderbean=new ServiceProviderBean();
        serviceproviderbean.setName("Looking for Light Weight Vehicle Services");
        serviceproviderbean.setCategory("light");
        serviceproviderbean.setId("");
        ServiceProviderBean serviceproviderbean1=new ServiceProviderBean();
        serviceproviderbean1.setCategory("two");
        serviceproviderbean1.setId("");
        serviceproviderbean1.setName("Looking for Two Wheeler Vehicle Services");
        ServiceProviderBean serviceproviderbean2=new ServiceProviderBean();
        serviceproviderbean2.setName("Looking for Heavy Weight Vehicle Services");
        serviceproviderbean2.setCategory("heavy");
        serviceproviderbean2.setId("");
        arrayList.add(serviceproviderbean);
        arrayList.add(serviceproviderbean1);
        arrayList.add(serviceproviderbean2);
        searchListAdapter = new SearchListAdapter(getActivity(),spArrayList);
        recView_SPList.setAdapter(searchListAdapter);
        recView_SPList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        googlemap=googleMap;
        if (googleMap != null) {
            googleMap.getMapType();

            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    if(search)
                    {
                        search=false;
                        view.findViewById(R.id.search_rv).setVisibility(View.GONE);
                    }
                }
            });
//            googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter(getActivity()));
            googlemap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker)
                {
                    ServiceProviderBean bean=sphashmap.get(marker);
                    Intent intent = new Intent(getActivity(), ServiceProviderDetailActivity.class);
                    //intent.putExtra("from","fromHome");
                    intent.putExtra("bean", bean);
                    getActivity().startActivity(intent);
                }
            });
            // To open service provider detail page from info window
//            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//                @Override
//                public void onInfoWindowClick(Marker marker) {
//                    Toast.makeText(getActivity(), marker.getId()+" : "+marker.getTitle(), Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getActivity(), ServiceProviderDetailActivity.class);
//                    getActivity().startActivity(intent);
//
//                }
//            });

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //TODO:
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            googleMap.setMyLocationEnabled(true);


//            LatLng latlonglocation1 = new LatLng(28.628781, 77.379296);
//            CameraUpdate cameraUpdate1 = CameraUpdateFactory.newLatLngZoom(latlonglocation1, 15);
//            MarkerOptions markerOptions1 = new MarkerOptions();
//            markerOptions1.position(latlonglocation1);
//            markerOptions1.title("1234");
//            // Changing marker icon
//            markerOptions1.icon(BitmapDescriptorFactory.fromResource(R.drawable.garage_icon));
//            googleMap.addMarker(markerOptions1);
//
//
//            LatLng latlonglocation2 = new LatLng(28.406198, 77.852145);
//            CameraUpdate cameraUpdate2 = CameraUpdateFactory.newLatLngZoom(latlonglocation2, 15);
//            MarkerOptions markerOptions2 = new MarkerOptions();
//            markerOptions2.position(latlonglocation2);
//            // Changing marker icon
//            markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.garage_icon));
//            googleMap.addMarker(markerOptions2);
//
//            LatLng latlonglocation3 = new LatLng(31.1471, 75.3412);
//            CameraUpdate cameraUpdate3 = CameraUpdateFactory.newLatLngZoom(latlonglocation3, 15);
//            MarkerOptions markerOptions3 = new MarkerOptions();
//            markerOptions3.position(latlonglocation3);
//            // Changing marker icon
//            markerOptions3.icon(BitmapDescriptorFactory.fromResource(R.drawable.garage_icon));
//            googleMap.addMarker(markerOptions3);
//
//
//            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//            builder.include(latlonglocation1);
//            builder.include(latlonglocation2);
//            builder.include(latlonglocation3);
//            LatLngBounds bounds = builder.build();
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200, 200, 5));


        }


    }

    private void setMarkers(JSONArray jsonarray)
    {
        try
        {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (int i=0;i<jsonarray.length();i++)
            {
                JSONObject jsonobject=jsonarray.getJSONObject(i);
                ServiceProviderBean serviceproviderbean=new ServiceProviderBean();
                serviceproviderbean.setLng(jsonobject.getString("long"));
                serviceproviderbean.setLat(jsonobject.getString("lat"));
                serviceproviderbean.setAddress(jsonobject.getString("address"));
                serviceproviderbean.setId(jsonobject.getString("id"));
                serviceproviderbean.setName(jsonobject.getString("name"));
                serviceproviderbean.setProfile_thumb(jsonobject.getString("profile"));
                serviceproviderbean.setProfile(jsonobject.getString("profile_thumb"));
                serviceproviderbean.setContact_no(jsonobject.getString("contact_no"));
                serviceproviderbean.setEmail(jsonobject.getString("email"));
                serviceproviderbean.setCategory(jsonobject.getString("work_category"));
                serviceproviderbean.setReviews(jsonobject.getString("rating"));
                if(!serviceproviderbean.getLat().isEmpty() && !serviceproviderbean.getLng().isEmpty()) {
                    LatLng latlonglocation = new LatLng(Double.parseDouble(serviceproviderbean.getLat()), Double.parseDouble(serviceproviderbean.getLng()));
                    builder.include(latlonglocation);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latlonglocation);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.garage_icon));
                    sphashmap.put(googlemap.addMarker(markerOptions), serviceproviderbean);
                }
            }
            if(sphashmap.size()==0)
            {
                LatLng latLng = new LatLng(Double.parseDouble(SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getString(SPreferenceKey.LATITUDE)),
                        Double.parseDouble(SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getString(SPreferenceKey.LONGITUDE)));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(15)
                        .build();
                googlemap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
            else
            {
                googlemap.setInfoWindowAdapter(new MyInfoWindowAdapter(getActivity(), sphashmap));
                LatLngBounds bounds = builder.build();
                googlemap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200, 200, 5));
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

/* SERVICES */

    private void searchByTextService()
    {

        MultipartEntityBuilder multipartbuilder = MultipartEntityBuilder.create();
        multipartbuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartbuilder.addTextBody("serviceName",searchView_Home.getQuery().toString().trim());
        multipartbuilder.addTextBody("lat", SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getString(SPreferenceKey.LATITUDE));
        multipartbuilder.addTextBody("long",SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getString(SPreferenceKey.LONGITUDE));

        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setIsLoader(false);
        serviceBean.setMethodName("Consumers/searchbytext");
        serviceBean.setActivity(getActivity());
        serviceBean.setFragment(HomeMapFragment.this);
        serviceBean.setApilistener(this);

        CustomHandler customHandler = new CustomHandler(serviceBean);
        customHandler.makeMultipartRequest(multipartbuilder);
    }


    // hit service of List of service providers on map
    private void serviceProviderList() {
        try
        {
            MultipartEntityBuilder multipartbuilder = MultipartEntityBuilder.create();
            multipartbuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartbuilder.addTextBody("lat", SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getString(SPreferenceKey.LATITUDE));
            multipartbuilder.addTextBody("long",SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getString(SPreferenceKey.LONGITUDE));
            multipartbuilder.addTextBody("category_id", "");
            multipartbuilder.addTextBody("distance", "0");
            multipartbuilder.addTextBody("price", "0");
            multipartbuilder.addTextBody("rating", "0");
            ServiceBean serviceBean = new ServiceBean();
            serviceBean.setMethodName("Consumers/list_serviceprovider");
            serviceBean.setActivity(getActivity());
            serviceBean.setFragment(HomeMapFragment.this);
            serviceBean.setApilistener(this);
            CustomHandler customHandler = new CustomHandler(serviceBean);
            customHandler.makeMultipartRequest(multipartbuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void myServerResponse(JSONObject responseObj) {

        try
        {
            if(responseObj!=null)
            {
                if(responseObj.getString("status").equalsIgnoreCase("SUCCESS") && responseObj.getString("requestKey").equalsIgnoreCase("list_serviceprovider"))
                {
                    setMarkers(responseObj.getJSONArray("response"));
                }
                else if(responseObj.getString("status").equalsIgnoreCase("SUCCESS") && responseObj.getString("requestKey").equalsIgnoreCase("searchbytext"))
                {
                    Log.d("SearchResponse", ""+responseObj.toString());

                    JSONArray responseArr = responseObj.getJSONArray("response");
                    spArrayList = new ArrayList<ServiceProviderBean>();
                    for(int i=0;i<responseArr.length();i++)
                    {
                        ServiceProviderBean serviceproviderbean = new ServiceProviderBean();
                        JSONObject myObj = responseArr.getJSONObject(i);

                        serviceproviderbean.setName(myObj.getString("name"));
                        serviceproviderbean.setId(myObj.getString("id"));
                        serviceproviderbean.setAddress(myObj.getString("address"));
                        serviceproviderbean.setEmail(myObj.getString("email"));
                        serviceproviderbean.setContact_no(myObj.getString("contact_no"));
                        serviceproviderbean.setLat(myObj.getString("lat"));
                        serviceproviderbean.setLng(myObj.getString("long"));
                        serviceproviderbean.setProfile_thumb(myObj.getString("profile_thumb"));
                        serviceproviderbean.setProfile(myObj.getString("profile"));
                        serviceproviderbean.setMin_charge(myObj.getString("st_charge"));
                        serviceproviderbean.setWorkingdays(myObj.getString("workingDays"));
                        serviceproviderbean.setRating(myObj.getString("rating"));
                        serviceproviderbean.setCategory(myObj.getString("work_category"));
                        serviceproviderbean.setStart(myObj.getString("start_time"));
                        serviceproviderbean.setEnd(myObj.getString("end_time"));
                        serviceproviderbean.setSpeciality(myObj.getString("specilityName"));
                        spArrayList.add(serviceproviderbean);

                    }
                    searchListAdapter.setArraList(spArrayList);
                    view.findViewById(R.id.search_rv).setVisibility(View.VISIBLE);
                    recView_SPList.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast toast = Toast.makeText(getActivity(),responseObj.getString("message"), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                Log.i("JsonResponse", ""+responseObj.toString());

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            Fragment f1 = getActivity().getSupportFragmentManager().findFragmentById(R.id.home_map);
            if (f1 != null) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(f1).commit();
            }
            view = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}


package mobulous12.airmechanics.customer.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.ServiceProviderBean;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.customer.adapters.FavoritesRecyclerAdapter;
import mobulous12.airmechanics.customer.adapters.MyBookingsRecyclerAdapter;
import mobulous12.airmechanics.beans.BookingBean;
import mobulous12.airmechanics.databinding.FragmentMyBookingsBinding;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

public class MyBookingsFragment extends Fragment implements ApiListener {

    View view;
    public static boolean bookingsFrag=false;

    RecyclerView recyclerView_myBookings;
    MyBookingsRecyclerAdapter bookingsRecyclerAdapter;
    private   LayoutInflater myinflater;
    private   ArrayList<BookingBean> bookingArrList;

    public MyBookingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.blank_at_right_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_show_service_provider).setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState)
    {
        FragmentMyBookingsBinding binding=DataBindingUtil.inflate(inflater, R.layout.fragment_my_bookings, container, false);
        view=binding.getRoot();
        bookingsFrag=true;

        myinflater = inflater;

        recyclerView_myBookings = (RecyclerView) view.findViewById(R.id.recyclerView_myBookings);

        ((HomeActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.headername_my_bookings));
        ((HomeActivity) getActivity()).toolbarVisible();
        ((HomeActivity)getActivity()).setNavigationIcon();


        myJobReqServiceHit();

        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(messageReciever,
                new IntentFilter("NOTIFYCUSTBOOK"));
        return view;
    }
    private BroadcastReceiver messageReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            myJobReqServiceHit();
        }
    };

//    Service for MyBookings Lists:
    private void myJobReqServiceHit()
    {
        try {

            MultipartEntityBuilder multipartbuilder = MultipartEntityBuilder.create();
            multipartbuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            multipartbuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN));

            ServiceBean serviceBean = new ServiceBean();
            serviceBean.setMethodName("Consumers/my_job_request");
            serviceBean.setActivity( getActivity());
            serviceBean.setApilistener(this);

            CustomHandler customHandler = new CustomHandler(serviceBean);
            customHandler.makeMultipartRequest(multipartbuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void myServerResponse(JSONObject responseObj)
    {
        try {
            if (responseObj != null)
            {
                if (responseObj.getString("status").equalsIgnoreCase("SUCCESS") && responseObj.getString("requestKey").equalsIgnoreCase("my_job_request"))
                {
                    JSONObject response = responseObj.getJSONObject("response");
                    JSONArray userArr = response.getJSONArray("user");

                    bookingArrList=new ArrayList<>();

                    for (int i = 0; i < userArr.length(); i++)
                    {
                        JSONObject jsonobject = userArr.getJSONObject(i);
                        BookingBean bean = new BookingBean();
                        bean.setBookingid(jsonobject.getString("id"));
                        bean.setUserName(jsonobject.getString("userName"));
                        bean.setUserImage(jsonobject.getString("userImage"));
                        bean.setUseraddress(jsonobject.getString("address"));
                        bean.setUsernumber(jsonobject.getString("contact_no"));
                        bean.setStatus(jsonobject.getString("status"));
                        bean.setMinCharge(jsonobject.getString("minCharge"));
                        bean.setRequestname(jsonobject.getString("request_Title"));
                        bean.setRequestDate(jsonobject.getString("requestDate"));
                        bean.setRequestTime(jsonobject.getString("Request_time"));
                        bean.setRequestdesc(jsonobject.getString("request_description"));
                        bean.setRequestcategory(jsonobject.getString("category"));
                        bean.setOpenTime(jsonobject.getString("open_time"));
                        bean.setCloseTime(jsonobject.getString("close_time"));

                        bookingArrList.add(bean);

                        /*Recycler view*/
                        bookingsRecyclerAdapter = new MyBookingsRecyclerAdapter(getActivity(),bookingArrList );
                        bookingsRecyclerAdapter.onItemClickListener(new MyBookingsRecyclerAdapter.MyClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer,new BookingDetailFragment(),"bookingDetailFragment").addToBackStack("bookingdetails").commit();
                            }
                        });
                        recyclerView_myBookings.setAdapter(bookingsRecyclerAdapter);
                        recyclerView_myBookings.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                }
                else
                {
                    Toast toast = Toast.makeText(getActivity(),responseObj.getString("message"), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                Log.v("JsonResponse", ""+responseObj.toString());
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        bookingsFrag=true;
    }

    @Override
    public void onPause() {
        super.onPause();
        bookingsFrag=false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bookingsFrag=false;
    }
}

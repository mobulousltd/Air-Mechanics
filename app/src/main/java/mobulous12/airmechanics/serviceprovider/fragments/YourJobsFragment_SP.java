package mobulous12.airmechanics.serviceprovider.fragments;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.BookingBean;
import mobulous12.airmechanics.customer.adapters.MyBookingsRecyclerAdapter;
import mobulous12.airmechanics.databinding.FragmentYourJobsSpBinding;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;
import mobulous12.airmechanics.serviceprovider.activities.JobOrderDetailActivity;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

public class YourJobsFragment_SP extends Fragment implements View.OnClickListener, ApiListener {

    private View view;
    private RelativeLayout root_insideCardView_1;

    private String date;
    private RecyclerView recyclerView;
    public static final int JOBSTATUSCHANGE=005;


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
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_show_service_provider).setVisible(false);
//        menu.findItem(R.id.action_show_myJob_Orders).setVisible(false);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState)
    {
        FragmentYourJobsSpBinding binding=DataBindingUtil.inflate(inflater, R.layout.fragment_your_jobs_sp, container, false);
        view=binding.getRoot();

        // RETRIEVING DATE
        Bundle bundle = getArguments();
        date = bundle.getString("date");


        ((HomeActivityServicePro)getActivity()).setToolbarTitleSP(getResources().getString(R.string.headername_yourjobs));
        ((HomeActivityServicePro)getActivity()).setNavigationIconSP();

       recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_yourJobs);




        // HITTING SERVICE
        findJobRequestByDateServiceHit();

        return view;
    }



    @Override
    public void onClick(View view) {

        switch (view.getId())
        {

        }
    }

    private void findJobRequestByDateServiceHit()
    {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN));
        multipartEntityBuilder.addTextBody("date", date);

        ServiceBean bean = new ServiceBean();
        bean.setActivity(getActivity());
        bean.setMethodName("Services/findJobsRequestBydate");
        bean.setApilistener(this);

        CustomHandler customHandler = new CustomHandler(bean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);

    }

    @Override
    public void myServerResponse(JSONObject jsonObject) {

        try {

            if (jsonObject.getString("status").equals("SUCCESS")&& jsonObject.getString("requestKey").equalsIgnoreCase("findJobsRequestBydate"))
            {
                final ArrayList<BookingBean> bookingBeen = new ArrayList<>();

                JSONArray response = jsonObject.getJSONArray("response");
                for (int i = 0; i < response.length(); i++)
                {
                    BookingBean bean = new BookingBean();
                    JSONObject object = response.getJSONObject(i);
                    bean.setBookingid(object.getString("id"));
                    bean.setUserName(object.getString("userName"));
                    bean.setUsernumber(object.getString("contact_no"));
                    bean.setUseraddress(object.getString("address"));
                    bean.setLatitude(object.getString("lat"));
                    bean.setLongitude(object.getString("long"));
                    bean.setServiceType(object.getString("service_type"));
                    bean.setStatus(object.getString("status"));
                    bean.setRequestname(object.getString("request_name"));
                    bean.setOpenTime(object.getString("open_time"));
                    bean.setCloseTime(object.getString("close_time"));
                    bean.setRequestdesc(object.getString("request_description"));
                    bean.setMinCharge(object.getString("minCharge"));
                    bean.setCreatedOn(object.getString("createdOn"));
                    bean.setCategoryId(object.getString("Categorie_id"));
                    bean.setCategory(object.getString("category"));
                    bean.setRequestTime(object.getString("time"));
                    bean.setRequestDate(object.getString("date"));
                    bean.setProfile_thumb(object.getString("profile_thumb"));

                    bookingBeen.add(bean);

                }
                MyBookingsRecyclerAdapter adapter = new MyBookingsRecyclerAdapter(getActivity(), bookingBeen);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
                adapter.onItemClickListener(new MyBookingsRecyclerAdapter.MyClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        BookingBean bean=bookingBeen.get(position);
                        Intent intent=new Intent(getActivity(), JobOrderDetailActivity.class);
                        intent.putExtra("bookingbean", bean);
                        startActivityForResult(intent, JOBSTATUSCHANGE);
                    }
                });
            }
        }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==getActivity().RESULT_OK)
        {
            if(requestCode==JOBSTATUSCHANGE)
            {
                findJobRequestByDateServiceHit();
            }
        }
    }


    }


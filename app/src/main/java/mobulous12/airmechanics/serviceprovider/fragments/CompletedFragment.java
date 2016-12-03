package mobulous12.airmechanics.serviceprovider.fragments;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.BookingBean;
import mobulous12.airmechanics.databinding.FragmentCompletedBinding;
import mobulous12.airmechanics.serviceprovider.activities.BillPaymentActivitySp;
import mobulous12.airmechanics.serviceprovider.activities.JobOrderDetailActivity;
import mobulous12.airmechanics.serviceprovider.adapters.CompletedRecyclerAdapter;
import mobulous12.airmechanics.serviceprovider.adapters.PendingRecyclerAdapter;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

public class CompletedFragment extends Fragment implements ApiListener {

private View view;
    private RecyclerView recyclerView_completedFrag;
    private CompletedRecyclerAdapter completedRecyclerAdapter;
    private ArrayList<BookingBean> beanArrayList;

    public CompletedFragment() {  }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState)
    {
        FragmentCompletedBinding binding=DataBindingUtil.inflate(inflater, R.layout.fragment_completed, container, false);
        view=binding.getRoot();
        recyclerView_completedFrag = (RecyclerView) view.findViewById(R.id.recyclerView_completedFrag);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                completedRecyclerAdapter = new CompletedRecyclerAdapter(getActivity());
//                recyclerView_completedFrag.setAdapter(completedRecyclerAdapter);
//                recyclerView_completedFrag.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//                completedRecyclerAdapter.onItemClickListener(new CompletedRecyclerAdapter.MyClickListener()
//                {
//                    @Override
//                    public void onItemClick(int position, View v) {
//                        startActivity(new Intent(getActivity(), BillPaymentActivitySp.class));
//                    }
//                });
//            }
//        },500);

        return view;
    }


    public void completedServiceHit()
    {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN));

        ServiceBean bean = new ServiceBean();
        bean.setActivity(getActivity());
        bean.setApilistener(this);
        bean.setMethodName("Services/complete_job_request");

        CustomHandler customHandler = new CustomHandler(bean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);
    }


    // SERVER RESPONSE
    @Override
    public void myServerResponse(JSONObject jsonObject) {

        Log.e("JOB_ORDERS_COMPLETED", jsonObject.toString());

        if (jsonObject != null)
        {
            try {

                if (jsonObject.getString("status").equals("SUCCESS"))
                {
                    JSONObject response = jsonObject.getJSONObject("response");
                    JSONArray userArray = response.getJSONArray("user");

                    beanArrayList = new ArrayList<>();

                    for (int i=0; i<userArray.length(); i++)
                    {
                        JSONObject obj = userArray.getJSONObject(i);
                        BookingBean bean = new BookingBean();
                        bean.setBookingid(obj.getString("id"));
                        bean.setUserName(obj.getString("userName"));
                        bean.setUserImage(obj.getString("userImage"));
                        bean.setUseraddress(obj.getString("address"));
                        bean.setUsernumber(obj.getString("contact_no"));
                        bean.setStatus(obj.getString("status"));
                        bean.setMinCharge(obj.getString("minCharge"));
                        bean.setRequestname(obj.getString("request_Title"));
                        bean.setRequestDate(obj.getString("requestDate"));
                        bean.setRequestTime(obj.getString("Request_time"));
                        bean.setRequestdesc(obj.getString("request_description"));
                        bean.setRequestcategory(obj.getString("category"));
                        bean.setOpenTime(obj.getString("open_time"));
                        bean.setCloseTime(obj.getString("close_time"));
                        beanArrayList.add(bean);
                    }
                    if(beanArrayList.size()==0)
                    {
                        view.findViewById(R.id.tv_complete).setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        view.findViewById(R.id.tv_complete).setVisibility(View.GONE);
                    }
                    completedRecyclerAdapter = new CompletedRecyclerAdapter(getActivity(), beanArrayList);
                    recyclerView_completedFrag.setAdapter(completedRecyclerAdapter);
                    recyclerView_completedFrag.setLayoutManager(new LinearLayoutManager(getActivity()));

                    completedRecyclerAdapter.onItemClickListener(new CompletedRecyclerAdapter.MyClickListener()
                    {
                        @Override
                        public void onItemClick(int position, View v) {
                            startActivityForResult(new Intent(getActivity(), BillPaymentActivitySp.class), 001);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}

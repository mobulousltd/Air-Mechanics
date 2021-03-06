package mobulous12.airmechanics.serviceprovider.fragments;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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
import mobulous12.airmechanics.serviceprovider.adapters.PendingInProgressCompletedAdapter;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

public class CompletedFragment extends Fragment implements ApiListener {

private View view;
    private RecyclerView recyclerView_completedFrag;
    private PendingInProgressCompletedAdapter adapter;
    private ArrayList<BookingBean> beanArrayList;

    public CompletedFragment() {  }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState)
    {
        FragmentCompletedBinding binding  = DataBindingUtil.inflate(inflater,R.layout.fragment_completed,container,false);
        view = binding.getRoot();
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
        multipartEntityBuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getString(SPreferenceKey.TOKEN));

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

        if (jsonObject != null)
        {
            try {

               if ( (jsonObject.getString("status").equals("SUCCESS")) && (jsonObject.getString("requestKey").equalsIgnoreCase("complete_job_request")) )
                {
                    Log.w("JOB_ORDERS_COMPLETED", jsonObject.toString());
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
                        bean.setProfile_thumb(obj.getString("userImage"));
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
                        bean.setCategory(obj.getString("category"));
                        JSONArray reqImgJsonArray = obj.getJSONArray("request_image");
                        if(reqImgJsonArray.length()>0)
                        {
                            bean.setRequestImage(reqImgJsonArray.getString(0));
                        }
                        else
                        {
                            bean.setRequestImage(obj.getString("userImage"));
                        }

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
//                    completedRecyclerAdapter = new CompletedRecyclerAdapter(getActivity(), beanArrayList);
                    adapter = new PendingInProgressCompletedAdapter(getActivity(), beanArrayList);
                    recyclerView_completedFrag.setAdapter(adapter);
                    recyclerView_completedFrag.setLayoutManager(new LinearLayoutManager(getActivity()));

                    adapter.onItemClickListener(new PendingInProgressCompletedAdapter.ClickListener()
                    {
                        @Override
                        public void onItemClick(int position, View v) {
                            BookingBean bean = beanArrayList.get(position);
                            Intent intent = new Intent(getActivity(), BillPaymentActivitySp.class);
                            intent.putExtra("bean", bean);
                            startActivityForResult(intent, 001);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==getActivity().RESULT_OK) {
            if (requestCode == 001)
            {
                completedServiceHit();
            }
        }
    }
}

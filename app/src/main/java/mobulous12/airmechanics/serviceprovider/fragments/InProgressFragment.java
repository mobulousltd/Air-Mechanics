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
import mobulous12.airmechanics.databinding.FragmentInProgressBinding;
import mobulous12.airmechanics.serviceprovider.activities.BillPaymentActivitySp;
import mobulous12.airmechanics.serviceprovider.activities.JobOrderDetailActivity;
import mobulous12.airmechanics.serviceprovider.adapters.CompletedRecyclerAdapter;
import mobulous12.airmechanics.serviceprovider.adapters.InProgressRecyclerAdapter;
import mobulous12.airmechanics.serviceprovider.adapters.PendingRecyclerAdapter;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

/**
 * A simple {@link Fragment} subclass.
 */
public class InProgressFragment extends Fragment implements ApiListener {

    public static final int JOBSTATUSCHANGE=005;
    private RecyclerView recyclerView_inProgressFrag;
    private InProgressRecyclerAdapter inProgressRecyclerAdapter;
    private View view;

    private ArrayList<BookingBean> beanArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState)
    {
        FragmentInProgressBinding binding=DataBindingUtil.inflate(inflater, R.layout.fragment_in_progress, container, false);
        view=binding.getRoot();
        recyclerView_inProgressFrag = (RecyclerView) view.findViewById(R.id.recyclerView_inProgressFrag);


        return view;
    }

    public void inProgressServiceHit()
    {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN));

        ServiceBean bean = new ServiceBean();
        bean.setActivity(getActivity());
        bean.setApilistener(this);
        bean.setMethodName("Services/process_job_request");

        CustomHandler customHandler = new CustomHandler(bean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);
    }


    // SERVER RESPONSE
    @Override
    public void myServerResponse(JSONObject jsonObject) {

        Log.e("JOB_ORDERS_IN_PROCESS", jsonObject.toString());

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
                        BookingBean bean = new BookingBean();
                        JSONObject obj = userArray.getJSONObject(i);
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
                        view.findViewById(R.id.tv_progress).setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        view.findViewById(R.id.tv_progress).setVisibility(View.GONE);
                    }
                    inProgressRecyclerAdapter = new InProgressRecyclerAdapter(getActivity(), beanArrayList);
                    recyclerView_inProgressFrag.setAdapter(inProgressRecyclerAdapter);
                    recyclerView_inProgressFrag.setLayoutManager(new LinearLayoutManager(getActivity()));

                    inProgressRecyclerAdapter.onItemClickListener(new InProgressRecyclerAdapter.MyClickListener() {
                        @Override
                        public void onItemClick(int position, View v)
                        {
                            BookingBean bean=beanArrayList.get(position);
                            Intent intent=new Intent(getActivity(), JobOrderDetailActivity.class);
                            intent.putExtra("bookingbean", bean);
                            startActivityForResult(intent, JOBSTATUSCHANGE);
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
        if(resultCode==getActivity().RESULT_OK)
        {
            if(requestCode==JOBSTATUSCHANGE)
            {
                inProgressServiceHit();
            }
        }
    }
}

package mobulous12.airmechanics.serviceprovider.fragments;


import android.app.Activity;
import android.app.Service;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.databinding.FragmentJobRequestBinding;
import mobulous12.airmechanics.beans.JobRequestModel;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;
import mobulous12.airmechanics.serviceprovider.adapters.JobRequestAdapter;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.utils.MyApplication;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

public class JobRequestFragment extends Fragment implements ApiListener {

    Activity activity;
    public static boolean jobRequestFrag=false;
    private View view;
    private List<JobRequestModel> jobRequestModelList;
    private  RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
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
        FragmentJobRequestBinding binding=DataBindingUtil.inflate(inflater, R.layout.fragment_job_request, container, false);
        view=binding.getRoot();
        activity=getActivity();
        jobRequestFrag=true;
        ((HomeActivityServicePro) getActivity()).setToolbarTitleSP("Job Request");
        ((HomeActivityServicePro) getActivity()).setNavigationIconSP();


        // INSTANTIATING REQUEST_MODEL LIST

        // HITTING NEW_JOB_REQUEST_SERVICE
        newJobRequestHit();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_job_request);


        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(messageReciever,
                new IntentFilter("NOTIFYSP"));
        return view;
    }
    private BroadcastReceiver messageReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            newJobRequestHit();
        }
    };


    private void newJobRequestHit()
    {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN));

        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setActivity(MyApplication.getInstance().getActivity());
        serviceBean.setMethodName("Services/NewJobRequest");
        serviceBean.setApilistener(this);

        CustomHandler customHandler = new CustomHandler(serviceBean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);
    }


    @Override
    public void myServerResponse(JSONObject jsonObject) {

        try
        {
            if (jsonObject.getString("status").equals("SUCCESS"))
            {
                if(jsonObject.getString("requestKey").equals("NewJobRequest"))
                {
                    JSONObject response = jsonObject.getJSONObject("response");
                    JSONArray userArray = response.getJSONArray("user");

                    jobRequestModelList = new ArrayList<>();
                    for (int i=0; i<userArray.length(); i++)
                    {
                        JSONObject arrayObj = userArray.getJSONObject(i);

                        JobRequestModel model = new JobRequestModel();
                        model.setId(arrayObj.getString("id"));
                        model.setDate(arrayObj.getString("requestDate"));
                        model.setRequestTitle(arrayObj.getString("request_Title"));
                        model.setRequestDescription(arrayObj.getString("request_description"));

                        //Adding RequestModel class object into jobRequestModelList ONE at a TIME
                        jobRequestModelList.add(model);

                    }
                    if(jobRequestModelList.size()==0)
                    {
                        view.findViewById(R.id.tv_job).setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        view.findViewById(R.id.tv_job).setVisibility(View.GONE);
                    }
                    JobRequestAdapter adapter=new JobRequestAdapter(getActivity(), jobRequestModelList);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter.onItemClickListener_JobRequest_SP(new JobRequestAdapter.JobRequestInterface() {
                        @Override
                        public void onItemClick(int position, View view) {
                            Fragment fragment = new JobRequestDetailFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("reqid", jobRequestModelList.get(position).getId());
                            fragment.setArguments(bundle);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_serviceHomeContainer, fragment).addToBackStack("jobReqFrag").commit();

                        }
                    });

                }
            }
            else
            {
                Toast.makeText(getActivity(), ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("JOB_REQUEST_FRAG : ", jsonObject+"");

    }

    @Override
    public void onResume() {
        super.onResume();
        jobRequestFrag=true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        jobRequestFrag=false;
    }

    @Override
    public void onPause() {
        super.onPause();
        jobRequestFrag=false;
    }
}

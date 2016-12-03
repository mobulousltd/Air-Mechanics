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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import mobulous12.airmechanics.R;
import mobulous12.airmechanics.customer.activities.AcceptRejectDetailActivity;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.customer.adapters.NewJobReqRecyclerAdapter;
import mobulous12.airmechanics.databinding.FragmentNewJobRequestBinding;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;


public class NewJobRequest extends Fragment implements ApiListener {

    public static boolean jobRequest=false;
    private View view;
    private RecyclerView recyVw_newJobRequest;
    private NewJobReqRecyclerAdapter jobReqRecyclerAdapter;
    private String status="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        FragmentNewJobRequestBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_new_job_request,container,false);
        view = binding.getRoot();
        jobRequest=true;

        ((HomeActivity) getActivity()).setToolbarTitle("Job Requests");
        ((HomeActivity) getActivity()).toolbarVisible();
        ((HomeActivity) getActivity()).setNavigationIcon();

        recyVw_newJobRequest = (RecyclerView) view.findViewById(R.id.recyVw_newJobRequest);
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(messageReciever,
                new IntentFilter("NOTIFYCUST"));

        return view;
    }
    private BroadcastReceiver messageReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            newJobReqServiceHit();
        }
    };

    //services
    private void newJobReqServiceHit()
    {
        if(getActivity()!=null)
        {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartEntityBuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN));

            ServiceBean serviceBean = new ServiceBean();
            serviceBean.setActivity(getActivity());
            serviceBean.setMethodName("Consumers/NewJobRequest");
            serviceBean.setApilistener(this);

            CustomHandler customHandler = new CustomHandler(serviceBean);
            customHandler.makeMultipartRequest(multipartEntityBuilder);
        }
    }


    @Override
    public void myServerResponse(JSONObject jsonObject)
    {
        try {
            if (jsonObject != null)
            {
                if (jsonObject.getString("status").equalsIgnoreCase("SUCCESS"))
                {
                    Log.e("JSON Response", ""+jsonObject.toString());

                    if (jsonObject.getString("requestKey").equalsIgnoreCase("NewJobRequest"))
                    {
                        JSONObject response = jsonObject.getJSONObject("response");
                        final JSONArray userArr = response.getJSONArray("user");

                        jobReqRecyclerAdapter = new NewJobReqRecyclerAdapter(getActivity(),userArr);
                        recyVw_newJobRequest.setAdapter(jobReqRecyclerAdapter);
                        recyVw_newJobRequest.setLayoutManager(new LinearLayoutManager(getActivity()));
//                        click listener per item in list
                        jobReqRecyclerAdapter.onItemClickListener(new NewJobReqRecyclerAdapter.JobReqClickListener() {
                            @Override
                            public void onItemClick(int position, View v)
                            {
                                try
                                {
                                    JSONObject j_Object = userArr.getJSONObject(position);
                                    status = j_Object.getString("status");
                                    if(status.equalsIgnoreCase("priceupdate"))
                                    {
                                        Intent intent=new Intent(getActivity(), AcceptRejectDetailActivity.class);
                                        intent.putExtra("requestid", j_Object.getString("id"));
                                        getActivity().startActivity(intent);
                                    }
                                    else {

                                    }

                                }
                                catch (Exception e)
                                { e.printStackTrace();  }

                            }
                        });

                    }
                }
                else {}

                Log.e("JSON Response", ""+jsonObject.toString());
            }
        }catch (Exception e)
        {    e.printStackTrace();  }

    }

    @Override
    public void onResume() {
        super.onResume();
        jobRequest=true;
        newJobReqServiceHit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        jobRequest=false;
    }

    @Override
    public void onPause() {
        super.onPause();
        jobRequest=false;
    }

    // House-Keeping Stuff
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
    }


}

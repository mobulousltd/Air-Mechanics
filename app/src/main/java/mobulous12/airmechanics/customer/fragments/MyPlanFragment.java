package mobulous12.airmechanics.customer.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.PlanBean;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.customer.adapters.MyPlanRecyclerAdapter;
import mobulous12.airmechanics.databinding.FragmentMyPlanBinding;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

public class MyPlanFragment extends Fragment implements ApiListener {

    private RecyclerView recyclerView_myPlan;
    private MyPlanRecyclerAdapter myPlanRecyclerAdapter;
    private View view;
    private TextView tv_plan;
    private ArrayList<PlanBean> planBeanArrayList=new ArrayList<>();

    public MyPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        FragmentMyPlanBinding binding=DataBindingUtil.inflate(inflater, R.layout.fragment_my_plan, container, false);
        view=binding.getRoot();
        /*Recycler view*/
        recyclerView_myPlan = (RecyclerView) view.findViewById(R.id.recyclerView_myPlan);
        tv_plan = (TextView) view.findViewById(R.id.tv_plan);
        if(SharedPreferenceWriter.getInstance(getActivity()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
        {
            ((HomeActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.headername_myplan));
            ((HomeActivity)getActivity()).setNavigationIcon();
        }
        else
        {
            ((HomeActivityServicePro)getActivity()).setToolbarTitleSP(getResources().getString(R.string.headername_myplan));
            ((HomeActivityServicePro)getActivity()).setNavigationIconSP();

        }
        myPlanServiceHit();

        return view;
    }

//    /*Services*/
    private void myPlanServiceHit()
    {
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        entityBuilder.addTextBody("token",SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getString(SPreferenceKey.TOKEN));

        ServiceBean serviceBean = new ServiceBean();
        if(SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
        {
            serviceBean.setMethodName("Consumers/myplan");
        }
        else
        {
            serviceBean.setMethodName("Services/myplan");
        }
        serviceBean.setActivity(getActivity());
        serviceBean.setApilistener(MyPlanFragment.this);

        CustomHandler customHandler = new CustomHandler(serviceBean);
        customHandler.makeMultipartRequest(entityBuilder);

    }

    @Override
    public void myServerResponse(JSONObject jsonObject) {

        try {
            if (jsonObject != null)
            {
                if (jsonObject.getString("status").equalsIgnoreCase("SUCCESS"))
                {
                    if (jsonObject.getString("requestKey").equalsIgnoreCase("myplan"))
                    {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                        String doIhavePlan = jsonObject1.getString("plan");
                        if (!doIhavePlan.equalsIgnoreCase("no"))
                        {
                            tv_plan.setVisibility(View.GONE);

                        PlanBean planBean = new PlanBean();
                        planBean.setPlanId(jsonObject1.getString("palnid"));
                        planBean.setPlanAmount(jsonObject1.getString("rate"));
                        planBean.setExpiryDate(jsonObject1.getString("expiryDate"));
//                       CUSTOMER
                        if (SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
                        {
                            if (jsonObject1.getString("validity").equalsIgnoreCase("1 months")) {
                                planBean.setPlanName(getActivity().getString(R.string.duration_myplan));
                            } else {
                                planBean.setPlanName(getActivity().getString(R.string.duration_annual_subscription));
                            }
                            planBean.setRemainingPoints(jsonObject1.getString("remainingpoin"));
                            planBean.setDescription("You have " + planBean.getRemainingPoints() + " credits.");
                        }
                        else    // SERVICE PROVIDER
                        {
                            if (jsonObject1.getString("planname").equalsIgnoreCase("Yearly"))
                            {
                                planBean.setPlanName(getActivity().getString(R.string.duration_annual_subscription));
                            } else {
                                planBean.setPlanName(jsonObject1.getString("planname"));
                            }
                            planBean.setDescription(getActivity().getString(R.string.description_myplan));
                        }

                        planBean.setExpiryDate(jsonObject1.getString("expiryDate"));
                        planBeanArrayList.add(planBean);

//                        if (planBeanArrayList.size() == 0) {
//                            tv_plan.setVisibility(View.VISIBLE);
//                        } else {
//                            tv_plan.setVisibility(View.GONE);
//                        }
                        myPlanRecyclerAdapter = new MyPlanRecyclerAdapter(getActivity(), planBeanArrayList);
                        recyclerView_myPlan.setAdapter(myPlanRecyclerAdapter);
                        recyclerView_myPlan.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                        else {
                            tv_plan.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    Log.v("JSON_Response", "" + jsonObject.toString());
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

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


}

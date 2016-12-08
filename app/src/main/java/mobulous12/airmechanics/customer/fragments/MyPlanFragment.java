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

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPlanFragment extends Fragment implements ApiListener {

    private RecyclerView recyclerView_myPlan;
    private MyPlanRecyclerAdapter myPlanRecyclerAdapter;
    private View view;
    private ArrayList<PlanBean> planBeanArrayList;

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

        if(SharedPreferenceWriter.getInstance(getActivity()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
        {
            ((HomeActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.headername_myplan));
            ((HomeActivity)getActivity()).setNavigationIcon();

            /*  Hitting Customer Myplan Service  */
            customerMyPlanServiceHit();
        }
        else
        {
            ((HomeActivityServicePro)getActivity()).setToolbarTitleSP(getResources().getString(R.string.headername_myplan));
            ((HomeActivityServicePro)getActivity()).setNavigationIconSP();

        }

        return view;
    }

//    /*Services*/
    private void customerMyPlanServiceHit()
    {
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        entityBuilder.addTextBody("token",SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getString(SPreferenceKey.TOKEN));

        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setMethodName("Consumers/myplan");
        serviceBean.setActivity(getActivity());
        serviceBean.setApilistener(MyPlanFragment.this);

        CustomHandler customHandler = new CustomHandler(serviceBean);
        customHandler.makeMultipartRequest(entityBuilder);

    }

    @Override
    public void myServerResponse(JSONObject jsonObject)
    {
        try{
            if(jsonObject !=null)
            {
                if(jsonObject.getString("status").equalsIgnoreCase("SUCCESS"))
                {
                    if(jsonObject.getString("requestKey").equalsIgnoreCase("myplan"))
                    {
                        JSONArray responseArr = jsonObject.getJSONArray("response");

//                     customer
                        if(SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
                        {
                            planBeanArrayList = new ArrayList<PlanBean>();
                            for (int i = 0; i < responseArr.length(); i++)
                            {
                                JSONObject j_obj = responseArr.getJSONObject(i);
                                PlanBean planBean = new PlanBean();

                                planBean.setPlanId(j_obj.getString("palnid"));
                                planBean.setPlanAmount(j_obj.getString("sr_val"));
                                planBean.setRemainingPoints(j_obj.getString("remainingpoin"));
                                planBean.setExpiryDate(j_obj.getString("expiryDate"));
                                planBean.setCredits(j_obj.getString("creadits"));

                                planBeanArrayList.add(planBean);

                            }

                       /*Recycler view and Adapter*/
                            myPlanRecyclerAdapter = new MyPlanRecyclerAdapter(getActivity(), planBeanArrayList);
                            recyclerView_myPlan.setAdapter(myPlanRecyclerAdapter);
                            recyclerView_myPlan.setLayoutManager(new LinearLayoutManager(getActivity()));
                        }
            //  service provider
                        else
                        {
                            Log.e("MyPlanFragment : ", jsonObject+"");
                        }

                    }

                }
                else {
                    Log.v("JSON_Response", ""+jsonObject.toString());
                }


            }

           }


        catch (Exception e){
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
//        if(SharedPreferenceWriter.getInstance(getActivity()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN)) {
//
//            menu.findItem(R.id.action_show_service_provider).setVisible(false);
//        }else {
//            menu.findItem(R.id.action_show_myJob_Orders).setVisible(false);
//        }
    }


}

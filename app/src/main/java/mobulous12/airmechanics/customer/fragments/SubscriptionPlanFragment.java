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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.PlanBean;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.customer.adapters.SubscriptionPlanRecyclerAdapter;
import mobulous12.airmechanics.databinding.FragmentSubscriptionPlanBinding;
import mobulous12.airmechanics.databinding.SubscriptionPlanCustomCardsBinding;
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
public class SubscriptionPlanFragment extends Fragment implements ApiListener, View.OnClickListener {


    ArrayList<PlanBean> arrayList;
    RecyclerView recyclerView_subscriptionPlan;
    SubscriptionPlanRecyclerAdapter subsPlanRecyclerAdapter;
    private View view;

    ///////////////////////
    private TextView monthlySubscriptionTitle;
    private TextView monthlySubscriptionAmount;
    private TextView monthlySubscription_benefit;
    private TextView monthlySubscription_description;
    private Button btn_MonthlySubscription_BuyNow;

    private TextView annualSubscriptionTitle;
    private TextView annualSubscriptionAmount;
    private TextView annualSubscription_benefit;
    private TextView annualSubscription_description;
    private Button btn_AnnualSubscription_BuyNow;

    private String id = "", annualid="", monthlyid="";
    private String monthlyPayAmount="",annualPayAmount="",transactionId="",payamount="";
    //////////////////////


    public SubscriptionPlanFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        SubscriptionPlanCustomCardsBinding binding=DataBindingUtil.inflate(inflater,R.layout.subscription_plan_custom_cards, container, false);
        view=binding.getRoot();

            monthlySubscriptionTitle = (TextView) view.findViewById(R.id.textView_durationMonthlySubscriptionPlan);
            monthlySubscriptionAmount = (TextView) view.findViewById(R.id.textView_monthlyAmountSubscriptionPlan);
            monthlySubscription_benefit = (TextView) view.findViewById(R.id.textView1_benefitsHeadingSubcriptionPlan);
            monthlySubscription_description = (TextView) view.findViewById(R.id.textView_upper_descriptionSubscriptionPlan);
        btn_MonthlySubscription_BuyNow = (Button) view.findViewById(R.id.button_buyNow_monthlysubscription);
            annualSubscriptionTitle = (TextView) view.findViewById(R.id.textView_durationAnnualSubscriptionPlan);
            annualSubscriptionAmount = (TextView) view.findViewById(R.id.textView_annualyAmountSubscriptionPlan);
            annualSubscription_benefit = (TextView) view.findViewById(R.id.textView2_benefitsHeadingSubcriptionPlan);
            annualSubscription_description = (TextView) view.findViewById(R.id.textView_lower_descriptionSubscriptionPlan);
        btn_AnnualSubscription_BuyNow = (Button) view.findViewById(R.id.button_buyNow_annualSubscription);

        generateTransId();
        subscriptionPlanListServicehit();
        if(SharedPreferenceWriter.getInstance(getActivity()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
        {
            ((HomeActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.headername_subscriptionplan));
            ((HomeActivity)getActivity()).setNavigationIcon();
        }
        else
        {
            ((HomeActivityServicePro)getActivity()).setToolbarTitleSP(getResources().getString(R.string.headername_subscriptionplan));
            ((HomeActivityServicePro)getActivity()).setNavigationIconSP();
        }
//
//        recyclerView_subscriptionPlan = (RecyclerView) view.findViewById(R.id.recyclerView_subscriptionPlan);
//        subsPlanRecyclerAdapter = new SubscriptionPlanRecyclerAdapter(getActivity());
//        recyclerView_subscriptionPlan.setAdapter(subsPlanRecyclerAdapter);
//        recyclerView_subscriptionPlan.setLayoutManager(new LinearLayoutManager(getActivity()));



        return view;
    }

     private void generateTransId()
      {
        Random rand = new Random();
        String randomString = Integer.toString(rand.nextInt()) + (System.currentTimeMillis() / 1000L);
        Log.i("TRANSANCTION_ID", "--"+"TRANS"+randomString);
        transactionId="TRANS"+randomString.substring(1, 11);

        Random r = new Random(System.currentTimeMillis());
        String transId = "TRANS" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);
        Log.i("TRANS_ID",transId);
      }

/*  Services*/
    private void subscriptionPlanListServicehit()
    {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.addTextBody("token",SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getString(SPreferenceKey.TOKEN));

        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setActivity(getActivity());
        if (SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
        {
            serviceBean.setMethodName("Consumers/plans_list");
        }
        else
        {
            serviceBean.setMethodName("Services/plans_list");
        }
        serviceBean.setApilistener(SubscriptionPlanFragment.this);

        CustomHandler customHandler = new CustomHandler(serviceBean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);
    }

    private void planPurchaseServiceHit()
    {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN));
        multipartEntityBuilder.addTextBody("planId", id);
        multipartEntityBuilder.addTextBody("transactionId", transactionId);
        multipartEntityBuilder.addTextBody("payamount", payamount);

        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setActivity(getActivity());
        if (SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
        {
            serviceBean.setMethodName("Consumers/planpayment");
        }
        else
        {   
            serviceBean.setMethodName("Services/planpayment");
        }
        serviceBean.setApilistener(SubscriptionPlanFragment.this);

        CustomHandler customHandler = new CustomHandler(serviceBean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);
    }



    @Override
    public void myServerResponse(JSONObject jsonObject)
    {
        try{
            if(jsonObject !=null)
            {
                if(jsonObject.getString("status").equalsIgnoreCase("SUCCESS"))
                {
                    if(jsonObject.getString("requestKey").equalsIgnoreCase("plans_list"))
                    {
                        JSONArray responseArr = jsonObject.getJSONArray("response");
//                     //   customer
                        if(SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
                        {

                            arrayList=new ArrayList<PlanBean>();
                            for(int i = 0;i<responseArr.length();i++)
                            {
                                JSONObject j_obj = responseArr.getJSONObject(i);
                                if(j_obj.getString("validity").equalsIgnoreCase("1 months"))
                                {
                                    monthlyid=j_obj.getString("id");
                                    monthlySubscriptionTitle.setText(getString(R.string.duration_myplan));
                                    monthlyPayAmount =j_obj.getString("payamount") ;
                                    monthlySubscriptionAmount.setText("$"+monthlyPayAmount);
                                    btn_MonthlySubscription_BuyNow.setOnClickListener(this);
                                    monthlySubscription_description.setText("Monthly credits will be "+j_obj.getString("creadits")+" points.");

                                }
                                else {
                                    annualid=j_obj.getString("id");
                                    annualSubscriptionTitle.setText(getString(R.string.duration_annual_subscription));
                                    annualPayAmount =j_obj.getString("payamount") ;
                                    annualSubscriptionAmount.setText("$"+annualPayAmount);
                                    btn_AnnualSubscription_BuyNow.setOnClickListener(this);
                                    monthlySubscription_description.setText("Annual credits will be "+j_obj.getString("creadits")+" points.");
                                }
                            }
                        }

//                   //  service provider
                        else {
                                for (int i = 0; i < responseArr.length(); i++)
                                {
                                    JSONObject object = responseArr.getJSONObject(i);
                                    String planType = object.getString("plan_type");
                                    String rate = object.getString("rate");
                                    String description = object.getString("description");
                                    if (i == 0)
                                    {
                                        monthlyid=object.getString("id");
                                        monthlySubscriptionTitle.setText(getString(R.string.duration_myplan));
                                        monthlyPayAmount = rate;
                                        monthlySubscriptionAmount.setText("$"+monthlyPayAmount);
                                        monthlySubscription_description.setText(description);
                                        btn_MonthlySubscription_BuyNow.setOnClickListener(this);
                                    }
                                    else {
                                        annualid=object.getString("id");
                                        annualSubscriptionTitle.setText(getString(R.string.duration_annual_subscription));
                                        annualPayAmount = rate;
                                        annualSubscriptionAmount.setText("$"+annualPayAmount);
                                        annualSubscription_description.setText(description);
                                        btn_AnnualSubscription_BuyNow.setOnClickListener(this);
                                    }
                                }
                             }
                    }
                    if(jsonObject.getString("requestKey").equalsIgnoreCase("planpayment"))
                    {
                        JSONObject response = jsonObject.getJSONObject("response");
                        String planId = response.getString("plan_id");

//                   customer
                        if(SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
                        {
                            if(planId.equalsIgnoreCase("1"))
                            {
                                Toast.makeText(getActivity(), "Monthly Plan purchased Successfully.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getActivity(), "Annual Plan purchased Successfully.", Toast.LENGTH_SHORT).show();
                            }

                          ((HomeActivity) getActivity()).getSupportFragmentManager().popBackStack();

                        }
//                service provider
                        else
                        {
                            if(planId.equalsIgnoreCase("1"))
                            {
                                Toast.makeText(getActivity(), "Monthly Plan purchased Successfully.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getActivity(), "Annual Plan purchased Successfully.", Toast.LENGTH_SHORT).show();
                            }

                            ((HomeActivityServicePro) getActivity()).getSupportFragmentManager().popBackStack();
                        }

                    }

                }
                else {
                    Log.v("JSON_Response", ""+jsonObject.toString());
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.button_buyNow_monthlysubscription:

                id=monthlyid;
                payamount = monthlyPayAmount;
                planPurchaseServiceHit();
                break;

            case R.id.button_buyNow_annualSubscription:
                id=annualid;
                payamount = annualPayAmount;
                planPurchaseServiceHit();
                break;
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

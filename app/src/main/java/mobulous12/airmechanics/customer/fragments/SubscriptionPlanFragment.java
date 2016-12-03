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

import mobulous12.airmechanics.R;
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

        if(SharedPreferenceWriter.getInstance(getActivity()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
        {
            ((HomeActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.headername_subscriptionplan));
            ((HomeActivity)getActivity()).setNavigationIcon();

            customerSubsPlanServiceHit();
        }
        else
        {
            ((HomeActivityServicePro)getActivity()).setToolbarTitleSP(getResources().getString(R.string.headername_subscriptionplan));
            ((HomeActivityServicePro)getActivity()).setNavigationIconSP();
            spSubsPlanServiceHit();
        }

//
//        recyclerView_subscriptionPlan = (RecyclerView) view.findViewById(R.id.recyclerView_subscriptionPlan);
//        subsPlanRecyclerAdapter = new SubscriptionPlanRecyclerAdapter(getActivity());
//        recyclerView_subscriptionPlan.setAdapter(subsPlanRecyclerAdapter);
//        recyclerView_subscriptionPlan.setLayoutManager(new LinearLayoutManager(getActivity()));



        return view;
    }


    private void customerSubsPlanServiceHit()
    {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.addTextBody("token",SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getString(SPreferenceKey.TOKEN));

        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setActivity(getActivity());
        serviceBean.setMethodName("Consumers/plans_list");
        serviceBean.setApilistener(SubscriptionPlanFragment.this);

        CustomHandler customHandler = new CustomHandler(serviceBean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);
    }

    private void spSubsPlanServiceHit()
    {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.addTextBody("token",SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getString(SPreferenceKey.TOKEN));

        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setActivity(getActivity());
        serviceBean.setMethodName("Services/plans_list");
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

                            for(int i = 0;i<responseArr.length();i++)
                            {
                                JSONObject j_obj = responseArr.getJSONObject(i);
                                String amount =j_obj.getString("payamount");
                                String credits = j_obj.getString("creadits");
                                String duration = j_obj.getString("validity");
                                if(i == 0)
                                {
                                    monthlySubscriptionTitle.setText(getString(R.string.duration_myplan));
                                    monthlySubscriptionAmount.setText("$"+amount);
                                    btn_MonthlySubscription_BuyNow.setOnClickListener(this);

                                }
                                else {
                                    annualSubscriptionTitle.setText(getString(R.string.duration_annual_subscription));
                                    annualSubscriptionAmount.setText("$"+amount);
                                    btn_AnnualSubscription_BuyNow.setOnClickListener(this);
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
                                        monthlySubscriptionTitle.setText(getString(R.string.duration_myplan));
                                        monthlySubscriptionAmount.setText("$"+rate);
                                        monthlySubscription_description.setText(description);
                                        btn_MonthlySubscription_BuyNow.setOnClickListener(this);
                                    }
                                    else {
                                        annualSubscriptionTitle.setText(getString(R.string.duration_annual_subscription));
                                        annualSubscriptionAmount.setText("$"+rate);
                                        annualSubscription_description.setText(description);
                                        btn_AnnualSubscription_BuyNow.setOnClickListener(this);
                                    }
                                }
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

                Toast.makeText(getActivity(), "Monthly Plan purchased Successfully.", Toast.LENGTH_SHORT).show();
                if(getActivity() instanceof HomeActivity)
                {
                    ((HomeActivity) getActivity()).getSupportFragmentManager().popBackStack();
                }
                else
                {
                    ((HomeActivityServicePro) getActivity()).getSupportFragmentManager().popBackStack();
                }

                break;

            case R.id.button_buyNow_annualSubscription:

                Toast.makeText(getActivity(), "Annual Plan purchased Successfully.", Toast.LENGTH_SHORT).show();
                if(getActivity() instanceof HomeActivity)
                {
                    ((HomeActivity) getActivity()).getSupportFragmentManager().popBackStack();
                }
                else
                {
                    ((HomeActivityServicePro) getActivity()).getSupportFragmentManager().popBackStack();
                }

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

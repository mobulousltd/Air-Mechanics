package mobulous12.airmechanics.customer.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.ServiceProviderBean;
import mobulous12.airmechanics.customer.adapters.ServiceProviderRecyclerAdapter;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

public class ServiceProviderActivity extends AppCompatActivity implements View.OnClickListener, ApiListener {

    ArrayList<ServiceProviderBean> serviceProviderArrayList;

    private RecyclerView recyclerView_ServiceProvider;
    private TextView textView_Sorting,textView_Filter;
    private ServiceProviderRecyclerAdapter serviceProviderRecyclerAdapter;
    public AlertDialog.Builder alertbuilder_Sort,alertbuilder_Filter;
    private ImageView back_ServiceProvider;
    private LayoutInflater inflater;
    private String filter="";
    private int d=0,r=0,p=0;
    private SwipeRefreshLayout swipeRefresh;
    private boolean isListEmpty = false;
    private int counterForList = 0;
    private TextView tv_noListSp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_service_provider);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        tv_noListSp = (TextView) findViewById(R.id.tv_noListSp);
        if (tv_noListSp != null) {
            tv_noListSp.setVisibility(View.GONE);
        }

        recyclerView_ServiceProvider = (RecyclerView) findViewById(R.id.recyclerView_ServiceProvider);
        textView_Sorting = (TextView) findViewById(R.id.textView_Sorting);
        textView_Filter= (TextView) findViewById(R.id.textView_Filter);
        back_ServiceProvider = (ImageView) findViewById(R.id.back_ServiceProviderActivity);
        inflater = getLayoutInflater();
        textView_Sorting.setOnClickListener(this);
        textView_Filter.setOnClickListener(this);
        back_ServiceProvider.setOnClickListener(this);
        ServiceProviderBean bean=getIntent().getParcelableExtra("bean");
        if(bean!=null)
        {
            filter=bean.getCategory();
        }
        serviceProviderList();

//              Load items again
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//              Refresh items
                d=0;
                r=0;
                p=0;
                filter = "";
                serviceProviderList();
            }
        });

    }


    private void showSortDialog()
    {
        alertbuilder_Sort = new AlertDialog.Builder(this);
        alertbuilder_Sort.setCancelable(false);
        View view1=inflater.inflate(R.layout.popup_title_sort, null);
        alertbuilder_Sort.setCustomTitle(view1);
        View view2 = inflater.inflate(R.layout.sort_your_list,null);
        final RadioGroup rdGrp_Sorting = (RadioGroup) view2.findViewById(R.id.radioGroup_Sorting);
        rdGrp_Sorting.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId)
                {
                    case R.id.radio1_byDistance:
                        d=1;
                        r=0;
                        p=0;
                        break;
                    case R.id.radio2_byRating:
                        d=0;
                        r=1;
                        p=0;
                        break;
                    case R.id.radio3_byPrice:
                        d=0;
                        r=0;
                        p=1;
                        break;
                }
            }
        });
        if(d==1)
        {
            ((RadioButton)view2.findViewById(R.id.radio1_byDistance)).setChecked(true);
        }
        if(r==1)
        {
            ((RadioButton)view2.findViewById(R.id.radio2_byRating)).setChecked(true);
        }
        if(p==1)
        {
            ((RadioButton)view2.findViewById(R.id.radio3_byPrice)).setChecked(true);
        }
        alertbuilder_Sort.setView(view2);
        alertbuilder_Sort.setPositiveButton(getResources().getString(R.string.popup_done), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

                serviceProviderList();
            }
        });
        alertbuilder_Sort.setNegativeButton(getResources().getString(R.string.popup_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = alertbuilder_Sort.create();
        alertDialog.show();
        //Buttons
        Button positive_button =  alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negative_button =  alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        setPositiveNegativeButtonColor(positive_button,negative_button);

    }


    private void showFilterDialog()
    {
        alertbuilder_Filter = new AlertDialog.Builder(this);
        alertbuilder_Filter.setCancelable(false);

        //  ALertDialog - title
        View view1=inflater.inflate(R.layout.popup_title_filter, null);
        alertbuilder_Filter.setCustomTitle(view1);

        //  ALertDialog - content view
        View view2 = inflater.inflate(R.layout.filter_option,null);
        switch (filter)
        {
            case "two":
                ((RadioButton)view2.findViewById(R.id.radio1_twoWheeler)).setChecked(true);
                break;
            case "light":
                ((RadioButton)view2.findViewById(R.id.radio2_lightWeight)).setChecked(true);
                break;
            case "heavy":
                ((RadioButton)view2.findViewById(R.id.radio3_heavyWeight)).setChecked(true);
                break;
        }

        final RadioGroup rdGrp_Filter = (RadioGroup) view2.findViewById(R.id.radioGroup_Filter);
        rdGrp_Filter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId)
                {
                    case R.id.radio1_twoWheeler:
                        filter = "two";
                        break;
                    case R.id.radio2_lightWeight:
                        filter = "light";
                        break;
                    case R.id.radio3_heavyWeight:
                        filter = "heavy";
                        break;
                }
            }
        });
        alertbuilder_Filter.setView(view2);
        alertbuilder_Filter.setPositiveButton(getResources().getString(R.string.popup_done), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                serviceProviderList();
            }
        });
        alertbuilder_Filter.setNegativeButton(getResources().getString(R.string.popup_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = alertbuilder_Filter.create();
        alertDialog.show();
        //Buttons
        Button positive_button =  alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negative_button =  alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        setPositiveNegativeButtonColor(positive_button,negative_button);
    }

    private void setPositiveNegativeButtonColor(Button positive,Button negative)
    {

//        Font.setFontButton(positive,this);
        positive.setTextColor(getResources().getColor(R.color.blue));

//        Font.setFontButton(negative,this);
        negative.setTextColor(getResources().getColor(R.color.black));

    }

    private void serviceProviderList() {
        try
        {
            MultipartEntityBuilder multipartbuilder = MultipartEntityBuilder.create();
            multipartbuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartbuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.TOKEN));
            multipartbuilder.addTextBody("lat", SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.LATITUDE));
            multipartbuilder.addTextBody("long", SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.LONGITUDE));
            multipartbuilder.addTextBody("category_id",filter);
            multipartbuilder.addTextBody("distance",String.valueOf(d));
            multipartbuilder.addTextBody("rating",String.valueOf(r));
            multipartbuilder.addTextBody("price",String.valueOf(p));

            ServiceBean serviceBean = new ServiceBean();
            serviceBean.setMethodName("Consumers/list_serviceprovider");
            serviceBean.setActivity(ServiceProviderActivity.this);
            serviceBean.setApilistener(this);


            CustomHandler customHandler = new CustomHandler(serviceBean);
            customHandler.makeMultipartRequest(multipartbuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void myServerResponse(JSONObject responseObj) {

        try
        {
            if(responseObj!=null)
            {
                if(responseObj.getString("status").equalsIgnoreCase("SUCCESS") && responseObj.getString("requestKey").equalsIgnoreCase("list_serviceprovider"))
                {
                    JSONArray jsonArray=responseObj.getJSONArray("response");

                    if(jsonArray.length() == 0)
                    {
                        Log.w("SP_LIST", ""+responseObj.toString());
                        String msg = responseObj.getString("message");
                        tv_noListSp.setVisibility(View.VISIBLE);
                        tv_noListSp.setText(msg);

                    }
                    else {
                        tv_noListSp.setVisibility(View.GONE);
                    }

                    serviceProviderArrayList=new ArrayList<ServiceProviderBean>();
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                        ServiceProviderBean serviceproviderbean = new ServiceProviderBean();
                        serviceproviderbean.setLng(jsonobject.getString("long"));
                        serviceproviderbean.setLat(jsonobject.getString("lat"));
                        serviceproviderbean.setAddress(jsonobject.getString("address"));
                        serviceproviderbean.setId(jsonobject.getString("id"));
                        serviceproviderbean.setName(jsonobject.getString("name"));
                        serviceproviderbean.setCompanyName(jsonobject.getString("companyName"));
                        serviceproviderbean.setProfile_thumb(jsonobject.getString("profile_thumb"));
                        serviceproviderbean.setProfile(jsonobject.getString("profile"));
                        serviceproviderbean.setContact_no(jsonobject.getString("contact_no"));
                        serviceproviderbean.setEmail(jsonobject.getString("email"));
                        serviceproviderbean.setCategory(jsonobject.getString("work_category"));
                        serviceproviderbean.setWorkingdays(jsonobject.getString("workingDays"));
                        serviceproviderbean.setReviews(jsonobject.getString("review"));
                        serviceproviderbean.setRating(jsonobject.getString("rating"));
                        serviceproviderbean.setRequestCount(jsonobject.getString("review_count"));

                        serviceProviderArrayList.add(serviceproviderbean);
                    }
                    if (jsonArray.length() > 0)
                    {
                        counterForList++;
                    }
                    serviceProviderRecyclerAdapter = new ServiceProviderRecyclerAdapter(this, serviceProviderArrayList);
                    recyclerView_ServiceProvider.setAdapter(serviceProviderRecyclerAdapter);
                    serviceProviderRecyclerAdapter.onItemClickListener(new ServiceProviderRecyclerAdapter.MyClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Intent intent = new Intent(ServiceProviderActivity.this,ServiceProviderDetailActivity.class);
                            intent.putExtra("bean",serviceProviderArrayList.get(position));
                            startActivity(intent);
                        }
                    });
                        recyclerView_ServiceProvider.setLayoutManager(new LinearLayoutManager(this));

                    // Load complete
                    swipeRefresh.setRefreshing(false);
                }
                else
                {
                    Toast toast = Toast.makeText(this,responseObj.getString("message"), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }



                Log.e("JsonResponse_SPA", ""+responseObj.toString());


            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id)
        {
            case R.id.textView_Sorting:
                if (counterForList > 0)
                {
                    showSortDialog();
                }
                break;

            case R.id.textView_Filter:
                if (counterForList > 0)
                {
                    showFilterDialog();
                }
                break;

            case R.id.back_ServiceProviderActivity:
                     finish();
                break;
        }

    }

    @Override
    public void onBackPressed() {

        finish();
    }
}

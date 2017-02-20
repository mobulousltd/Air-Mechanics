package mobulous12.airmechanics.serviceprovider.fragments;


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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.EarningsBean;
import mobulous12.airmechanics.databinding.FragMyEarningsBinding;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;
import mobulous12.airmechanics.serviceprovider.adapters.MyEarningsAdapter;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;


public class MyEarningsFragment_SP extends Fragment implements ApiListener {

    private View view;
    private static int  offSet = 0;
    private static int c = 0;
    private ArrayList<EarningsBean> arrayList;
    private FragMyEarningsBinding binding;
    private MyEarningsAdapter adapter;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater,R.layout.frag_my_earnings,container,false);
        view=binding.getRoot();
        ((HomeActivityServicePro) getActivity()).setToolbarTitleSP("My Earnings");
        ((HomeActivityServicePro) getActivity()).setNavigationIconSP();


//        if (c == 0)
//        {
//            offSet = c;
//            ++c;
//        }
//        else {
//
//            offSet += 10;
//        }

        myEarningsListService();  // MyEarnings List Service Hit

        return view;
    }


/*SERVICES*/
    private void myEarningsListService()
    {
        MultipartEntityBuilder multipartbuilder = MultipartEntityBuilder.create();
        multipartbuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartbuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getString(SPreferenceKey.TOKEN));
        multipartbuilder.addTextBody("offset", String.valueOf(offSet));

        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setMethodName("Services/my_earnings");
        serviceBean.setActivity(getActivity());
        serviceBean.setApilistener(this);

        CustomHandler customHandler = new CustomHandler(serviceBean);
        customHandler.makeMultipartRequest(multipartbuilder);
    }


/*Responses*/
    @Override
    public void myServerResponse(JSONObject responseObj) {
      try {
            if (responseObj != null)
            {
                if (responseObj.getString("status").equalsIgnoreCase("SUCCESS") && responseObj.getString("requestKey").equalsIgnoreCase("my_earnings"))
                {
                    Log.w("my_earnings", ""+responseObj.toString());

                    JSONArray responseArray = responseObj.getJSONArray("response");

                    if(responseArray.length() == 0)
                    {
                        binding.tvNoEarnings.setVisibility(View.VISIBLE);
                    }
                    else {
                        binding.tvNoEarnings.setVisibility(View.GONE);
                    }

                    arrayList  = new ArrayList<EarningsBean>();

                    for(int i =0;i<responseArray.length();i++)
                    {
                        JSONObject jsonObject = responseArray.getJSONObject(i);
                        EarningsBean bean = new EarningsBean();

                        bean.setCustomerName(jsonObject.getString("customer_name"));
                        bean.setPaidAmount(jsonObject.getString("paid_amount"));
                        bean.setDate(jsonObject.getString("date"));
                        bean.setAddress(jsonObject.getString("address"));

                        arrayList.add(bean);

                    }

                    /*RECYCLER VIEW and adapter functionality*/

                    adapter = new MyEarningsAdapter(getActivity(),arrayList);
                    binding.recyclerMyEarnings.setAdapter(adapter);
                    binding.recyclerMyEarnings.setLayoutManager(new LinearLayoutManager(getActivity()));

                }
                else {
                    Toast.makeText(getActivity(),responseObj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }

           } catch (JSONException e) {
            e.printStackTrace();
        }

    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.blank_at_right_menu,menu);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_show_service_provider).setVisible(false);
//        menu.findItem(R.id.action_show_myJob_Orders).setVisible(false);


    }

}

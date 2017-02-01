package mobulous12.airmechanics.serviceprovider.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import mobulous12.airmechanics.R;
import mobulous12.airmechanics.databinding.FragmentMyEarningsSpBinding;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

public class MyEarningsFragment_SP extends Fragment implements ApiListener {

    private View view;
    private TextView textView_nameMyEarning,textView_amountMyEarning,textView_dateEarning,descrip1_earning,descrip2_earning;
    private static int  offSet = 0;
    private static int c = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState)
    {
        FragmentMyEarningsSpBinding binding= DataBindingUtil.inflate(inflater, R.layout.fragment_my_earnings_sp, container, false);
        view=binding.getRoot();
        ((HomeActivityServicePro) getActivity()).setToolbarTitleSP("My Earnings");
        ((HomeActivityServicePro) getActivity()).setNavigationIconSP();

        textView_nameMyEarning = (TextView) view.findViewById(R.id.textView_nameMyEarning);
        textView_amountMyEarning = (TextView) view.findViewById(R.id.textView_amountMyEarning);
        textView_dateEarning = (TextView) view.findViewById(R.id.textView_dateEarning);
        descrip1_earning = (TextView) view.findViewById(R.id.descrip1_earning);
        descrip2_earning = (TextView) view.findViewById(R.id.descrip2_earning);

        if (c == 0)
        {
            offSet = c;
            ++c;
        }
        else {

            offSet += 10;
        }

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

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONObject;
import mobulous12.airmechanics.R;
import mobulous12.airmechanics.databinding.FragmentJobRequestDetailBinding;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

public class JobRequestDetailFragment extends Fragment implements ApiListener{

    String custid, reqid;
    View view;
    private EditText et_price;
    private boolean isPriceSent=false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        FragmentJobRequestDetailBinding binding=DataBindingUtil.inflate(inflater, R.layout.fragment_job_request_detail, container, false);
        view=binding.getRoot();
        et_price = (EditText) view.findViewById(R.id.editText_price_dynamic_job_request_detail);
        ((HomeActivityServicePro) getActivity()).setToolbarTitleSP("Job Request Detail");
        ((HomeActivityServicePro) getActivity()).setNavigationIconSP();
        reqid=getArguments().getString("reqid");

        view.findViewById(R.id.btn_send_job_request_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPriceSent)
                {
                    Toast.makeText(getActivity(),"You've already set the Price.",Toast.LENGTH_SHORT).show();
                    view.findViewById(R.id.btn_send_job_request_detail).setVisibility(View.GONE);
                }
                else
                {
                    if(et_price.getText().toString().trim().isEmpty())
                    {
                        Toast.makeText(getActivity(), "Please enter price for this job request.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        updatePriceServiceHit();
                    }
                }
            }
        });
        requestDetailServiceHit();
        return view;
    }

    //services
    private void requestDetailServiceHit()
    {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getString(SPreferenceKey.TOKEN));
        multipartEntityBuilder.addTextBody("requestId", reqid);
        ServiceBean bean = new ServiceBean();
        bean.setActivity(getActivity());
        bean.setMethodName("Services/request_details");
        bean.setApilistener(JobRequestDetailFragment.this);

        CustomHandler customHandler = new CustomHandler(bean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);
    }

    private void updatePriceServiceHit()
    {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.addTextBody("token",SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getString(SPreferenceKey.TOKEN));
        multipartEntityBuilder.addTextBody("requestId",reqid);
        multipartEntityBuilder.addTextBody("price",et_price.getText().toString().trim());

        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setActivity(getActivity());
        serviceBean.setMethodName("Services/update_price");
        serviceBean.setApilistener(JobRequestDetailFragment.this);

        CustomHandler customHandler = new CustomHandler(serviceBean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);

    }

    @Override
    public void myServerResponse(final JSONObject jsonObject) {
        try {
            if (jsonObject != null) {
                if (jsonObject.getString("status").equalsIgnoreCase("SUCCESS"))
                {
                    if(jsonObject.getString("requestKey").equalsIgnoreCase("request_details"))
                    {
                        JSONObject jsonObject1=jsonObject.getJSONObject("response").getJSONObject("user");
                        if(jsonObject1.getString("status").equals("request"))
                        {
                            isPriceSent=false;
                            view.findViewById(R.id.btn_send_job_request_detail).setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            view.findViewById(R.id.btn_send_job_request_detail).setVisibility(View.GONE);
                            isPriceSent=true;
                        }
                        ((TextView)view.findViewById(R.id.textView_title_job_request_detail)).setText(jsonObject1.getString("request_Title"));
                        ((TextView)view.findViewById(R.id.textView_description_job_request_detail)).setText(jsonObject1.getString("request_description"));
                        et_price.setText(jsonObject1.getString("minCharge"));
                        ((TextView)view.findViewById(R.id.textView_date_job_request_detail)).setText(jsonObject1.getString("requestDate"));
                        view.findViewById(R.id.ll_jobrequestdetail).setVisibility(View.VISIBLE);
                    }
                    if(jsonObject.getString("requestKey").equalsIgnoreCase("update_price"))
                    {
                        view.findViewById(R.id.btn_send_job_request_detail).setVisibility(View.GONE);
                        Toast.makeText(getActivity(),"Price sent to Customer Successfully",Toast.LENGTH_SHORT).show();
                        isPriceSent=true;
                    }
                }
            }
            else
            {
            }
            Log.e("JSON Response", ""+jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

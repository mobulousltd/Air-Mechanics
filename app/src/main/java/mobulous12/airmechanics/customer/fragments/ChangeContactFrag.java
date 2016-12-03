package mobulous12.airmechanics.customer.fragments;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONObject;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.customer.activities.SignUpActivity;
import mobulous12.airmechanics.customer.activities.VerificationActivity;
import mobulous12.airmechanics.databinding.FragmentChangeContactBinding;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;


public class ChangeContactFrag extends Fragment implements ApiListener {

    private  View view;
    private static final int VERFICATION = 003;
    private EditText editText_newContact;



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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentChangeContactBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_change_contact, container, false);
        view = binding.getRoot();
        if(SharedPreferenceWriter.getInstance(getActivity()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
        {
            ((HomeActivity) getActivity()).setToolbarTitle(getResources().getString(R.string.headername_changecontact));
            ((HomeActivity) getActivity()).toolbarVisible();
            ((HomeActivity) getActivity()).setNavigationIcon();
            Font.setFontHeader(HomeActivity.toolbar_title, getActivity());
        }
        else
        {
            ((HomeActivityServicePro)getActivity()).setToolbarTitleSP(getResources().getString(R.string.headername_changecontact));
            ((HomeActivityServicePro) getActivity()).toolbarVisibleSP();
            ((HomeActivityServicePro)getActivity()).setNavigationIconSP();
            Font.setFontHeader(HomeActivityServicePro.toolbar_titleSP, getActivity());
        }
        /*Views*/
        editText_newContact = (EditText) view.findViewById(R.id.editText_newContact);
        view.findViewById(R.id.submit_newContact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Patterns.PHONE.matcher(editText_newContact.getText().toString()).matches() && !editText_newContact.getText().toString().isEmpty() && (editText_newContact.getText().toString().length()>8 || editText_newContact.getText().toString().length()<15)) {

                    senCodeServiceHit();
                }else {
                    Toast.makeText(getActivity(),"Please enter valid Contact Number.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }





    //services
    private void senCodeServiceHit() {
        try {

            MultipartEntityBuilder multipartbuilder = MultipartEntityBuilder.create();
            multipartbuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartbuilder.addTextBody("country_code", "+91");
            multipartbuilder.addTextBody("mobile", editText_newContact.getText().toString().trim());
            multipartbuilder.addTextBody("email", "");
            ServiceBean serviceBean = new ServiceBean();
            if (SharedPreferenceWriter.getInstance(getActivity()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
            {
                serviceBean.setMethodName("Consumers/sendCode");
            }
            else
            {
                serviceBean.setMethodName("Services/sendCode");
            }
            serviceBean.setActivity(getActivity());
            serviceBean.setApilistener(this);
            CustomHandler customHandler = new CustomHandler(serviceBean);
            customHandler.makeMultipartRequest(multipartbuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void myServerResponse(JSONObject jsonObject) {

        try
        {
            if(jsonObject!=null)
            {
                if(jsonObject.getString("status").equalsIgnoreCase("SUCCESS") && jsonObject.getString("requestKey").equalsIgnoreCase("sendCode"))
                {
                    String code = jsonObject.getJSONObject("response").getString("verification_code");
                    Intent intent  =new Intent(getActivity(), VerificationActivity.class);
                    intent.putExtra("vcode",code);
                    intent.putExtra("number", editText_newContact.getText().toString());
                    startActivityForResult(intent, VERFICATION);
                }
                else
                {

                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
                Log.i("JsonResponse", ""+jsonObject.toString());

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==getActivity().RESULT_OK)
        {
            if(requestCode==VERFICATION)
            {
//                Intent intent=new Intent();
//                intent.putExtra("number", "dsfasd");
//                getTargetFragment().onActivityResult(getTargetRequestCode(), getActivity().RESULT_OK, intent);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
    }
}

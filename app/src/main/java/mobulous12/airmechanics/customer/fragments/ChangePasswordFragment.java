package mobulous12.airmechanics.customer.fragments;


import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONObject;

import java.io.File;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.databinding.FragmentChangePasswordBinding;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

public class ChangePasswordFragment extends Fragment implements View.OnClickListener, ApiListener{

    private View view;
    private TextView headername_changePassFrag;
    private EditText editText_old_pass,editText_new_pass,editText_confirm_new_pass;

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
//        if(SharedPreferenceWriter.getInstance(getActivity()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
//        {
//            menu.findItem(R.id.action_show_service_provider).setVisible(false);
//        }
//        else {
//            menu.findItem(R.id.action_show_myJob_Orders).setVisible(false);
//        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        FragmentChangePasswordBinding binding=DataBindingUtil.inflate(inflater,R.layout.fragment_change_password, container, false);
        view=binding.getRoot();
        if(SharedPreferenceWriter.getInstance(getActivity()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
        {
            ((HomeActivity) getActivity()).setToolbarTitle(getResources().getString(R.string.headername_change_password));
            ((HomeActivity) getActivity()).toolbarGone();
            ((HomeActivity) getActivity()).setNavigationIcon();
        }
        else
        {
            ((HomeActivityServicePro)getActivity()).setToolbarTitleSP(getResources().getString(R.string.headername_change_password));
            ((HomeActivityServicePro)getActivity()).toolbarGoneSP();
            ((HomeActivityServicePro)getActivity()).setNavigationIconSP();

        }
        headername_changePassFrag  = (TextView) view.findViewById(R.id.headername_changePassFrag);
        editText_old_pass  = (EditText) view.findViewById(R.id.editText_old_pass);
        editText_new_pass  = (EditText) view.findViewById(R.id.editText_new_pass);
        editText_confirm_new_pass  = (EditText) view.findViewById(R.id.editText_confirm_new_pass);

        view.findViewById(R.id.button_submitChangePassword).setOnClickListener(this);
        view.findViewById(R.id.back_changePassFrag).setOnClickListener(this);

        return view;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.button_submitChangePassword:
                if(editText_old_pass.getText().toString().isEmpty())
                {
                    Toast.makeText(getActivity(),"Please enter your Current password",Toast.LENGTH_SHORT).show();
                }
                else if(editText_old_pass.getText().toString().length()<6)
                {
                    Toast.makeText(getActivity(),"Please enter valid current password.",Toast.LENGTH_SHORT).show();
                }
                else if(editText_new_pass.getText().toString().isEmpty())
                {
                    Toast.makeText(getActivity(),"Please enter your New password",Toast.LENGTH_SHORT).show();
                }
                else if(editText_new_pass.getText().toString().length()<6)
                {
                    Toast.makeText(getActivity(),"Please enter valid New password",Toast.LENGTH_SHORT).show();
                }
                else if(editText_confirm_new_pass.getText().toString().isEmpty())
                {
                    Toast.makeText(getActivity(),"Please confirm your New password",Toast.LENGTH_SHORT).show();
                }
                else if(editText_confirm_new_pass.getText().toString().length()<6)
                {
                    Toast.makeText(getActivity(),"Please enter valid password",Toast.LENGTH_SHORT).show();
                }
                else if(!editText_confirm_new_pass.getText().toString().equals(editText_new_pass.getText().toString()))
                {
                    Toast.makeText(getActivity(),"New Password and confirm password doesn't matched",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    changePassServiceHit();
                }
                break;
            case R.id.back_changePassFrag:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }

    }

    //services
    private void changePassServiceHit() {

        MultipartEntityBuilder multipartbuilder = MultipartEntityBuilder.create();
        multipartbuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartbuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN));
        multipartbuilder.addTextBody("oldPass", editText_old_pass.getText().toString());
        multipartbuilder.addTextBody("newPass", editText_new_pass.getText().toString());
        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setActivity(getActivity());
        if (SharedPreferenceWriter.getInstance(getActivity()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
        {
            serviceBean.setMethodName("Consumers/resetPassword");
        }
        else {
            serviceBean.setMethodName("Services/resetPassword");
        }
        serviceBean.setApilistener(this);

        CustomHandler customHandler = new CustomHandler(serviceBean);
        customHandler.makeMultipartRequest(multipartbuilder);
    }

    @Override
    public void myServerResponse(JSONObject responseObj) {

        try
        {
            if (responseObj != null)
            {
                if (responseObj.getString("status").equalsIgnoreCase("SUCCESS"))
                {
                    if (responseObj.getString("requestKey").equalsIgnoreCase("resetPassword"))
                    {
                       getActivity(). getSupportFragmentManager().popBackStack();
                        Toast.makeText(getActivity(),"Your Password is changed successfully!",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "" + responseObj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
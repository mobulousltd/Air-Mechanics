package mobulous12.airmechanics.customer.activities;


import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONObject;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.serviceprovider.dialogs.ForgotPassSendFragmentSP;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;


public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener, ApiListener{


    ImageView backArrow_ForgotPassword;
    TextView textView_registeredContactEmail,headername_forgotPassword;
    Button button_send_forgotPasswordActivity,button_cancel_forgotPasswordActivity;
    RelativeLayout root_forgot_password;
    private EditText editText_emailOrContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);

        backArrow_ForgotPassword = (ImageView) findViewById(R.id.back_ForgotPasswordActivity);
        textView_registeredContactEmail = (TextView) findViewById(R.id.textView_registeredContactEmail);
        editText_emailOrContact = (EditText) findViewById(R.id.editText_emailOrContactInput);
        headername_forgotPassword = (TextView) findViewById(R.id.headername_forgotPassword);
        button_send_forgotPasswordActivity = (Button) findViewById(R.id.button_send_forgotPasswordActivity);
        button_cancel_forgotPasswordActivity = (Button) findViewById(R.id.button_cancel_forgotPasswordActivity);
        root_forgot_password = (RelativeLayout) findViewById(R.id.activity_forgot_password);
        try{root_forgot_password.setOnClickListener(this);}
        catch (Exception e){e.printStackTrace();}
        backArrow_ForgotPassword.setOnClickListener(this);
        button_send_forgotPasswordActivity.setOnClickListener(this);
        button_cancel_forgotPasswordActivity.setOnClickListener(this);

    }


    //services
    private void forgotpasswordServiceHit() {
        MultipartEntityBuilder multipartbuilder = MultipartEntityBuilder.create();
        multipartbuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartbuilder.addTextBody("mobile", editText_emailOrContact.getText().toString());
        multipartbuilder.addTextBody("country_code", "91");
        multipartbuilder.addTextBody("device_token", SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.DEVICETOKEN));
        multipartbuilder.addTextBody("device_type", "android");

        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setActivity(ForgotPasswordActivity.this);
        serviceBean.setApilistener(this);
        if (SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
        {
            serviceBean.setMethodName("Consumers/forgotPassword");
        } else {
            serviceBean.setMethodName("Services/forgotPassword");
        }
        CustomHandler customHandler = new CustomHandler(serviceBean);
        customHandler.makeMultipartRequest(multipartbuilder);
    }

    @Override
    public void myServerResponse(JSONObject responseObj){
        try {
            if (responseObj != null) {
                if (responseObj.getString("status").equalsIgnoreCase("SUCCESS"))
                {
                    if (responseObj.getString("requestKey").equalsIgnoreCase("forgotPassword"))
                    {
                        if(SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.CUSTOMER_LOGIN)) {
                            Toast.makeText(ForgotPasswordActivity.this, "Your Password will be send to your Phone Number.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else if(SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.SERVICE_PROVIDER_LOGIN))
                        {
                            ForgotPassSendFragmentSP forgotPassSendFragmentSP = new ForgotPassSendFragmentSP();
                            forgotPassSendFragmentSP.show(getSupportFragmentManager(),"forgotPassSendFragSP");

                        }
                    }
                }
                else
                {
                    Toast.makeText(ForgotPasswordActivity.this, ""+responseObj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {

        }
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.activity_forgot_password:
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                break;
            case R.id.back_ForgotPasswordActivity:
                if(SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.CUSTOMER_LOGIN)) {

                    finish();
                }
                else if(SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.SERVICE_PROVIDER_LOGIN))
                {
                    finish();

                }


                break;
            case  R.id.button_send_forgotPasswordActivity:
                if ((Patterns.PHONE.matcher(editText_emailOrContact.getText().toString()).matches())||(Patterns.EMAIL_ADDRESS.matcher(editText_emailOrContact.getText().toString()).matches()))
                {
                    forgotpasswordServiceHit();
                }
                else
                {
                    if (editText_emailOrContact.getText().toString().trim().isEmpty())
                    {
                        Toast.makeText(this, "Please enter Email/Contact number", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Enter valid Contact Number or Email Address", Toast.LENGTH_SHORT).show();
                }
                break;
            case  R.id.button_cancel_forgotPasswordActivity:

                finish();

                break;
        }

    }

    @Override
    public void onBackPressed() {

        finish();

    }


}

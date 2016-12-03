package mobulous12.airmechanics.customer.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.ProfileBean;
import mobulous12.airmechanics.customer.fragments.SendCodeFragment;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

import static mobulous12.airmechanics.R.id.editText_contact_number;
import static mobulous12.airmechanics.R.id.editText_email;
import static mobulous12.airmechanics.R.id.editText_password;


public class VerificationActivity extends AppCompatActivity implements View.OnClickListener, ApiListener {

    String code, email="";
    public static VerificationActivity verificationActivity;
    private EditText editText_verification_code;
    ProfileBean bean;

    TextView headername_verification,textView_verification_code,textView_dont_get_verification_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataBindingUtil.setContentView(this, R.layout.activity_verification);
        verificationActivity = this;
        code = getIntent().getStringExtra("vcode");
        bean=getIntent().getParcelableExtra("customerdata");

        editText_verification_code = (EditText) findViewById(R.id.editText_verification_code);
        headername_verification = (TextView) findViewById(R.id.headername_verification);
        textView_verification_code = (TextView) findViewById(R.id.textView_verification_code);
        textView_dont_get_verification_code = (TextView) findViewById(R.id.textView_dont_get_verification_code);

        findViewById(R.id.back_VerificationActivity).setOnClickListener(this);
        findViewById(R.id.button_Send_VerificationActivity).setOnClickListener(this);
        findViewById(R.id.button_Resend_VerificationActivity).setOnClickListener(this);
        findViewById(R.id.activity_verification).setOnClickListener(this);
        editText_verification_code.setText(getIntent().getStringExtra("vcode"));
        editText_verification_code.setSelection(editText_verification_code.getText().toString().length());
    }

    //services
    private void senCodeServiceHit()
    {
        try {
            MultipartEntityBuilder multipartbuilder = MultipartEntityBuilder.create();
            multipartbuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartbuilder.addTextBody("country_code", "+91");
            multipartbuilder.addTextBody("mobile", bean.getContactno());
            multipartbuilder.addTextBody("email", email);
            ServiceBean serviceBean = new ServiceBean();
            if (SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
            {
                serviceBean.setMethodName("Consumers/sendCode");
            }
            else {
                serviceBean.setMethodName("Services/sendCode");
            }

            serviceBean.setActivity(this);
            serviceBean.setApilistener(this);
            CustomHandler customHandler = new CustomHandler(serviceBean);
            customHandler.makeMultipartRequest(multipartbuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeContactServiceHit() {
        try {

            MultipartEntityBuilder multipartbuilder = MultipartEntityBuilder.create();
            multipartbuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartbuilder.addTextBody("country_code", "+91");
            multipartbuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.TOKEN));
            multipartbuilder.addTextBody("contact_no", getIntent().getStringExtra("number"));
            ServiceBean serviceBean = new ServiceBean();
            if (SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
            {
                serviceBean.setMethodName("Consumers/change_contact");
            }
            else
            {
                serviceBean.setMethodName("Services/change_contact");
            }
            serviceBean.setActivity(this);
            serviceBean.setApilistener(this);
            CustomHandler customHandler = new CustomHandler(serviceBean);
            customHandler.makeMultipartRequest(multipartbuilder);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void signUpServiceHit()
    {
        try
        {
//            ,,email,password,country_code,contact_no,address,device_type,device_token,lat,long,category,from,to,working_days,employees,description,min_charges,specility

            MultipartEntityBuilder multipartbuilder = MultipartEntityBuilder.create();
            multipartbuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartbuilder.addTextBody("fullname", bean.getFullname());
            multipartbuilder.addTextBody("email", bean.getEmail());
            multipartbuilder.addTextBody("password", bean.getPassword());
            multipartbuilder.addTextBody("country_code", "+91");
            multipartbuilder.addTextBody("contact_no",  bean.getContactno());
            multipartbuilder.addTextBody("address",  bean.getAddress());
            multipartbuilder.addTextBody("city", "noida");
            multipartbuilder.addTextBody("companyName", bean.getCompanyname());
            multipartbuilder.addTextBody("category", bean.getCategory());
            multipartbuilder.addTextBody("from", bean.getFrom());
            multipartbuilder.addTextBody("to", bean.getTo());
            multipartbuilder.addTextBody("radius", bean.getRadius());
            multipartbuilder.addTextBody("working_days", bean.getWorking_days());
            multipartbuilder.addTextBody("employees", bean.getEmployees());
            multipartbuilder.addTextBody("min_charges", bean.getMnCharg());
            multipartbuilder.addTextBody("specility", bean.getSpeciality());
            multipartbuilder.addTextBody("description", "");
            if(bean.getImagePath().isEmpty())
            {
                multipartbuilder.addTextBody("profile","");
            }
            else {
                multipartbuilder.addBinaryBody("profile", new File(bean.getImagePath()));
            }
            multipartbuilder.addTextBody("lat", bean.getLat());
            multipartbuilder.addTextBody("long", bean.getLng());
            multipartbuilder.addTextBody("device_token", SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.DEVICETOKEN));
            multipartbuilder.addTextBody("device_type", "android");

            ServiceBean serviceBean = new ServiceBean();
            if (SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
            {
                serviceBean.setMethodName("Consumers/Signup");
            }
            else {
                serviceBean.setMethodName("Services/Signup");
            }
            serviceBean.setActivity(this);
            serviceBean.setApilistener(this);
            CustomHandler customHandler = new CustomHandler(serviceBean);
            customHandler.makeMultipartRequest(multipartbuilder);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void myServerResponse(JSONObject responseObj) {
        try
        {
            if(responseObj!=null)
            {
                if(responseObj.getString("status").equalsIgnoreCase("SUCCESS"))
                {
                    if(responseObj.getString("requestKey").equals("Signup"))
                    {
                        JSONObject response=responseObj.getJSONObject("response");
                        SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.LOGINTYPE, "normal");
                        SharedPreferenceWriter.getInstance(getApplicationContext()).writeBooleanValue(SPreferenceKey.LOGINKEY, true);
                        SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.TOKEN, response.getString("token"));
                        SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.FullName, response.getString("full_name"));
                        SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.Email, response.getString("email"));
                        SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.PhoneNumber, response.getString("contact_no"));
                        SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.ADDRESS, response.getString("address"));
                        SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.IMAGE, response.getString("profile"));
                        SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.FullName, response.getString("full_name"));
                        if(response.has("companyName")) {
                            SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.COMPANYNAME, response.getString("companyName"));
                        }
//                        if(response.getString("user_type").equalsIgnoreCase("customer"))
//                        {
//                            SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.CUSTOMER_LOGIN, true);
//                        }
//                        else
//                        {
//                            SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, true);
//                            new SendCodeFragment().show(getSupportFragmentManager(), "sendCodeSP");
//                        }
                        if(SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
                        {
                            new SendCodeFragment().show(getSupportFragmentManager(), "sendCode");
                        }
                        else
                        {
                            new SendCodeFragment().show(getSupportFragmentManager(), "sendCodeSP");
                        }
                    }
                    else if (responseObj.getString("requestKey").equals("sendCode"))
                    {
                        code = responseObj.getJSONObject("response").getString("verification_no");
                        editText_verification_code.setText(code);
                        editText_verification_code.setSelection(editText_verification_code.getText().toString().length());
                    }
                    else if(responseObj.getString("requestKey").equals("change_contact"))
                    {
                        setResult(RESULT_OK);
                        finish();
                    }
                }
                else
                {
                    Toast.makeText(VerificationActivity.this, ""+responseObj.getString("message"), Toast.LENGTH_SHORT).show();
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back_VerificationActivity:
                finish();
                break;
            case R.id.button_Send_VerificationActivity:
                if(editText_verification_code.getText().toString().equals(code))
                {
                    if(bean!=null)
                    {
                        signUpServiceHit();
                    }
                    else
                    {
                        changeContactServiceHit();
                    }
                }
                else
                {
                    Toast.makeText(VerificationActivity.this, "Please enter correct verification code", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.button_Resend_VerificationActivity:
                if(bean!=null)
                {
                    email=bean.getEmail();
                }
                senCodeServiceHit();

                break;
            case R.id.activity_verification:
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                break;
        }
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}

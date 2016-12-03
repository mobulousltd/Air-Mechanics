package mobulous12.airmechanics.serviceprovider.activities;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.BookingBean;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

public class JobOrderDetailActivity extends AppCompatActivity implements View.OnClickListener, ApiListener {

    BookingBean bean;
    private RelativeLayout rootServiceType;
    private RelativeLayout rootCustomerDetails;
    private RelativeLayout rootCustomerAddress;
    private RelativeLayout rootRequestDetail;


    private ImageView imgCategoryType;
    private ImageView imgCustomerDetails;
    private ImageView imgCustomerAddress;
    private ImageView imgRequestDetail;

    private boolean isCategoryTypeOpen = true;
    private boolean isCustomerDetailsOpen = true;
    private boolean isCustomerAddressOpen = true;
    private boolean isRequestDetailOpen= true;

    private TextView textViewCategoryType;
    private TextView textViewCustomerDetails;
    private TextView textViewCustomerAddress;
    private TextView textViewRequestDetail;

    private TextView textViewCategoryTypeDynamic;
    private TextView textViewCustomerDetailsDynamic;
    private TextView textViewCustomerAddressDynamic;
    private TextView textViewRequestDetailDynamic;

    private String jobStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.fragment_job_order_detail);
        bean= getIntent().getParcelableExtra("bookingbean");

        rootServiceType = (RelativeLayout) findViewById(R.id.root_service_type_jOD);
        rootCustomerDetails = (RelativeLayout) findViewById(R.id.root_customer_details_jOD);
        rootCustomerAddress = (RelativeLayout) findViewById(R.id.root_customer_address_jOD);
        rootRequestDetail = (RelativeLayout) findViewById(R.id.root_request_detail_jOD);

        findViewById(R.id.back_jOD).setOnClickListener(this);
        findViewById(R.id.button_cancel_jOD).setOnClickListener(this);
        findViewById(R.id.button_change_job_jOD).setOnClickListener(this);

        rootServiceType.setOnClickListener(this);
        rootCustomerDetails.setOnClickListener(this);
        rootCustomerAddress.setOnClickListener(this);
        rootRequestDetail.setOnClickListener(this);


        imgCategoryType = (ImageView) findViewById(R.id.imageView_service_type_jOD);
        imgCustomerDetails = (ImageView) findViewById(R.id.imageView_rightArrow_customer_details_jOD);
        imgCustomerAddress = (ImageView) findViewById(R.id.imageView_customer_address_jOD);
        imgRequestDetail = (ImageView) findViewById(R.id.imageView_request_detail_jOD);

        textViewCategoryType = (TextView) findViewById(R.id.textView_service_type_jOD);
        textViewCustomerDetails = (TextView) findViewById(R.id.textView_customer_details_jOD);
        textViewCustomerAddress = (TextView) findViewById(R.id.textView_customer_address_jOD);
        textViewRequestDetail = (TextView) findViewById(R.id.textView_request_detail_jOD);

        textViewCategoryTypeDynamic = (TextView) findViewById(R.id.textView_service_type_dynamic_jOD);
        textViewCustomerDetailsDynamic = (TextView) findViewById(R.id.textView_customer_details_dynamic_jOD);
        textViewCustomerAddressDynamic = (TextView) findViewById(R.id.textView_customer_address_dynamic_jOD);
        textViewRequestDetailDynamic = (TextView) findViewById(R.id.textView_request_detail_dynamic_jOD);

        textViewCategoryTypeDynamic.setVisibility(View.GONE);
        textViewCustomerDetailsDynamic.setVisibility(View.GONE);
        textViewCustomerAddressDynamic.setVisibility(View.GONE);
        textViewRequestDetailDynamic.setVisibility(View.GONE);

        // setting values
        switch (bean.getRequestcategory())
        {
            case "two":
                textViewCategoryTypeDynamic.setText("Two Wheeler");
                break;
            case "light":
                textViewCategoryTypeDynamic.setText("Light Weight vehicle");
                break;
            case "heavy":
                textViewCategoryTypeDynamic.setText("Heavy Weight vehicle");
                break;
        }

        textViewCustomerDetailsDynamic.setText("Customer Name : "+bean.getUserName()+ "\nCustomer Number : " +bean.getUsernumber());
        textViewCustomerAddressDynamic.setText(bean.getUseraddress());
        textViewRequestDetailDynamic.setText("Title : "+bean.getRequestname() +"\nDescription : "+ bean.getRequestdesc() +"\nMinimum Charge : $"+ bean.getMinCharge());

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.root_service_type_jOD:

                if (isCategoryTypeOpen)
                {
                    textViewCategoryTypeDynamic.setVisibility(View.VISIBLE);
                    isCategoryTypeOpen = false;
                    rootServiceType.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewCategoryType.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewCategoryType.setTextColor(getResources().getColor(R.color.white));
                    imgCategoryType.setImageResource(R.drawable.down_arrow);
                }
                else {
                    textViewCategoryTypeDynamic.setVisibility(View.GONE);
                    isCategoryTypeOpen = true;
                    rootServiceType.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewCategoryType.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewCategoryType.setTextColor(getResources().getColor(R.color.text_gray));
                    imgCategoryType.setImageResource(R.drawable.greyright_arrow);
                }

                break;
            case R.id.root_customer_details_jOD:

                if (isCustomerDetailsOpen)
                {
                    textViewCustomerDetailsDynamic.setVisibility(View.VISIBLE);
                    isCustomerDetailsOpen = false;
                    rootCustomerDetails.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewCustomerDetails.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewCustomerDetails.setTextColor(getResources().getColor(R.color.white));
                    imgCustomerDetails.setImageResource(R.drawable.down_arrow);
                }
                else {
                    textViewCustomerDetailsDynamic.setVisibility(View.GONE);
                    isCustomerDetailsOpen = true;
                    rootCustomerDetails.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewCustomerDetails.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewCustomerDetails.setTextColor(getResources().getColor(R.color.text_gray));
                    imgCustomerDetails.setImageResource(R.drawable.greyright_arrow);
                }

                break;
            case R.id.root_customer_address_jOD:

                if (isCustomerAddressOpen)
                {
                    textViewCustomerAddressDynamic.setVisibility(View.VISIBLE);
                    isCustomerAddressOpen = false;
                    rootCustomerAddress.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewCustomerAddress.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewCustomerAddress.setTextColor(getResources().getColor(R.color.white));
                    imgCustomerAddress.setImageResource(R.drawable.down_arrow);
                }
                else {
                    textViewCustomerAddressDynamic.setVisibility(View.GONE);
                    isCustomerAddressOpen = true;
                    rootCustomerAddress.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewCustomerAddress.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewCustomerAddress.setTextColor(getResources().getColor(R.color.text_gray));
                    imgCustomerAddress.setImageResource(R.drawable.greyright_arrow);
                }

                break;

            // REQUEST DETAIL
            case R.id.root_request_detail_jOD:
                if (isRequestDetailOpen)
                {
                    textViewRequestDetailDynamic.setVisibility(View.VISIBLE);
                    isRequestDetailOpen = false;
                    rootRequestDetail.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewRequestDetail.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewRequestDetail.setTextColor(getResources().getColor(R.color.white));
                    imgRequestDetail.setImageResource(R.drawable.down_arrow);
                }
                else {
                    textViewRequestDetailDynamic.setVisibility(View.GONE);
                    isRequestDetailOpen = true;
                    rootRequestDetail.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewRequestDetail.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewRequestDetail.setTextColor(getResources().getColor(R.color.text_gray));
                    imgRequestDetail.setImageResource(R.drawable.greyright_arrow);
                }
                break;

            case R.id.button_cancel_jOD:
                finish();
                break;

            case R.id.button_change_job_jOD:
                showStatusDialog();
                break;

            case R.id.back_jOD:
                finish();
                break;
        }

    }
    private void showStatusDialog()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        //  ALertDialog - title
        View view1=getLayoutInflater().inflate(R.layout.job_order_detail_popup_title, null);
        builder.setCustomTitle(view1);


        //  ALertDialog - content View
        View view2 = getLayoutInflater().inflate(R.layout.job_order_detail_popup,null);
        RadioButton radio1 = (RadioButton) view2.findViewById(R.id.radio1_completed_jod_popup);
        RadioButton radio2 = (RadioButton) view2.findViewById(R.id.radio2_inprogress_jod_popup);
        RadioButton radio3 = (RadioButton) view2.findViewById(R.id.radio3_pending_popup);
        RadioGroup radioGroup = (RadioGroup) view2.findViewById(R.id.radioGroup_jod_popup);

//        RADIO GROUP LISTENER
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    switch (checkedId)
                    {
                        case R.id.radio1_completed_jod_popup:
                            jobStatus = "complete";
                            break; case
                            R.id.radio2_inprogress_jod_popup:
                        jobStatus = "process";
                            break;
                        case R.id.radio3_pending_popup:
                            jobStatus = "pending";
                            break;
                    }
                }
            });

        builder.setView(view2);


        builder.setPositiveButton(getResources().getString(R.string.popup_done), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // Hitting change job status service
                changeJobStatusServiceHit();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.popup_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();



        //Buttons
        Button positive_button =  alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negative_button =  alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        setPositiveNegativeButtonColor(positive_button,negative_button);

    }

    private void setPositiveNegativeButtonColor(Button positive,Button negative)
    {

        Font.setFontButton(positive,this);
        positive.setTextColor(getResources().getColor(R.color.blue));

        Font.setFontButton(negative,this);
        negative.setTextColor(getResources().getColor(R.color.black));

    }

    private void changeJobStatusServiceHit()
    {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.addTextBody("token", SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN));
        multipartEntityBuilder.addTextBody("requestId", bean.getBookingid());
        multipartEntityBuilder.addTextBody("status", jobStatus);

        ServiceBean bean = new ServiceBean();
        bean.setActivity(this);
        bean.setMethodName("Services/change_Jobstatus");
        bean.setApilistener(this);

        CustomHandler customHandler = new CustomHandler(bean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);
    }

    @Override
    public void myServerResponse(JSONObject jsonObject) {
        Log.e("JOB_ORDER_DETAIL", jsonObject+"");
        try {
            if (jsonObject.getString("status").equals("SUCCESS")&& jsonObject.getString("requestKey").equals("change_Jobstatus"))
            {
                Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

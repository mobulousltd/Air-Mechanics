package mobulous12.airmechanics.serviceprovider.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONObject;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.BookingBean;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

public class BillPaymentActivitySp extends AppCompatActivity implements View.OnClickListener, ApiListener {

    private RelativeLayout rootTypeOfService;
    private RelativeLayout rootDescription;
    private RelativeLayout rootTotalPrice;


    private ImageView imgTypeOfService;
    private ImageView imgDescription;
    private ImageView imgTotalPrice;

    private boolean isTypeOfServiceOpen = true;
    private boolean isDescriptionOpen = true;
    private boolean isTotalPriceOpen = true;

    private TextView textViewTypeOfService;
    private TextView textViewDescription;
    private TextView textViewTotalPrice;

    private TextView textViewTypeOfServiceDynamic;
    private TextView textViewDescriptionDynamic;
    private TextView textViewTotalPriceDynamic;

    private ImageView toolbarBackButton;

    private Button button_send;

    private String price="";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.bill_payment_sp);

        toolbarBackButton = (ImageView) findViewById(R.id.imageView_left_toolbar);
        toolbarBackButton.setOnClickListener(this);

        rootTypeOfService = (RelativeLayout) findViewById(R.id.root_type_of_vechile_sp);
        rootDescription = (RelativeLayout) findViewById(R.id.root_description_bill_payment_sp);
        rootTotalPrice = (RelativeLayout) findViewById(R.id.root_total_price_sp);
        button_send = (Button) findViewById(R.id.button_send_billPayment_sp);

        rootTypeOfService.setOnClickListener(this);
        rootDescription.setOnClickListener(this);
        rootTotalPrice.setOnClickListener(this);
        button_send.setOnClickListener(this);

        imgTypeOfService = (ImageView) findViewById(R.id.imageView_type_of_vechile_billPayment_sp);
        imgDescription = (ImageView) findViewById(R.id.imageView_rightArrow_description_billPayment_sp);
        imgTotalPrice = (ImageView) findViewById(R.id.imageView_total_price_billPayment_sp);

        textViewTypeOfService = (TextView) findViewById(R.id.textView_type_of_vechile_sp);
        textViewDescription = (TextView) findViewById(R.id.textView_description_billPayment_sp);
        textViewTotalPrice = (TextView) findViewById(R.id.textView_total_price_sp);

        textViewTypeOfServiceDynamic = (TextView) findViewById(R.id.textView_type_of_vechile_dynamic_sp);
        textViewDescriptionDynamic = (TextView) findViewById(R.id.textView_description_dynamic_billPayment_sp);
        textViewTotalPriceDynamic = (TextView) findViewById(R.id.textView_total_price_dynamic_sp);

        textViewTypeOfServiceDynamic.setVisibility(View.GONE);
        textViewDescriptionDynamic.setVisibility(View.GONE);
        textViewTotalPriceDynamic.setVisibility(View.GONE);

        Intent intent = getIntent();
        if (intent != null)
        {
            BookingBean bean = intent.getParcelableExtra("bean");
            if (bean != null)
            {
                String category = bean.getCategory();
                String title = bean.getRequestname();
                String desc = bean.getRequestdesc();
                price = bean.getMinCharge();
                textViewTypeOfServiceDynamic.setText(category);
                textViewDescriptionDynamic.setText("Title : "+title+"\n"+"Description : "+desc);
                textViewTotalPriceDynamic.setText("$ "+price);
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.root_type_of_vechile_sp:

                if (isTypeOfServiceOpen)
                {
                    textViewTypeOfServiceDynamic.setVisibility(View.VISIBLE);
                    isTypeOfServiceOpen = false;
                    rootTypeOfService.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewTypeOfService.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewTypeOfService.setTextColor(getResources().getColor(R.color.white));
                    imgTypeOfService.setImageResource(R.drawable.down_arrow);
                }
                else {
                    textViewTypeOfServiceDynamic.setVisibility(View.GONE);
                    isTypeOfServiceOpen = true;
                    rootTypeOfService.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewTypeOfService.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewTypeOfService.setTextColor(getResources().getColor(R.color.text_gray));
                    imgTypeOfService.setImageResource(R.drawable.greyright_arrow);
                }

                break;
            case R.id.root_description_bill_payment_sp:

                if (isDescriptionOpen)
                {
                    textViewDescriptionDynamic.setVisibility(View.VISIBLE);
                    isDescriptionOpen = false;
                    rootDescription.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewDescription.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewDescription.setTextColor(getResources().getColor(R.color.white));
                    imgDescription.setImageResource(R.drawable.down_arrow);
                }
                else {
                    textViewDescriptionDynamic.setVisibility(View.GONE);
                    isDescriptionOpen = true;
                    rootDescription.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewDescription.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewDescription.setTextColor(getResources().getColor(R.color.text_gray));
                    imgDescription.setImageResource(R.drawable.greyright_arrow);
                }

                break;
            case R.id.root_total_price_sp:

                if (isTotalPriceOpen)
                {
                    textViewTotalPriceDynamic.setVisibility(View.VISIBLE);
                    isTotalPriceOpen = false;
                    rootTotalPrice.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewTotalPrice.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewTotalPrice.setTextColor(getResources().getColor(R.color.white));
                    imgTotalPrice.setImageResource(R.drawable.down_arrow);
                }
                else {
                    textViewTotalPriceDynamic.setVisibility(View.GONE);
                    isTotalPriceOpen = true;
                    rootTotalPrice.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewTotalPrice.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewTotalPrice.setTextColor(getResources().getColor(R.color.text_gray));
                    imgTotalPrice.setImageResource(R.drawable.greyright_arrow);
                }

                break;

            case R.id.button_send_billPayment_sp:
                finish();
                Toast.makeText(this, "Bill Sent to Customer successfully!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.imageView_left_toolbar:
                finish();
                break;
        }

    }

    private void sendBillServiceHit()
    {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        multipartEntityBuilder.addTextBody("token", SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN));
        multipartEntityBuilder.addTextBody("request_id", "");
        multipartEntityBuilder.addTextBody("price", price);

        ServiceBean bean = new ServiceBean();
        bean.setActivity(this);
        bean.setMethodName("Services/sendbill");
        bean.setApilistener(this);


        CustomHandler customHandler = new CustomHandler(bean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);

    }

    @Override
    public void myServerResponse(JSONObject jsonObject) {

    }
}

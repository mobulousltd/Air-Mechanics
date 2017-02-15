package mobulous12.airmechanics.customer.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.http.util.EncodingUtils;

import java.util.HashMap;
import java.util.Map;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.BookingBean;
import mobulous12.airmechanics.databinding.ActivityPaymentBinding;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.utils.MyApplication;


public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityPaymentBinding binding ;
    private MyApplication.enIsComingFrom isComingFrom;
    private WebView webView_payment;
    private BookingBean bookingBean;
    private String baseUrl = "";
    private String resultUrl = "http://airmechaniks.com/admin/admins/success";
    private String errorUrl = "https://www.jambopay.com/ErrorPage.htm";
    private Toolbar toolbar_payment;

    String userId,paymentType,paymentId,payAmount,type,myUrl = "http://airmechaniks.com/admin/admins/testpay";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     binding =   DataBindingUtil.setContentView(this,R.layout.activity_payment);
     webView_payment = binding.webViewPayment;

        //Toolbar
        toolbar_payment = binding.toolbarPayment;
        setSupportActionBar(toolbar_payment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_payment.setNavigationIcon(R.drawable.back);
        toolbar_payment.setNavigationOnClickListener(this);
        getSupportActionBar().setTitle("");


        isComingFrom = (MyApplication.enIsComingFrom) this.getIntent().getSerializableExtra("isComingFrom");

        baseUrl = myUrl.trim();
        if(isComingFrom  == MyApplication.enIsComingFrom.eeBillPayment)
        {
            final Bundle beanBundle = this.getIntent().getBundleExtra("beanBundle");
            bookingBean = beanBundle.getParcelable("bookingBean");

            if (SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
            {
                userId = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN);
                paymentType = "1";
                paymentId = bookingBean.getServiceproviderid();
                payAmount = bookingBean.getPayAmount();
                type="Customer";
//                String myUrl = "http://mobulous.co.in/airMechanics/admin/admins/testpay/" + userId + "/" + paymentType + "/" + paymentId + "/" + payAmount + "";
            }
        }
        if (isComingFrom == MyApplication.enIsComingFrom.eeSubscriptionPlan)
        {
            userId =  SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN);
            paymentType = "2";
            if (SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))//            CUSTOMER
            {
                type="Customer";
                paymentId = getIntent().getStringExtra("plan_id");
                payAmount = getIntent().getStringExtra("payamount");
            }
            else // SERVICE PROVIDER
            {
                type="Service";
                paymentId = getIntent().getStringExtra("plan_id");
                payAmount = getIntent().getStringExtra("payamount");
            }
        }
        webView_payment.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView_payment.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (url.equalsIgnoreCase(resultUrl))
                {
                    if(isComingFrom  == MyApplication.enIsComingFrom.eeBillPayment)
                    {
                        final Bundle beanBundle = PaymentActivity.this.getIntent().getBundleExtra("beanBundle");
                        bookingBean = beanBundle.getParcelable("bookingBean"); Intent returnIntent = new Intent();
                        returnIntent.putExtra("beanBundle",beanBundle);
                        returnIntent.putExtra("bookingBean",bookingBean);
                        setResult(RESULT_OK,returnIntent);
                        finish();
                    }
                    else
                    {
                        setResult(RESULT_OK,new Intent());
                        finish();
                    }
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

        });
        Map<String, String> mapParams = new HashMap<>();
//                Key :token, request_id, totalamount, type(1,2), service ( Customer, Service)
        mapParams.put("token", userId);
        mapParams.put("request_id", paymentId);
        mapParams.put("totalamount", payAmount);
        mapParams.put("type", paymentType);
        mapParams.put("service", type);
        WebSettings webSettings = webView_payment.getSettings();
        webSettings.setJavaScriptEnabled(true);
//                webView_payment.loadUrl(baseUrl);
        String postData = "token="+userId+"&request_id="+paymentId+"&totalamount="+payAmount+"&type="+paymentType+"&service"+type;
        webView_payment.postUrl(baseUrl, EncodingUtils.getBytes(postData, "BASE64"));
        webView_payment.requestFocus();

    }

    private void subscriptionPayment(String userId, String paymentType, String paymentId, String paymentAmount)
    {

        String myUrl = "http://mobulous.co.in/airMechanics/admin/admins/testpay/" + userId + "/" + paymentType + "/" + paymentId + "/" + paymentAmount + "";
        baseUrl = myUrl.trim();

        webView_payment.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView_payment.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                if (url.equalsIgnoreCase(resultUrl))
                {
                    setResult(RESULT_OK,new Intent());
                    finish();
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });
        WebSettings webSettings = webView_payment.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView_payment.loadUrl(baseUrl);
        webView_payment.requestFocus();
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PaymentActivity.this);
        alertDialog.setTitle(getResources().getString(R.string.app_name));
        alertDialog.setMessage("Do you want to cancel this transaction?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
    // toolbar back navigation icon listener
    @Override
    public void onClick(View v) {
        onBackPressed();
    }
}

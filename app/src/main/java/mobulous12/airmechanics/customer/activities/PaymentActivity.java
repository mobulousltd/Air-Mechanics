package mobulous12.airmechanics.customer.activities;

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
    private String resultUrl = "http://mobulous.co.in/airMechanics/admin/admins/success";
    private String errorUrl = "https://www.jambopay.com/ErrorPage.htm";
    private Toolbar toolbar_payment;

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

        final Bundle beanBundle = this.getIntent().getBundleExtra("beanBundle");
        bookingBean = beanBundle.getParcelable("bookingBean");
        isComingFrom = (MyApplication.enIsComingFrom) this.getIntent().getSerializableExtra("isComingFrom");


        if(isComingFrom  == MyApplication.enIsComingFrom.eeBillPayment)
        {

            if (SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
            {
                String userId = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN);
                String paymentType = "2";
                String paymentId = bookingBean.getServiceproviderid();
                String payAmount = bookingBean.getPayAmount();
                String myUrl = "http://mobulous.co.in/airMechanics/admin/admins/testpay/" + userId + "/" + paymentType + "/" + paymentId + "/" + payAmount + "";
                baseUrl = myUrl.trim();

                webView_payment.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
                webView_payment.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);

                        if (url.equalsIgnoreCase(resultUrl))
                        {

                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("beanBundle",beanBundle);
                            returnIntent.putExtra("bookingBean",bookingBean);
                            setResult(RESULT_OK,returnIntent);
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
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // toolbar back navigation icon listener
    @Override
    public void onClick(View v) {
        onBackPressed();
    }
}

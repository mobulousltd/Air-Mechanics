package mobulous12.airmechanics.customer.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.databinding.AcceptRejectDetailCardsBinding;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.utils.CircleImageView;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

public class AcceptRejectDetailActivity extends AppCompatActivity implements View.OnClickListener, ApiListener {

    AcceptRejectDetailCardsBinding binding;
    private TextView tv_minchrge,tv_ServiceProName,tv_Date,tv_descrip,textView_AcceptRejectTime, tv_jobtitle;
    private String reqid, expiredtime;
    private CircleImageView image_AcceptRej;

    long days, hours, minutes, seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this, R.layout.accept_reject_detail_cards);
        reqid=getIntent().getStringExtra("requestid");
          /*views*/

        tv_minchrge=(TextView)findViewById(R.id.tv_minchrge);
        textView_AcceptRejectTime=(TextView)findViewById(R.id.textView_AcceptRejectTime);

        tv_ServiceProName = (TextView) findViewById(R.id.textView_AcceptRejectServiceProName);
        tv_Date = (TextView) findViewById(R.id.textView_AcceptRejectDate);
        tv_descrip = (TextView) findViewById(R.id.textView_description_accept_reject);
        image_AcceptRej = (CircleImageView) findViewById(R.id.circularImageView1_AcceptReject);
        tv_jobtitle=(TextView)findViewById(R.id.tv_jobtitle);
        binding.imgBack.setOnClickListener(this);
        binding.textViewAcceptQuote.setOnClickListener(this);
        binding.textViewRejectQuote.setOnClickListener(this);

        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if(nm!=null)
        {
            nm.cancelAll();
        }

        detailServiceHit();
    }
    private void setTimer()
    {
        int index=expiredtime.indexOf(".");

        long h=Long.parseLong("00");
        long m=Long.parseLong(expiredtime.substring(0, index));
        long s=Long.parseLong(expiredtime.substring(index+1, expiredtime.length()));
        long minuts= TimeUnit.MINUTES.toMillis(m);
        final long hour=TimeUnit.HOURS.toMillis(h);
        long secnd=TimeUnit.SECONDS.toMillis(s);
        long timemilli=hour+minuts+secnd;
//        long durationSeconds=TimeUnit.HOURS.toSeconds(h)+TimeUnit.MINUTES.toSeconds(m)+TimeUnit.SECONDS.toSeconds(s);
//        Toast.makeText(AcceptRejectDetailActivity.this, ""+String.format("%02d:%02d:%02d", durationSeconds / 3600,
//                (durationSeconds % 3600) / 60, (durationSeconds % 60)), Toast.LENGTH_SHORT).show();

        new CountDownTimer(timemilli, 1000)
        {
            @Override
            public void onFinish()
            {
                //Action for when the timer has finished.
//                Toast.makeText(getApplicationContext(), "Timer finished", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onTick(long millisUntilFinished)
            {
                timeCalculate(millisUntilFinished/1000);
                binding.tvDay.setText(String.format("%02d", days));
                binding.tvHr.setText(String.format("%02d", hours));
                binding.tvMin.setText(String.format("%02d", minutes));
                binding.tvSec.setText(String.format("%02d", seconds));
//                Log.i("Timer", ""+String.format("%2d", minutes)+" : "+String.format("%2d", seconds));
            }
        }.start();
    }

    public String timeCalculate(long ttime)
    {
        String daysT = "", restT = "";
        days = (Math.round(ttime) /( 86400));
        hours = (Math.round(ttime) / 3600) - (days * 24);
        minutes = (Math.round(ttime) / 60) - (days * 1440) - (hours * 60);
        seconds = Math.round(ttime) % 60;

        if(days==1) daysT = String.format("%d day ", days);
        if(days>1) daysT = String.format("%d days ", days);

        restT = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        return daysT + restT;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_accept_Quote:
                acceptServiceHit();
                break;
            case R.id.textView_reject_Quote:
                rejectServiceHit();
                break;
            case R.id.img_back:
                finish();
                break;
        }

    }

    //    Services
    private void detailServiceHit()
    {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.addTextBody("token", SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN));
        multipartEntityBuilder.addTextBody("requestId", reqid);

        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setActivity(this);
        serviceBean.setMethodName("Consumers/request_details");
        serviceBean.setApilistener(this);

        CustomHandler customHandler = new CustomHandler(serviceBean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);

    }
    private void acceptServiceHit()
    {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.addTextBody("token", SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN));
        multipartEntityBuilder.addTextBody("requestId", reqid);


        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setActivity(this);
        serviceBean.setMethodName("Consumers/request_accept");
        serviceBean.setApilistener(this);

        CustomHandler customHandler = new CustomHandler(serviceBean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);
    }

    private void rejectServiceHit() {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.addTextBody("token", SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN));
        multipartEntityBuilder.addTextBody("requestId", reqid);


        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setActivity(this);
        serviceBean.setMethodName("Consumers/request_reject");
        serviceBean.setApilistener(this);

        CustomHandler customHandler = new CustomHandler(serviceBean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);
    }

    @Override
    public void myServerResponse(JSONObject jsonObject)
    {
        try {
            if (jsonObject != null) {
                if (jsonObject.getString("status").equals("SUCCESS"))
                {
                    if(jsonObject.getString("requestKey").equalsIgnoreCase("request_details"))
                    {
                        JSONObject j_object = jsonObject.getJSONObject("response").getJSONObject("user");
                        expiredtime=j_object.getString("expiredtime");
                        if(!expiredtime.isEmpty())
                        {
                            setTimer();
                        }
                        tv_ServiceProName.setText(j_object.getString("userName"));
                        tv_descrip.setText(j_object.getString("request_description"));
                        String date=j_object.getString("requestDate");
                        tv_Date.setText(date);
                        tv_minchrge.setText(j_object.getString("minCharge"));
                        tv_jobtitle.setText(j_object.getString("request_Title"));
                        textView_AcceptRejectTime.setText(j_object.getString("open_time")+" - "+j_object.getString("close_time"));
                        AQuery aQuery = new AQuery(image_AcceptRej);
                        if (j_object.getString("userImage").equals("")){
                            aQuery.id(image_AcceptRej).image(R.drawable.default_image);
                        }else {
                            aQuery.id(image_AcceptRej).image(j_object.getString("userImage"));
                        }

                    }
                    if(jsonObject.getString("requestKey").equalsIgnoreCase("request_accept"))
                    {
                        finish();
                        Toast.makeText(AcceptRejectDetailActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    if(jsonObject.getString("requestKey").equalsIgnoreCase("request_reject"))
                    {
                        finish();
                        Toast.makeText(AcceptRejectDetailActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                }
            }
            else {
            }

            Log.e("JSON Response: ",""+jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

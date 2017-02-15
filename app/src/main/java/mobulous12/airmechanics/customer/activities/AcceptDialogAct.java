package mobulous12.airmechanics.customer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONObject;
import mobulous12.airmechanics.R;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

/**
 * Created by mobulous12 on 15/2/17.
 */

public class AcceptDialogAct extends Activity implements View.OnClickListener, ApiListener {

    private  String reqId = "";
    private String status = "";
    private String notify="";
    private String login_type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            setFinishOnTouchOutside(false);
        }
        else
        {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up_accept_reject);

        reqId = getIntent().getStringExtra("requestid");
        status = getIntent().getStringExtra("status");
        notify = getIntent().getStringExtra("notify");
        login_type = getIntent().getStringExtra("login_type");
        String spName = getIntent().getStringExtra("spName");
        String address = getIntent().getStringExtra("address");
        String exdate = getIntent().getStringExtra("exdate");
        String minCh1 = getIntent().getStringExtra("minCh1");
        String minCh2 = getIntent().getStringExtra("minCh2");
        String message = getIntent().getStringExtra("message");
        String title = getIntent().getStringExtra("title");


            TextView accept = (TextView) findViewById(R.id.tv_accept_Quote);
            TextView reject = (TextView) findViewById(R.id.tv_reject_Quote);
            TextView tv_msg = (TextView) findViewById(R.id.tv_msg);
            TextView tv_Name = (TextView) findViewById(R.id.tv_Name);
            TextView tv_Date = (TextView) findViewById(R.id.tv_Date);
            TextView tv_title = (TextView) findViewById(R.id.tv_title);
            TextView tv_minchrge = (TextView) findViewById(R.id.tv_minchrge);
            TextView tv_address = (TextView) findViewById(R.id.tv_address);

            tv_msg.setText(message);
            tv_Name.setText(spName);
            tv_Date.setText(exdate);
            tv_title.setText("Title : "+title);
            tv_minchrge.setText(minCh2);
            tv_address.setText(address);

        accept.setOnClickListener(this);
        reject.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_accept_Quote:
                acceptServiceHit();
                break;
            case R.id.tv_reject_Quote:
                rejectServiceHit();
                break;

        }
    }

//    SERVICES

    private void acceptServiceHit() {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.addTextBody("token", SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN));
        multipartEntityBuilder.addTextBody("requestId", reqId);


        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setActivity(AcceptDialogAct.this);
        serviceBean.setMethodName("Consumers/request_accept");
        serviceBean.setApilistener(this);

        CustomHandler customHandler = new CustomHandler(serviceBean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);
    }

    private void rejectServiceHit() {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.addTextBody("token", SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN));
        multipartEntityBuilder.addTextBody("requestId", reqId);


        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setActivity(AcceptDialogAct.this);
        serviceBean.setMethodName("Consumers/request_reject");
        serviceBean.setApilistener(this);

        CustomHandler customHandler = new CustomHandler(serviceBean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);
    }



//   SERVICE RESPONSE
    @Override
    public void myServerResponse(JSONObject jsonObject)
    {
        try
        {
            if (jsonObject != null)
            {
                if (jsonObject.getString("status").equals("SUCCESS"))
                {
                    if (jsonObject.getString("requestKey").equalsIgnoreCase("request_accept"))
                    {
                        Toast.makeText(this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        Intent i  = new Intent(AcceptDialogAct.this,HomeActivity.class);
                        i.putExtra("fromKey", "acceptPopUp");
                        i.putExtra("requestid", reqId);
                        i.putExtra("status", status);
                        i.putExtra("type", status);
                        i.putExtra("notify", notify);
                        startActivity(i);
                        this.finish();

                    }
                    if (jsonObject.getString("requestKey").equalsIgnoreCase("request_reject")) {

                        Toast.makeText(this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        this.finish();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

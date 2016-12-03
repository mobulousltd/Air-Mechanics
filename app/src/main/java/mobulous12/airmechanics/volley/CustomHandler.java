package mobulous12.airmechanics.volley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONObject;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.RoleSelectionActivity;
import mobulous12.airmechanics.customer.activities.*;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.utils.MyApplication;
import mobulous12.airmechanics.utils.NetworkConnectionCheck;

/**
 * Created by mobulous2 on 31/3/16.
 */

public class CustomHandler implements Response.Listener, Response.ErrorListener, DialogInterface.OnCancelListener
{

    CustomRequest customRequest;
    CustomDialog customDialog;
    private Activity activity;
    private Fragment fragment;
    ProgressDialog progressDialog;
    ServiceBean serviceBean;
    private ApiListener listener;

    public CustomHandler(ServiceBean serviceBean)
    {
        if (null != serviceBean)
        {
            this.serviceBean = serviceBean;
            activity = serviceBean.getActivity();
            fragment = serviceBean.getFragment();
            listener = serviceBean.getApilistener();
        }
    }
    private void showProgressDialog()
    {
        if (serviceBean.isLoader())
        {
            customDialog = CustomDialog.show(activity, true, true, this);
            try
            {
                customDialog.setCancelable(false);
                customDialog.show();
            } catch (Exception e) {
                e.printStackTrace();


            }
//            progressDialog = new ProgressDialog(activity);
//            progressDialog.setMessage(activity.getResources().getString(R.string.loading));
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//            if (!progressDialog.isShowing())
//                // changing Progress Dialog GRAVITY
//            progressDialog.setProgressDrawable(activity.getResources().getDrawable(R.drawable.progress_dialog));
//            Window window = progressDialog.getWindow();
//            WindowManager.LayoutParams params = window.getAttributes();
//            params.gravity = Gravity.CENTER;
//            window.setAttributes(params);
//            progressDialog.show();
        }

    }

    private void hideProgressDialog()
    {   if (serviceBean.isLoader()) {
        if (customDialog.isShowing())
            customDialog.dismiss();
    }
    }
    public boolean checkInternet()
    {
        if (!new NetworkConnectionCheck(activity.getApplicationContext()).isConnect())
        {

            Toast toast = Toast.makeText(activity.getApplicationContext(), "Internet Disconnected", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return false;
        }
        else
        {
            return true;
        }
    }
    public void makeMultipartRequest(MultipartEntityBuilder entity)
    {
        if(checkInternet())
        {
            showProgressDialog();
            customRequest = new CustomRequest(serviceBean.getMethodName(), entity, this, this, serviceBean.getActivity().getApplicationContext());
            if(!serviceBean.isRetrypolicy())
            {
                customRequest.setRetryPolicy(new DefaultRetryPolicy(
                        30000,
                        -1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            }
            else
            {
                customRequest.setRetryPolicy(new DefaultRetryPolicy(
                        30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            }
            MyApplication.getInstance().addToRequestQueue(customRequest, serviceBean.getMethodName());
        }
    }
    public void makeLocationRequest(String url)
    {
        if(checkInternet())
        {
            showProgressDialog();
            customRequest = new CustomRequest(url,this, this);
            if(!serviceBean.isRetrypolicy())
            {
                customRequest.setRetryPolicy(new DefaultRetryPolicy(
                        30000,
                        -1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            }
            else
            {
                customRequest.setRetryPolicy(new DefaultRetryPolicy(
                        30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            }
            MyApplication.getInstance().addToRequestQueue(customRequest, serviceBean.getMethodName());
        }
    }
//    public void makeMultipartRequest(MultipartEntity entity)
//    {
//        showProgressDialog();
//        customRequest = new CustomRequest(serviceBean.getMethodName(), entity, this, this, serviceBean.getActivity().getApplicationContext());
//        if(serviceBean.isRetrypolicy())
//        {
//            customRequest.setRetryPolicy(new DefaultRetryPolicy(
//                    0,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        }
//        MyApplication.getInstance().addToRequestQueue(customRequest, serviceBean.getMethodName());
//    }

    @Override
    public void onResponse(Object response)
    {
        try {
            hideProgressDialog();
            JSONObject obj = (JSONObject) response;
            if(obj.getString("message").equals("Invalid User."))
            {
                Intent intent=new Intent(activity, RoleSelectionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                SharedPreferenceWriter.getInstance(activity).writeBooleanValue(SPreferenceKey.LOGINKEY, false);
                SharedPreferenceWriter.getInstance(activity).writeBooleanValue(SPreferenceKey.CUSTOMER_LOGIN, false);
                SharedPreferenceWriter.getInstance(activity).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, false);
                activity.startActivity(intent);
                activity.finish();
            }
            listener.myServerResponse(obj);
//        serverResponse(obj);
            Log.i("Response", obj.toString());
        }
        catch (Exception e)
        {

        }

    }
    @Override
    public void onErrorResponse(VolleyError error) {
        hideProgressDialog();
        Toast.makeText(activity.getApplicationContext(), "Server not Responding", Toast.LENGTH_SHORT).show();
        VolleyLog.d("MODULE ERROR", "Error: " + error.toString());
        VolleyLog.d("MODULE ERROR", "Error: " + error.getLocalizedMessage());
        Log.i("Error", "" + error.getLocalizedMessage());
        Log.i("Error",""+error.getMessage());
    }

    public void onStop(String requesttag)
    {
        MyApplication.getInstance().cancelPendingRequests(requesttag);
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

//    private void serverResponse(JSONObject response)
//    {
//        try
//        {
//            if (fragment != null)
//            {
//
//            }
//            else if (activity != null)
//            {
//                if (activity.getClass().getSimpleName().equalsIgnoreCase("LoginActivity"))
//                {
//                    ((LoginActivity) activity).getServerResponse(response);
//                }
//
//
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
    }


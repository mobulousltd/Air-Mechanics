package mobulous12.airmechanics.customer.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Handler;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.RoleSelectionActivity;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;
import mobulous12.airmechanics.serviceprovider.activities.SignUpServiceProActivity;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener,ApiListener {

    String image = "", email = "", name = "", gmailid = "", fbid = "";
    private Button button_SignIn;
    private RelativeLayout root_activity_login;
    private LinearLayout linear_skipToHome, linearlayout_newSignUp;
    private EditText editText_Contact, editText_Password;
    CallbackManager callbackManager;
    AccessToken accessToken;

    private static final String TAG = "Login";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_login);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//hide keyboard when activity appears
        /*VIEWS*/
        root_activity_login = (RelativeLayout) findViewById(R.id.root_activity_login);
        button_SignIn = (Button) findViewById(R.id.button_SignIn);
        linear_skipToHome = (LinearLayout) findViewById(R.id.linear_skipToHome);

        linearlayout_newSignUp = (LinearLayout) findViewById(R.id.linearlayout_newSignUp);
        editText_Contact = (EditText) findViewById(R.id.editText_Contact);
        editText_Password = (EditText) findViewById(R.id.editText_Password);

        /*google*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
//        new DownloadImage().execute();
        //fb
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();
        accessToken = AccessToken.getCurrentAccessToken();
        findViewById(R.id.imageView_Fb).setOnClickListener(this);
        findViewById(R.id.imageView_G_plus).setOnClickListener(this);
        if (SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
        {
            linear_skipToHome.setVisibility(View.VISIBLE);
        }
        else
        {
            linear_skipToHome.setVisibility(View.INVISIBLE);
        }


        /*listeners*/
        root_activity_login.setOnClickListener(this);
        linear_skipToHome.setOnClickListener(this);
        button_SignIn.setOnClickListener(this);
        findViewById(R.id.textView_ForgotPassword).setOnClickListener(this);

        /*fb and google listeneres*/
        findViewById(R.id.imageView_Fb).setOnClickListener(this);
        findViewById(R.id.imageView_G_plus).setOnClickListener(this);

        linearlayout_newSignUp.setOnClickListener(this);
    }



    //    gmail sigin
    private void gmailSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
        } else {
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.i("user name", "" + acct.getDisplayName());
            Uri personPhoto = acct.getPhotoUrl();
            if (personPhoto != null) {
                image = personPhoto.toString();
            } else {
                image = "";
            }
            gmailid = acct.getId();
            email = acct.getEmail();
            name = acct.getDisplayName();
            checksocialmediaServiceMedia();
            if (!name.isEmpty()) {
                signOut();
            }
            signOut();
        } else {
        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
            }
        });
    }
    //gmail signin finish

    //fb signin
    private void fbSignIn() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("mobulous12.airmechanics", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult result) {
                accessToken = AccessToken.getCurrentAccessToken();
                getUserDetail();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "" + error.toString(), Toast.LENGTH_SHORT).show();
                Log.i("Facebook Error----->", error.toString());

            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Request Cancelled", Toast.LENGTH_SHORT).show();
                Log.i("facebook onCancel----->", "cancel");
            }
        });

    }

    private void getUserDetail() {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            if (object != null) {
                                Log.e("JSON------>>>", object.toString());
                                email = object.getString("email");
                                name = object.getString("name");
                                fbid = object.getString("id");

                                image = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                image = "https://graph.facebook.com/" + fbid + "/picture?type=large";
                                Log.i("fbbb", "" + image);
                                if (image == null) {
                                    image = "";
                                }
                                checksocialmediaServiceMedia();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,birthday,gender,first_name,last_name,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }
    //fb sign in finish

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.root_activity_login:
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            break;
            case R.id.linear_skipToHome:
//               Toast.makeText(this,"This functionality is under implementation.",Toast.LENGTH_LONG).show();

                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.LOGINKEY, false);
                Intent in=new Intent(this, HomeActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);
                finish();
                break;
            case R.id.button_SignIn:
                if(validateCustomer())
                {
                    loginServiceHit();
                }
                break;
            case R.id.textView_ForgotPassword:
                startActivity(new Intent(this, ForgotPasswordActivity.class));

                break;
            case R.id.linearlayout_newSignUp:
                if (SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
                {
                    Intent intent = new Intent(this, SignUpActivity.class);
                    startActivity(intent);

                }
                else if (SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.SERVICE_PROVIDER_LOGIN))
                {
                    startActivity(new Intent(this, SignUpServiceProActivity.class));
                }
                break;
            case R.id.imageView_G_plus:
                gmailSignIn();
                break;
            case R.id.imageView_Fb:
                fbSignIn();
                break;
        }

    }

    private boolean validateCustomer()
    {
        if(editText_Contact.getText().toString().isEmpty())
        {
            Toast.makeText(LoginActivity.this, "Please enter your contact number", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!Patterns.PHONE.matcher(editText_Contact.getText().toString()).matches())
        {
            Toast.makeText(getApplicationContext(), "Please enter a valid Contact Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(editText_Contact.getText().toString().length()<8 || editText_Contact.getText().toString().length()>15)
        {
            Toast.makeText(LoginActivity.this, "Please enter a valid Contact  number.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(editText_Password.getText().toString().isEmpty())
        {
            Toast.makeText(LoginActivity.this, "Please enter your Password", Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (editText_Password.getText().toString().length() < 6)
        {
            Toast.makeText(getApplicationContext(), "Please enter a valid Password.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }

    //services
    private void checksocialmediaServiceMedia() {
        MultipartEntityBuilder multipartbuilder = MultipartEntityBuilder.create();
        multipartbuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartbuilder.addTextBody("email", email);
        multipartbuilder.addTextBody("google_id", gmailid);
        multipartbuilder.addTextBody("fb_id", fbid);
        multipartbuilder.addTextBody("name", name);
        multipartbuilder.addTextBody("image", image);
        multipartbuilder.addTextBody("address", "H-35, Sector-63");
        multipartbuilder.addTextBody("city", "noida");
        multipartbuilder.addTextBody("lat", SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.LATITUDE));
        multipartbuilder.addTextBody("long", SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.LONGITUDE));
        multipartbuilder.addTextBody("deviceToken", SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.DEVICETOKEN));
        multipartbuilder.addTextBody("deviceType", "android");

        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setActivity(LoginActivity.this);
        serviceBean.setApilistener(this);
        if (SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.CUSTOMER_LOGIN)) {
            serviceBean.setMethodName("Consumers/check_socailMedia");
        } else {
            serviceBean.setMethodName("Services/check_socailMedia");
        }
        CustomHandler customHandler = new CustomHandler(serviceBean);
        customHandler.makeMultipartRequest(multipartbuilder);
    }

    private void loginServiceHit() {
        MultipartEntityBuilder multipartbuilder = MultipartEntityBuilder.create();
        multipartbuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartbuilder.addTextBody("username", editText_Contact.getText().toString());
        multipartbuilder.addTextBody("password", editText_Password.getText().toString());
        multipartbuilder.addTextBody("device_token", SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.DEVICETOKEN));
        multipartbuilder.addTextBody("device_type", "android");

        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setActivity(LoginActivity.this);
        serviceBean.setApilistener(this);
        if (SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
        {
            serviceBean.setMethodName("Consumers/login");
        } else {
            serviceBean.setMethodName("Services/login");
        }
        CustomHandler customHandler = new CustomHandler(serviceBean);
        customHandler.makeMultipartRequest(multipartbuilder);
    }

    @Override
    public void myServerResponse(JSONObject responseObj) {
            try {
                if (responseObj != null)
                {
                    if(responseObj.getString("status").equalsIgnoreCase("SUCCESS"))
                    {
                        if(responseObj.getString("requestKey").equalsIgnoreCase("login"))
                        {
                            JSONObject response=responseObj.getJSONObject("response");
                            SharedPreferenceWriter.getInstance(getApplicationContext()).writeBooleanValue(SPreferenceKey.LOGINKEY, true);
                            SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.TOKEN, response.getString("token"));
                            SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.FullName, response.getString("full_name"));
                            SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.Email, response.getString("email"));
                            SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.PhoneNumber, response.getString("contact_no"));
                            SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.ADDRESS, response.getString("address"));
                            SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.IMAGE, response.getString("profile"));
                            SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.FullName, response.getString("full_name"));
                            SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.LOGINTYPE, "normal");
                            if(response.getString("user_type").equalsIgnoreCase("customer"))
                            {
                                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.CUSTOMER_LOGIN, true);
                                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, false);
                                Intent intent=new Intent(this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.CUSTOMER_LOGIN, false);
                                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, true);
                                Intent intent=new Intent(this, HomeActivityServicePro.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                        if(responseObj.getString("requestKey").equalsIgnoreCase("check_socailMedia"))
                        {
                            JSONObject response=responseObj.getJSONObject("response");
                            SharedPreferenceWriter.getInstance(getApplicationContext()).writeBooleanValue(SPreferenceKey.LOGINKEY, true);
                            SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.TOKEN, response.getString("token"));
                            SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.FullName, response.getString("full_name"));
                            SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.Email, response.getString("email"));
                            SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.PhoneNumber, response.getString("contact_no"));
                            SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.ADDRESS, response.getString("address"));
                            SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.IMAGE, response.getString("profile"));
                            SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.FullName, response.getString("full_name"));
                            SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(SPreferenceKey.LOGINTYPE, "social");
                            if(response.getString("user_type").equalsIgnoreCase("customer"))
                            {
                                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.CUSTOMER_LOGIN, true);
                                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, false);
                                Intent intent=new Intent(this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, true);
                                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.CUSTOMER_LOGIN, false);
                                Intent intent=new Intent(this, HomeActivityServicePro.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, ""+responseObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        Intent in1=new Intent(this, RoleSelectionActivity.class);
        in1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.LOGINKEY, false);
        SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.CUSTOMER_LOGIN, false);
        SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, false);
        startActivity(in1);
        finish();
    }
}

package mobulous12.airmechanics.customer.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONObject;

import java.io.File;

import mobulous12.airmechanics.MapActivityToPickAddress;
import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.ProfileBean;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.fonts.FontBinding;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.utils.CircularImageView;
import mobulous12.airmechanics.utils.TakeImage;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, ApiListener {

    private RelativeLayout root_activity_sign_up;
    private Button button_SignIn_SignUpActivity;
    private RelativeLayout root_back_sign_up_activity;
    private TextView textView_back_signUp;
    private EditText editText_fullName, editText_email, editText_password,
            editText_confirmPassword, editText_contact_number;
    TextView textView_address1;
    private String picpath = "";
    ProfileBean bean;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int ADDRESSFETCH=011;
    private static final int CAMERA_REQUEST = 1888;
    CircularImageView circularImageView_profile_pic;ProfileBean profileBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");

        root_activity_sign_up = (RelativeLayout) findViewById(R.id.root_activity_sign_up);
        editText_fullName = (EditText) findViewById(R.id.editText_full_name);
        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_password = (EditText) findViewById(R.id.editText_password);
        editText_confirmPassword = (EditText) findViewById(R.id.editText_confirm_password);
        editText_contact_number = (EditText) findViewById(R.id.editText_contact_number);
        textView_address1 = (TextView) findViewById(R.id.textView_address1);
        textView_address1.setText(SharedPreferenceWriter.getInstance(SignUpActivity.this).getString(SPreferenceKey.ADDRESS));
        button_SignIn_SignUpActivity = (Button) findViewById(R.id.button_SignIn_SignUpActivity);
        textView_back_signUp = (TextView) findViewById(R.id.textView_back_signUp);
        root_back_sign_up_activity = (RelativeLayout) findViewById(R.id.root_back_sign_up_activity);
        circularImageView_profile_pic = (CircularImageView) findViewById(R.id.circularImageView_profile_pic);
        circularImageView_profile_pic.setOnClickListener(this);

        profileBean = new ProfileBean();
        profileBean.setLat(SharedPreferenceWriter.getInstance(SignUpActivity.this).getString(SPreferenceKey.LATITUDE));
        profileBean.setLng(SharedPreferenceWriter.getInstance(SignUpActivity.this).getString(SPreferenceKey.LONGITUDE));


        /*listeners*/
        root_activity_sign_up.setOnClickListener(this);
        root_back_sign_up_activity.setOnClickListener(this);
        button_SignIn_SignUpActivity.setOnClickListener(this);
        textView_address1.setOnClickListener(this);
    }

    private boolean validateCustomerData()
    {
        if (editText_fullName.getText().toString().trim().isEmpty())
        {
            showToast("Please enter Full name");
            return false;
        }
        else if(editText_email.getText().toString().isEmpty())
        {
            showToast("Please enter Email Address");
            return false;
        }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(editText_email.getText().toString()).matches()) {
            showToast("Please enter valid Email Address");
            return false;
        }
        else if(editText_password.getText().toString().trim().isEmpty())
        {
            showToast("Please enter Password");
            return false;
        }
        else if (editText_password.getText().toString().length() < 6)
        {
            showToast("Password must be atleast 6 characters long");
            return false;
        }
        else if (editText_confirmPassword.getText().toString().isEmpty())
        {
            showToast("Please Confirm Password.");
            return false;
        }
        else if (editText_confirmPassword.getText().toString().length() < 6) {

            showToast("Confirm Password must be atleast 6 characters long");
            return false;
        }
        else if(!editText_confirmPassword.getText().toString().equals(editText_password.getText().toString()))
        {
            showToast("Password and Confirm password doesn't match");
        }
        else if (editText_contact_number.getText().toString().isEmpty())
        {
            showToast("Please enter your contact number.");
            return false;
        }
        else if(editText_contact_number.getText().toString().length()<8 || editText_contact_number.getText().toString().length()>15)
        {

            showToast( "Please enter a valid Contact  number.");

            return false;
        }
        else if (!Patterns.PHONE.matcher(editText_contact_number.getText().toString()).matches()) {
            showToast("Please enter valid Contact Number");
            return false;
        }
        else if (textView_address1.getText().toString().trim().isEmpty()) {
            showToast("Please enter your Address");
            return false;
        }
//        else if(picpath.isEmpty())
//        {
//            showToast("Please upload your profile picture");
//            return false;
//        }
        else
        {
            return true;
        }
        return false;
    }

    public ProfileBean storeValuesInBean() {
        profileBean.setFullname(editText_fullName.getText().toString().trim());
        profileBean.setEmail(editText_email.getText().toString().trim());
        profileBean.setPassword(editText_password.getText().toString().trim());
        profileBean.setContactno(editText_contact_number.getText().toString().trim());
        profileBean.setAddress(textView_address1.getText().toString().trim());
        profileBean.setImagePath(picpath);
        profileBean.setCity("Sector-63, Noida");
        profileBean.setCountry_code("91");
        return profileBean;
    }

    private void showToast(String message)
    {

        Toast toast = Toast.makeText(this,message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }



    public void showDialogForUpload() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Upload your Profile Image");

        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), TakeImage.class);
                        intent.putExtra("from", "gallery");
                        startActivityForResult(intent, RESULT_LOAD_IMAGE);
                        dialog.cancel();
                    }
                });
        alertDialogBuilder.setNeutralButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alertDialogBuilder.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), TakeImage.class);
                intent.putExtra("from", "camera");
                startActivityForResult(intent, CAMERA_REQUEST);
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
        TextView textView=(TextView) alertDialog.findViewById(android.R.id.message);
        FontBinding.setFont(textView,"regular");

        //Buttons
        Button positive_button =  alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negative_button =  alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button neutral_btn =  alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);

        setPositiveNegativeButtonColor(positive_button,negative_button,neutral_btn);

    }


    private void setPositiveNegativeButtonColor(Button positive,Button negative,Button neutral)
    {
//        Font.setFontButton(positive,this);
        positive.setTextColor(getResources().getColor(R.color.blue));
//        Font.setFontButton(negative,this);
        negative.setTextColor(getResources().getColor(R.color.blue));
        neutral.setTextColor(getResources().getColor(R.color.black));
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.root_activity_sign_up: {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            break;
            case R.id.circularImageView_profile_pic:
                showDialogForUpload();
                break;

            case R.id.root_back_sign_up_activity:

                finish();
                break;
            case R.id.button_SignIn_SignUpActivity:

                if (validateCustomerData())
                {
                    bean = storeValuesInBean();
                    senCodeServiceHit();
                }
                break;
            case R.id.textView_address1:
                Intent intent=new Intent(new Intent(SignUpActivity.this, MapActivityToPickAddress.class));
                intent.putExtra("lat", profileBean.getLat());
                intent.putExtra("lng", profileBean.getLng());
                intent.putExtra("address", textView_address1.getText().toString());
                startActivityForResult(intent, ADDRESSFETCH);
                break;
        }
    }

    private void senCodeServiceHit() {
        try {

            MultipartEntityBuilder multipartbuilder = MultipartEntityBuilder.create();
            multipartbuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            multipartbuilder.addTextBody("country_code", "+91");
            multipartbuilder.addTextBody("mobile", editText_contact_number.getText().toString().trim());
            multipartbuilder.addTextBody("email", editText_email.getText().toString().trim());

            ServiceBean serviceBean = new ServiceBean();
            serviceBean.setMethodName("Consumers/sendCode");
            serviceBean.setActivity(SignUpActivity.this);
            serviceBean.setApilistener(this);


            CustomHandler customHandler = new CustomHandler(serviceBean);
            customHandler.makeMultipartRequest(multipartbuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void myServerResponse(JSONObject responseObj) {

        try
        {
            if(responseObj!=null)
            {
                if(responseObj.getString("status").equalsIgnoreCase("SUCCESS") && responseObj.getString("requestKey").equalsIgnoreCase("sendCode"))
                {
                    Log.i("JsonResponse", ""+responseObj.toString());
                    String code = responseObj.getJSONObject("response").getString("verification_code");
                    Intent intent = new Intent(this, VerificationActivity.class);
                    intent.putExtra("customerdata",bean);
                    intent.putExtra("vcode",code);
                    startActivity(intent);
                }
                else
                {

                    showToast(""+responseObj.getString("message"));
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE || requestCode == CAMERA_REQUEST) {
                if (resultCode == RESULT_OK)
                {
                    picpath = data.getStringExtra("filePath");
                    if (picpath != null) {
                        File imgFile = new File(data.getStringExtra("filePath"));
                        if (imgFile.exists()) {
                            circularImageView_profile_pic.setImageURI(Uri.fromFile(imgFile));
                        }
                    } else {
                        picpath = "";
                    }
                    Log.i("File Path", "" + picpath);

                }
            }
            if (requestCode == ADDRESSFETCH)
            {
                if(data!=null)
                {
                    textView_address1.setText(data.getStringExtra("address"));
                    profileBean.setLat(data.getStringExtra("lat"));
                    profileBean.setLng(data.getStringExtra("long"));
                }
            }
        }
    }

    @Override
    public void onBackPressed() {

        finish();
    }
}
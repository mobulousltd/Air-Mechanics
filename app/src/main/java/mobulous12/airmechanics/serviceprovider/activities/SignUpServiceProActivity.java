package mobulous12.airmechanics.serviceprovider.activities;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import mobulous12.airmechanics.MapActivityToPickAddress;
import mobulous12.airmechanics.R;
import mobulous12.airmechanics.airinterfaces.MyDialogListenerInterface;
import mobulous12.airmechanics.beans.ProfileBean;
import mobulous12.airmechanics.customer.activities.VerificationActivity;
import mobulous12.airmechanics.fonts.FontBinding;
import mobulous12.airmechanics.serviceprovider.adapters.DocumentsAdapter;
import mobulous12.airmechanics.serviceprovider.dialogs.CategoriesDialogFragment;
import mobulous12.airmechanics.serviceprovider.dialogs.ServiceRadiusDialogFrag;
import mobulous12.airmechanics.serviceprovider.dialogs.SpecialityDialogFrag;
import mobulous12.airmechanics.serviceprovider.dialogs.WorkingDaysDialogFrag;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.utils.CircularImageView;
import mobulous12.airmechanics.utils.TakeImage;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

public class SignUpServiceProActivity extends AppCompatActivity implements View.OnClickListener, ApiListener,
        MyDialogListenerInterface
{

    ProfileBean profileBean;
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private static int ADDRESSFETCH=011;

    private LinearLayout root_activity_sign_up_servicepro;
    private Button button_signIn_servicePro_SignUpActivity;
    private RelativeLayout root_back_signUp_servicePro;
    private TextView textView_back_signUp_servicePro,textView_attachDocs_servicePro, textview_address_servicePro,textview_speciality_sp;;
    private EditText editText_fullName_SP,editText_companyName_SP, et_minchrge_sp, editText_email_SP,editText_password_SP,
            editText_confirmPassword_SP,editText_contactNumber_SP;
    CircularImageView circularImageView_profile_pic_signUp_servicePro;
    String profilepic="", docpic="", type="", radius="5", categories="",workdays="", speciality="";
    RecyclerView attched_docs;
    ArrayList<String> arrayList=new ArrayList<String>();
    DocumentsAdapter documentsAdapter;
    private TextView textview_categories_sp,textview_open_sp,textview_close_sp, textview_serviceArea_sp,textview_workdays_sp;
    private EditText editText_employees_sp;
    Calendar calendar;
    private int hour,minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_sign_up_servicepro1);
        /*views*/
        root_activity_sign_up_servicepro = (LinearLayout) findViewById(R.id.root_activity_sign_up_servicepro);
        et_minchrge_sp=(EditText)findViewById(R.id.et_minchrge_sp);
        editText_fullName_SP = (EditText) findViewById(R.id.editText_fullName_servicePro);
        editText_companyName_SP = (EditText) findViewById(R.id.editText_companyName_servicePro);
        editText_email_SP = (EditText) findViewById(R.id.editText_email_servicePro);
        editText_password_SP = (EditText) findViewById(R.id.editText_password_servicePro);
        editText_confirmPassword_SP = (EditText) findViewById(R.id.editText_confirmPassword_servicePro);
        editText_contactNumber_SP = (EditText) findViewById(R.id.editText_contactNumber_servicePro);
        textview_address_servicePro = (TextView) findViewById(R.id.textview_address_servicePro);
        textview_categories_sp = (TextView) findViewById(R.id.textview_categories_sp);
        textview_open_sp = (TextView) findViewById(R.id.textview_open_sp);
        textview_close_sp = (TextView) findViewById(R.id.textview_close_sp);
        textview_serviceArea_sp = (TextView) findViewById(R.id.textview_serviceArea_sp);
        editText_employees_sp = (EditText) findViewById(R.id.editText_employees_sp);

        textview_workdays_sp = (TextView) findViewById(R.id.textview_workdays_sp);
        textView_attachDocs_servicePro = (TextView) findViewById(R.id.textView_attachDocs_servicePro);

        textview_speciality_sp = (TextView) findViewById(R.id.textview_speciality_sp);
        textview_address_servicePro.setText(SharedPreferenceWriter.getInstance(SignUpServiceProActivity.this).getString(SPreferenceKey.ADDRESS));
        button_signIn_servicePro_SignUpActivity = (Button) findViewById(R.id.button_signIn_servicePro_SignUpActivity);
        root_back_signUp_servicePro = (RelativeLayout) findViewById(R.id.root_back_signUp_servicePro);
        textView_back_signUp_servicePro = (TextView) findViewById(R.id.textView_back_signUp_servicePro);

        circularImageView_profile_pic_signUp_servicePro=(CircularImageView)findViewById(R.id.circularImageView_profile_pic_signUp_servicePro);

        circularImageView_profile_pic_signUp_servicePro.setOnClickListener(this);
        attched_docs=(RecyclerView)findViewById(R.id.attched_docs);

        documentsAdapter=new DocumentsAdapter(this, arrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        attched_docs.setLayoutManager(linearLayoutManager);
        attched_docs.setAdapter(documentsAdapter);

        documentsAdapter.onItemClickListener(new DocumentsAdapter.MyClickListener()
        {
            @Override
            public void onItemClick(int position, View v) {

            }

            @Override
            public void OnLongPress(int position, View v) {
                if(arrayList.size()>0)
                {
                    removedocspopup(position);
                }
            }
        });
        root_activity_sign_up_servicepro.setOnClickListener(this);
        button_signIn_servicePro_SignUpActivity.setOnClickListener(this);
        root_back_signUp_servicePro.setOnClickListener(this);

        findViewById(R.id.linear_service_area_sp).setOnClickListener(this);
        findViewById(R.id.linear_categories_sp).setOnClickListener(this);
        textview_open_sp.setOnClickListener(this);
        textview_close_sp.setOnClickListener(this);
        findViewById(R.id.linear_working_days_sp).setOnClickListener(this);
        findViewById(R.id.linear_attachDocs_sp).setOnClickListener(this);
        findViewById(R.id.linear_address_sp).setOnClickListener(this);
        findViewById(R.id.linear_speciality_sp).setOnClickListener(this);
        profileBean = new ProfileBean();
        profileBean.setLat(SharedPreferenceWriter.getInstance(SignUpServiceProActivity.this).getString(SPreferenceKey.LATITUDE));
        profileBean.setLng(SharedPreferenceWriter.getInstance(SignUpServiceProActivity.this).getString(SPreferenceKey.LONGITUDE));
        calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute= calendar.get(Calendar.MINUTE);
    } //onCreate() Ends Here



    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.root_activity_sign_up_servicepro: {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            break;
            case R.id.root_back_signUp_servicePro:
                finish();
                break;
            case R.id.button_signIn_servicePro_SignUpActivity:
                if(validations())
                {
                    SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.CUSTOMER_LOGIN, false);
                    SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, true);
                    profileBean.setFullname(editText_fullName_SP.getText().toString().trim());
                    profileBean.setCompanyname(editText_companyName_SP.getText().toString());
                    SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.COMPANYNAME, profileBean.getCompanyname());
                    profileBean.setEmail(editText_email_SP.getText().toString().trim());
                    profileBean.setPassword(editText_password_SP.getText().toString().trim());
                    profileBean.setContactno(editText_contactNumber_SP.getText().toString().trim());
                    profileBean.setAddress(textview_address_servicePro.getText().toString().trim());
                    profileBean.setImagePath(profilepic);
                    profileBean.setCity("Sector-63, Noida");
                    profileBean.setCountry_code("+91");
                    profileBean.setFrom(textview_open_sp.getText().toString());
                    profileBean.setTo(textview_close_sp.getText().toString());
                    profileBean.setRadius(radius);
                    profileBean.setEmployees(editText_employees_sp.getText().toString());
                    profileBean.setWorking_days(workdays);
                    profileBean.setCategory(categories);
                    profileBean.setSpeciality(speciality);
                    profileBean.setMnCharg(et_minchrge_sp.getText().toString());

                    senCodeServiceHit();
                }
                break;
            case R.id.linear_attachDocs_sp:
                if(arrayList.size()==7)
                {
                    Toast.makeText(SignUpServiceProActivity.this, "Not more than 7 documents can be attached", Toast.LENGTH_SHORT).show();
                }
                else {
                    type="docs";
                    showDialogForUpload("Upload your Documents");
                }

                break;
            case R.id.circularImageView_profile_pic_signUp_servicePro:

                type="profile";
                showDialogForUpload("Upload your Profile Image");
                break;

            case R.id.linear_address_sp:
                Intent intent=new Intent(new Intent(SignUpServiceProActivity.this, MapActivityToPickAddress.class));
                intent.putExtra("lat", profileBean.getLat());
                intent.putExtra("lng", profileBean.getLng());
                intent.putExtra("address", textview_address_servicePro.getText().toString());
                startActivityForResult(intent, ADDRESSFETCH);
                break;
            case R.id.linear_categories_sp:

                showCategoriesDialog();
                break;
            case R.id.linear_working_days_sp:

               showworkDaysDialog();
                break;

            case R.id.linear_speciality_sp:
                SpecialityDialogFrag specialityDialogFrag = new SpecialityDialogFrag();
                Bundle bundle=new Bundle();
                bundle.putString("specialty", speciality);
                specialityDialogFrag.setArguments(bundle);
                specialityDialogFrag.show(getSupportFragmentManager(), "Speciality");
                break;
            case R.id.linear_service_area_sp:
                showServiceAreaDialog();
                break;
            /*timings*/
            case R.id.textview_open_sp:
                showTimer(textview_open_sp);
                break;
            case R.id.textview_close_sp:
                showTimer(textview_close_sp);
                break;
              /*timings ENDS*/

        }
    }

    private boolean validations()
    {
        if (editText_fullName_SP.getText().toString().trim().isEmpty())
        {
            showToast("Please Enter Full Name");
            return false;
        }
        else if (editText_companyName_SP.getText().toString().trim().isEmpty())
        {
            showToast("Please enter company name");
            return false;
        }
        else if (editText_email_SP.getText().toString().trim().isEmpty())
        {
            showToast("Please enter Email");
            return false;
        }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(editText_email_SP.getText().toString()).matches()) {
            showToast("Please enter valid Email");
            return false;
        }
        else if (editText_password_SP.getText().toString().trim().isEmpty())
        {
            showToast("Please enter Password ");
            return false;
        }
        else if (editText_password_SP.getText().toString().length() < 6) {

            showToast("Password must be atleast 6 characters long");
            return false;
        }
        else if (editText_confirmPassword_SP.getText().toString().trim().isEmpty())
        {
            showToast( "Please enter confirm password");
            return false;
        }
        else if (editText_confirmPassword_SP.getText().toString().length() < 6)
        {
            showToast("Confirm Password must be atleast 6 characters long");
            return false;
        }
        else if(!editText_confirmPassword_SP.getText().toString().equals(editText_password_SP.getText().toString()))
        {
            showToast("Password and Confirm password doesn't match");
            return false;
        }
        else if (editText_contactNumber_SP.getText().toString().trim().isEmpty())
        {
            showToast( "Please Enter Contact Number");
            return false;
        }
        else if(editText_contactNumber_SP.getText().toString().length()<8 || editText_contactNumber_SP.getText().toString().length()>15)
        {
            showToast( "Please enter a valid Contact  number.");
            return false;
        }
        else if (!Patterns.PHONE.matcher(editText_contactNumber_SP.getText().toString()).matches())
        {
            showToast("Please Enter valid Contact Number");
            return false;
        }
        else if (textview_address_servicePro.getText().toString().trim().isEmpty()) {
            showToast("Please Enter valid Address");
            return false;
        }
        else if (textview_categories_sp.getText().toString().trim().isEmpty()) {
            showToast("Please select at least one category");
            return false;
        }
        else if (textview_open_sp.getText().toString().trim().isEmpty()) {
            showToast("Please enter opening time");
            return false;
        } else if (textview_close_sp.getText().toString().trim().isEmpty()) {
            showToast("Please enter closing time");

            return false;
        }
        else if (textview_serviceArea_sp.getText().toString().trim().isEmpty()) {
            showToast("Please enter service area radius");

            return false;
        }
        else if (editText_employees_sp.getText().toString().trim().isEmpty())
        {
            showToast("Please enter number of employees");
            return false;
        }
        else if (textview_workdays_sp.getText().toString().trim().isEmpty()) {
            showToast("Please enter working days");
            return false;
        }
        else if (speciality.isEmpty()) {
            showToast("Please enter working days");
            return false;
        }
        else if(et_minchrge_sp.getText().toString().isEmpty())
        {
            showToast("Please enter Minimum Charges");
            return false;
        }
        else
        {
            return true;
        }
    }
    private void showToast(String message)
    {

        Toast toast = Toast.makeText(this,message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /*Dialogs */
    private void showCategoriesDialog()
    {

        CategoriesDialogFragment categoriesFragment = new CategoriesDialogFragment();

        Bundle bundle=new Bundle();
        bundle.putString("categories", categories);
        categoriesFragment.setArguments(bundle);
        categoriesFragment.show(getSupportFragmentManager(), "categoriesFrag");

    }
    private void showworkDaysDialog()
    {
        WorkingDaysDialogFrag daysDialogFrag = new WorkingDaysDialogFrag();
        Bundle bundle=new Bundle();
        bundle.putString("workdays", workdays);
        daysDialogFrag.setArguments(bundle);
        daysDialogFrag.show(getSupportFragmentManager(), "WorkingDays");
    }

    private void showServiceAreaDialog()
    {

        ServiceRadiusDialogFrag fragment=new ServiceRadiusDialogFrag();
        Bundle bundle=new Bundle();
        bundle.putString("radius", radius);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "Radius");

    }

    private void showTimer(final TextView tv)
    {
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
//                String aMpM = "AM";
//                if(selectedHour >11)
//                {
//                    aMpM = "PM";
//                }
//
//                //Make the 24 hour time format to 12 hour time format
//                int currentHour;
//                if(selectedHour>11)
//                {
//                    currentHour = selectedHour - 12;
//                }
//                else
//                {
//                    currentHour = selectedHour;
//                }
//                tv.setText(selectedHour + ":" + selectedMinute);


                String hours = selectedHour+"";
                String minutes = selectedMinute+"";

                if (selectedMinute < 10)
                {
                    minutes = "0"+minutes;
                }
                if (selectedHour < 10)
                {
                    hours = "0"+selectedHour;
                }

                tv.setText(hours + ":" + minutes);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.show();
    }



    // profile pic capture
    public void showDialogForUpload(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage(message);

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
        TextView msgTxt = (TextView) alertDialog.findViewById(android.R.id.message);
        FontBinding.setFont(msgTxt, "regular");

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


    public  void removedocspopup(final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignUpServiceProActivity.this);
        alertDialogBuilder.setTitle(getString(R.string.app_name));
        alertDialogBuilder.setMessage("Do you want to delete this image?");

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                arrayList.remove(position);
                documentsAdapter.setArrayList(arrayList);
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();
        TextView msgTxt = (TextView) alertDialog.findViewById(android.R.id.message);
        FontBinding.setFont(msgTxt, "regular");

        //Buttons
        Button positive_button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negative_button = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        positive_button.setTextColor(getResources().getColor(R.color.blue));
        negative_button.setTextColor(getResources().getColor(R.color.black));

    }

/*dialogs ENDS */

    //services

    private void senCodeServiceHit() {
        try {

            MultipartEntityBuilder multipartbuilder = MultipartEntityBuilder.create();
            multipartbuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            multipartbuilder.addTextBody("country_code", "+91");
            multipartbuilder.addTextBody("mobile", editText_contactNumber_SP.getText().toString().trim());
            multipartbuilder.addTextBody("email", editText_email_SP.getText().toString().trim());
            ServiceBean serviceBean = new ServiceBean();
            serviceBean.setMethodName("Services/sendCode");
            serviceBean.setActivity(SignUpServiceProActivity.this);
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
                    String code = responseObj.getJSONObject("response").getString("verification_no");
                    Intent intent = new Intent(this, VerificationActivity.class);
                    intent.putExtra("customerdata",profileBean);
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
        if(resultCode==RESULT_OK)
        {
            if (requestCode == RESULT_LOAD_IMAGE || requestCode == CAMERA_REQUEST)
            {
                if (resultCode == RESULT_OK) {

                    String picpath = data.getStringExtra("filePath");
                    if (picpath != null) {
                        File imgFile = new File(data.getStringExtra("filePath"));
                        if (type.equals("profile")) {
                            if (imgFile.exists()) {
                                profilepic = data.getStringExtra("filePath");
                                circularImageView_profile_pic_signUp_servicePro.setImageURI(Uri.fromFile(imgFile));
                            }
                        } else {
                            if (imgFile.exists()) {
                                if (arrayList.size() < 8) {
                                    arrayList.add(data.getStringExtra("filePath"));
                                    documentsAdapter.setArrayList(arrayList);
                                }
                            }
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
                    textview_address_servicePro.setText(data.getStringExtra("address"));
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

    @Override
    public void onFinishDialog(String inputText, String type)
    {

//        Radius
        if(type.equals("radius"))
        {
            radius=inputText;

            if(radius.equals("5")) {
                textview_serviceArea_sp.setText(getResources().getString(R.string.uptofive));
            }
            if(radius.equals("10")) {
                textview_serviceArea_sp.setText(getResources().getString(R.string.uptoten));
            }
             if(radius.equals("20")) {
                 textview_serviceArea_sp.setText(getResources().getString(R.string.upto20));
            }

        }

//        Categories

        if(type.equals("categories"))
        {
            categories = inputText;

            textview_categories_sp.setText(categories);

            String cat[]=inputText.split(",");
            String category="";
            for(int i=0;i<cat.length;i++)
            {
                if(cat[i].equals(getString(R.string.two_wheeler)))
                {
                    category="two";
                }
                if(cat[i].equals(getString(R.string.light_weight_vehicle)))
                {
                    if(category.isEmpty())
                    {
                        category="light";
                    }
                    else
                    {
                        category+=","+"light";
                    }
                }
                if(cat[i].equals(getString(R.string.heavy_weight_vehicle)))
                {
                    if(category.isEmpty())
                    {
                        category="heavy";
                    }
                    else
                    {
                        category+=","+"heavy";
                    }
                }
            }
            categories=category;
        }

        //        Working Days

        if(type.equals("workdays"))
        {
            workdays = inputText;

            textview_workdays_sp.setText(workdays);

            String days[]=inputText.split(",");
            String workday="";
            for(int i=0;i<days.length;i++)
            {
                if(days[i].equals(getString(R.string.sunday)))
                {
                    workday="0";
                }
                if(days[i].equals(getString(R.string.monday)))
                {
                    if(workday.isEmpty())
                    {
                        workday="1";
                    }
                    else
                    {
                        workday+=","+"1";
                    }
                }
                if(days[i].equals(getString(R.string.tuesday)))
                {
                    if(workday.isEmpty())
                    {
                        workday="2";
                    }
                    else
                    {
                        workday+=","+"2";
                    }
                }

                if(days[i].equals(getString(R.string.wednesday)))
                {
                    if(workday.isEmpty())
                    {
                        workday="3";
                    }
                    else
                    {
                        workday+=","+"3";
                    }
                }
                if(days[i].equals(getString(R.string.thursday)))
                {
                    if(workday.isEmpty())
                    {
                        workday="4";
                    }
                    else
                    {
                        workday+=","+"4";
                    }
                }
                if(days[i].equals(getString(R.string.friday)))
                {
                    if(workday.isEmpty())
                    {
                        workday="5";
                    }
                    else
                    {
                        workday+=","+"5";
                    }
                }
                if(days[i].equals(getString(R.string.saturday)))
                {
                    if(workday.isEmpty())
                    {
                        workday="6";
                    }
                    else
                    {
                        workday+=","+"6";
                    }
                }
            }
            workdays=workday;
        }
        if(type.equals("speciality"))
        {
            speciality=inputText;
            if(!inputText.isEmpty())
            {
                if(inputText.equals("two"))
                {
                    textview_speciality_sp.setText(getString(R.string.two_wheeler));
                }
                else if(inputText.equals("light"))
                {
                    textview_speciality_sp.setText(getString(R.string.light_weight_vehicle));
                }
                else
                {
                    textview_speciality_sp.setText(getString(R.string.heavy_weight_vehicle));
                }
            }
        }

    }

}
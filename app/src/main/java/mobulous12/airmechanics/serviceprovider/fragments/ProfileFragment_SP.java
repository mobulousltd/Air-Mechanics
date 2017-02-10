package mobulous12.airmechanics.serviceprovider.fragments;


import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mobulous12.airmechanics.MapActivityToPickAddress;
import mobulous12.airmechanics.R;
import mobulous12.airmechanics.airinterfaces.MyDialogListenerInterface;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.customer.fragments.ChangeContactFrag;
import mobulous12.airmechanics.customer.fragments.ChangePasswordFragment;
import mobulous12.airmechanics.databinding.FragmentProfileSpBinding;
import mobulous12.airmechanics.fonts.FontBinding;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;
import mobulous12.airmechanics.fonts.Font;
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

import static android.app.Activity.RESULT_OK;

public class ProfileFragment_SP extends Fragment implements View.OnClickListener, MyDialogListenerInterface, ApiListener
{

    private View view;
    private MenuItem save, editing;
    private Menu myMenu;
    Calendar calendar;
    private EditText editText_name_profileSP, editText_email_profileSP,
            editText_employees_profileSP;
//    private EditText et_companyname;
    private TextView textView_userName_ProfileSP, textView_openText_profileSP, textView_closeText_profileSP,
            tv_serviceArea_spProfile,tv_workdays_spProfile, tv_address_profileSP,tv_categoriesSP,tv_specialitySP,
            editText_contactNumber_profileSP,tv_minCharge_profileSP,editText_companyNameSP;

    private int hour,minute;

    private String workdays="",radius="5", picpath = "", type="", lat="", lng="", categories="",speciality="";;

    private static int ADDRESSFETCH=011;
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private TextView textView_contactTextProfileSP;
    private LinearLayout linear_setContactSP,ll_ContactSP;
    private TextView tv_contactNumSP;
    private EditText et_minCharge_profileSP;
    private boolean isEditing = false;


    public ProfileFragment_SP() {
        // Required empty public constructor
    }
    private CircularImageView profileImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ((HomeActivityServicePro)getActivity()).setToolbarTitleSP(getResources().getString(R.string.headername_profile));
        ((HomeActivityServicePro)getActivity()).toolbarVisibleSP();
        ((HomeActivityServicePro)getActivity()).setNavigationIconSP();


        if(view==null)
        {
            FragmentProfileSpBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_sp, container, false);
            view = binding.getRoot();
            //Views
            profileImage = (CircularImageView) view.findViewById(R.id.circularImageView_profileSP);
            textView_userName_ProfileSP = (TextView) view.findViewById(R.id.textView_userName_ProfileSP);
            textView_openText_profileSP = (TextView) view.findViewById(R.id.textView_openText_profileSP);
            textView_closeText_profileSP = (TextView) view.findViewById(R.id.textView_closeText_profileSP);
            tv_serviceArea_spProfile = (TextView) view.findViewById(R.id.tv_serviceArea_spProfile);
            tv_workdays_spProfile = (TextView) view.findViewById(R.id.tv_workdays_spProfile);

            editText_name_profileSP = (EditText) view.findViewById(R.id.editText_name_profileSP);
//        editText_contactNumber_profileSP = (TextView) view.findViewById(R.id.editText_contactNumber_profileSP);
            editText_companyNameSP = (EditText) view.findViewById(R.id.editText_companyNameSP);
            editText_email_profileSP = (EditText) view.findViewById(R.id.editText_email_profileSP);
            tv_address_profileSP = (TextView) view.findViewById(R.id.tv_address_profileSP);
            editText_employees_profileSP = (EditText) view.findViewById(R.id.editText_employees_profileSP);

            et_minCharge_profileSP = (EditText) view.findViewById(R.id.et_minCharge_profileSP);
//        et_companyname=(EditText)view.findViewById(R.id.et_companyname);
            tv_categoriesSP = (TextView) view.findViewById(R.id.tv_categoriesSP);
            tv_specialitySP = (TextView) view.findViewById(R.id.tv_specialitySP);

        /*change Contact views*/
            textView_contactTextProfileSP = (TextView) view.findViewById(R.id.textView_contactTextProfileSP);
            linear_setContactSP = (LinearLayout) view.findViewById(R.id.linear_setContactSP);
            tv_contactNumSP = (TextView) view.findViewById(R.id.tv_contactNumSP);
            ll_ContactSP = (LinearLayout) view.findViewById(R.id.ll_ContactSP);


            tv_contactNumSP.setVisibility(View.VISIBLE);
            ll_ContactSP.setVisibility(View.GONE);

            view.findViewById(R.id.ll_changepasssp).setOnClickListener(this);
            textView_openText_profileSP.setOnClickListener(this);
            textView_closeText_profileSP.setOnClickListener(this);
            tv_serviceArea_spProfile.setOnClickListener(this);
            tv_workdays_spProfile.setOnClickListener(this);
            profileImage.setOnClickListener(this);
            tv_address_profileSP.setOnClickListener(this);
            tv_categoriesSP.setOnClickListener(this);
            tv_specialitySP.setOnClickListener(this);


//        editText_contactNumber_profileSP.setOnClickListener(this);
            linear_setContactSP.setOnClickListener(this);


//         /*make views Non - Editable*/
            profileImage.setEnabled(false);
            editText_name_profileSP.setEnabled(false);
//        editText_contactNumber_profileSP.setEnabled(false);
            editText_companyNameSP.setEnabled(false);
            editText_email_profileSP.setEnabled(false);
            linear_setContactSP.setEnabled(false);
            et_minCharge_profileSP.setEnabled(false);
//        et_companyname.setEnabled(false);
            tv_categoriesSP.setEnabled(false);
            tv_specialitySP.setEnabled(false);
            tv_address_profileSP.setEnabled(false);
            editText_employees_profileSP.setEnabled(false);
            textView_openText_profileSP.setEnabled(false);
            textView_closeText_profileSP.setEnabled(false);
            tv_serviceArea_spProfile.setEnabled(false);
            tv_workdays_spProfile.setEnabled(false);
            view.findViewById(R.id.ll_changepasssp).setEnabled(false);

            if(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.LOGINTYPE).equals("social"))
            {
                view.findViewById(R.id.ll_changepasssp).setVisibility(View.GONE);
            }

            lat=SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.LATITUDE);
            lng=SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.LONGITUDE);
            calendar = Calendar.getInstance();
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute= calendar.get(Calendar.MINUTE);

            viewProfileServiceHit();
        }
        else
        {
            if(savedInstanceState != null) {
                isEditing = savedInstanceState.getBoolean("isEditing");
            }

        }
//        view.findViewById(R.id.ll_changeContactsp).setVisibility(View.GONE);

//        //setting data
//        textView_userName_ProfileSP.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.FullName));
//        editText_name_profileSP.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.FullName));
//        editText_contactNumber_profileSP.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.PhoneNumber));
//        editText_email_profileSP.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.Email));
//        tv_address_profileSP.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.ADDRESS));
//        editText_employees_profileSP.setText("");
//        if(!SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.IMAGE).isEmpty())
//        {
//            AQuery aQuery=new AQuery(profileImage);
//            aQuery.id(profileImage).image(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.IMAGE));
//        }
//        else
//        {
//            profileImage.setImageResource(R.drawable.default_profile_pic);
//        }

        return view;
    }  // onCreateView Ends Here


    //validation name
    private boolean validateName(EditText editText)
    {
        final String regex = "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$";
        final String string = editText.getText().toString().trim();

        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);

        if (matcher.find())
        {
            for (int i = 1; i <= matcher.groupCount(); i++)
            {
                Log.d("Name Matching","Group " + i + ": " + matcher.group(i));
            }
            return true;
        }

            return false;

    }

    //validations
    private boolean validate()
    {
        if (editText_name_profileSP.getText().toString().trim().equals(""))
        {
            Toast.makeText(getActivity(), "Please enter your Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!validateName(editText_name_profileSP))
        {
            Toast.makeText(getActivity(), "Please enter valid Name", Toast.LENGTH_SHORT).show();
            editText_name_profileSP.setText(textView_userName_ProfileSP.getText());
            return false;
        }
        else if (tv_contactNumSP.getText().toString().trim().equalsIgnoreCase(""))
        {
            Toast.makeText(getActivity(), "Please update Contact Number.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (editText_email_profileSP.getText().toString().trim().equals(""))
        {
            Toast.makeText(getActivity(), "Please enter your Email Address", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (editText_companyNameSP.getText().toString().trim().equals(""))
        {
            Toast.makeText(getActivity(), "Please enter your Company Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(editText_email_profileSP.getText().toString()).matches())
        {
            Toast.makeText(getActivity(), "Please enter valid Email Address", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (tv_address_profileSP.getText().toString().trim().equals(""))
        {
            Toast.makeText(getActivity(), "Please enter Address", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (tv_categoriesSP.getText().toString().trim().equals(""))
        {
            Toast.makeText(getActivity(), "Please select at least one category", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (tv_specialitySP.getText().toString().trim().equals(""))
        {
            Toast.makeText(getActivity(),"Please select at least one speciality", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(textView_openText_profileSP.getText().toString().isEmpty())
        {
            Toast.makeText(getActivity(), "Please enter opening time.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(textView_closeText_profileSP.getText().toString().isEmpty())
        {
            Toast.makeText(getActivity(), "Please enter closing time.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(et_minCharge_profileSP.getText().toString().isEmpty())
        {
            Toast.makeText(getActivity(), "Please enter Minimum Charge.", Toast.LENGTH_SHORT).show();
            return false;
        }
//        else if(et_companyname.getText().toString().isEmpty())
//        {
//            Toast.makeText(getActivity(), "Please enter your company name.", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        else if(radius.isEmpty())
        {
            Toast.makeText(getActivity(), "Please select service area radius.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(editText_employees_profileSP.getText().toString().isEmpty())
        {
            Toast.makeText(getActivity(), "Please enter number of employees", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(workdays.isEmpty())
        {
            Toast.makeText(getActivity(), "Please select working days.", Toast.LENGTH_SHORT).show();
            return false;
        }

            return true;
    }

//    /*  Dialogs */

    private void showTimer(final TextView tv)
    {
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {

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

    private void showCategoriesDialog()
    {
        CategoriesDialogFragment categoriesFragment = new CategoriesDialogFragment();
        Bundle bundle=new Bundle();
        bundle.putString("categories", categories);
        categoriesFragment.setArguments(bundle);
        categoriesFragment.setTargetFragment(ProfileFragment_SP.this, 300);
        categoriesFragment.show(getActivity().getSupportFragmentManager(), "categoriesFrag");
        tv_specialitySP.setText("");

    }
    private void showSpecialityDialog()
    {
        SpecialityDialogFrag specialityFrag = new SpecialityDialogFrag();
        Bundle bundle=new Bundle();
        bundle.putString("specialty", speciality);
        bundle.putString("selectedCategories",categories);
        specialityFrag.setArguments(bundle);
        specialityFrag.setTargetFragment(ProfileFragment_SP.this, 300);
        if(!(categories.isEmpty()) )
        {
            specialityFrag.show(getActivity().getSupportFragmentManager(), "Speciality");
        }
        else {
            Toast.makeText(getActivity(), "Please Select at least one Category first.", Toast.LENGTH_SHORT).show();
        }

    }

    private void showServiceAreaDialog()
    {
        ServiceRadiusDialogFrag fragment=new ServiceRadiusDialogFrag();
        Bundle bundle=new Bundle();
        bundle.putString("radius", radius);
        fragment.setArguments(bundle);
        fragment.setTargetFragment(ProfileFragment_SP.this, 300);
        fragment.show(getFragmentManager(), "Radius");
    }

    private void showworkDaysDialog()
    {
        WorkingDaysDialogFrag daysDialogFrag = new WorkingDaysDialogFrag();
        Bundle bundle=new Bundle();
        bundle.putString("workdays", workdays);
        daysDialogFrag.setArguments(bundle);
        daysDialogFrag.setTargetFragment(ProfileFragment_SP.this, 300);  // Sets the target fragment for use later when sending results
        daysDialogFrag.show(getFragmentManager(), "WorkingDays");
    }

    public void showDialogForUpload(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), TakeImage.class);
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
                Intent intent = new Intent(getActivity().getApplicationContext(), TakeImage.class);
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


//    OnclickListeners callback
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.linear_setContactSP:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_serviceHomeContainer, new ChangeContactFrag(), "changeContactSP").addToBackStack("changeContactSP").commit();
                break;
            case R.id.circularImageView_profileSP:
                type="profile";
                showDialogForUpload("Upload your Profile Image");
                break;
            case R.id.ll_changepasssp:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_serviceHomeContainer,new ChangePasswordFragment(),"changePassSPFrag").addToBackStack("changePassSP").commit();
                break;
            case R.id.textView_openText_profileSP:
                showTimer(textView_openText_profileSP);
                break;
            case R.id.textView_closeText_profileSP:
                showTimer(textView_closeText_profileSP);
                break;
            case R.id.tv_serviceArea_spProfile:
                showServiceAreaDialog();
                break;
            case R.id.tv_workdays_spProfile:
                showworkDaysDialog();
                break;
            case R.id.tv_categoriesSP:
                showCategoriesDialog();
                break;
            case R.id.tv_specialitySP:
                showSpecialityDialog();
                break;
            case R.id.tv_address_profileSP:
                Intent intent = new Intent(getActivity(), MapActivityToPickAddress.class);
                intent.putExtra("lat",lat);
                intent.putExtra("lng",lng);
                intent.putExtra("address",tv_address_profileSP.getText().toString().trim());
                startActivityForResult(intent,ADDRESSFETCH);
                break;
        }
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu,menu);
        myMenu = menu;
//      Retrieve menuItems
        save = menu.findItem(R.id.save_profile);
        editing = menu.findItem(R.id.edit_profile);

//        Checking to keep editing state Intact
        if(isEditing)
        {
            save.setVisible(true);
            editing.setVisible(false);

            profileImage.setEnabled(true);
            editText_name_profileSP.setEnabled(true);
            editText_companyNameSP.setEnabled(true);
            tv_address_profileSP.setEnabled(true);
            editText_employees_profileSP.setEnabled(true);
            textView_openText_profileSP.setEnabled(true);
            textView_closeText_profileSP.setEnabled(true);
            tv_serviceArea_spProfile.setEnabled(true);
            tv_workdays_spProfile.setEnabled(true);
            linear_setContactSP.setEnabled(true);
            ll_ContactSP.setVisibility(View.VISIBLE);
            ll_ContactSP.setEnabled(true);
            et_minCharge_profileSP.setEnabled(true);
            tv_categoriesSP.setEnabled(true);
            tv_specialitySP.setEnabled(true);

            if (SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.LOGINTYPE).equals("normal")) {
                editText_email_profileSP.setEnabled(true);
                view.findViewById(R.id.ll_changepasssp).setEnabled(true);
            }

            // Update contact field with New Number
            tv_contactNumSP.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.PhoneNumber));

        }
        else
        {
            save.setVisible(false);
            editing.setVisible(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.edit_profile:
              /*make view Editable*/
                isEditing = true;   // Editing is Enabled
                save.setVisible(true);
                editing.setVisible(false);

                profileImage.setEnabled(true);
                editText_name_profileSP.setEnabled(true);
                editText_companyNameSP.setEnabled(true);
//                editText_contactNumber_profileSP.setEnabled(true);
                tv_address_profileSP.setEnabled(true);
                editText_employees_profileSP.setEnabled(true);
                textView_openText_profileSP.setEnabled(true);
                textView_closeText_profileSP.setEnabled(true);
                tv_serviceArea_spProfile.setEnabled(true);
                tv_workdays_spProfile.setEnabled(true);
                linear_setContactSP.setEnabled(true);
                ll_ContactSP.setVisibility(View.VISIBLE);
                ll_ContactSP.setEnabled(true);
                et_minCharge_profileSP.setEnabled(true);
//                et_companyname.setEnabled(true);
                tv_categoriesSP.setEnabled(true);
                tv_specialitySP.setEnabled(true);
//                view.findViewById(R.id.ll_changeContactsp).setVisibility(View.VISIBLE);
                if(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.LOGINTYPE).equals("normal"))
                {
                    editText_email_profileSP.setEnabled(true);
                    view.findViewById(R.id.ll_changepasssp).setEnabled(true);
                }
                break;

            case R.id.save_profile:
              /*make view non-Editable*/
                isEditing = false;  // Editing is Disabled
                save.setVisible(false);
                editing.setVisible(true);

                profileImage.setEnabled(false);
                editText_name_profileSP.setEnabled(false);
                editText_companyNameSP.setEnabled(false);
//                editText_contactNumber_profileSP.setEnabled(false);
                editText_email_profileSP.setEnabled(false);
                tv_address_profileSP.setEnabled(false);
                editText_employees_profileSP.setEnabled(false);
                textView_openText_profileSP.setEnabled(false);
                textView_closeText_profileSP.setEnabled(false);
                tv_serviceArea_spProfile.setEnabled(false);
                tv_workdays_spProfile.setEnabled(false);
                linear_setContactSP.setEnabled(false);
                ll_ContactSP.setVisibility(View.GONE);
                ll_ContactSP.setEnabled(false);
                et_minCharge_profileSP.setEnabled(false);
//                et_companyname.setEnabled(false);
                tv_categoriesSP.setEnabled(false);
                tv_specialitySP.setEnabled(false);
//                view.findViewById(R.id.ll_changeContactsp).setVisibility(View.GONE);
                view.findViewById(R.id.ll_changepasssp).setEnabled(false);
                if(validate())
                {
                    editProfileServiceHit();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_show_service_provider).setVisible(false);
    }


//    SERVICES
    private void viewProfileServiceHit() {

        MultipartEntityBuilder multipartbuilder = MultipartEntityBuilder.create();
        multipartbuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartbuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN));

        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setActivity(getActivity());
        serviceBean.setMethodName("Services/viewProfile");
        serviceBean.setApilistener(this);

        CustomHandler customHandler = new CustomHandler(serviceBean);
        customHandler.makeMultipartRequest(multipartbuilder);
    }

    private void editProfileServiceHit()
    {
        MultipartEntityBuilder multipartbuilder = MultipartEntityBuilder.create();
        multipartbuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartbuilder.addTextBody("lat", lat);
        multipartbuilder.addTextBody("long", lng);
        multipartbuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getString(SPreferenceKey.TOKEN));
        multipartbuilder.addTextBody("fullname", editText_name_profileSP.getText().toString());
        multipartbuilder.addTextBody("companyName", editText_companyNameSP.getText().toString());
        multipartbuilder.addTextBody("email", editText_email_profileSP.getText().toString());
        multipartbuilder.addTextBody("country_code", "+91");
        multipartbuilder.addTextBody("contact_no",  tv_contactNumSP.getText().toString());
        multipartbuilder.addTextBody("address",  tv_address_profileSP.getText().toString());
        multipartbuilder.addTextBody("city", "noida");
        multipartbuilder.addTextBody("description", "");
        multipartbuilder.addTextBody("category", categories);
        multipartbuilder.addTextBody("speciality", speciality);
        multipartbuilder.addTextBody("from", textView_openText_profileSP.getText().toString());
        multipartbuilder.addTextBody("to", textView_closeText_profileSP.getText().toString());
        multipartbuilder.addTextBody("radius", radius);
        multipartbuilder.addTextBody("working_days", workdays);
        multipartbuilder.addTextBody("employees", editText_employees_profileSP.getText().toString());
        multipartbuilder.addTextBody("min_charges", et_minCharge_profileSP.getText().toString());
//        multipartbuilder.addTextBody("companyName", et_companyname.getText().toString());


        if(picpath.isEmpty())
        {
            multipartbuilder.addTextBody("profile","");
        }
        else {
            multipartbuilder.addBinaryBody("profile", new File(picpath));
        }

        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setActivity(getActivity());
        serviceBean.setMethodName("Services/editProfile");
        serviceBean.setApilistener(this);

        CustomHandler customHandler = new CustomHandler(serviceBean);
        customHandler.makeMultipartRequest(multipartbuilder);

    }


//  API-RESPONSES
    @Override
    public void myServerResponse(JSONObject responseObj) {

        try
        {
            if (responseObj != null)
            {
                if (responseObj.getString("status").equalsIgnoreCase("SUCCESS"))
                {

                    if (responseObj.getString("requestKey").equalsIgnoreCase("viewProfile"))
                    {
                        JSONObject response = responseObj.getJSONObject("response");
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.FullName, response.getString("full_name"));
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.COMPANYNAME, response.getString("companyName"));
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.PhoneNumber, response.getString("contact_no"));
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.Email, response.getString("email"));
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.MYADDRESS, response.getString("address"));
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.IMAGE, response.getString("profile"));

                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.MINCHARGE, response.getString("min_charge"));
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.CATEGORIES, response.getString("categories"));
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.SPECIALITY, response.getString("specilityName"));
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.WORKDAYS, response.getString("workingDays"));

                        //profile username
                        textView_userName_ProfileSP.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.FullName));
                        //profile image
                        AQuery aQuery = new AQuery(profileImage);
                        if (!SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.IMAGE).isEmpty())
                        {
                            aQuery.id(profileImage).image(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.IMAGE));

                        } else {
                            aQuery.id(profileImage).image(R.drawable.default_profile_pic);
                        }

                        lat = response.getString("lat");
                        lng = response.getString("long");

                        editText_name_profileSP.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.FullName));
                        editText_companyNameSP.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.COMPANYNAME));
                        tv_contactNumSP.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.PhoneNumber));
                        editText_email_profileSP.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.Email));
                        tv_address_profileSP.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.MYADDRESS));

                        textView_openText_profileSP.setText(response.getString("start_time"));
                        textView_closeText_profileSP.setText(response.getString("end_time"));
                        editText_employees_profileSP.setText(response.getString("no_employe"));

                        et_minCharge_profileSP.setText(response.getString("min_charge"));

                        categories=response.getString("categories");
                        speciality = response.getString("specilityName");
                        workdays=response.getString("workingDays");
//                        et_companyname.setText(response.getString("companyName"));


//                      categories
                        String categoryArr[] = categories.split(",");
                        String category="";
                        for(int i=0;i<categoryArr.length;i++)
                        {
                            if(categoryArr[i].equals("two"))
                            {
                                category = getString(R.string.two_wheeler);
                            }
                            if(categoryArr[i].equals("light"))
                            {
                                if(category.isEmpty())
                                {
                                    category = getString(R.string.light_weight_vehicle);
                                }
                                else {
                                    category += ","+getString(R.string.light_weight_vehicle);
                                }
                            }
                            if(categoryArr[i].equals("heavy"))
                            {
                                if(category.isEmpty())
                                {
                                    category = getString(R.string.heavy_weight_vehicle);
                                }
                                else {
                                    category += ","+getString(R.string.heavy_weight_vehicle);
                                }
                            }
                        }

                        tv_categoriesSP.setText(category);

//                        speciality

                            if(speciality.equals("two"))
                            {
                                tv_specialitySP.setText(getString(R.string.two_wheeler));
                            }
                            if(speciality.equals("light"))
                            {
                                tv_specialitySP.setText(getString(R.string.light_weight_vehicle));
                            }
                            if(speciality.equals("heavy"))
                            {
                                tv_specialitySP.setText(getString(R.string.heavy_weight_vehicle));
                            }


//                        workdays
                        String days[]=workdays.split(",");
                        String workday="";

                        for(int i=0;i<days.length;i++)
                        {
                            if(days[i].equals("0"))
                            { workday=getString(R.string.sunday);  }
                            if(days[i].equals("1"))
                            {
                                if(workday.isEmpty())
                                {
                                    workday=getString(R.string.monday);
                                }
                                else
                                {
                                    workday+=","+getString(R.string.monday);
                                }
                            }
                            if(days[i].equals("2"))
                            {
                                if(workday.isEmpty())
                                {
                                    workday=getString(R.string.tuesday);
                                }
                                else
                                {
                                    workday+=","+getString(R.string.tuesday);
                                }
                            }

                            if(days[i].equals("3"))
                            {
                                if(workday.isEmpty())
                                {
                                    workday=getString(R.string.wednesday);
                                }
                                else
                                {
                                    workday+=","+getString(R.string.wednesday);
                                }
                            }
                            if(days[i].equals("4"))
                            {
                                if(workday.isEmpty())
                                {
                                    workday=getString(R.string.thursday);
                                }
                                else
                                {
                                    workday+=","+getString(R.string.thursday);
                                }
                            }
                            if(days[i].equals("5"))
                            {
                                if(workday.isEmpty())
                                {
                                    workday=getString(R.string.friday);
                                }
                                else
                                {
                                    workday+=","+getString(R.string.friday);
                                }
                            }
                            if(days[i].equals("6"))
                            {
                                if(workday.isEmpty())
                                {
                                    workday=getString(R.string.saturday);
                                }
                                else
                                {
                                    workday+=","+getString(R.string.saturday);
                                }
                            }
                        }
                        tv_workdays_spProfile.setText(workday);

                        radius=response.getString("radius");
                        if(!radius.isEmpty())
                        {
                            if(radius.equals("5"))
                            {
                                tv_serviceArea_spProfile.setText(getResources().getString(R.string.uptofive));
                            }
                            else if(radius.equals("10"))
                            {
                                tv_serviceArea_spProfile.setText(getResources().getString(R.string.uptoten));
                            }
                            else
                            {
                                tv_serviceArea_spProfile.setText(getResources().getString(R.string.upto20));
                            }
                        }


                    }
                    if (responseObj.getString("requestKey").equalsIgnoreCase("editProfile"))
                    {
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.PROFILEUPDATED,"1");

                        JSONObject response = responseObj.getJSONObject("response");

                        textView_userName_ProfileSP.setText(response.getString("full_name"));
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.COMPANYNAME, response.getString("companyName"));
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.IMAGE, response.getString("profile"));
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.FullName, response.getString("full_name"));
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.COMPANYNAME, response.getString("companyName"));
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.Email, response.getString("email"));
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.MYADDRESS, response.getString("address"));

                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.MINCHARGE, response.getString("min_charge"));
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.CATEGORIES, response.getString("categories"));
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.SPECIALITY, response.getString("specilityName"));
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.WORKDAYS, response.getString("workingDays"));


                        //profile username
                        textView_userName_ProfileSP.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.FullName));
                        //profile image
                        AQuery aQuery = new AQuery(profileImage);
                        if (!SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.IMAGE).isEmpty()) {
                            aQuery.id(profileImage).image(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.IMAGE));

                        } else {
                            aQuery.id(profileImage).image(R.drawable.default_profile_pic);
                        }

                        editText_name_profileSP.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.FullName));
                        editText_companyNameSP.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.COMPANYNAME));
//                        tv_contactNumSP.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.PhoneNumber));
                        editText_email_profileSP.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.Email));
                        tv_address_profileSP.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.MYADDRESS));

//
//                        tv_categoriesSP.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.CATEGORIES));
//                        tv_specialitySP.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.SPECIALITY));
//                        et_minCharge_profileSP.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.MINCHARGE));
//                        tv_workdays_spProfile.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.WORKDAYS));


                        getActivity().getSupportFragmentManager().popBackStack();
                        Toast.makeText(getActivity(),"Profile Updated Successfully.",Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "" + responseObj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }

        }
        catch (Exception e) {
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

                    picpath = data.getStringExtra("filePath");
                    if (picpath != null) {
                        File imgFile = new File(data.getStringExtra("filePath"));
                        if (imgFile.exists()) {
                            profileImage.setImageURI(Uri.fromFile(imgFile));
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
                    tv_address_profileSP.setText(data.getStringExtra("address"));
                    lat=data.getStringExtra("lat");
                    lng=data.getStringExtra("long");
                }
            }
        }

    }

//    MYDIALOG LISTENER INTERFACE CALLBACK
    @Override
    public void onFinishDialog(String inputText, String type)
    {
//        Radius
        if(type.equals("radius"))
        {
            radius=inputText;


            if(radius.equals("5"))
            {
                tv_serviceArea_spProfile.setText(getResources().getString(R.string.uptofive));
            }
            else if(radius.equals("10")) {
                tv_serviceArea_spProfile.setText(getResources().getString(R.string.uptoten));
            }
            else
            {
                tv_serviceArea_spProfile.setText(getResources().getString(R.string.upto20));
            }

        }
//      Categories

        if(type.equals("categories"))
        {
            categories = inputText;

            tv_categoriesSP.setText(categories);

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

//        /*speciality*/
        if(type.equals("speciality"))
        {
            speciality=inputText;
            if(!inputText.isEmpty())
            {
                if(inputText.equals("two"))
                {
                    tv_specialitySP.setText(getString(R.string.two_wheeler));
                }
                else if(inputText.equals("light"))
                {
                    tv_specialitySP.setText(getString(R.string.light_weight_vehicle));
                }
                else
                {
                    tv_specialitySP.setText(getString(R.string.heavy_weight_vehicle));
                }
            }
        }
//        Working Days
        if(type.equals("workdays"))
        {
            workdays = inputText;

            tv_workdays_spProfile.setText(workdays);

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
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("isEditing",isEditing); // to save the state of Editing


    }
}
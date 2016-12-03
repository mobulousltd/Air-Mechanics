package mobulous12.airmechanics.customer.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.androidquery.AQuery;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONObject;

import java.io.File;

import mobulous12.airmechanics.MapActivityToPickAddress;
import mobulous12.airmechanics.R;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.customer.activities.LoginActivity;
import mobulous12.airmechanics.fonts.FontBinding;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.utils.CircularImageView;
import mobulous12.airmechanics.utils.TakeImage;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment implements View.OnClickListener, ApiListener {


    private Menu myMenu;
    private boolean isEditable = false;
    private TextView textView_userName_Profile, textView_nameText_profile,
            textView_name_profile, textView_contact_numberText_profile,tv_setContact,
            textView_contact_number_profile, textView_emailText_profile, textView_email_profile,
            textView_addressText_profile, textView_address_profile,
            textView_change_password_profile, textView_app_notification_profile;

    EditText editText_name_profile, editText_contact_number_profile, editText_email_profile, editText_address_profile;
    private MenuItem save;
    private MenuItem editing;
    private CircularImageView profile_image;
    private TextView profile_name;
    private Typeface regular_font;
    private RelativeLayout root_changePassword;
    private LinearLayout linear_setContact;
    private static int ADDRESSFETCH=011;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int CHANGECONTACT = 002;
    private static final int CAMERA_REQUEST = 1888;
    private String picpath = "";
    private TextView tv_address_profile;
    private String lat="", lng="";
    private  View view;
    private TextView tv_contactNum;


    public MyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_screen, container, false);

        regular_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-Regular.ttf");

        ((HomeActivity) getActivity()).setToolbarTitle(getResources().getString(R.string.headername_profile));
        ((HomeActivity) getActivity()).toolbarVisible();
        ((HomeActivity) getActivity()).setNavigationIcon();

        Font.setFontHeader(HomeActivity.toolbar_title, getActivity());


        /*views*/
        textView_userName_Profile = (TextView) view.findViewById(R.id.textView_userName_Profile);

        textView_nameText_profile = (TextView) view.findViewById(R.id.textView_nameText_profile);
        editText_name_profile = (EditText) view.findViewById(R.id.editText_name_profile);

        textView_contact_numberText_profile = (TextView) view.findViewById(R.id.textView_contact_numberText_profile);
//        tv_setContact = (TextView) view.findViewById(R.id.tv_setContact);
        tv_contactNum = (TextView) view.findViewById(R.id.tv_contactNum);
//        editText_contact_number_profile = (EditText) view.findViewById(R.id.editText_contact_number_profile);

        textView_emailText_profile = (TextView) view.findViewById(R.id.textView_emailText_profile);
        editText_email_profile = (EditText) view.findViewById(R.id.editText_email_profile);


        textView_addressText_profile = (TextView) view.findViewById(R.id.textView_addressText_profile);
        tv_address_profile = (TextView) view.findViewById(R.id.tv_address_profile);

//        editText_address_profile = (EditText) view.findViewById(R.id.editText_address_profile);

        textView_change_password_profile = (TextView) view.findViewById(R.id.textView_change_password_profile);
//        textView_app_notification_profile = (TextView) view.findViewById(R.id.textView_app_notification_profile);
        root_changePassword = (RelativeLayout) view.findViewById(R.id.root_changePassword);
        linear_setContact = (LinearLayout) view.findViewById(R.id.linear_setContact);
        profile_image = (CircularImageView) view.findViewById(R.id.circularImageView_profileScreen);
        profile_name = (TextView) view.findViewById(R.id.textView_userName_Profile);


//        tv_setContact.setVisibility(View.GONE);
        tv_contactNum.setVisibility(View.VISIBLE);

        /*make edit texts Non - Editable*/

        editText_name_profile.setEnabled(false);
        linear_setContact.setEnabled(false);
//        tv_setContact.setEnabled(false);
//        editText_contact_number_profile.setEnabled(false);
        editText_email_profile.setEnabled(false);
//        editText_address_profile.setEnabled(false);

        profile_image.setEnabled(false);
        tv_address_profile.setEnabled(false);
        root_changePassword.setEnabled(false);


        /*set Fonts*/
        Font.setFontTextView(textView_userName_Profile, getActivity());
        Font.setFontTextView(textView_nameText_profile, getActivity());
        editText_name_profile.setTypeface(regular_font);
        Font.setFontTextView(textView_contact_numberText_profile, getActivity());
//        Font.setFontTextView(tv_setContact, getActivity());
//        editText_contact_number_profile.setTypeface(regular_font);
        Font.setFontTextView(textView_emailText_profile, getActivity());
        editText_email_profile.setTypeface(regular_font);
        Font.setFontTextView(textView_addressText_profile, getActivity());
        Font.setFontTextView(tv_address_profile, getActivity());
//        editText_address_profile.setTypeface(regular_font);

        Font.setFontTextView(textView_change_password_profile, getActivity());
//        Font.setFontTextView(textView_app_notification_profile, getActivity());


        view.findViewById(R.id.ll_Contact).setVisibility(View.GONE);

        profile_image.setOnClickListener(this);
        linear_setContact.setOnClickListener(this);
        tv_address_profile.setOnClickListener(this);
        root_changePassword.setOnClickListener(this);

        if(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.LOGINTYPE).equals("social"))
        {
            view.findViewById(R.id.ll_changepasssp).setVisibility(View.GONE);
        }
        viewProfileServiceHit();

        return view;
    }

    @Override
    public void onClick(View view) {

        switch ((view.getId()))
        {


            case R.id.circularImageView_profileScreen:
                // Profile image upload
                showDialogForUpload("Upload your Profile Image");
                break;

            case R.id.linear_setContact:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer, new ChangeContactFrag(), "changeContactFragment").addToBackStack("changeContact").commit();
                break;

            // change password event callback
            case R.id.root_changePassword:
                if (SharedPreferenceWriter.getInstance(getActivity()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN)) {

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer, new ChangePasswordFragment(), "changePasswordFragment").addToBackStack("changePassword").commit();

                } else {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_serviceHomeContainer, new ChangePasswordFragment(), "changePasswordFragSP").addToBackStack("changePasswordSP").commit();
                }
                break;

            case R.id.tv_address_profile:
                Intent intent = new Intent(getActivity(), MapActivityToPickAddress.class);
                intent.putExtra("lat",lat);
                intent.putExtra("lng",lng);
                intent.putExtra("address",tv_address_profile.getText().toString().trim());
                startActivityForResult(intent,ADDRESSFETCH);
                break;

        }


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu, menu);
        myMenu = menu;

        save = menu.findItem(R.id.save_profile);
        editing = menu.findItem(R.id.edit_profile);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
              /*make edit texts Editable*/
            case R.id.edit_profile:

                isEditable = true;
                save.setVisible(true);
                editing.setVisible(false);

                 /*make edit texts Editable*/
                profile_image.setEnabled(true);
                editText_name_profile.setEnabled(true);
                linear_setContact.setEnabled(true);
                tv_address_profile.setEnabled(true);
                root_changePassword.setEnabled(true);
                view.findViewById(R.id.ll_Contact).setVisibility(View.VISIBLE);
                if(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.LOGINTYPE).equals("normal"))
                {
                    editText_email_profile.setEnabled(true);
                }
                break;

            case R.id.save_profile:

                isEditable = false;

                save.setVisible(false);
                editing.setVisible(true);

                 /*make edit texts NON-Editable*/
                profile_image.setEnabled(false);
                editText_name_profile.setEnabled(false);
                linear_setContact.setEnabled(false);
                editText_email_profile.setEnabled(false);
                tv_address_profile.setEnabled(false);
                root_changePassword.setEnabled(false);
                view.findViewById(R.id.ll_Contact).setVisibility(View.GONE);

                if(validateData())
                {
                    editProfileServiceHit();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validateData()
    {
        if (editText_name_profile.getText().toString().trim().equals(""))
        {
            Toast.makeText(getActivity(), "Please enter Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (editText_email_profile.getText().toString().trim().equals(""))
        {
            Toast.makeText(getActivity(), "Please enter Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(editText_email_profile.getText().toString()).matches())
        {
            Toast.makeText(getActivity(), "Please enter valid Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (tv_address_profile.getText().toString().trim().equals(""))
        {
            Toast.makeText(getActivity(), "Please enter Address", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }


    // Profile image upload
    public void showDialogForUpload(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getActivity(), TakeImage.class);
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
                Intent intent = new Intent(getActivity(), TakeImage.class);
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



    private void viewProfileServiceHit() {

        MultipartEntityBuilder multipartbuilder = MultipartEntityBuilder.create();
        multipartbuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartbuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN));

        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setActivity(getActivity());
        serviceBean.setMethodName("Consumers/viewProfile");
        serviceBean.setApilistener(this);

        CustomHandler customHandler = new CustomHandler(serviceBean);
        customHandler.makeMultipartRequest(multipartbuilder);
    }

    private void editProfileServiceHit()
    {
        MultipartEntityBuilder multipartbuilder = MultipartEntityBuilder.create();
        multipartbuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartbuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN));
        if(picpath.isEmpty())
        {
            multipartbuilder.addTextBody("profile", "");
        }
        else {
             multipartbuilder.addBinaryBody("profile",new File(picpath));
        }
        multipartbuilder.addTextBody("user_name", editText_name_profile.getText().toString().trim());
        multipartbuilder.addTextBody("email", editText_email_profile.getText().toString().trim());
        multipartbuilder.addTextBody("address",  tv_address_profile.getText().toString().trim());
        multipartbuilder.addTextBody("lat", lat);
        multipartbuilder.addTextBody("long",lng);

        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setActivity(getActivity());
        serviceBean.setMethodName("Consumers/editProfile");
        serviceBean.setApilistener(this);

        CustomHandler customHandler = new CustomHandler(serviceBean);
        customHandler.makeMultipartRequest(multipartbuilder);

    }


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
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.PhoneNumber, response.getString("contact_no"));
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.Email, response.getString("email"));
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.ADDRESS, response.getString("address"));
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.IMAGE, response.getString("profile"));
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.NOTIFICATION, response.getString("notification"));

                        //profile username
                        profile_name.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.FullName));
                        //profile image
                        AQuery aQuery = new AQuery(profile_image);
                        if (!SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.IMAGE).isEmpty()) {
                            aQuery.id(profile_image).image(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.IMAGE));

                        } else {
                            aQuery.id(profile_image).image(R.drawable.default_profile_pic);
                        }

                        lat = response.getString("lat");
                        lng = response.getString("longs");
                        editText_name_profile.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.FullName));
                        tv_contactNum.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.PhoneNumber));
                        editText_email_profile.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.Email));
                        tv_address_profile.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.ADDRESS));


                    }
                    if (responseObj.getString("requestKey").equalsIgnoreCase("editProfile"))
                    {

                        JSONObject response = responseObj.getJSONObject("response");


                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.IMAGE, response.getString("profile"));
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.FullName, response.getString("full_name"));
                        //SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.PhoneNumber, response.getString("contact_no"));
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.Email, response.getString("email"));
                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.ADDRESS, response.getString("address"));

                        //profile username
                        profile_name.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.FullName));
                        //profile image
                        AQuery aQuery = new AQuery(profile_image);
                        if (!SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.IMAGE).isEmpty()) {
                            aQuery.id(profile_image).image(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.IMAGE));

                        } else {
                            aQuery.id(profile_image).image(R.drawable.default_profile_pic);
                        }

                        editText_name_profile.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.FullName));
//                        tv_contactNum.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.PhoneNumber));
                        editText_email_profile.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.Email));
                        tv_address_profile.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.ADDRESS));



                        ((HomeActivity)getActivity()).getSupportFragmentManager().popBackStack();
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
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_show_service_provider).setVisible(false);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if (requestCode == RESULT_LOAD_IMAGE || requestCode == CAMERA_REQUEST)
            {
                if (resultCode == RESULT_OK)
                {
                    picpath = data.getStringExtra("filePath");
                    if (picpath != null) {
                        File imgFile = new File(data.getStringExtra("filePath"));
                        if (imgFile.exists()) {
                            profile_image.setImageURI(Uri.fromFile(imgFile));
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
                    tv_address_profile.setText(data.getStringExtra("address"));
                    lat=data.getStringExtra("lat");
                    lng=data.getStringExtra("long");
                }
            }
            if (requestCode == CHANGECONTACT)
            {
                if(data!=null)
                {
                    Toast.makeText(getActivity(), ""+data.getStringExtra("number"), Toast.LENGTH_SHORT).show();
                }
            }

        }

    }



}
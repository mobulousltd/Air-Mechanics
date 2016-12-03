package mobulous12.airmechanics.customer.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONObject;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.SplashActivity;
import mobulous12.airmechanics.beans.ServiceProviderBean;
import mobulous12.airmechanics.databinding.ServiceProviderDetailBinding;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.utils.CircularImageView;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

public class ServiceProviderDetailActivity extends AppCompatActivity implements View.OnClickListener, ApiListener {

    AQuery aQuery;
    private RelativeLayout rootCategories, rootAddress, rootWorkHours, rootSpeciality, rootDescription, rootReviews, rootContactNumber;
    LinearLayout ll_mincharge;


    //  textView_contact_number_serviceProviderDetail


    private TextView textViewCategoriesDynamic;
    private TextView textViewAddressDynamic;
    private TextView textViewWorkHoursDynamic;
    private TextView textViewSpecialityDynamic;
    private TextView textViewDescriptionDynamic;
    private TextView textViewReviewsDynamic, tv_minchrge;
    private TextView textViewContactNumberDynamic;

    private ImageView imgCategories;
    private ImageView imgAddress;
    private ImageView imgWorkHours;
    private ImageView imgSpeciality;
    private ImageView imgDescription;
    private ImageView imgReviews, img_minchrge;
    private ImageView imgContactNumber;

    boolean isCategoryOpen = true;
    boolean isAddressOpen = true;
    boolean isWorkHourOpen = true;
    boolean isSpecialityOpen = true;
    boolean isDescriptionOpen = true;
    boolean isReviewOpen = true;
    boolean isminchrge = true;
    boolean isContactNumberOpen = true;

    private ImageView favoriteImage;
    private TextView serviceProviderDetail_title;
    private TextView serviceProviderName;
    private TextView km;
    private View view;
    private TextView mon, tue, wed, thur, fri, sat, sun;
    ServiceProviderBean serviceProviderBean;
    CircularImageView circularImageView_profile_pic_serviceProviderDetail;

    public static ServiceProviderDetailActivity serviceProviderDetailActivity;
    private static final int CALL_REQ_CODE = 125;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceProviderDetailBinding  binding = DataBindingUtil.setContentView(this, R.layout.service_provider_detail);
        view = binding.getRoot();
        serviceProviderDetailActivity = this;
        serviceProviderBean = getIntent().getParcelableExtra("bean");

        circularImageView_profile_pic_serviceProviderDetail = (CircularImageView) findViewById(R.id.circularImageView_profile_pic_serviceProviderDetail);
        serviceProviderDetail_title = (TextView) findViewById(R.id.textView_serviceProviderDetail);
        serviceProviderName = (TextView) findViewById(R.id.textView_raj_auto_garage_serviceProviderDetail);
        km = (TextView) findViewById(R.id.textView_km_serviceProviderDetail);
        mon = (TextView) findViewById(R.id.textView_mon_serviceProviderDetail);
        tue = (TextView) findViewById(R.id.textView_tue_serviceProviderDetail);
        wed = (TextView) findViewById(R.id.textView_wed_serviceProviderDetail);
        thur = (TextView) findViewById(R.id.textView_thur_serviceProviderDetail);
        fri = (TextView) findViewById(R.id.textView_fri_serviceProviderDetail);
        sat = (TextView) findViewById(R.id.textView_sat_serviceProviderDetail);
        sun = (TextView) findViewById(R.id.textView_sun_serviceProviderDetail);
        tv_minchrge = (TextView) findViewById(R.id.tv_minchrge);
        rootCategories = (RelativeLayout) findViewById(R.id.root_categories_serviceProviderDetail);
        rootAddress = (RelativeLayout) findViewById(R.id.root_address_serviceProviderDetail);
        rootContactNumber = (RelativeLayout) findViewById(R.id.root_contact_number_serviceProviderDetail);
        rootWorkHours = (RelativeLayout) findViewById(R.id.root_work_hours_serviceProviderDetail);
        rootSpeciality = (RelativeLayout) findViewById(R.id.root_speciality_serviceProviderDetail);
        rootDescription = (RelativeLayout) findViewById(R.id.root_description_serviceProviderDetail);
        rootReviews = (RelativeLayout) findViewById(R.id.root_reviews_serviceProviderDetail);


        findViewById(R.id.button_ask_for_quote_serviceProviderDetail).setOnClickListener(this);
        findViewById(R.id.imageView_back_).setOnClickListener(this);

        favoriteImage = (ImageView) findViewById(R.id.imageView_favorite_serviceProviderDetail);

        if(!SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.LOGINKEY))
        {
            favoriteImage.setVisibility(View.INVISIBLE);
        }

        favoriteImage.setOnClickListener(this);

        rootCategories.setOnClickListener(this);
        rootAddress.setOnClickListener(this);
        rootContactNumber.setOnClickListener(this);
        rootWorkHours.setOnClickListener(this);
        rootSpeciality.setOnClickListener(this);
        rootDescription.setOnClickListener(this);
        rootReviews.setOnClickListener(this);
        ll_mincharge = (LinearLayout) findViewById(R.id.ll_mincharge);
        ll_mincharge.setOnClickListener(this);
//        categoriesColorChanger = (RelativeLayout) findViewById(R.id.categories_backgroundChanger);
//        addressColorChanger = (RelativeLayout) findViewById(R.id.address_backgroundChanger);
//        workHoursColorChanger = (RelativeLayout) findViewById(R.id.work_hours_backgroundChanger);
//        specialityColorChanger = (RelativeLayout) findViewById(R.id.speciality_backgroundChanger);
//        descriptionColorChanger = (RelativeLayout) findViewById(R.id.description_backgroundChanger);
//        reviewsColorChanger = (RelativeLayout) findViewById(R.id.review_backgroundChanger);
//
//        categoriesColorChanger.setOnClickListener(this);
//        addressColorChanger.setOnClickListener(this);
//        workHoursColorChanger.setOnClickListener(this);
//        specialityColorChanger.setOnClickListener(this);
//        descriptionColorChanger.setOnClickListener(this);
//        reviewsColorChanger.setOnClickListener(this);


        textViewCategoriesDynamic = (TextView) findViewById(R.id.textView_categories_dynamic);
        textViewAddressDynamic = (TextView) findViewById(R.id.textView_address_dynamic);
        textViewContactNumberDynamic = (TextView) findViewById(R.id.textView_contact_number_dynamic);
        textViewContactNumberDynamic.setOnClickListener(this);
        textViewWorkHoursDynamic = (TextView) findViewById(R.id.textView_work_hours_dynamic);
        textViewSpecialityDynamic = (TextView) findViewById(R.id.textView_speciality_dynamic);
        textViewDescriptionDynamic = (TextView) findViewById(R.id.textView_description_dynamic);
        textViewReviewsDynamic = (TextView) findViewById(R.id.textView_reviews_dynamic);

        textViewCategoriesDynamic.setVisibility(View.GONE);
        textViewAddressDynamic.setVisibility(View.GONE);
        textViewContactNumberDynamic.setVisibility(View.GONE);
        textViewWorkHoursDynamic.setVisibility(View.GONE);
        textViewSpecialityDynamic.setVisibility(View.GONE);
        textViewDescriptionDynamic.setVisibility(View.GONE);
        textViewReviewsDynamic.setVisibility(View.GONE);
        tv_minchrge.setVisibility(View.GONE);


        imgCategories = (ImageView) findViewById(R.id.imageView_rightArrow_categories_serviceProviderDetail_1);
        imgAddress = (ImageView) findViewById(R.id.imageView_rightArrow_address_serviceProviderDetail_2);
        imgContactNumber = (ImageView) findViewById(R.id.imageView_rightArrow_contact_number_serviceProviderDetail_2);
        imgWorkHours = (ImageView) findViewById(R.id.imageView_rightArrow_work_hours_serviceProviderDetail_3);
        imgSpeciality = (ImageView) findViewById(R.id.imageView_rightArrow_speciality_serviceProviderDetail_4);
        imgDescription = (ImageView) findViewById(R.id.imageView_rightArrow_description_serviceProviderDetail_5);
        imgReviews = (ImageView) findViewById(R.id.imageView_rightArrow_reviews_serviceProviderDetail_6);
        img_minchrge = (ImageView) findViewById(R.id.img_minchrge);

        serviceProviderDetail_Service();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_mincharge:
                if (isminchrge) {
                    tv_minchrge.setVisibility(View.VISIBLE);
                    img_minchrge.setRotation(90.0f);
                    isminchrge = false;
                    ll_mincharge.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                } else {
                    tv_minchrge.setVisibility(View.GONE);
                    img_minchrge.setRotation(0.0f);
                    isminchrge = true;
                    ll_mincharge.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                break;
            case R.id.root_categories_serviceProviderDetail:
                if (isCategoryOpen) {
                    textViewCategoriesDynamic.setVisibility(View.VISIBLE);
                    imgCategories.setRotation(90.0f);
                    isCategoryOpen = false;
                    rootCategories.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                } else {
                    textViewCategoriesDynamic.setVisibility(View.GONE);
                    imgCategories.setRotation(0.0f);
                    isCategoryOpen = true;
                    rootCategories.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                break;

            case R.id.root_address_serviceProviderDetail:
                if (isAddressOpen) {
                    textViewAddressDynamic.setVisibility(View.VISIBLE);
                    imgAddress.setRotation(90.0f);
                    isAddressOpen = false;
                    rootAddress.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                } else {
                    textViewAddressDynamic.setVisibility(View.GONE);
                    imgAddress.setRotation(0.0f);
                    isAddressOpen = true;
                    rootAddress.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                break;

//            CONTACT NUMBER
            case R.id.root_contact_number_serviceProviderDetail:
                if (isContactNumberOpen) {
                    textViewContactNumberDynamic.setVisibility(View.VISIBLE);
                    imgContactNumber.setRotation(90.0f);
                    isContactNumberOpen = false;
                    rootContactNumber.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                } else {
                    textViewContactNumberDynamic.setVisibility(View.GONE);
                    imgContactNumber.setRotation(0.0f);
                    isContactNumberOpen = true;
                    rootContactNumber.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                break;

            case R.id.root_work_hours_serviceProviderDetail:
                if (isWorkHourOpen) {
                    textViewWorkHoursDynamic.setVisibility(View.VISIBLE);
                    imgWorkHours.setRotation(90.0f);
                    isWorkHourOpen = false;
                    rootWorkHours.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                } else {
                    textViewWorkHoursDynamic.setVisibility(View.GONE);
                    imgWorkHours.setRotation(0.0f);
                    isWorkHourOpen = true;
                    rootWorkHours.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                break;

            case R.id.root_speciality_serviceProviderDetail:
                if (isSpecialityOpen) {
                    textViewSpecialityDynamic.setVisibility(View.VISIBLE);
                    imgSpeciality.setRotation(90.0f);
                    isSpecialityOpen = false;
                    rootSpeciality.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                } else {
                    textViewSpecialityDynamic.setVisibility(View.GONE);
                    imgSpeciality.setRotation(0.0f);
                    isSpecialityOpen = true;
                    rootSpeciality.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                break;

            case R.id.root_description_serviceProviderDetail:
                if (isDescriptionOpen) {
                    textViewDescriptionDynamic.setVisibility(View.VISIBLE);
                    imgDescription.setRotation(90.0f);
                    isDescriptionOpen = false;
                    rootDescription.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                } else {
                    textViewDescriptionDynamic.setVisibility(View.GONE);
                    imgDescription.setRotation(0.0f);
                    isDescriptionOpen = true;
                    rootDescription.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                break;

            case R.id.root_reviews_serviceProviderDetail:
                if (isReviewOpen) {
                    textViewReviewsDynamic.setVisibility(View.VISIBLE);
                    imgReviews.setRotation(90.0f);
                    isReviewOpen = false;
                    rootReviews.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                } else {
                    textViewReviewsDynamic.setVisibility(View.GONE);
                    imgReviews.setRotation(0.0f);
                    isReviewOpen = true;
                    rootReviews.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                break;

            case R.id.imageView_back_:
                finish();
                break;

            case R.id.button_ask_for_quote_serviceProviderDetail:
                if(!SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.LOGINKEY))
                {
                    Snackbar snackbar = Snackbar.make(view,"You must SignIn to avail exciting services!",Snackbar.LENGTH_LONG)
                            .setAction("SIGN IN", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent in1=new Intent(ServiceProviderDetailActivity.this, LoginActivity.class);
                                    in1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    SharedPreferenceWriter.getInstance(ServiceProviderDetailActivity.this).writeBooleanValue(SPreferenceKey.LOGINKEY, false);
                                    SharedPreferenceWriter.getInstance(ServiceProviderDetailActivity.this).writeBooleanValue(SPreferenceKey.CUSTOMER_LOGIN, true);
                                    SharedPreferenceWriter.getInstance(ServiceProviderDetailActivity.this).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, false);
                                    startActivity(in1);
                                    (ServiceProviderDetailActivity.this).finishAffinity();
                                }
                            });
                    // Changing Action text color
                    snackbar.setActionTextColor(getResources().getColor(R.color.blue));
                    snackbar.show();
                }
                else {
                    Intent intent = new Intent(this, CalenderActivity.class);
                    intent.putExtra("bean", serviceProviderBean);
                    startActivity(intent);
                }

                break;
            case R.id.imageView_favorite_serviceProviderDetail:
                    favoriteServiceHit();
                break;

                // Dynamic Contact number listener
            case R.id.textView_contact_number_dynamic:

                //     /***Calling Permission Checking****/
                int callPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

                if (callPermissionCheck == PackageManager.PERMISSION_GRANTED) {
                    String uri = "tel:" + textViewContactNumberDynamic.getText().toString();
                    Intent intent1 = new Intent(Intent.ACTION_CALL);
                    intent1.setData(Uri.parse(uri));
                    startActivity(intent1);

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_REQ_CODE);

                }

                break;

        }

    }

    /***Handle the Calling permissions request response****/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CALL_REQ_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Calling Permission Granted.Now, click at Contact Number to Call.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Calling Permission Denied.", Toast.LENGTH_LONG).show();
            }
        }
    }

    //service
    private void serviceProviderDetail_Service()
    {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        multipartEntityBuilder.addTextBody("service_id", serviceProviderBean.getId());
        multipartEntityBuilder.addTextBody("lat", SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.LATITUDE));
        multipartEntityBuilder.addTextBody("long", SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.LONGITUDE));

        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setApilistener(this);
        serviceBean.setActivity(this);
        serviceBean.setMethodName("Consumers/service_ProviderDetail");

        CustomHandler customHandler = new CustomHandler(serviceBean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);
    }
    private void favoriteServiceHit()
    {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.TOKEN));
        multipartEntityBuilder.addTextBody("serviceProviderId", serviceProviderBean.getId());
        ServiceBean bean = new ServiceBean();
        bean.setActivity(this);
        bean.setMethodName("Consumers/follow_serviceProvider");
        bean.setApilistener(this);

        CustomHandler customHandler = new CustomHandler(bean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);
    }

    @Override
    public void onBackPressed() {

            //startActivity(new Intent(this, ServiceProviderActivity.class));
            finish();
    }

    @Override
    public void myServerResponse(JSONObject jsonObject) {
        try
        {
            if(jsonObject!=null)
            {
                if(jsonObject.getString("status").equalsIgnoreCase("SUCCESS"))
                {
                    if(jsonObject.getString("requestKey").equalsIgnoreCase("service_ProviderDetail"))
                    {
                        JSONObject response=jsonObject.getJSONObject("response");
                        //setting details
                        if(response.getString("workingDays").contains("0"))
                        {
                            sun.setBackgroundResource(R.drawable.green_circle);
                        }
                        if(response.getString("workingDays").contains("1"))
                        {
                            mon.setBackgroundResource(R.drawable.green_circle);
                        }
                        if(response.getString("workingDays").contains("2"))
                        {
                            tue.setBackgroundResource(R.drawable.green_circle);
                        }
                        if(response.getString("workingDays").contains("3"))
                        {
                            wed.setBackgroundResource(R.drawable.green_circle);
                        }
                        if(response.getString("workingDays").contains("4"))
                        {
                            thur.setBackgroundResource(R.drawable.green_circle);
                        }
                        if(response.getString("workingDays").contains("5"))
                        {
                            fri.setBackgroundResource(R.drawable.green_circle);
                        }

                        if(response.getString("workingDays").contains("6"))
                        {
                            sat.setBackgroundResource(R.drawable.green_circle);
                        }

                        if (response.getString("status").equalsIgnoreCase("follow"))
                        {
                            favoriteImage.setImageResource(R.drawable.favorite_selected);

                        }
                        else {
                            favoriteImage.setImageResource(R.drawable.favorite);
                        }

                        textViewAddressDynamic.setText(response.getString("address"));
                        serviceProviderName.setText(response.getString("name"));
                        serviceProviderBean.setStart(response.getString("start_time"));
                        serviceProviderBean.setEnd(response.getString("end_time"));
                        tv_minchrge.setText(response.getString("st_charge"));
                        textViewWorkHoursDynamic.setText(response.getString("start_time")+" to "+response.getString("end_time"));
                        km.setText(response.getString("distance")+" KM");
                        aQuery=new AQuery(circularImageView_profile_pic_serviceProviderDetail);
                        if(response.getString("profile").isEmpty())
                        {
                            aQuery.id(circularImageView_profile_pic_serviceProviderDetail).image(R.drawable.default_profile_pic);
                        }
                        else
                        {
                            aQuery.id(circularImageView_profile_pic_serviceProviderDetail).image(response.getString("profile"));
                        }
                        String cat="";
                        if(response.getString("work_category").contains("two"))
                        {
                            cat=getString(R.string.two_wheeler);
                        }
                        if(response.getString("work_category").contains("light"))
                        {
                            if(cat.isEmpty())
                            {
                                cat=getString(R.string.light_weight_vehicle);
                            }
                            else
                            {
                                cat+=", "+getString(R.string.light_weight_vehicle);
                            }
                        }
                        if(response.getString("work_category").contains("heavy"))
                        {
                            if(cat.isEmpty())
                            {
                                cat=getString(R.string.heavy_weight_vehicle);
                            }
                            else
                            {
                                cat+=", "+getString(R.string.heavy_weight_vehicle);
                            }
                        }
                        textViewSpecialityDynamic.setText(cat);
                        textViewCategoriesDynamic.setText(cat);

                        // Code for new field i.e. Contact Number
                        textViewContactNumberDynamic.setText(response.getString("contact_no"));
                    }

                    if (jsonObject.getString("requestKey").equals("follow_serviceProvider"))
                    {
                        JSONObject response = jsonObject.getJSONObject("response");
                        JSONObject favorite = response.getJSONObject("Favourite");
                        if (favorite.getString("status").equals("follow"))
                        {
                            favoriteImage.setImageResource(R.drawable.favorite_selected);

                        }
                        else {
                            favoriteImage.setImageResource(R.drawable.favorite);

                        }
                        Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                }

                Log.i("JsonResponse", ""+jsonObject.toString());

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
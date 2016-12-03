package mobulous12.airmechanics.serviceprovider.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONObject;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.RoleSelectionActivity;
import mobulous12.airmechanics.airinterfaces.MyDialogListenerInterface;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.customer.fragments.ChangeContactFrag;
import mobulous12.airmechanics.customer.fragments.MyBookingsFragment;
import mobulous12.airmechanics.customer.fragments.MyPlanFragment;
import mobulous12.airmechanics.customer.fragments.SubscriptionPlanFragment;
import mobulous12.airmechanics.customer.fragments.WebViewsFragment;
import mobulous12.airmechanics.fonts.FontBinding;
import mobulous12.airmechanics.serviceprovider.fragments.CompletedFragment;
import mobulous12.airmechanics.serviceprovider.fragments.HomeFragment;
import mobulous12.airmechanics.serviceprovider.fragments.InProgressFragment;
import mobulous12.airmechanics.serviceprovider.fragments.JobRequestDetailFragment;
import mobulous12.airmechanics.serviceprovider.fragments.JobRequestFragment;
import mobulous12.airmechanics.serviceprovider.fragments.MyEarningsFragment_SP;
import mobulous12.airmechanics.serviceprovider.fragments.NotificationFragment_SP;
import mobulous12.airmechanics.serviceprovider.fragments.PendingFragment;
import mobulous12.airmechanics.serviceprovider.fragments.ProfileFragment_SP;
import mobulous12.airmechanics.serviceprovider.fragments.YourJobsFragment_SP;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.utils.MyApplication;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

import static mobulous12.airmechanics.R.id.imageView_plan_arrowServicePro;

public class HomeActivityServicePro extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , View.OnClickListener, ApiListener {


    /*variables*/

    TextView profile_name;ImageView profile_image;
    public static TextView toolbar_titleSP;
    public  Toolbar toolbarSP;
    private     DrawerLayout drawerSP;
    private  NavigationView navigationViewSP;
    public static HomeActivityServicePro homeActivitySP;
    private ActionBarDrawerToggle toggleSP;
    private boolean  isPlanArrowOpenSP = true,isJobArrowOpen=true;

    public static boolean homeact=false;
    private LinearLayout  linearlayout_myProfileSP,linearlayout_myJobOrdersSP,linearlayout_notificationSP,
            linearlayout_planSP,linearlayout_myPlanSP,linearlayout_subscriptionPlanSP,linearlayout_contactUsSP,
            linearlayout_shareSP,linearlayout_myEarningsSP,linearlayout_aboutSP,linearlayout_aboutServicePro,
            linearlayout_logoutSP,linearlayout_TCSP,linearlayout_jobOrderSP,linearlayout_myJobReqSP;
    private ImageView imageView_plan_arrowSP;
    private ImageView imageView_jobOrderArrowSP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_home_service_pro);

        SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, true);

        homeActivitySP = this;
        homeact=true;
        /*set toolbar*/
        toolbarSP = (Toolbar) findViewById(R.id.toolbar_servicePro);
        toolbarSP.setTitle("");
        toolbar_titleSP = (TextView) findViewById(R.id.toolbar_titleSP);
        setSupportActionBar(toolbarSP);


//     home Service Provider
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_serviceHomeContainer, new HomeFragment(), "homeFragSP").commit();
        MyApplication.getInstance().setActivity(HomeActivityServicePro.this);
        /*DRAWER*/
        drawerSP = (DrawerLayout) findViewById(R.id.drawer_layout_servicePro);
        toggleSP = new ActionBarDrawerToggle(
                this, drawerSP, toolbarSP, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerSP.setDrawerListener(toggleSP);

        toggleSP.setDrawerIndicatorEnabled(false);
        toggleSP.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.menu));

        toggleSP.setToolbarNavigationClickListener(sp_toolbarNavigationClickListener);
        toggleSP.syncState();

        /*Navigation view */
        navigationViewSP = (NavigationView) findViewById(R.id.nav_view_servicePro);
        View headerSP = navigationViewSP.getHeaderView(0);
        navigationViewSP.setNavigationItemSelectedListener(this);
        profile_name=(TextView)headerSP.findViewById(R.id.textView_userName_HomeServicePro);
        profile_name.setText(SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.FullName));
        profile_image=(ImageView)headerSP.findViewById(R.id.circularImageView_profilePic_HomeServicePro);
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceWriter.getInstance(getApplicationContext()).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, true);

                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_serviceHomeContainer, new ProfileFragment_SP(), "profileFragSP").addToBackStack("profileSP").commit();

                close_SPHomeNavigationDrawer();
            }
        });
        AQuery aQuery=new AQuery(profile_image);
        if(!SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.IMAGE).isEmpty())
        {
            aQuery.id(profile_image).image(SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.IMAGE));
        }
        else
        {
            aQuery.id(profile_image).image(R.drawable.default_profile_pic);
        }

          /*Navigation drawer  main Views */

               /*linear layouts*/
        linearlayout_myProfileSP = (LinearLayout) headerSP.findViewById(R.id.linearlayout_myProfileServicePro);
//        linearlayout_myJobOrdersSP = (LinearLayout) headerSP.findViewById(R.id.linearlayout_myJobOrdersServicePro);
        linearlayout_notificationSP = (LinearLayout) headerSP.findViewById(R.id.linearlayout_notificationServicePro);
        linearlayout_planSP = (LinearLayout) headerSP.findViewById(R.id.linearlayout_planServicePro);
        linearlayout_myPlanSP = (LinearLayout) headerSP.findViewById(R.id.linearlayout_myPlanServicePro);
        linearlayout_subscriptionPlanSP = (LinearLayout) headerSP.findViewById(R.id.linearlayout_subscriptionPlanServicePro);
        linearlayout_myEarningsSP = (LinearLayout) headerSP.findViewById(R.id.linearlayout_myEarningsServicePro);
        linearlayout_shareSP = (LinearLayout) headerSP.findViewById(R.id.linearlayout_shareServicePro);
        linearlayout_contactUsSP = (LinearLayout) headerSP.findViewById(R.id.linearlayout_contactUsServicePro);
        linearlayout_aboutSP = (LinearLayout) headerSP.findViewById(R.id.linearlayout_aboutServicePro);
        linearlayout_TCSP = (LinearLayout) headerSP.findViewById(R.id.linearlayout_TCServicePro);
        linearlayout_logoutSP = (LinearLayout) headerSP.findViewById(R.id.linearlayout_logoutServicePro);

        linearlayout_jobOrderSP = (LinearLayout) headerSP.findViewById(R.id.linearlayout_jobOrderPro);
        linearlayout_myJobOrdersSP = (LinearLayout) headerSP.findViewById(R.id.linearlayout_myJobOrdersSP);
        linearlayout_myJobReqSP = (LinearLayout) headerSP.findViewById(R.id.linearlayout_myJobReqSP);

        imageView_plan_arrowSP = (ImageView)headerSP.findViewById(R.id.imageView_plan_arrowServicePro);
        imageView_jobOrderArrowSP = (ImageView)headerSP.findViewById(R.id.imageView_jobOrderArrowSP);

        /* make these layouts invisible at start */

        linearlayout_myPlanSP.setVisibility(View.GONE);
        linearlayout_subscriptionPlanSP.setVisibility(View.GONE);
        linearlayout_myJobOrdersSP.setVisibility(View.GONE);
        linearlayout_myJobReqSP.setVisibility(View.GONE);


         /*CLICK LISTENERS ON DIFFERENT layout to load different FRAGMENTS*/

        linearlayout_myProfileSP.setOnClickListener(this);

        linearlayout_jobOrderSP.setOnClickListener(this);
        linearlayout_myJobOrdersSP.setOnClickListener(this);
        linearlayout_myJobReqSP.setOnClickListener(this);

        linearlayout_notificationSP.setOnClickListener(this);
        linearlayout_planSP.setOnClickListener(this);
        linearlayout_myPlanSP.setOnClickListener(this);
        linearlayout_subscriptionPlanSP.setOnClickListener(this);
        linearlayout_myEarningsSP.setOnClickListener(this);
        linearlayout_shareSP.setOnClickListener(this);
        linearlayout_contactUsSP.setOnClickListener(this);
        linearlayout_aboutSP.setOnClickListener(this);
        linearlayout_TCSP.setOnClickListener(this);
        linearlayout_logoutSP.setOnClickListener(this);
        if(getIntent().getStringExtra("notify")!=null)
        {
            String notifi=getIntent().getStringExtra("notify");
            if(notifi.equals("request"))
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_serviceHomeContainer, new JobRequestFragment(), "jobReqDetailFragSP").addToBackStack("jobReqDetailFrag").commit();
            }
            if(notifi.equals("pending"))
            {
                Intent intent = new Intent(this,MyJobOrdersActivity.class);
                startActivity(intent);
            }
        }


    } //on create ENDS here

    public  void toolbarGoneSP()
    {
        toolbarSP.setVisibility(View.GONE);
    }

    public  void toolbarVisibleSP()
    {
        toolbarSP.setVisibility(View.VISIBLE);
    }


    private void checkPlanStatusOnGone_SP() {
        imageView_plan_arrowSP.setRotation(90.0f);
        linearlayout_myPlanSP.setVisibility(View.VISIBLE);
        linearlayout_subscriptionPlanSP.setVisibility(View.VISIBLE);

    }
    private void checkJobOrderStatusOnGone_SP() {
        imageView_jobOrderArrowSP.setRotation(90.0f);
        linearlayout_myJobOrdersSP.setVisibility(View.VISIBLE);
        linearlayout_myJobReqSP.setVisibility(View.VISIBLE);

    }

    /*****Handling Toolbar  and its components *****/

    public void setToolbarTitleSP(String title)
    {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.framelayout_serviceHomeContainer);

        if (toolbar_titleSP != null)
        {
            if (    (f instanceof CompletedFragment)
                    || (f instanceof InProgressFragment)
                    || (f instanceof PendingFragment)
                    || (f instanceof JobRequestFragment)
                    || (f instanceof JobRequestDetailFragment)
                    ||  (f instanceof NotificationFragment_SP )
                    ||  (f instanceof ProfileFragment_SP)
                    ||  (f instanceof MyPlanFragment)
                    ||  (f instanceof SubscriptionPlanFragment)
                    ||  (f instanceof MyEarningsFragment_SP)
                    ||  (f instanceof YourJobsFragment_SP)
                    || ( f instanceof WebViewsFragment)
                    ||(f instanceof ChangeContactFrag)   )
            {
                toolbar_titleSP.setText(title);
            } else {
                toolbar_titleSP.setText(getResources().getString(R.string.headername_home));
            }
        }
    }

    public void setNavigationIconSP()
    {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.framelayout_serviceHomeContainer);

        if (toggleSP != null)
        {
            if (    (f instanceof CompletedFragment)
                    || (f instanceof InProgressFragment)
                    || (f instanceof PendingFragment)
                    || (f instanceof JobRequestDetailFragment)
                    || (f instanceof JobRequestFragment)
                    ||  (f instanceof NotificationFragment_SP )
                    ||  (f instanceof ProfileFragment_SP)
                    ||  (f instanceof MyPlanFragment)
                    ||  (f instanceof SubscriptionPlanFragment)
                    ||  (f instanceof MyEarningsFragment_SP)
                    ||  (f instanceof YourJobsFragment_SP)
                    ||(f instanceof WebViewsFragment)
                    ||(f instanceof ChangeContactFrag)
                    )
            {

                toggleSP.setHomeAsUpIndicator(R.drawable.back);
                toggleSP.syncState();
            } else {
                toggleSP.setHomeAsUpIndicator(R.drawable.menu);
                toggleSP.syncState();
            }
        }
    }


    //          Handling listeners on left-icon in toolbar
    private View.OnClickListener sp_toolbarNavigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            if (drawerSP.isDrawerVisible(GravityCompat.START))
            {
                setUserNameImage();
//                onResume();
                drawerSP.openDrawer(GravityCompat.START);
            }
            else {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.framelayout_serviceHomeContainer);
                if (    (f instanceof CompletedFragment)
                        || (f instanceof InProgressFragment)
                        || (f instanceof PendingFragment)
                        || (f instanceof JobRequestFragment)
                        || (f instanceof JobRequestDetailFragment)
                        ||  (f instanceof NotificationFragment_SP )
                        ||  (f instanceof ProfileFragment_SP)
                        ||  (f instanceof MyPlanFragment)
                        ||  (f instanceof SubscriptionPlanFragment)
                        ||  (f instanceof MyEarningsFragment_SP)
                        ||  (f instanceof YourJobsFragment_SP)
                        || ( f instanceof WebViewsFragment)
                        ||(f instanceof ChangeContactFrag)
                          )
                {
                    onResume();
                    getSupportFragmentManager().popBackStack();

                }
                else
                {
                    setUserNameImage();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    drawerSP.openDrawer(GravityCompat.START);
                }
            }
        }
    };




    private void setUserNameImage()
    {
        profile_name.setText(SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.FullName));

        AQuery aQuery=new AQuery(profile_image);
        if(!SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.IMAGE).isEmpty())
        {
            aQuery.id(profile_image).image(SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.IMAGE));
        }
        else
        {
            aQuery.id(profile_image).image(R.drawable.default_profile_pic);
        }
    }


    private void close_SPHomeNavigationDrawer()
    {
        if (drawerSP.isDrawerOpen(GravityCompat.START))
        {
            drawerSP.closeDrawer(GravityCompat.START);


            imageView_plan_arrowSP.setRotation(0.0f);
            isPlanArrowOpenSP = true;
            linearlayout_myPlanSP.setVisibility(View.GONE);
            linearlayout_subscriptionPlanSP.setVisibility(View.GONE);

                isJobArrowOpen = true;
                imageView_jobOrderArrowSP.setRotation(0.0f);
                linearlayout_myJobOrdersSP.setVisibility(View.GONE);
                linearlayout_myJobReqSP.setVisibility(View.GONE);

        }
    }



    /*click events on side- menu*/
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.linearlayout_myProfileServicePro:
                close_SPHomeNavigationDrawer();
                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, true);
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_serviceHomeContainer, new ProfileFragment_SP(), "profileFragSP").addToBackStack("profileSP").commit();

                break;


            case R.id.linearlayout_notificationServicePro:

                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_serviceHomeContainer, new NotificationFragment_SP(), "notificationsFragSP").addToBackStack("notificationsSP").commit();

                close_SPHomeNavigationDrawer();

                break;

            case R.id.linearlayout_jobOrderPro:

                if (isJobArrowOpen) {
                    checkJobOrderStatusOnGone_SP();
                    isJobArrowOpen = false;
                } else {
                    imageView_jobOrderArrowSP.setRotation(0.0f);
                    linearlayout_myJobOrdersSP.setVisibility(View.GONE);
                    linearlayout_myJobReqSP.setVisibility(View.GONE);
                    isJobArrowOpen = true;
                }
                break;

            case R.id.linearlayout_myJobOrdersSP:

                close_SPHomeNavigationDrawer();
                Intent intent = new Intent(this,MyJobOrdersActivity.class);
                startActivity(intent);
                break;
            case R.id.linearlayout_myJobReqSP:
                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, true);
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_serviceHomeContainer, new JobRequestFragment(), "jobReqDetailFragSP").addToBackStack("jobReqDetailFrag").commit();
                close_SPHomeNavigationDrawer();
                break;

            case R.id.linearlayout_planServicePro:

                if (isPlanArrowOpenSP) {
                    checkPlanStatusOnGone_SP();
                    isPlanArrowOpenSP = false;
                } else {
                    imageView_plan_arrowSP.setRotation(0.0f);
                    linearlayout_myPlanSP.setVisibility(View.GONE);
                    linearlayout_subscriptionPlanSP.setVisibility(View.GONE);
                    isPlanArrowOpenSP = true;
                }
                break;

            case R.id.linearlayout_myPlanServicePro:
                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, true);
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_serviceHomeContainer, new MyPlanFragment(), "myPlanSPFrag").addToBackStack("myplanSP").commit();
                close_SPHomeNavigationDrawer();
                break;

            case R.id.linearlayout_subscriptionPlanServicePro:
                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, true);
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_serviceHomeContainer, new SubscriptionPlanFragment(), "subscriptionSPFrag").addToBackStack("subscriptionplanSP").commit();
                close_SPHomeNavigationDrawer();
                break;


            case R.id.linearlayout_myEarningsServicePro:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_serviceHomeContainer, new MyEarningsFragment_SP(), "myEarningSPFrag").addToBackStack("myearningSP").commit();
                close_SPHomeNavigationDrawer();
                break;

            case R.id.linearlayout_contactUsServicePro:
                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, true);
                Bundle bundle2=new Bundle();
                bundle2.putString("page_type", "contactus");
                WebViewsFragment webViewsFragment2 =new WebViewsFragment();
                webViewsFragment2.setArguments(bundle2);
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_serviceHomeContainer, webViewsFragment2, "webViewsSPFrag").addToBackStack("webViews_sp").commit();
                close_SPHomeNavigationDrawer();
                break;

            case R.id.linearlayout_aboutServicePro:

                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, true);
                Bundle bundle=new Bundle();
                bundle.putString("page_type", "aboutus");
                WebViewsFragment webViewsFragment =new WebViewsFragment();
                webViewsFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_serviceHomeContainer, webViewsFragment, "webViewsSPFrag").addToBackStack("webViews_sp").commit();
                close_SPHomeNavigationDrawer();
                break;
            case R.id.linearlayout_TCServicePro:
                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, true);
                Bundle bundle1=new Bundle();
                bundle1.putString("page_type", "t_and_c");
                WebViewsFragment webViewsFragment1 =new WebViewsFragment();
                webViewsFragment1.setArguments(bundle1);

                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_serviceHomeContainer, webViewsFragment1, "webViewsSP1Frag").addToBackStack("webViews_sp1").commit();
                close_SPHomeNavigationDrawer();
                break;
            case R.id.linearlayout_shareServicePro:
                String shareBody = "Install Airmechaniks to know about near by garage and their services.";
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Invite your Friends"));
                break;

            case R.id.linearlayout_logoutServicePro:
                AlertDialog.Builder logout =  new AlertDialog.Builder(this);
                logout.setCancelable(false);
                logout.setTitle("Logout");
                logout.setIcon(R.drawable.logo);
                logout.setMessage("Do you want to logout ?");
                logout.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        logoutServiceHit();
                    }
                });
                logout.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = logout.create();
                alertDialog.show();
                TextView msgTxt = (TextView) alertDialog.findViewById(android.R.id.message);
                FontBinding.setFont(msgTxt, "regular");
                //Buttons
                Button positive_button =  alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                Button negative_button =  alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                setPositiveNegativeButtonColor(positive_button,negative_button);

                break;
        }
    }
    private void setPositiveNegativeButtonColor(Button positive,Button negative)
    {
//        Font.setFontButton(positive,this);
        positive.setTextColor(getResources().getColor(R.color.blue));
//        Font.setFontButton(negative,this);
        negative.setTextColor(getResources().getColor(R.color.black));
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_servicePro);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_show_service_provider)
        {
            startActivity(new Intent(this, MyJobOrdersActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_servicePro);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


/*services*/

    private void logoutServiceHit() {
        try {

            MultipartEntityBuilder multipartbuilder = MultipartEntityBuilder.create();
            multipartbuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartbuilder.addTextBody("token",SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN));
            ServiceBean serviceBean = new ServiceBean();
            serviceBean.setMethodName("Services/logout");
            serviceBean.setActivity(this);
            serviceBean.setApilistener(this);
            CustomHandler customHandler = new CustomHandler(serviceBean);
            customHandler.makeMultipartRequest(multipartbuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void myServerResponse(JSONObject responseObj)
    {
        try {
            if (responseObj != null)
            {
                if (responseObj.getString("status").equalsIgnoreCase("SUCCESS"))
                {
                    if (responseObj.getString("requestKey").equals("logout"))
                    {
                        Log.i("JsonResponse", ""+responseObj.toString());
                        Intent intent=new Intent(HomeActivityServicePro.this, RoleSelectionActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        SharedPreferenceWriter.getInstance(getApplicationContext()).writeBooleanValue(SPreferenceKey.LOGINKEY, false);
                        SharedPreferenceWriter.getInstance(getApplicationContext()).writeBooleanValue(SPreferenceKey.CUSTOMER_LOGIN, false);
                        SharedPreferenceWriter.getInstance(getApplicationContext()).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, false);
                        startActivity(intent);
                        finish();

                    }
                }
                else {
                    Log.i("JsonResponse", ""+responseObj.toString());
                    Toast.makeText(this,""+responseObj.getString("message"),Toast.LENGTH_LONG).show();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        homeact=true;
        profile_name.setText(SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.FullName));

        AQuery aQuery=new AQuery(profile_image);
        if(!SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.IMAGE).isEmpty())
        {
            aQuery.id(profile_image).image(SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.IMAGE));
        }
        else
        {
            aQuery.id(profile_image).image(R.drawable.default_profile_pic);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        homeact=false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        homeact=false;
    }
}

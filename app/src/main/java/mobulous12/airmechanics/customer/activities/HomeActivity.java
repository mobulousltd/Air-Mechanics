package mobulous12.airmechanics.customer.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

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
import com.google.android.gms.maps.GoogleMap;
import mobulous12.airmechanics.R;
import mobulous12.airmechanics.RoleSelectionActivity;
import mobulous12.airmechanics.customer.fragments.BillPaymentFragment;
import mobulous12.airmechanics.customer.fragments.BookingDetailFragment;
import mobulous12.airmechanics.customer.fragments.ChangeContactFrag;
import mobulous12.airmechanics.customer.fragments.ChangePasswordFragment;
import mobulous12.airmechanics.customer.fragments.FavoritesFragment;
import mobulous12.airmechanics.customer.fragments.HomeMapFragment;
import mobulous12.airmechanics.customer.fragments.MyBookingsFragment;
import mobulous12.airmechanics.customer.fragments.MyPlanFragment;
import mobulous12.airmechanics.customer.fragments.MyProfileFragment;
import mobulous12.airmechanics.customer.fragments.NewJobRequest;
import mobulous12.airmechanics.customer.fragments.NotificationsFragment;
import mobulous12.airmechanics.customer.fragments.RateScreenFragment;
import mobulous12.airmechanics.customer.fragments.RejectedQuotesFragment;
import mobulous12.airmechanics.customer.fragments.SettingsFragment;
import mobulous12.airmechanics.customer.fragments.SubscriptionPlanFragment;
import mobulous12.airmechanics.customer.fragments.WebViewsFragment;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.fonts.Font;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    /*variables*/

    public static boolean homeact=false;
    public static HomeActivity homeActivity;
    public   Toolbar toolbar;
    private GoogleMap mMap;
    public static TextView toolbar_title;
    TextView profile_name;
    ImageView profile_image;
    private  NavigationView navigationView;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private boolean isBookingArrowOpen = true, isPlanArrowOpen = true,isAirInfoOpen = true;

    private LinearLayout linearlayout_myBooking,
            linearlayout_booking, linearlayout_plan, linearlayout_myPlan,
            linearlayout_myProfile, linearlayout_favorites,
            linearlayout_subscriptionPlan,linearlayout_about, linearlayout_notification,linearlayout_share,
            linearlayout_job_rejected,linearlayout_settings,linearlayout_newJobReq;

    private ImageView imageView_plan_arrow, imageView_booking_arrow,imageView_airInfo;
    private LinearLayout ll_airInfo,ll_aboutUsInfo,ll_contactUsInfo,ll_TCInfo,ll_customerLogin,ll_serviceProLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_main);
        homeact=true;


        /*set toolbar*/
        homeActivity = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarVisible();
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);


     /*DRAWER*/
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.menu));

        toggle.setToolbarNavigationClickListener(toolbarNavigationClickListener);
        toggle.syncState();

//       Map
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer, new HomeMapFragment(), "homeMapFragment").commit();


        /*Navigation drawer*/

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        profile_name=(TextView)header.findViewById(R.id.textView_userName_Home);
        profile_image=(ImageView)header.findViewById(R.id.circularImageView_profilePic_Home);

        if(!SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.LOGINKEY))
        {
            profile_name.setText("Welcome Guest");
            profile_name.setTypeface(Typeface.createFromAsset(this.getAssets(),
                    "fonts/Raleway-SemiBold_0.ttf"));
            profile_image.setVisibility(View.INVISIBLE);
        }

            profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferenceWriter.getInstance(getApplicationContext()).writeBooleanValue(SPreferenceKey.CUSTOMER_LOGIN, true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer, new MyProfileFragment(), "myProfileFragment").addToBackStack("profile").commit();
                    closeHomeNavigationDrawer();
                }
            });




        /*Navigation drawer  main Views */

//        TextView textView_userName_Home, textView_myProfile,
//                textView_booking, textView_myBooking, textView_favorite,
//                textView_notification, textView_settings, textView_plan,
//                textView_myPlan, textView_subscriptionPlan, textView_about, textView_share, textView_logout;
//
//
//        textView_userName_Home = (TextView) header.findViewById(R.id.textView_userName_Home);
//        textView_myProfile = (TextView) header.findViewById(R.id.textView_myProfile);
//        textView_booking = (TextView) header.findViewById(R.id.textView_booking);
//        textView_myBooking = (TextView) header.findViewById(R.id.textView_myBooking);
//        textView_favorite = (TextView) header.findViewById(R.id.textView_favorite);
//        textView_notification = (TextView) header.findViewById(R.id.textView_notification);
//        textView_settings = (TextView) header.findViewById(R.id.textView_settings);
//        textView_plan = (TextView) header.findViewById(R.id.textView_plan);
//        textView_myPlan = (TextView) header.findViewById(R.id.textView_myPlan);
//        textView_subscriptionPlan = (TextView) header.findViewById(R.id.textView_subscriptionPlan);
//        textView_about = (TextView) header.findViewById(R.id.textView_about);
//        textView_share = (TextView) header.findViewById(R.id.textView_share);
//        textView_logout = (TextView) header.findViewById(R.id.textView_logout);


    /*linear layouts*/
        linearlayout_myProfile = (LinearLayout) header.findViewById(R.id.linearlayout_myProfile);
        linearlayout_booking = (LinearLayout) header.findViewById(R.id.linearlayout_booking);
        linearlayout_myBooking = (LinearLayout) header.findViewById(R.id.linearlayout_myBooking);
        linearlayout_newJobReq = (LinearLayout) header.findViewById(R.id.linearlayout_newJobReq);
        linearlayout_favorites = (LinearLayout) header.findViewById(R.id.linearlayout_favorites);
        linearlayout_plan = (LinearLayout) header.findViewById(R.id.linearlayout_plan);
        linearlayout_notification = (LinearLayout) header.findViewById(R.id.linearlayout_notification);
        linearlayout_myPlan = (LinearLayout) header.findViewById(R.id.linearlayout_myPlan);
        linearlayout_subscriptionPlan = (LinearLayout) header.findViewById(R.id.linearlayout_subscriptionPlan);
        linearlayout_subscriptionPlan = (LinearLayout) header.findViewById(R.id.linearlayout_subscriptionPlan);
        linearlayout_about = (LinearLayout) header.findViewById(R.id.linearlayout_about);

        linearlayout_settings = (LinearLayout) header.findViewById(R.id.linearlayout_settings);
        linearlayout_share = (LinearLayout) header.findViewById(R.id.linearlayout_share);
//        linearlayout_logout = (LinearLayout) header.findViewById(R.id.linearlayout_logout);
        ll_airInfo = (LinearLayout) header.findViewById(R.id.ll_airInfo);
        ll_aboutUsInfo = (LinearLayout) header.findViewById(R.id.ll_aboutUsInfo);
        ll_contactUsInfo = (LinearLayout)  header.findViewById(R.id.ll_contactUsInfo);
        ll_TCInfo = (LinearLayout)  header.findViewById(R.id.ll_TCInfo);
        ll_customerLogin = (LinearLayout)  header.findViewById(R.id.ll_customerLogin);
        ll_serviceProLogin = (LinearLayout)  header.findViewById(R.id.ll_serviceProLogin);



        /*ARROWS*/
        imageView_plan_arrow = (ImageView) header.findViewById(R.id.imageView_plan_arrow);
        imageView_booking_arrow = (ImageView) header.findViewById(R.id.imageView_booking_arrow);
        imageView_airInfo = (ImageView) header.findViewById(R.id.imageView_airInfo);



        linearlayout_job_rejected=(LinearLayout)header.findViewById(R.id.linearlayout_job_rejected);
        linearlayout_job_rejected.setOnClickListener(this);
        /* make these layouts invisible at start */
        linearlayout_myBooking.setVisibility(View.GONE);
        linearlayout_newJobReq.setVisibility(View.GONE);
        linearlayout_job_rejected.setVisibility(View.GONE);
        linearlayout_myPlan.setVisibility(View.GONE);
        linearlayout_subscriptionPlan.setVisibility(View.GONE);


        ll_airInfo.setVisibility(View.GONE);
        ll_aboutUsInfo.setVisibility(View.GONE);
        ll_contactUsInfo.setVisibility(View.GONE);
        ll_TCInfo.setVisibility(View.GONE);
        ll_customerLogin.setVisibility(View.GONE);
        ll_serviceProLogin.setVisibility(View.GONE);



        /*CLICK LISTENERS ON DIFFERENT layout to load different FRAGMENTS*/
        linearlayout_myProfile.setOnClickListener(this);
        linearlayout_booking.setOnClickListener(this);
        linearlayout_favorites.setOnClickListener(this);
        linearlayout_plan.setOnClickListener(this);
        linearlayout_myBooking.setOnClickListener(this);
        linearlayout_newJobReq.setOnClickListener(this);
        linearlayout_notification.setOnClickListener(this);
        linearlayout_myPlan.setOnClickListener(this);
        linearlayout_subscriptionPlan.setOnClickListener(this);
        linearlayout_about.setOnClickListener(this);
        linearlayout_settings.setOnClickListener(this);
        linearlayout_share.setOnClickListener(this);


        if(!SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.LOGINKEY))
        {
            doGuestStuff();
            ll_airInfo.setOnClickListener(this);
            ll_aboutUsInfo.setOnClickListener(this);
            ll_contactUsInfo.setOnClickListener(this);
            ll_TCInfo.setOnClickListener(this);
            ll_customerLogin.setOnClickListener(this);
            ll_serviceProLogin.setOnClickListener(this);
        }


        if(getIntent().getStringExtra("notify")!=null)
        {
            String notifi=getIntent().getStringExtra("notify");
            if(notifi.equalsIgnoreCase("priceupdate"))
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer, new NewJobRequest(), "newJobRequestFragment").addToBackStack("newJobRequest").commit();
            }
            else
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer, new MyBookingsFragment(), "myBookingsFragment").addToBackStack("mybookings").commit();
            }
        }
    }  //oncreate ends Here

private void doGuestStuff()
{
    ll_airInfo.setVisibility(View.VISIBLE);
    ll_customerLogin.setVisibility(View.VISIBLE);
    ll_serviceProLogin.setVisibility(View.VISIBLE);

    //    make all other fields GONE  for Guest


    profile_name.setText("Welcome Guest");
    profile_name.setTypeface(Typeface.createFromAsset(this.getAssets(),
            "fonts/Raleway-SemiBold_0.ttf"));
    profile_image.setVisibility(View.INVISIBLE);
    linearlayout_myProfile.setVisibility(View.GONE);
    linearlayout_booking.setVisibility(View.GONE);
    linearlayout_myBooking.setVisibility(View.GONE);
    linearlayout_newJobReq.setVisibility(View.GONE);
    linearlayout_job_rejected.setVisibility(View.GONE);
    linearlayout_notification.setVisibility(View.GONE);
    linearlayout_favorites.setVisibility(View.GONE);
    linearlayout_plan.setVisibility(View.GONE);
    linearlayout_myPlan.setVisibility(View.GONE);
    linearlayout_subscriptionPlan.setVisibility(View.GONE);
    linearlayout_about.setVisibility(View.GONE);
    linearlayout_settings.setVisibility(View.GONE);
    linearlayout_share.setVisibility(View.GONE);


}

    /*****Handling Toolbar  and its components *****/
    public void setToolbarTitle(String title)
    {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.framelayout_homeContainer);

        if (toolbar_title != null)
        {
            if ((f instanceof MyProfileFragment)
                    || (f instanceof MyBookingsFragment)
                    || (f instanceof ChangePasswordFragment)
                    || (f instanceof FavoritesFragment)
                    || (f instanceof BookingDetailFragment)
                    || (f instanceof NotificationsFragment)
                    || (f instanceof MyPlanFragment)
                    || (f instanceof SubscriptionPlanFragment)
                    || (f instanceof BillPaymentFragment)
                    || (f instanceof RateScreenFragment)
                    || (f instanceof RejectedQuotesFragment)
                    || (f instanceof WebViewsFragment)
                    || (f instanceof SettingsFragment)
                    || (f instanceof ChangeContactFrag)
                    || (f instanceof NewJobRequest)
                    )
            {
                toolbar_title.setText(title);
            } else {
                toolbar_title.setText(getResources().getString(R.string.headername_home));
            }
        }
    }

    public void setNavigationIcon()
    {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.framelayout_homeContainer);

        if (toggle != null)
        {
            if (    (f instanceof MyProfileFragment)
                    || (f instanceof MyBookingsFragment)
                    || (f instanceof ChangePasswordFragment)
                    || (f instanceof FavoritesFragment)
                    || (f instanceof BookingDetailFragment)
                    || (f instanceof NotificationsFragment)
                    || (f instanceof MyPlanFragment)
                    || (f instanceof SubscriptionPlanFragment)
                    || (f instanceof BillPaymentFragment)
                    || (f instanceof RateScreenFragment)
                    || (f instanceof RejectedQuotesFragment)
                    || (f instanceof WebViewsFragment)
                    || (f instanceof SettingsFragment)
                    || (f instanceof ChangeContactFrag)
                    || (f instanceof NewJobRequest) )
            {

                toggle.setHomeAsUpIndicator(R.drawable.back);
                toggle.syncState();
            } else {
                toggle.setHomeAsUpIndicator(R.drawable.menu);
                toggle.syncState();
            }
        }
    }

    //          Handling listeners on left-icon in toolbar
    private View.OnClickListener toolbarNavigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {

            if (drawer.isDrawerVisible(GravityCompat.START))
            {
                setUserNameImage();
//                onResume();
                drawer.openDrawer(GravityCompat.START);

            }
            else {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.framelayout_homeContainer);
                if (    (f instanceof MyProfileFragment)
                        || (f instanceof MyBookingsFragment)
                        || (f instanceof ChangePasswordFragment)
                        || (f instanceof FavoritesFragment)
                        || (f instanceof BookingDetailFragment)
                        || (f instanceof NotificationsFragment)
                        || (f instanceof MyPlanFragment)
                        || (f instanceof SubscriptionPlanFragment)
                        || (f instanceof BillPaymentFragment)
                        || (f instanceof RateScreenFragment)
                        || (f instanceof RejectedQuotesFragment)
                        || (f instanceof WebViewsFragment)
                        || (f instanceof SettingsFragment)
                        || (f instanceof ChangeContactFrag)
                        || (f instanceof NewJobRequest) )

                {

                    onResume();
                    getSupportFragmentManager().popBackStack();

                }
                else
                {
                    setUserNameImage();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        }
    };

    private void setUserNameImage()
    {
        if(!SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.LOGINKEY))
        {
            profile_name.setText("Welcome Guest");
            profile_name.setTypeface(Typeface.createFromAsset(this.getAssets(),
                    "fonts/Raleway-SemiBold_0.ttf"));
            profile_image.setVisibility(View.INVISIBLE);
        }
        else {
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

    }

    public  void toolbarGone()
    {
        toolbar.setVisibility(View.GONE);
    }

    public  void toolbarVisible()
    {
        toolbar.setVisibility(View.VISIBLE);
    }

    private void checkBookingStatusOnGone() {
        imageView_booking_arrow.setRotation(90.0f);
        linearlayout_myBooking.setVisibility(View.VISIBLE);
        linearlayout_newJobReq.setVisibility(View.VISIBLE);
        linearlayout_job_rejected.setVisibility(View.VISIBLE);

    }

    private void checkPlanStatusOnGone() {
        imageView_plan_arrow.setRotation(90.0f);
        linearlayout_myPlan.setVisibility(View.VISIBLE);
        linearlayout_subscriptionPlan.setVisibility(View.VISIBLE);

    }
    private void checkAirInfoStatusOnGone()
    {
        imageView_airInfo.setRotation(90.0f);
        ll_aboutUsInfo.setVisibility(View.VISIBLE);
        ll_contactUsInfo.setVisibility(View.VISIBLE);
        ll_TCInfo.setVisibility(View.VISIBLE);

    }

    private void closeHomeNavigationDrawer()
    {
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);

            imageView_booking_arrow.setRotation(0.0f);
            isBookingArrowOpen = true;
            linearlayout_myBooking.setVisibility(View.GONE);
            linearlayout_newJobReq.setVisibility(View.GONE);
            linearlayout_job_rejected.setVisibility(View.GONE);
            imageView_plan_arrow.setRotation(0.0f);
            isPlanArrowOpen = true;
            linearlayout_myPlan.setVisibility(View.GONE);
            linearlayout_subscriptionPlan.setVisibility(View.GONE);

            isAirInfoOpen = true;
            imageView_airInfo.setRotation(0.0f);
            ll_aboutUsInfo.setVisibility(View.GONE);
            ll_contactUsInfo.setVisibility(View.GONE);
            ll_TCInfo.setVisibility(View.GONE);


        }
    }



    /* click events in navigation drawer menu */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_airInfo:
                if (isAirInfoOpen) {
                    checkAirInfoStatusOnGone();
                    isAirInfoOpen = false;
                } else {
                    imageView_airInfo.setRotation(0.0f);
                    ll_aboutUsInfo.setVisibility(View.GONE);
                    ll_contactUsInfo.setVisibility(View.GONE);
                    ll_TCInfo.setVisibility(View.GONE);
                    isAirInfoOpen = true;
                }
                break;

            case R.id.ll_aboutUsInfo:

                Bundle bun1=new Bundle();
                bun1.putString("page_type", "aboutus");
                WebViewsFragment webFrag1 =new WebViewsFragment();
                webFrag1.setArguments(bun1);
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer, webFrag1, "webViewsFragment").addToBackStack("webViews").commit();
                closeHomeNavigationDrawer();
                break;
            case R.id.ll_contactUsInfo:

                Bundle bun2=new Bundle();
                bun2.putString("page_type", "contactus");
                WebViewsFragment webFrag2 =new WebViewsFragment();
                webFrag2.setArguments(bun2);
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer, webFrag2, "webViewsFragment").addToBackStack("webViews").commit();
                closeHomeNavigationDrawer();
                break;
             case R.id.ll_TCInfo:

                Bundle bun3=new Bundle();
                bun3.putString("page_type", "t_and_c");
                WebViewsFragment webFrag3 =new WebViewsFragment();
                webFrag3.setArguments(bun3);
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer, webFrag3, "webViewsFragment").addToBackStack("webViews").commit();
                closeHomeNavigationDrawer();
                break;

            case R.id.ll_customerLogin:

                Intent in1=new Intent(this, LoginActivity.class);
                in1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.LOGINKEY, false);
                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.CUSTOMER_LOGIN, true);
                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, false);
                startActivity(in1);
                finish();
                break;
            case R.id.ll_serviceProLogin:

                Intent in2=new Intent(this, LoginActivity.class);
                in2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.LOGINKEY, false);
                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.CUSTOMER_LOGIN, false);
                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, true);
                startActivity(in2);
                finish();
                break;



            case R.id.linearlayout_myProfile:

                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.CUSTOMER_LOGIN, true);
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer, new MyProfileFragment(), "myProfileFragment").addToBackStack("profile").commit();
                closeHomeNavigationDrawer();
                break;

            case R.id.linearlayout_booking:
                if (isBookingArrowOpen) {
                    checkBookingStatusOnGone();
                    isBookingArrowOpen = false;
                } else {
                    imageView_booking_arrow.setRotation(0.0f);
                    linearlayout_myBooking.setVisibility(View.GONE);
                    linearlayout_newJobReq.setVisibility(View.GONE);
                    linearlayout_job_rejected.setVisibility(View.GONE);
                    isBookingArrowOpen = true;
                }
                break;


            case R.id.linearlayout_myBooking:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer, new MyBookingsFragment(), "myBookingsFragment").addToBackStack("mybookings").commit();
                closeHomeNavigationDrawer();
                break;
            case R.id.linearlayout_job_rejected:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer, new RejectedQuotesFragment(), "rejectedFragment").addToBackStack("mybookings").commit();
                closeHomeNavigationDrawer();
                break;
            case R.id.linearlayout_newJobReq:
                closeHomeNavigationDrawer();
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer, new NewJobRequest(), "newJobRequestFragment").addToBackStack("newJobRequest").commit();
                break;

            case R.id.linearlayout_favorites:

                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer, new FavoritesFragment(), "favoritesFragment").addToBackStack("favourites").commit();

                closeHomeNavigationDrawer();
                break;

            case R.id.linearlayout_plan:
                if (isPlanArrowOpen) {
                    checkPlanStatusOnGone();
                    isPlanArrowOpen = false;
                } else {
                    imageView_plan_arrow.setRotation(0.0f);
                    linearlayout_myPlan.setVisibility(View.GONE);
                    linearlayout_subscriptionPlan.setVisibility(View.GONE);
                    isPlanArrowOpen = true;
                }
                break;

            case R.id.linearlayout_myPlan:
                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.CUSTOMER_LOGIN, true);

                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer, new MyPlanFragment(), "myPlanFragment").addToBackStack("myplan").commit();
                closeHomeNavigationDrawer();

                break;

            case R.id.linearlayout_subscriptionPlan:
                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.CUSTOMER_LOGIN, true);

                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer, new SubscriptionPlanFragment(), "subscriptionFragment").addToBackStack("subscriptionplan").commit();
                closeHomeNavigationDrawer();
                break;
            case R.id.linearlayout_about:

                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.CUSTOMER_LOGIN, true);

                Bundle bundle=new Bundle();
                bundle.putString("page_type", "aboutus");
                WebViewsFragment webViewsFragment =new WebViewsFragment();
                webViewsFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer, webViewsFragment, "webViewsFragment").addToBackStack("webViews").commit();
                closeHomeNavigationDrawer();
                break;
            case R.id.linearlayout_notification:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer, new NotificationsFragment(), "notificationsFragment").addToBackStack("notifications").commit();
                closeHomeNavigationDrawer();
                break;


            case R.id.linearlayout_settings:

                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.CUSTOMER_LOGIN, true);
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer, new SettingsFragment(), "settingsFragment").addToBackStack("settingsFrag").commit();

                closeHomeNavigationDrawer();

                break;
           /* case R.id.linearlayout_logout:

                AlertDialog.Builder logout =  new AlertDialog.Builder(this);
                logout.setCancelable(false);
                logout.setTitle("Logout");
                logout.setIcon(R.drawable.logo);
                logout.setMessage("Do you want to logout ?");
                logout.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        startActivity(new Intent(HomeActivity.this, RoleSelectionActivity.class));
                        SharedPreferenceWriter.getInstance(getApplicationContext()).writeBooleanValue(SPreferenceKey.LOGINKEY, false);
                        SharedPreferenceWriter.getInstance(getApplicationContext()).writeBooleanValue(SPreferenceKey.CUSTOMER_LOGIN, false);
                        SharedPreferenceWriter.getInstance(getApplicationContext()).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, false);
                        finish();


                    }
                });
                logout.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = logout.create();
                alertDialog.show();

                //Buttons
                Button positive_button =  alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                Button negative_button =  alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                setPositiveNegativeButtonColor(positive_button,negative_button);




                break;*/
            case R.id.linearlayout_share:

                String shareBody = "Install Airmechaniks to know about near by garage and their services.";
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Invite your Friends"));
                closeHomeNavigationDrawer();
                break;

        }

    }

    /*-----------*/


    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();

        }
        // getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer, new HomeMapFragment(), "homeMapFragment").commit();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_show_service_provider) {

            startActivity(new Intent(this, ServiceProviderActivity.class));

        }


        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(!SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.LOGINKEY))
        {
            profile_name.setText("Welcome Guest");
            profile_name.setTypeface(Typeface.createFromAsset(this.getAssets(),
                    "fonts/Raleway-SemiBold_0.ttf"));
            profile_image.setVisibility(View.INVISIBLE);
        }
        else {
            homeact = true;
            profile_name.setText(SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.FullName));

            AQuery aQuery = new AQuery(profile_image);
            if (!SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.IMAGE).isEmpty()) {
                aQuery.id(profile_image).image(SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.IMAGE));
            } else {
                aQuery.id(profile_image).image(R.drawable.default_profile_pic);
            }
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
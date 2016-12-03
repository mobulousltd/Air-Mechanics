package mobulous12.airmechanics.serviceprovider.activities;

import android.app.Service;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.BookingBean;
import mobulous12.airmechanics.fonts.FontBinding;
import mobulous12.airmechanics.serviceprovider.fragments.CompletedFragment;
import mobulous12.airmechanics.serviceprovider.fragments.InProgressFragment;
import mobulous12.airmechanics.serviceprovider.fragments.PendingFragment;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

public class MyJobOrdersActivity extends AppCompatActivity implements ApiListener {

    public boolean jobOrdersAct=false;
    ViewPager viewPager = null;
    FragmentManager fragmentManager;
    private Toolbar toolbar;
    private TextView toolbar_titleSP;
    private TabLayout tabLayout;
    private TextView textview_headername_myJobOrders;
    private TextView  tv1,tv2,tv3;
    private View tabOne,tab2,tab3;

    private ArrayList<BookingBean> beanArrayList;

    private PendingFragment pendingFragment;
    private InProgressFragment inProgressFragment;
    private CompletedFragment completedFragment;
    private Bundle bundle;

    boolean isServiceHit = true;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this,R.layout.activity_my_job_orders);
        pendingFragment = new PendingFragment();
        inProgressFragment = new InProgressFragment();
        completedFragment = new CompletedFragment();

        findViewById(R.id.back_myJobOrdersActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //ViewPager configure
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        fragmentManager = getSupportFragmentManager();
        setUpMyViewPager(viewPager); //My own Method defined below in code
        //TabLayout configure
        tabLayout = (TabLayout) findViewById(R.id.tablayout_myJobOrders);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
                ViewGroup vgTab = (ViewGroup) vg.getChildAt(tab.getPosition());
                int tabChildsCount = vgTab.getChildCount();
                for (int i = 0; i < tabChildsCount; i++)
                {
                    View tabViewChild = vgTab.getChildAt(i);
                    if (tabViewChild instanceof TextView)
                    {
                        FontBinding.setFont(((TextView)tabViewChild), "semibold");
                    }
                }
                viewPager.setCurrentItem(tab.getPosition());
                // HITTING SERVICE
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
                ViewGroup vgTab = (ViewGroup) vg.getChildAt(tab.getPosition());
                int tabChildsCount = vgTab.getChildCount();
                for (int i = 0; i < tabChildsCount; i++)
                {
                    View tabViewChild = vgTab.getChildAt(i);
                    if (tabViewChild instanceof TextView)
                    {
                        FontBinding.setFont(((TextView)tabViewChild), "regular");
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // HITTING SERVICE
            }
        });

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        ViewGroup vgTab = (ViewGroup) vg.getChildAt(0);
        int tabChildsCount = vgTab.getChildCount();
        for (int i = 0; i < tabChildsCount; i++)
        {
            View tabViewChild = vgTab.getChildAt(i);
            if (tabViewChild instanceof TextView)
            {
                FontBinding.setFont(((TextView)tabViewChild), "semibold");
            }
        }

//        /* Tabs 1 , 2 , 3*/
//        tabOne = (View) LayoutInflater.from(this).inflate(R.layout.custom_tab_item, null);
//        tv1 = (TextView)tabOne.findViewById(R.id.tab);
//
//        tab2 = (View) LayoutInflater.from(this).inflate(R.layout.custom_tab_item, null);
//        tv2 = (TextView)tab2.findViewById(R.id.tab);
//
//        tab3 = (View) LayoutInflater.from(this).inflate(R.layout.custom_tab_item, null);
//        tv3 = (TextView)tab3.findViewById(R.id.tab);
//
//        final Typeface semiBold_font = Typeface.createFromAsset(getAssets(), "fonts/Raleway-SemiBold_0.ttf");
//        final Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");
//
//
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if(position == 0)
//                {
//                    tv1.setText(getResources().getString(R.string.pending_tab));
//                    tv1.setTypeface(semiBold_font);
//                    tv2.setTypeface(regular_font);
//                    tv3.setTypeface(regular_font);
//
//                }
//                if(position == 1)
//                {
//                    tv2.setText(getResources().getString(R.string.inprogress_tab));
//                    tv2.setTypeface(semiBold_font);
//                    tv1.setTypeface(regular_font);
//                    tv3.setTypeface(regular_font);
//
//                }
//                if(position == 2)
//                {
//                    tv3.setText(getResources().getString(R.string.completed_tab));
//                    tv3.setTypeface(semiBold_font);
//                    tv1.setTypeface(regular_font);
//                    tv2.setTypeface(regular_font);
//                }
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//
//        setupTabTexts();


        /*     **** What things do **** ----
        *
        *   tabLayout.setupWithViewPager() –--- Assigns the ViewPager to TabLayout.
        *   setupViewPager() –--- Defines the number of tabs by setting appropriate fragment and tab name.
        *   ViewPagerAdapter –--- Custom adapter class provides fragments required for the view pager.
        */


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position)
                {
                    case 0:
                        pendingFragment.pendingServiceHit(MyJobOrdersActivity.this);
                        break;
                    case 1:
                        inProgressFragment.inProgressServiceHit();
                        break;
                    case 2:
                        completedFragment.completedServiceHit();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });





    }

    @Override
    protected void onResume() {
        super.onResume();
        // Hitting PENDING SERVICE when Screen Appear
        if (isServiceHit)
        {
            pendingFragment.pendingServiceHit(MyJobOrdersActivity.this);
            isServiceHit = false;
        }
    }

    private void setUpMyViewPager(ViewPager viewPager)
    {
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(fragmentManager);

        myViewPagerAdapter.addNewFragment(pendingFragment, "Pending");
        myViewPagerAdapter.addNewFragment(inProgressFragment, "InProgress");
        myViewPagerAdapter.addNewFragment(completedFragment, "Completed");

        viewPager.setAdapter(myViewPagerAdapter);


    }

    @Override
    public void myServerResponse(JSONObject jsonObject) {

        Log.e("JOB_ORDERS_PENDING", jsonObject.toString());


        beanArrayList = new ArrayList<>();

        if (jsonObject != null)
        {
            try {

                if (jsonObject.getString("status").equals("SUCCESS"))
                {
                    JSONObject response = jsonObject.getJSONObject("response");
                    JSONArray userArray = response.getJSONArray("user");

                    bundle = new Bundle();
                    BookingBean bean = new BookingBean();

                    for (int i=0; i<userArray.length(); i++)
                    {
                        JSONObject obj = userArray.getJSONObject(i);
                        bean.setUserName(obj.getString("request_Title"));
                        bean.setStatus(obj.getString("status"));
                        bean.setMinCharge(obj.getString("minCharge"));
                        bean.setRequestDate(obj.getString("requestDate"));
                        bean.setRequestTime(obj.getString("Request_time"));

                        beanArrayList.add(bean);
                    }
                    bundle.putParcelableArrayList("rejected_user_data", beanArrayList);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    // My Adapter class for View Pager
    class MyViewPagerAdapter extends FragmentStatePagerAdapter
    {
        private final List<Fragment> myFragmentsList = new ArrayList<>();
        private final List<String> myFragmentTitleList = new ArrayList<>();

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return myFragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return myFragmentsList.size();
        }

        public void addNewFragment(Fragment fragment, String title)
        {
            myFragmentsList.add(fragment);
            myFragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {

            return myFragmentTitleList.get(position);
        }
    }
    private void setupTabTexts()
    {

        Typeface semiBold_font = Typeface.createFromAsset(getAssets(), "fonts/Raleway-SemiBold_0.ttf");
        Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");


        /* Tabs 1 , 2 , 3*/
        tabOne = (View) LayoutInflater.from(this).inflate(R.layout.custom_tab_item, null);
        tv1 = (TextView)tabOne.findViewById(R.id.tab);

        tab2 = (View) LayoutInflater.from(this).inflate(R.layout.custom_tab_item, null);
        tv2 = (TextView)tab2.findViewById(R.id.tab);

        tab3 = (View) LayoutInflater.from(this).inflate(R.layout.custom_tab_item, null);
        tv3 = (TextView)tab3.findViewById(R.id.tab);

        tv1.setText(getResources().getString(R.string.pending_tab));
        tv1.setTypeface(semiBold_font);
        tabLayout.getTabAt(0).setCustomView(tabOne);


        tv2.setText(getResources().getString(R.string.inprogress_tab));
        tv2.setTypeface(regular_font);
        tabLayout.getTabAt(1).setCustomView(tab2);


        tv3.setText(getResources().getString(R.string.completed_tab));
        tv3.setTypeface(regular_font);
        tabLayout.getTabAt(2).setCustomView(tab3);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}


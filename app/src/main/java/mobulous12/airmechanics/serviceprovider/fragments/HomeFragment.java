package mobulous12.airmechanics.serviceprovider.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;
import com.androidquery.AQuery;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.databinding.FragmentHomeFragBinding;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.utils.CalendarView;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.utils.CircularImageView;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

public class HomeFragment extends Fragment implements ApiListener {

    public CalendarView homefragment_calview;
    HashSet<Date> events;
    HashMap<Date, String>counts;
    private View view;
    private TextView textView_name_homeSP,textView_address_homeSP;
    private CircularImageView profileImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        try {
            if (view == null)
            {
                FragmentHomeFragBinding binding=DataBindingUtil.inflate(inflater, R.layout.fragment_home_frag_, container, false);
                view=binding.getRoot();
            }
            else
            {
                ViewGroup parent = (ViewGroup) view.getParent();
                if (parent != null){ parent.removeView(view);}
                FragmentHomeFragBinding binding=DataBindingUtil.inflate(inflater, R.layout.fragment_home_frag_, container, false);
                view=binding.getRoot();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


//        try {
//
//            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
//            events.add(format1.parse("2016-11-29"));
//            events.add(format1.parse("2016-11-20"));
//            events.add(format1.parse("2016-11-26"));
//            events.add(format1.parse("2016-11-23"));
//        }
//        catch (Exception e)
//        {
//
//        }
        ((HomeActivityServicePro) getActivity()).setToolbarTitleSP(getResources().getString(R.string.headername_home));
        ((HomeActivityServicePro) getActivity()).setNavigationIconSP();


        textView_name_homeSP=(TextView)view.findViewById(R.id.textView_name_homeSP);
        textView_address_homeSP=(TextView)view.findViewById(R.id.textView_address_homeSP);
        profileImage = (CircularImageView) view.findViewById(R.id.circleImageView);
//        calendar_date_display=(TextView)view.findViewById(R.id.calendar_date_display);

        //profile username and address
        textView_name_homeSP.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.COMPANYNAME));
        textView_address_homeSP.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.ADDRESS));
        //profile image
        AQuery aQuery = new AQuery(profileImage);
        if (!SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.IMAGE).isEmpty())
        {
            aQuery.id(profileImage).image(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.IMAGE));

        } else {
            aQuery.id(profileImage).image(R.drawable.default_profile_pic);
        }


        homefragment_calview=(CalendarView)view.findViewById(R.id.homefragment_calview);
        homefragment_calview.txtDate=(TextView)view.findViewById(R.id.calendar_date_display);

        CalendarView.currentDate=Calendar.getInstance();

        /*set fonts*/
        Font.setFontTextViewBold(homefragment_calview.txtDate,getActivity());


        homefragment_calview.setEventHandler(new CalendarView.EventHandler()
        {
            @Override
            public void onDayLongPress(Date date)
            {
                try {
                    for (Date eventDate : events)
                    {
                        if(date.getDate()==eventDate.getDate() && date.getMonth()==eventDate.getMonth()&&eventDate.getYear()==date.getYear())
                        {
                            YourJobsFragment_SP sp = new YourJobsFragment_SP();
                            SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd");
                            String reqdate=s.format(date);
                            Bundle bundle = new Bundle();
                            bundle.putString("date", reqdate);
                            sp.setArguments(bundle);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_serviceHomeContainer, sp, "yourJobsFragment").addToBackStack("yourJobs").commit();

                        }
                    }
//                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String formattedDate = format1.format(date);
//                    for (int i = 0; i < dateList.size(); i++) {
//                        if (dateList.get(i).getDate().substring(0, 10).equalsIgnoreCase(formattedDate.substring(0, 10))) {
//                            PendingJobForCalenderFrag frag = new PendingJobForCalenderFrag();
//                            Bundle bundle = new Bundle();
//                            bundle.putString("date", dateList.get(i).getDate());
//                            frag.setArguments(bundle);
//                            act.getSupportFragmentManager().beginTransaction().replace(R.id.framlayout, frag).addToBackStack(null).commit();
//                            break;
//                        }
////                    Condition if user click on date on which no booking are available
//                        if (i == dateList.size() - 1) {
//                            MyConstants.showMessage(act, getString(R.string.you_dont_have_any_booking_on));
//                        }
//                    }
////                condition if there is no booking
//                    if (dateList.size() == 0) {
//                        MyConstants.showMessage(act, getString(R.string.you_dont_have_any_booking_on));
//                    }
                } catch (StringIndexOutOfBoundsException e) {
                    Log.e("HomeCalenderFrag",e.toString());
                }
            }
        });
        view.findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CalendarView.currentDate.add(Calendar.MONTH, 1);
                homefragment_calview.updateCalendar(events,counts);
            }
        });
        view.findViewById(R.id.prev_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarView.currentDate.add(Calendar.MONTH, -1);
                homefragment_calview.updateCalendar(events,counts);
            }
        });

        // HITTING HOME SERVICE
        homeScreenServiceHit();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //profile username and address
        textView_name_homeSP.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.COMPANYNAME));
        textView_address_homeSP.setText(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.ADDRESS));
        //profile image
        AQuery aQuery = new AQuery(profileImage);
        if (!SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.IMAGE).isEmpty())
        {
            aQuery.id(profileImage).image(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.IMAGE));

        } else {
            aQuery.id(profileImage).image(R.drawable.default_profile_pic);
        }




    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        try {
//            Fragment f1 = getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
//            if (f1 != null) {
//                getActivity().getSupportFragmentManager().beginTransaction().remove(f1).commit();
//            }
//            view = null;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    private void homeScreenServiceHit()
    {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN));

        ServiceBean bean = new ServiceBean();
        bean.setActivity(getActivity());
        bean.setMethodName("Services/homescreen");
        bean.setApilistener(this);
        bean.setIsLoader(false);

        CustomHandler customHandler = new CustomHandler(bean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);
    }

    @Override
    public void myServerResponse(JSONObject jsonObject) {

        try {
            if (jsonObject.getString("status").equals("SUCCESS"))
            {
                if(jsonObject.getString("requestKey").equalsIgnoreCase("homescreen"))
                {
                JSONArray array = jsonObject.getJSONArray("response");
                    events=new HashSet<Date>();
                    counts=new HashMap<>();
                for (int i=0; i<array.length(); i++)
                {
                    JSONObject object = array.getJSONObject(i);
                    String date = object.getString("date");
                    String count = object.getString("count");

                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                    counts.put(format1.parse(date), count);
                    events.add(format1.parse(date));
                }

                homefragment_calview.updateCalendar(events, counts);
               }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

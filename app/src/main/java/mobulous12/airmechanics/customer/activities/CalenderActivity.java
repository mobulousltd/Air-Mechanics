package mobulous12.airmechanics.customer.activities;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

import mobulous12.airmechanics.MapActivityToPickAddress;
import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.ServiceProviderBean;
import mobulous12.airmechanics.customer.fragments.SubmitQuoteFragment;
import mobulous12.airmechanics.fonts.FontBinding;
import mobulous12.airmechanics.serviceprovider.activities.SignUpServiceProActivity;
import mobulous12.airmechanics.serviceprovider.adapters.DocumentsAdapter;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.utils.CalendarView;
import mobulous12.airmechanics.utils.TakeImage;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

public class CalenderActivity extends AppCompatActivity implements View.OnClickListener, ApiListener
{
    ServiceProviderBean bean;
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private static int ADDRESSFETCH=011;

    Calendar calendar;
    DocumentsAdapter documentsAdapter;
    String lat="", lng="", reqdate="", category="";
    String sp_day;
    private boolean isDayOk = false;

    RecyclerView rv_attchpic;
    ArrayList<String> arrayList=new ArrayList<String>();
    public CalendarView quote_calview;
    HashSet<Date> events; HashMap<Date, String>counts;
    private  int  hour, minute, day, month, year;
    EditText editText_description_quoteScreen, editText_title_quoteScreen;
    public static HashMap<Integer, TextView> tv_hashMap;
    private TextView  starttime, endtime, tv_address;
    private LinearLayout root_relative_quote;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_calender);

        bean=getIntent().getParcelableExtra("bean");

        calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute= calendar.get(Calendar.MINUTE);
        day=calendar.get(Calendar.DATE);
        month=calendar.get(Calendar.MONTH);
        year=calendar.get(Calendar.YEAR);
        rv_attchpic=(RecyclerView)findViewById(R.id.rv_attchpic);
        documentsAdapter=new DocumentsAdapter(this, arrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_attchpic.setLayoutManager(linearLayoutManager);
        rv_attchpic.setAdapter(documentsAdapter);

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
        root_relative_quote = (LinearLayout) findViewById(R.id.root_relative_quote);
        tv_address=(TextView)findViewById(R.id.tv_address);
        tv_address.setOnClickListener(this);
        tv_address.setText(SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.ADDRESS));
        starttime=(TextView)findViewById(R.id.starttime);
        starttime.setOnClickListener(this);
        endtime=(TextView)findViewById(R.id.endtime);
        endtime.setOnClickListener(this);
        editText_title_quoteScreen=(EditText)findViewById(R.id.editText_title_quoteScreen);
        editText_description_quoteScreen=(EditText)findViewById(R.id.editText_description_quoteScreen);
        findViewById(R.id.imageView_attachDocs_quote).setOnClickListener(this);
        findViewById(R.id.imageView_back_quote).setOnClickListener(this);
        root_relative_quote.setOnClickListener(this);
        findViewById(R.id.button_Submit_quote).setOnClickListener(this);

        /*calender*/
        quote_calview=(CalendarView)findViewById(R.id.quote_calview);
        quote_calview.txtDate=(TextView)findViewById(R.id.tvquote_month);

        CalendarView.currentDate=calendar;
        quote_calview.updateCalendar(events,counts);
        quote_calview.setEventHandler(new CalendarView.EventHandler()
        {
            @Override
            public void onDayLongPress(Date date)
            {
//               airmechanics E/Calendar Full Date:: Thu Nov 24 12:07:28 GMT+05:30 2016
//               airmechanics E/Request Date:: 2016-11-24
                try
                {
                    SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd");
                    reqdate=s.format(date);
//
//                    Log.e("Calendar Full Date:",date.toString());
//                    Log.e("Request Date:",reqdate);
//
                    String[] sp_dayArr = date.toString().split(" ");
//                    sp_day = sp_dayArr[0];   // Selected Day i.e. "Thu" -> Thu Nov 24 12:07:28 GMT+05:30 2016
//                    Log.e("Selected Day",sp_day);
//                    String day = "";
//
//                    String[] daysArr = bean.getWorkingdays().split(",");
//
//                    for(int i=0;i<daysArr.length;i++)
//                    {
//                        if(daysArr[i].equals(String.valueOf(0)))
//                        {
//                            day = "Sun";
//                        }
//                        if(daysArr[i].equals(String.valueOf(1)))
//                        {
//                            if(day.isEmpty())
//                            {
//                                day = "Mon";
//                            }
//                            else
//                            {
//                                day += ","+"Mon";
//                            }
//                        }if(daysArr[i].equals(String.valueOf(2)))
//                        {
//                            if(day.isEmpty())
//                            {
//                                day = "Tue";
//                            }
//                            else
//                            {
//                                day += ","+"Tue";
//                            }
//
//                        }if(daysArr[i].equals(String.valueOf(3)))
//                        {
//
//                            if(day.isEmpty())
//                            {
//                                day = "Wed";
//                            }
//                            else
//                            {
//                                day += ","+"Wed";
//                            }
//                        }if(daysArr[i].equals(String.valueOf(4)))
//                        {
//
//                            if(day.isEmpty())
//                            {
//                                day = "Thu";
//                            }
//                            else
//                            {
//                                day += ","+"Thu";
//                            }
//                        }if(daysArr[i].equals(String.valueOf(5)))
//                        {
//
//                            if(day.isEmpty())
//                            {
//                                day = "Fri";
//                            }
//                            else
//                            {
//                                day += ","+"Fri";
//                            }
//                        }if(daysArr[i].equals(String.valueOf(6)))
//                        {
//
//                            if(day.isEmpty())
//                            {
//                                day = "Sat";
//                            }
//                            else
//                            {
//                                day += ","+"Sat";
//                            }
//                        }
//                        if(bean.getWorkingdays().contains(""+date.getDay()))
//                        {
//                            isDayOk=true;
//                        }
//                        else {
//                            isDayOk=false;
//                        }
//
//                        Log.e("Day",day);
//                    }
                    if(bean.getWorkingdays().contains(""+date.getDay()))
                    {
                        isDayOk=true;
                    }
                    else
                    {
                        isDayOk=false;
                    }
                    if(!isDayOk)
                    {
                        reqdate="";
                        String[] weekdays = new DateFormatSymbols().getWeekdays();
                        Toast.makeText(CalenderActivity.this,bean.getName()+" don't work at "+weekdays[date.getDay()+1],Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.e("CalenderActivity",e.toString());
                }
            }
        });
//        right arrow of calender
        findViewById(R.id.ib_quotenext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CalendarView.currentDate.add(Calendar.MONTH, 1);
                quote_calview.updateCalendar(events,counts);
            }
        });
//       left arrow of calender
        findViewById(R.id.ib_quoteprev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarView.currentDate.add(Calendar.MONTH, -1);
                quote_calview.updateCalendar(events,counts);
            }
        });

        lat=SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.LATITUDE);
        lng=SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.LONGITUDE);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.tv_address:
                Intent intent = new Intent(this, MapActivityToPickAddress.class);
                intent.putExtra("lat",lat);
                intent.putExtra("lng",lng);
                intent.putExtra("address",tv_address.getText().toString().trim());
                startActivityForResult(intent,ADDRESSFETCH);
                break;
            case R.id.starttime:
                InputMethodManager imm1 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(view.getWindowToken(), 0);
                showTime(starttime);
                break;
            case R.id.endtime:
                InputMethodManager imm2 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(view.getWindowToken(), 0);
                showTime(endtime);
                break;
            case R.id.imageView_back_quote:
                finish();
                break;
            case R.id.root_relative_quote:
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                break;
            case R.id.button_Submit_quote:
                if(validateData())
                {
                    showFilterDialog();
                }
                break;
            case R.id.imageView_attachDocs_quote:
                if(arrayList.size()==7)
                {
                    Toast.makeText(CalenderActivity.this, "Not more than 7 Pictures can be attached", Toast.LENGTH_SHORT).show();
                }
                else {
                    showDialogForUpload();
                }
                break;
        }

    }

    private boolean validateData()
    {
        if (editText_title_quoteScreen.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Please enter Title", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (editText_description_quoteScreen.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Please enter Description", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (starttime.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Please enter opening time", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (endtime.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Please enter closing time", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (tv_address.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Please enter address", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(reqdate.isEmpty())
        {
            Toast.makeText(this, "Please select date for service.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else  if(!this.starttime.getText().toString().isEmpty())
        {
            int ok ;
            Date cloTime=  null,opTime = null;
            try {
                SimpleDateFormat s = new SimpleDateFormat("kk:mm");
                opTime = s.parse(this.starttime.getText().toString());
                cloTime = s.parse(this.endtime.getText().toString());

            } catch (ParseException e) {
                e.printStackTrace();
            }
            ok = (opTime != null) ? opTime.compareTo(cloTime) : 0;
            if (ok > 0) {
                Toast.makeText(this, "Open Timing should be before Close Timing.", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if(ok ==0)
            {
                Toast.makeText(this, "Open Timing can't be same as Close Timing.", Toast.LENGTH_SHORT).show();
                return false;
            }
            else
            {
                return true;
            }
        }

        else
        {
            return true;
        }
    }


    private boolean compareTime(String time)
    {
        int x=0, y=0;
        Date selecttime, openttime, endtime;
        try
        {
            SimpleDateFormat s=new SimpleDateFormat("kk:mm");
            selecttime=s.parse(time);
            openttime=s.parse(bean.getStart());
            endtime=s.parse(bean.getEnd());

            x=selecttime.compareTo(openttime);
            y=selecttime.compareTo(endtime);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if(x<0 || y>0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    //dialogs
    public void showTime(final TextView tv)
    {
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
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
                if(compareTime(hours + ":" + minutes))
                {
                    tv.setText(hours + ":" + minutes);
                }
                else
                {
                    Toast.makeText(CalenderActivity.this, bean.getName()+" is working from "+bean.getStart()+" to "+bean.getEnd()+".", Toast.LENGTH_SHORT).show();
                }
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.show();
    }
    private void showFilterDialog()
    {

       final AlertDialog.Builder alertbuilder_Filter;alertbuilder_Filter = new AlertDialog.Builder(this);
        alertbuilder_Filter.setCancelable(false);

        //  ALertDialog - title
        View view1=getLayoutInflater().inflate(R.layout.popup_title_filter_calender_activity_1, null);
        alertbuilder_Filter.setCustomTitle(view1);

        //  ALertDialog - content view
        View view2 = getLayoutInflater().inflate(R.layout.filter_option,null);
        // INVISIBLE CATEGORIES
        (view2.findViewById(R.id.radio1_twoWheeler)).setVisibility(View.GONE);
        (view2.findViewById(R.id.radio2_lightWeight)).setVisibility(View.GONE);
        (view2.findViewById(R.id.radio3_heavyWeight)).setVisibility(View.GONE);
        // Manipulating CATEGORY OPTIONS
        final String [] categoryArray = bean.getCategory().split(",");


        try {
            for (int i=0; i<categoryArray.length; i++)
            {
                if (categoryArray[i].equals("two")) {
                    (view2.findViewById(R.id.radio1_twoWheeler)).setVisibility(View.VISIBLE);
                }
                if (categoryArray[i].equals("light")) {
                    (view2.findViewById(R.id.radio2_lightWeight)).setVisibility(View.VISIBLE);
                }
                if (categoryArray[i].equals("heavy")) {
                    (view2.findViewById(R.id.radio3_heavyWeight)).setVisibility(View.VISIBLE);
                }
            }
        }catch (IndexOutOfBoundsException e)
        {}

        switch (category)
        {
            case "two":
                ((RadioButton)view2.findViewById(R.id.radio1_twoWheeler)).setChecked(true);
                break;
            case "light":
                ((RadioButton)view2.findViewById(R.id.radio2_lightWeight)).setChecked(true);
                break;
            case "heavy":
                ((RadioButton)view2.findViewById(R.id.radio3_heavyWeight)).setChecked(true);
                break;
        }

        final RadioGroup rdGrp_Filter = (RadioGroup) view2.findViewById(R.id.radioGroup_Filter);
        rdGrp_Filter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId)
                {
                    case R.id.radio1_twoWheeler:
                        category = "two";
                        break;
                    case R.id.radio2_lightWeight:
                        category = "light";
                        break;
                    case R.id.radio3_heavyWeight:
                        category = "heavy";
                        break;
                }
            }
        });
        alertbuilder_Filter.setView(view2);
        alertbuilder_Filter.setPositiveButton(getResources().getString(R.string.popup_done), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(!category.isEmpty())
                {
                    if(validateData())
                    {
                        requestServiceHit();
                    }
                }
            }
        });
        alertbuilder_Filter.setNegativeButton(getResources().getString(R.string.popup_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = alertbuilder_Filter.create();
        alertDialog.show();
        //Buttons
        Button positive_button =  alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negative_button =  alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        positive_button.setTextColor(getResources().getColor(R.color.blue));
        negative_button.setTextColor(getResources().getColor(R.color.black));
    }

    public void showDialogForUpload() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Upload Pictures");
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
        alertDialog.setCancelable(true);
        alertDialog.show();
        TextView msgTxt = (TextView) alertDialog.findViewById(android.R.id.message);
        FontBinding.setFont(msgTxt, "regular");

        //Buttons
        Button positive_button =  alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negative_button =  alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button neutral_btn =  alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);

        positive_button.setTextColor(getResources().getColor(R.color.blue));
        negative_button.setTextColor(getResources().getColor(R.color.blue));
        neutral_btn.setTextColor(getResources().getColor(R.color.black));
    }

    public  void removedocspopup(final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CalenderActivity.this);
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
    //services
    private void requestServiceHit() {
        try {

            MultipartEntityBuilder multipartbuilder = MultipartEntityBuilder.create();
            multipartbuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartbuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getApplicationContext()).getString(SPreferenceKey.TOKEN));
            multipartbuilder.addTextBody("service_id", bean.getId());
            multipartbuilder.addTextBody("category_name", category);
            multipartbuilder.addTextBody("date", reqdate);
            multipartbuilder.addTextBody("opentime", starttime.getText().toString());
            multipartbuilder.addTextBody("closetime", endtime.getText().toString());
            multipartbuilder.addTextBody("title", editText_title_quoteScreen.getText().toString());
            multipartbuilder.addTextBody("description", editText_description_quoteScreen.getText().toString());
            multipartbuilder.addTextBody("lat", lat);
            multipartbuilder.addTextBody("long", lng);
            multipartbuilder.addTextBody("address", tv_address.getText().toString());
            for(int i=0;i<arrayList.size();i++)
            {
                multipartbuilder.addBinaryBody("image["+i+"]" , new File(arrayList.get(i)));
            }
            ServiceBean serviceBean = new ServiceBean();
            serviceBean.setMethodName("Consumers/request_service");
            serviceBean.setActivity(CalenderActivity.this);
            serviceBean.setApilistener(this);


            CustomHandler customHandler = new CustomHandler(serviceBean);
            customHandler.makeMultipartRequest(multipartbuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void myServerResponse(JSONObject jsonresponse) {

        try
        {
            if(jsonresponse!=null)
            {
                if(jsonresponse.getString("status").equalsIgnoreCase("SUCCESS") )
                {
                   if(jsonresponse.getString("requestKey").equalsIgnoreCase("request_service"))
                   {

                       SubmitQuoteFragment submitQuoteDialogFrag = new SubmitQuoteFragment();
                       submitQuoteDialogFrag.show(getSupportFragmentManager(),"submitQuote");
                   }
                }
                else
                {
                }
                Log.e("JSONResponse", jsonresponse.toString());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed()
    {
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if (requestCode == RESULT_LOAD_IMAGE || requestCode == CAMERA_REQUEST)
            {
                String picpath=data.getStringExtra("filePath");
                if(picpath!=null)
                {
                    if (arrayList.size() < 8)
                    {
                        arrayList.add(picpath);
                        documentsAdapter.setArrayList(arrayList);
                    }
                }
                else
                {
                    picpath="";
                }
                Log.i("File Path", "" + picpath);
            }
            if (requestCode == ADDRESSFETCH)
            {
                if(data!=null)
                {
                    tv_address.setText(data.getStringExtra("address"));
                    lat=data.getStringExtra("lat");
                    lng=data.getStringExtra("long");
                }
            }
        }
    }


}


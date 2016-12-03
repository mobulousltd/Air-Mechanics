package mobulous12.airmechanics.customer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.customer.activities.CalenderActivity;
import mobulous12.airmechanics.databinding.CalItemBinding;
import mobulous12.airmechanics.utils.CalendarCollection;

/**
 * Created by mobulous11 on 12/10/16.
 */

public class CalendarAdapter extends BaseAdapter {
    private Context context;

    private Calendar month;
    public GregorianCalendar pmonth;
    /**
     * calendar instance for previous month for getting complete view
     */
    public GregorianCalendar pmonthmaxset;
    private GregorianCalendar selectedDate;
    int firstDay;
    int maxWeeknumber;
    int maxP;
    int calMaxP;
    int lastWeekDay;
    int leftDays;
    int mnthlength;
    String itemvalue, curentDateString;
    SimpleDateFormat df;

    private ArrayList<String> items;
    public static List<String> day_string;
    private View previousView;
    public ArrayList<CalendarCollection>  date_collection_arr;
    private OnDateSelected selected;
    private TextView last_textView;
    private TextView selected_day;


    public CalendarAdapter(Context context, GregorianCalendar monthCalendar,ArrayList<CalendarCollection> date_collection_arr) {
        this.date_collection_arr=date_collection_arr;
        CalendarAdapter.day_string = new ArrayList<String>();
        Locale.setDefault(Locale.US);
        month = monthCalendar;
        selectedDate = (GregorianCalendar) monthCalendar.clone();
        this.context = context;
        month.set(GregorianCalendar.DAY_OF_MONTH, 1);

        this.items = new ArrayList<String>();
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        curentDateString = df.format(selectedDate.getTime());
        refreshDays();

    }

    public void setItems(ArrayList<String> items) {
        for (int i = 0; i != items.size(); i++) {
            if (items.get(i).length() == 1) {
                items.set(i, "0" + items.get(i));
            }
        }
        this.items = items;
    }

    public int getCount() {
        return day_string.size();
    }

    public Object getItem(int position) {
        return day_string.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new view for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final TextView dayView;

        if (convertView == null) { // if it's not recycled, initialize some
            // attributes
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            v = inflater.inflate(R.layout.cal_item, null);
            CalItemBinding binding=DataBindingUtil.inflate(inflater, R.layout.cal_item, parent, false);
            v=binding.getRoot();
        }


        dayView = (TextView) v.findViewById(R.id.date);
        String[] separatedTime = day_string.get(position).split("-");


        String gridvalue = separatedTime[2].replaceFirst("^0*", "");
        if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
            dayView.setTextColor(Color.GRAY);
            dayView.setClickable(false);
            dayView.setFocusable(false);
            ((LinearLayout)dayView.getParent().getParent()).setTag("no");
        } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
            dayView.setTextColor(Color.GRAY);
            dayView.setClickable(false);
            dayView.setFocusable(false);
            ((LinearLayout)dayView.getParent().getParent()).setTag("no");

        } else {
            // setting curent month's days in blue color.
//            dayView.setTextColor(Color.BLACK);
            final View finalV = v;
            final TextView prevDayView;
            final View prevView;
            dayView.setTextColor(context.getResources().getColor(R.color.text_color));
            ((LinearLayout)dayView.getParent().getParent()).setTag("yes");

        }


        if (day_string.get(position).equals(curentDateString)) {

//            v.setBackgroundColor(context.getResources().getColor(R.color.dodgerblue));
            v.setBackgroundResource(R.drawable.filled);
        } else {
            v.setBackgroundResource(0);
        }


        dayView.setText(gridvalue);

        // create date string for comparison
        String date = day_string.get(position);

        if (date.length() == 1) {
            date = "0" + date;
        }
        String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
        if (monthStr.length() == 1) {
            monthStr = "0" + monthStr;
        }

        // show icon if date is not empty and it exists in the items array
        /*ImageView iw = (ImageView) v.findViewById(R.id.date_icon);
        if (date.length() > 0 && items != null && items.contains(date)) {
            iw.setVisibility(View.VISIBLE);
        } else {
            iw.setVisibility(View.GONE);
        }
        */

        setEventView(v, position,dayView);

        return v;
    }

    public View setSelected(View view,int pos) {
        if (previousView != null) {
            previousView.setBackgroundColor(Color.WHITE);
//            previousView.setBackgroundResource(R.drawable.whitecircle_selector);
        }

//        view.setBackgroundColor(context.getResources().getColor(R.color.dodgerblue));
        view.setBackgroundResource(R.drawable.filled);

        try {
            int len = day_string.size();
            if (len > pos) {
                if (day_string.get(pos).equals(curentDateString)) {

                } else {

                    previousView = view;

                }

            }

            String[] s = day_string.get(pos).split("-");
            if (view.getTag() != null && view.getTag().equals("yes")) {
                setDayColor(s[2], (TextView) view.findViewById(R.id.date));
            }


            if (selected != null) {

                selected.onDateSelected(s[2]);
            }
        }
        catch (Exception e)
        {

        }


        return view;
    }

    private void setDayColor(final String day,final TextView v){
        if(last_textView!=null){
            last_textView.setBackgroundResource(0);
            last_textView.setTextColor(context.getResources().getColor(R.color.dodgerblue));
        }
        if(selected_day!=null){
            selected_day.setTextColor(context.getResources().getColor(R.color.black));

        }
        int d=Integer.parseInt(day)+firstDay;
        int j=0;
        if(d>7){

           int i= d%7;
            if(i-1<1) {
                j = 7 + (i - 1);



            }else {
                j=i-1;

            }

        }
        else{
           j=d-1;

        }

        if(v!=null){
            v.setTextColor(context.getResources().getColor(R.color.white));
            selected_day=v;
        }
        last_textView=((TextView) CalenderActivity.tv_hashMap.get(j));
//        ((TextView)CalenderActivity.tv_hashMap.get(j)).setBackgroundResource(R.color.dodgerblue);
        ((TextView)CalenderActivity.tv_hashMap.get(j)).setBackgroundResource(R.drawable.blue_day_selector);
        ((TextView)CalenderActivity.tv_hashMap.get(j)).setTextColor(context.getResources().getColor(R.color.white));

    }

    public void refreshDays() {
        // clear items
        items.clear();
        day_string.clear();
        Locale.setDefault(Locale.US);
        pmonth = (GregorianCalendar) month.clone();
        // month start day. ie; sun, mon, etc
        firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
        // finding number of weeks in current month.
        maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        // allocating maximum row number for the gridview.
        mnthlength = maxWeeknumber * 7;
        maxP = getMaxP(); // previous month maximum day 31,30....
        calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25 ...
        /**
         * Calendar instance for getting a complete gridview including the three
         * month's (previous,current,next) dates.
         */
        pmonthmaxset = (GregorianCalendar) pmonth.clone();
        /**
         * setting the start date as previous month's required date.
         */
        pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

        /**
         * filling calendar gridview.
         */
        for (int n = 0; n < mnthlength; n++) {

            itemvalue = df.format(pmonthmaxset.getTime());
            pmonthmaxset.add(GregorianCalendar.DATE, 1);
            day_string.add(itemvalue);

        }
        if(last_textView!=null){
            last_textView.setBackgroundResource(R.color.white);
            last_textView.setTextColor(context.getResources().getColor(R.color.dodgerblue));
        }
        if(selected_day!=null){
            selected_day.setTextColor(context.getResources().getColor(R.color.black));
        }
    }

    private int getMaxP() {
        int maxP;
//        if (month.get(GregorianCalendar.MONTH) == month.getActualMinimum(GregorianCalendar.MONTH)) {
//            pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
//                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
//        }
        if (month.get(GregorianCalendar.MONTH) == Calendar.SUNDAY) {
            pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        }
        else {
            pmonth.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
        maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        return maxP;
    }




    public void setEventView(View v,int pos,TextView txt){

        int len=CalendarCollection.date_collection_arr.size();
        for (int i = 0; i < len; i++) {
            CalendarCollection cal_obj=CalendarCollection.date_collection_arr.get(i);
            String date=cal_obj.date;
            int len1=day_string.size();
            if (len1>pos) {

                if (day_string.get(pos).equals(date)) {
                    v.setBackgroundColor(Color.parseColor("#343434"));
//                    v.setBackgroundResource(R.drawable.rounded_calender_item);

                    txt.setTextColor(Color.WHITE);
                }
            }}



    }


    public void getPositionList(String date, final Activity act){

        int len=CalendarCollection.date_collection_arr.size();
        for (int i = 0; i < len; i++) {
            CalendarCollection cal_collection=CalendarCollection.date_collection_arr.get(i);
            String event_date=cal_collection.date;

            String event_message=cal_collection.event_message;

            if (date.equals(event_date)) {

                Toast.makeText(context, "You have event on this date: "+event_date, Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Date: "+event_date)
                        .setMessage("Event: "+event_message)
                        .setPositiveButton("OK",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which)
                            {
                                act.finish();
                            }
                        }).show();
                break;
            }else{


            }}



    }

    public void getSelectedDate(OnDateSelected selected){
        this.selected=selected;

    }

    public interface OnDateSelected{
        void onDateSelected(String month_date);
    }
}

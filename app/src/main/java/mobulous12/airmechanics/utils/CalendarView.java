package mobulous12.airmechanics.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import mobulous12.airmechanics.R;

public class CalendarView extends LinearLayout
{
    TextView textView, currenttv, lastdaytv;Date today;
    private HashSet<Date> eventDay;
    private HashMap<Integer, TextView> dayhshmap=new HashMap<>();
    // for logging
    private static final String LOGTAG = "Calendar View";

    // how many days to show, defaults to six weeks, 42 days
    private static int DAYS_COUNT = 42;

    // default date format
    private static final String DATE_FORMAT = "MMM yyyy";

    // date format
    private String dateFormat;

    // current displayed month
    public static Calendar currentDate = Calendar.getInstance();

    //event handling
    private EventHandler eventHandler = null;

    // internal components
    public ImageView btnPrev;
    public ImageView btnNext;
    public TextView txtDate;
    private ExpandableGridView grid;


    // month-season association (northern hemisphere, sorry australia :)
    int[] monthSeason = new int[] {2, 2, 3, 3, 3, 0, 0, 0, 1, 1, 1, 2};

    public CalendarView(Context context)
    {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initControl(context, attrs);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    /**
     * Load control xml layout
     */
    private void initControl(Context context, AttributeSet attrs)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_calendar, this);
        loadDateFormat(attrs);
        assignUiElements();
        assignClickHandlers();

        updateCalendar();
    }

    private void loadDateFormat(AttributeSet attrs)
    {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

        try
        {
            // try to load provided date format, and fallback to default otherwise
            dateFormat = ta.getString(R.styleable.CalendarView_dateFormat);
            if (dateFormat == null)
                dateFormat = DATE_FORMAT;
        }
        finally
        {
            ta.recycle();
        }
    }
    private void assignUiElements()
    {
        // layout is inflated, assign local variables to components
//		btnPrev = (ImageView)findViewById(R.id.prev_button);
//		btnNext = (ImageView)findViewById(R.id.next_button);
        dayhshmap.put(0, (TextView)findViewById(R.id.TextView_sun));
        dayhshmap.put(1, (TextView)findViewById(R.id.TextView_mon));
        dayhshmap.put(2, (TextView)findViewById(R.id.TextView_tue));
        dayhshmap.put(3, (TextView)findViewById(R.id.TextView_wed));
        dayhshmap.put(4, (TextView)findViewById(R.id.TextView_thur));
        dayhshmap.put(5, (TextView)findViewById(R.id.TextView_fri));
        dayhshmap.put(6, (TextView)findViewById(R.id.TextView_sat));

        txtDate = (TextView)findViewById(R.id.calendar_date_display);
        grid = (ExpandableGridView) findViewById(R.id.calendar_grid);
        grid.setExpanded(true);

    }

    private void assignClickHandlers()
    {
        // add one month and refresh UI
//		btnNext.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				currentDate.add(Calendar.MONTH, 1);
//				updateCalendar();
//			}
//		});
//
//		// subtract one month and refresh UI
//		btnPrev.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				currentDate.add(Calendar.MONTH, -1);
//				updateCalendar();
//			}
//		});

        // long-pressing a day
		/*grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> view, View cell, int position, long id)
			{
				// handle long-press
				if (eventHandler == null)
					return false;

				eventHandler.onDayLongPress((Date)view.getItemAtPosition(position));
				return true;
			}
		});*/
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(eventDay==null)
                {
                    try
                    {
                        SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd");
                        String reqdate=s.format((Date)parent.getItemAtPosition(position));
                        Date bookdate=s.parse(reqdate);
                        reqdate=s.format(new Date());
                        Date todaydate=s.parse(reqdate);
                        if(bookdate.compareTo(todaydate)>=0)
                        {
                            if(textView!=null && currenttv!=null)
                            {
                                if(!currenttv.equals(textView))
                                {
                                    textView.setBackgroundResource(0);
                                    textView.setTextColor(getResources().getColor(R.color.black));
                                }
                                else
                                {
                                    textView.setBackgroundResource(R.drawable.selected_circle_days);
                                    textView.setTextColor(getResources().getColor(R.color.white));
                                }
                            }
                            TextView tv=(TextView)view.findViewById(R.id.date);
                            textView=tv;
                            if(!tv.getText().toString().isEmpty())
                            {
                                tv.setBackgroundResource(R.drawable.filled);
                                tv.setTextColor(getResources().getColor(R.color.white));
                            }

                            eventHandler.onDayLongPress((Date)parent.getItemAtPosition(position));
                        }
                    }
                    catch (Exception e) {
                        Log.e("CalenderView",e.toString());
                    }
                }
                else
                {
                    eventHandler.onDayLongPress((Date)parent.getItemAtPosition(position));
                }
            }
        });

    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar()
    {
        updateCalendar(null,null);
    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar(HashSet<Date> events,HashMap<Date,String> count)
    {
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar)currentDate.clone();
        Calendar currentDate1 = Calendar.getInstance();
        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // move calendar backwards to the beginning of the week


        // fill cells
        if(calendar.get(Calendar.MONTH) + 1 == 1 || calendar.get(Calendar.MONTH) + 1 == 3 || calendar.get(Calendar.MONTH) + 1 == 5
                || calendar.get(Calendar.MONTH) + 1 == 7 || calendar.get(Calendar.MONTH) + 1 == 8
                || calendar.get(Calendar.MONTH) + 1 == 10 || calendar.get(Calendar.MONTH) + 1 == 12){
            DAYS_COUNT=31;
        }
        else if(calendar.get(Calendar.MONTH)+1== 4 || calendar.get(Calendar.MONTH)+1==6 ||
                calendar.get(Calendar.MONTH)+1==9 || calendar.get(Calendar.MONTH)+1==11 ){
            DAYS_COUNT=30;
        }
        else {
            if(calendar.get(Calendar.YEAR)%4==0)
                DAYS_COUNT=29;
            else
                DAYS_COUNT=28;
        }
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);
        while (cells.size() < DAYS_COUNT+monthBeginningCell)
        {
            cells.add(calendar.getTime());

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        // update grid
        grid.setAdapter(new CalendarAdapter(getContext(), cells, events,count));

        // update title
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        txtDate.setText(sdf.format(currentDate.getTime()));
        if(lastdaytv!=null)
        {
            lastdaytv.setBackgroundResource(0);
            lastdaytv.setTextColor(getResources().getColor(R.color.home_header_color));
        }
    }


    private class CalendarAdapter extends ArrayAdapter<Date>
    {
        // days with events
        private HashSet<Date> eventDays;
        HashMap<Date,String> countHashSet;
        // for view inflation
        private LayoutInflater inflater;
        TextView tv,count;


        public CalendarAdapter(Context context, ArrayList<Date> days, HashSet<Date> eventDays, HashMap<Date,String> countHashSet)
        {
            super(context, R.layout.cal_item, days);
            this.eventDays = eventDays;
            eventDay=eventDays;
			this.countHashSet=countHashSet;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent)
        {
            // day in question
            Date date = getItem(position);
            int day = date.getDate();
            int month = date.getMonth();
            int year = date.getYear();

            // today
            today = new Date();
            int i=0;
            // inflate item if it does not exist yet
            if (view == null)
                view = inflater.inflate(R.layout.cal_item, parent, false);
            tv=(TextView)view.findViewById(R.id.date) ;
			count=(TextView)view.findViewById(R.id.count) ;
            count.setVisibility(GONE);
            // if this day has an event, specify event image
            view.setBackgroundResource(0);


            // clear styling
            //((TextView)view).setTypeface(null, Typeface.NORMAL);

            if(position==0 && (day==25|| day==26 ||day==27 ||day==28 ||day==29 ||day==30 ||day==31)){
                tv.setText("");
            } else if(position==1 && ( day==26 ||day==27 ||day==28 ||day==29 ||day==30 ||day==31)){
                tv.setText("");
            } else if(position==2 && (day==27 ||day==28 ||day==29 ||day==30 ||day==31)){
                tv.setText("");
            }else if(position==3 && (day==28|| day==29 ||day==30 ||day==31)){
                tv.setText("");
            }else if(position==4 && (day==29 || day==30 ||day==31)){
                tv.setText("");
            }else if(position==5 && (day==30 || day==31)){
                tv.setText("");
            }else
            {
                tv.setText(String.valueOf(date.getDate()));
                if (day == today.getDate() && month == today.getMonth() && year==today.getYear())
                {
                    currenttv=tv;
                    tv.setBackgroundResource(R.drawable.selected_circle_days);
                    tv.setTextColor(getResources().getColor(R.color.white));
                    lastdaytv=dayhshmap.get(today.getDay());
                    lastdaytv.setBackgroundResource(R.drawable.blue_day_selector);
                    lastdaytv.setTextColor(getResources().getColor(R.color.white));
                }
                else
                {
                    tv.setBackgroundResource(0);
                    tv.setTextColor(getResources().getColor(R.color.black));
                }

                if (eventDays != null)
                {
                    for (Date eventDate : eventDays)
                    {
                        if (eventDate.getDate() == day && eventDate.getMonth() == month &&
                                eventDate.getYear() == year)
                        {
                            // mark this day for event
                            tv.setBackgroundResource(R.drawable.fill);
                            tv.setTextColor(getResources().getColor(R.color.black));
							count.setText(countHashSet.get(eventDate));
							count.setVisibility(VISIBLE);
                            break;
                        }

                    }
                }

            }





            //((TextView)view).setText(String.valueOf(date.getDate()));

            return view;
        }
    }

    /**
     * Assign event handler to be passed needed events
     */
    public void setEventHandler(EventHandler eventHandler)
    {
        this.eventHandler = eventHandler;
    }

    /**
     * This interface defines what events to be reported to
     * the outside world
     */
    public interface EventHandler
    {
        void onDayLongPress(Date date);
    }
}
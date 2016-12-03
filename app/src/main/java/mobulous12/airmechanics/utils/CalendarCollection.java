package mobulous12.airmechanics.utils;

import java.util.ArrayList;

/**
 * Created by mobulous11 on 12/10/16.
 */

public class CalendarCollection {

        public String date="";
        public String event_message="";

        public static ArrayList<CalendarCollection> date_collection_arr = new ArrayList<>(1);
        public CalendarCollection(String date,String event_message)
        {
            this.date=date;
            this.event_message=event_message;
        }

}

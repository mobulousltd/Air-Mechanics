package mobulous12.airmechanics.fonts;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import mobulous12.airmechanics.customer.activities.HomeActivity;

/**
 * Created by mobulous12 on 4/10/16.
 */

public class Font
{
    static Typeface face;

    public static void setFontTextView(TextView tv1, Activity activity)
    {
        face = Typeface.createFromAsset(activity.getAssets(),
                "fonts/Raleway-Regular.ttf");
        tv1.setTypeface(face);
    }

    public static void setFontTextViewBold(TextView tv1, Activity activity)
    {
        face = Typeface.createFromAsset(activity.getAssets(),
                "fonts/Raleway-SemiBold_0.ttf");
        tv1.setTypeface(face);
    }
    public static void setFontButton(Button button, Activity activity)
    {
        face = Typeface.createFromAsset(activity.getAssets(),
                "fonts/Raleway-SemiBold_0.ttf");
        button.setTypeface(face);
    }

    public static void setFontHeader(TextView tv2, Activity activity)
    {
        face = Typeface.createFromAsset(activity.getAssets(),
                "fonts/Raleway-Medium_0.ttf");
        tv2.setTypeface(face);
    }

    public static void setFontRobotoRegular(TextView tv2, Activity activity)
    {
        face = Typeface.createFromAsset(activity.getAssets(),
                "fonts/Roboto-Regular_0.ttf");
        tv2.setTypeface(face);
    }

    public static void setFontRalewayLight(TextView tv2, Activity activity)
    {
        face = Typeface.createFromAsset(activity.getAssets(),
                "fonts/Raleway-Light_0.ttf");
        tv2.setTypeface(face);
    }


//    public static void setFontToolbar(Toolbar toolbar, Activity activity)
//    {
//        face = Typeface.createFromAsset(activity.getAssets(),
//                "fonts/Raleway-Medium_0.ttf");
//        TextView tv2 =  HomeActivity.getActionBarTextView(toolbar);
//        tv2.setTypeface(face);
//
//    }
}

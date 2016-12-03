package mobulous12.airmechanics.customer.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.ServiceProviderBean;
import mobulous12.airmechanics.databinding.HomeScreenInfoWindowBinding;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.utils.CircularImageView;

/**
 * Created by mobulous11 on 6/10/16.
 */

public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private View customWindow = null;
    LayoutInflater layoutInflater;
    private Context context;
    HashMap<Marker, ServiceProviderBean> sphashmap;

    public MyInfoWindowAdapter(Context context, HashMap<Marker, ServiceProviderBean> sphashmap)
    {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.sphashmap=sphashmap;
    }

    @Override
    public View getInfoWindow(Marker marker)
    {
        HomeScreenInfoWindowBinding binding=DataBindingUtil.inflate(layoutInflater, R.layout.home_screen_info_window, null, false);
        customWindow=binding.getRoot();

        ServiceProviderBean bean=sphashmap.get(marker);
        TextView title = (TextView) customWindow.findViewById(R.id.title_customInfoWindow);
        TextView content = (TextView) customWindow.findViewById(R.id.tv_category);
        TextView location = (TextView) customWindow.findViewById(R.id.tv_location);
        CircularImageView profileimage=(CircularImageView)customWindow.findViewById(R.id.circularImageView_profile_pic_info_window);
        AQuery aquery=new AQuery(profileimage);
        if(bean.getProfile_thumb().isEmpty())
        {
            aquery.id(profileimage).image(R.drawable.default_profile_pic);
        }
        else
        {
            aquery.id(profileimage).image(bean.getProfile_thumb());
        }

        title.setText(bean.getName());
        location.setText(bean.getAddress());

        String cat="";
        if(bean.getCategory().contains("two"))
        {
            cat=context.getString(R.string.two_wheeler);
        }
        if(bean.getCategory().contains("light"))
        {
            if(cat.isEmpty())
            {
                cat=context.getString(R.string.light_weight_vehicle);
            }
            else
            {
                cat+=", "+context.getString(R.string.light_weight_vehicle);
            }
        }
        if(bean.getCategory().contains("heavy"))
        {
            if(cat.isEmpty())
            {
                cat=context.getString(R.string.heavy_weight_vehicle);
            }
            else
            {
                cat+=", "+context.getString(R.string.heavy_weight_vehicle);
            }
        }
        ImageView star1 = (ImageView) customWindow.findViewById(R.id.star1);
        ImageView star2 = (ImageView) customWindow.findViewById(R.id.star2);
        ImageView star3 = (ImageView) customWindow.findViewById(R.id.star3);
        ImageView star4 = (ImageView) customWindow.findViewById(R.id.star4);
        ImageView star5 = (ImageView) customWindow.findViewById(R.id.star5);
        if(!cat.isEmpty())
        {
            content.setText(cat);
        }
        int star=(int)Float.parseFloat(bean.getReviews());
        switch (Math.round(Float.parseFloat(bean.getReviews())))
        {
            case 1:
                star1.setImageResource(R.drawable.yellow_star);
                break;
            case 2:
                star1.setImageResource(R.drawable.yellow_star);
                star2.setImageResource(R.drawable.yellow_star);
                break;
            case 3:
                star1.setImageResource(R.drawable.yellow_star);
                star2.setImageResource(R.drawable.yellow_star);
                star3.setImageResource(R.drawable.yellow_star);
                break;
            case 4:
                star1.setImageResource(R.drawable.yellow_star);
                star2.setImageResource(R.drawable.yellow_star);
                star3.setImageResource(R.drawable.yellow_star);
                star4.setImageResource(R.drawable.yellow_star);
                break;
            case 5:
                star1.setImageResource(R.drawable.yellow_star);
                star2.setImageResource(R.drawable.yellow_star);
                star3.setImageResource(R.drawable.yellow_star);
                star4.setImageResource(R.drawable.yellow_star);
                star5.setImageResource(R.drawable.yellow_star);
                break;

        }
        return customWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
//        // Setting Content of Info Window Here...
//
//        // Inflate custom layout
//        customWindow = layoutInflater.inflate(R.layout.home_screen_info_window, null);
//
//
//        /*dynamic width and height of android device*/
//
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        WindowManager windowmanager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
//        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
//        int deviceWidth = displayMetrics.widthPixels;
//        int deviceHeight = displayMetrics.heightPixels;
//
//
//        // Set desired height and width
////        customWindow.setLayoutParams(new RelativeLayout.LayoutParams(deviceWidth-200, RelativeLayout.LayoutParams.WRAP_CONTENT));
//
//
//        TextView title = (TextView) customWindow.findViewById(R.id.title_customInfoWindow);
//        TextView content = (TextView) customWindow.findViewById(R.id.textView_info_window_detail);
//        TextView location = (TextView) customWindow.findViewById(R.id.textView_info_window_location);
//
//        Font.setFontTextView(title, (AppCompatActivity)context);
//        Font.setFontTextView(content, (AppCompatActivity)context);
//        Font.setFontTextView(location, (AppCompatActivity)context);

            return null;
    }

}



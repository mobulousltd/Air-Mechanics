package mobulous12.airmechanics.customer.fragments;


import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONObject;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.BookingBean;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.databinding.RateServiceProviderScreenBinding;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

/**
 * A simple {@link Fragment} subclass.
 */
public class RateScreenFragment extends Fragment implements View.OnClickListener, ApiListener {

    private View view;
    String star="0";
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageView star4;
    private ImageView star5;
    private BookingBean bookingBean;
    private EditText et_review;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.blank_at_right_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_show_service_provider).setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        RateServiceProviderScreenBinding binding=DataBindingUtil.inflate(inflater, R.layout.rate_service_provider_screen, container, false);
        view=binding.getRoot();

        bookingBean = getArguments().getParcelable("bookingBean");

        ((HomeActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.headername_rate_service));
        ((HomeActivity)getActivity()).setNavigationIcon();
        et_review = (EditText) view.findViewById(R.id.edit_writeReview_rateScreen);
//        RatingBar ratingBar = (RatingBar)view.findViewById(R.id.rating);
//        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            public void onRatingChanged(RatingBar ratingBar, float rating,  boolean fromUser) {
//                Toast.makeText(getActivity(), ""+String.valueOf(rating), Toast.LENGTH_SHORT).show();
//            }
//        });

        star1 = (ImageView) view.findViewById(R.id.imageView_star1_rateScreen);
        star2 = (ImageView) view.findViewById(R.id.imageView_star2_rateScreen);
        star3 = (ImageView) view.findViewById(R.id.imageView_star3_rateScreen);
        star4 = (ImageView) view.findViewById(R.id.imageView_star4_rateScreen);
        star5 = (ImageView) view.findViewById(R.id.imageView_star5_rateScreen);

        star1.setOnClickListener(this);
        star2.setOnClickListener(this);
        star3.setOnClickListener(this);
        star4.setOnClickListener(this);
        star5.setOnClickListener(this);
        view.findViewById(R.id.button_submit_Rate).setOnClickListener(this);
        AQuery aQuery=new AQuery(view.findViewById(R.id.circularImageView_rate_screen));
        aQuery.id(view.findViewById(R.id.circularImageView_rate_screen)).image(bookingBean.getUserImage());
        ((TextView)view.findViewById(R.id.textView_name_rateScreen)).setText(bookingBean.getUserName());
        ((TextView)view.findViewById(R.id.textView_address_rateScreen)).setText(bookingBean.getUseraddress());

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imageView_star1_rateScreen:

                star="1";
                star1.setImageResource(R.drawable.yellow_star);
                star2.setImageResource(R.drawable.gray_star);
                star3.setImageResource(R.drawable.gray_star);
                star4.setImageResource(R.drawable.gray_star);
                star5.setImageResource(R.drawable.gray_star);

                break;
            case R.id.imageView_star2_rateScreen:
                star="2";
                star1.setImageResource(R.drawable.yellow_star);
                star2.setImageResource(R.drawable.yellow_star);
                star3.setImageResource(R.drawable.gray_star);
                star4.setImageResource(R.drawable.gray_star);
                star5.setImageResource(R.drawable.gray_star);
                break;
            case R.id.imageView_star3_rateScreen:
                star="3";
                star1.setImageResource(R.drawable.yellow_star);
                star2.setImageResource(R.drawable.yellow_star);
                star3.setImageResource(R.drawable.yellow_star);
                star4.setImageResource(R.drawable.gray_star);
                star5.setImageResource(R.drawable.gray_star);
                break;
            case R.id.imageView_star4_rateScreen:
                star="4";
                star1.setImageResource(R.drawable.yellow_star);
                star2.setImageResource(R.drawable.yellow_star);
                star3.setImageResource(R.drawable.yellow_star);
                star4.setImageResource(R.drawable.yellow_star);
                star5.setImageResource(R.drawable.gray_star);
                break;
            case R.id.imageView_star5_rateScreen:
                star="5";
                star1.setImageResource(R.drawable.yellow_star);
                star2.setImageResource(R.drawable.yellow_star);
                star3.setImageResource(R.drawable.yellow_star);
                star4.setImageResource(R.drawable.yellow_star);
                star5.setImageResource(R.drawable.yellow_star);
                break;
            case R.id.button_submit_Rate:
                if(!star.isEmpty())
                {
                    if(!et_review.getText().toString().isEmpty())
                    {
                        rateServiceProService();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Please write your reviews for this service.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "Please give ratings for this service.", Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }

    private void rateServiceProService()
    {

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        multipartEntityBuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getString(SPreferenceKey.TOKEN));
        multipartEntityBuilder.addTextBody("service_id", bookingBean.getServiceproviderid());
        multipartEntityBuilder.addTextBody("request_id", bookingBean.getBookingid());
        multipartEntityBuilder.addTextBody("workRating",star);
        multipartEntityBuilder.addTextBody("review",et_review.getText().toString().trim());

        ServiceBean bean = new ServiceBean();
        bean.setActivity(getActivity());
        bean.setIsLoader(true);
        bean.setMethodName("Consumers/rate_serviceProvider");
        bean.setApilistener(this);


        CustomHandler customHandler = new CustomHandler(bean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);

    }

    @Override
    public void myServerResponse(JSONObject jsonObject)
    {
        if (jsonObject != null) {
            try
            {
                if ((jsonObject.getString("status").equalsIgnoreCase("SUCCESS")) && (jsonObject.getString("requestKey").equalsIgnoreCase("rate_serviceProvider")))
                {

                    int count=((HomeActivity)getActivity()).getSupportFragmentManager().getBackStackEntryCount();
                    for (int i=0;i<count;i++)
                    {
                        ((HomeActivity)getActivity()).getSupportFragmentManager().popBackStack();
                    }
                    Toast.makeText(getActivity(),"Ratings & Reviews Submitted Successfully!",Toast.LENGTH_SHORT).show();

                }
                else
                {

                }
                Log.e("Json Response", jsonObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


}

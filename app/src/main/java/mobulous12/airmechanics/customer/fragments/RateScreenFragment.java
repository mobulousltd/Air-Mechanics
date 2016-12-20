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
import android.widget.TextView;
import android.widget.Toast;

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
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageView star4;
    private ImageView star5;
    private boolean isStar1 = true;
    private boolean isStar2 = true;
    private boolean isStar3 = true;
    private boolean isStar4 = true;
    private boolean isStar5 = true;
    private BookingBean bookingBean;
    private EditText et_review;

    public RateScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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


        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imageView_star1_rateScreen:
                if (isStar1)
                {
                    star1.setImageResource(R.drawable.star);
                    isStar1 = false;
                }
                else
                {
                    star1.setImageResource(R.drawable.star_gray);
                    isStar1 = true;
                }
                break;
            case R.id.imageView_star2_rateScreen:
                if (isStar2)
                {
                    star2.setImageResource(R.drawable.star);
                    isStar2 = false;
                }
                else
                {
                    star2.setImageResource(R.drawable.star_gray);
                    isStar2 = true;
                }
                break;
            case R.id.imageView_star3_rateScreen:
                if (isStar3)
                {
                    star3.setImageResource(R.drawable.star);
                    isStar3 = false;
                }
                else
                {
                    star3.setImageResource(R.drawable.star_gray);
                    isStar3 = true;
                }
                break;
            case R.id.imageView_star4_rateScreen:
                if (isStar4)
                {
                    star4.setImageResource(R.drawable.star);
                    isStar4 = false;
                }
                else
                {
                    star4.setImageResource(R.drawable.star_gray);
                    isStar4 = true;
                }
                break;
            case R.id.imageView_star5_rateScreen:
                if (isStar5)
                {
                    star5.setImageResource(R.drawable.star);
                    isStar5 = false;
                }
                else
                {
                    star5.setImageResource(R.drawable.star_gray);
                    isStar5 = true;
                }
                break;
            case R.id.button_submit_Rate:
                rateServiceProService();
                int count=((HomeActivity)getActivity()).getSupportFragmentManager().getBackStackEntryCount();
                for (int i=0;i<count;i++)
                {
                    ((HomeActivity)getActivity()).getSupportFragmentManager().popBackStack();
                }
                Toast.makeText(getActivity(),"Ratings & Reviews Submitted Successfully!",Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void rateServiceProService()
    {

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        multipartEntityBuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getString(SPreferenceKey.TOKEN));
        multipartEntityBuilder.addTextBody("service_id", "");
        multipartEntityBuilder.addTextBody("request_id", bookingBean.getBookingid());
        multipartEntityBuilder.addTextBody("workRating","");
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

                    JSONObject response = jsonObject.getJSONObject("response");
                    String reqstId = response.getString("request_id");
                    String serviceId = response.getString("service_id");

                    Log.e("J_RESPONSE", jsonObject.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

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

}

package mobulous12.airmechanics.customer.fragments;


import android.databinding.DataBindingUtil;
import android.media.Image;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.BookingBean;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.databinding.BillPaymentBinding;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillPaymentFragment extends Fragment implements View.OnClickListener, ApiListener, CompoundButton.OnCheckedChangeListener {


    private RelativeLayout rootTypeOfService;
    private RelativeLayout rootDescription;
    private RelativeLayout rootTotalPrice;


    private ImageView imgTypeOfService;
    private ImageView imgDescription;
    private ImageView imgTotalPrice;

    private boolean isTypeOfServiceOpen = true;
    private boolean isDescriptionOpen = true;
    private boolean isTotalPriceOpen = true;

    private TextView textViewTypeOfService;
    private TextView textViewDescription;
    private TextView textViewTotalPrice;

    private TextView textViewTypeOfServiceDynamic;
    private TextView textViewDescriptionDynamic;
    private TextView textViewTotalPriceDynamic;
    private Button button_rate_us;
    private BookingBean bookingBean;
    private View view;
    private RelativeLayout root_descripBill;
    private ImageView profile;
    private TextView title, descrip;
    private LinearLayout ll_price;
    private CheckBox chBox_wallet;
    private TextView tv_walletBalance;
    private double payAmount=0;

    public BillPaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BillPaymentBinding binding=DataBindingUtil.inflate(inflater, R.layout.bill_payment, container, false);
        view=binding.getRoot();
        bookingBean = getArguments().getParcelable("bookingBean");

        ((HomeActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.headername_billpayment));
        ((HomeActivity)getActivity()).setNavigationIcon();
//        Font.setFontHeader(HomeActivity.toolbar_title, getActivity());

        rootTypeOfService = (RelativeLayout) view.findViewById(R.id.root_type_of_vechile);
        rootDescription = (RelativeLayout) view.findViewById(R.id.root_description_bill_payment);
        rootTotalPrice = (RelativeLayout) view.findViewById(R.id.root_total_price);
        button_rate_us = (Button) view.findViewById(R.id.button_rate_us);
        ll_price = (LinearLayout) view.findViewById(R.id.ll_price);
        chBox_wallet = (CheckBox) view.findViewById(R.id.chBox_wallet);
        tv_walletBalance = (TextView) view.findViewById(R.id.tv_walletBalance);

        if(bookingBean.getStatus().equalsIgnoreCase("payment"))
        {
            button_rate_us.setText("Paid");
        }

        rootTypeOfService.setOnClickListener(this);
        rootDescription.setOnClickListener(this);
        rootTotalPrice.setOnClickListener(this);
        button_rate_us.setOnClickListener(this);
        chBox_wallet.setOnCheckedChangeListener(this);

        imgTypeOfService = (ImageView) view.findViewById(R.id.imageView_type_of_vechile_billPayment);
        imgDescription = (ImageView) view.findViewById(R.id.imageView_rightArrow_description_billPayment);
        imgTotalPrice = (ImageView) view.findViewById(R.id.imageView_total_price_billPayment);

        textViewTypeOfService = (TextView) view.findViewById(R.id.textView_type_of_vechile);
        textViewDescription = (TextView) view.findViewById(R.id.textView_description_billPayment);
        textViewTotalPrice = (TextView) view.findViewById(R.id.textView_total_price);

        textViewTypeOfServiceDynamic = (TextView) view.findViewById(R.id.textView_type_of_vechile_dynamic);
//        textViewDescriptionDynamic = (TextView) view.findViewById(R.id.textView_description_dynamic_billPayment);
        textViewTotalPriceDynamic = (TextView) view.findViewById(R.id.textView_total_price_dynamic);
        root_descripBill = (RelativeLayout) view.findViewById(R.id.root_descripBill);
        profile = (ImageView) view.findViewById(R.id.img_bookBill);
        title = (TextView) view.findViewById(R.id.tv_bookTitleBill);
        descrip = (TextView) view.findViewById(R.id.tv_bookDescripBill);


        textViewTypeOfServiceDynamic.setVisibility(View.GONE);
        root_descripBill.setVisibility(View.GONE);
//        textViewDescriptionDynamic.setVisibility(View.GONE);
//        textViewTotalPriceDynamic.setVisibility(View.GONE);
        ll_price.setVisibility(View.GONE);


        /*set fonts*/
//        Font.setFontTextView(textViewTypeOfService, getActivity());
//        Font.setFontTextView(textViewDescription, getActivity());
//        Font.setFontTextView(textViewTotalPrice, getActivity());
//
//        Font.setFontTextView(textViewTypeOfServiceDynamic, getActivity());
//        Font.setFontTextView(textViewDescriptionDynamic, getActivity());
//        Font.setFontTextView(textViewTotalPriceDynamic, getActivity());
//
//
//        Font.setFontButton(button_rate_us, getActivity());

        if(!bookingBean.getStatus().isEmpty())
        {
            detailServiceHit();
        }

        return view;
    }

    private void setFields()
    {
        String cat="";
        if((bookingBean.getCategory()).contains("two"))
        {
            cat = getString(R.string.two_wheeler);
        }
        if((bookingBean.getCategory()).contains("light"))
        {
            if(cat.isEmpty())
            {
                cat=getString(R.string.light_weight_vehicle);
            }
            else
            {
                cat+=", "+getString(R.string.light_weight_vehicle);
            }
        }
        if((bookingBean.getCategory()).contains("heavy"))
        {
            if(cat.isEmpty())
            {
                cat=getString(R.string.heavy_weight_vehicle);
            }
            else
            {
                cat+=", "+getString(R.string.heavy_weight_vehicle);
            }
        }
//        categories/ type of vehicle and price
        textViewTypeOfServiceDynamic.setText(cat);
        textViewTotalPriceDynamic.setText("$"+bookingBean.getMinCharge());
        if(!bookingBean.getWalletAmount().isEmpty())
        { tv_walletBalance.setText("("+getString(R.string.current_balance_is)+" $ "+bookingBean.getWalletAmount()+")"); }
        else
        {  tv_walletBalance.setText("("+getString(R.string.current_balance_is)+" $ 0"+")"); }
        //    title , description and profile
        title.setText("Title: "+bookingBean.getRequestname());
        descrip.setText("Description: "+bookingBean.getRequestdesc());

       AQuery aQuery = new AQuery(profile);
        if(bookingBean.getRequestImage().isEmpty())
        {
            profile.setVisibility(View.GONE);
        }
        else
        {
            aQuery.id(profile).image(bookingBean.getRequestImage());
        }

    }
    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.root_type_of_vechile:

                if (isTypeOfServiceOpen)
                {
                    textViewTypeOfServiceDynamic.setVisibility(View.VISIBLE);
                    isTypeOfServiceOpen = false;
                    rootTypeOfService.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewTypeOfService.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewTypeOfService.setTextColor(getResources().getColor(R.color.white));
                    imgTypeOfService.setImageResource(R.drawable.down_arrow);
                }
                else {
                    textViewTypeOfServiceDynamic.setVisibility(View.GONE);
                    isTypeOfServiceOpen = true;
                    rootTypeOfService.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewTypeOfService.setTextColor(getResources().getColor(R.color.text_gray));
                    textViewTypeOfService.setBackgroundColor(getResources().getColor(R.color.white));
                    imgTypeOfService.setImageResource(R.drawable.greyright_arrow);
                }

                break;
            case R.id.root_description_bill_payment:

                if (isDescriptionOpen)
                {
//                    textViewDescriptionDynamic.setVisibility(View.VISIBLE);
                    root_descripBill.setVisibility(View.VISIBLE);
                    isDescriptionOpen = false;
                    rootDescription.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewDescription.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewDescription.setTextColor(getResources().getColor(R.color.white));
                    imgDescription.setImageResource(R.drawable.down_arrow);
                }
                else {
//                    textViewDescriptionDynamic.setVisibility(View.GONE);
                    root_descripBill.setVisibility(View.GONE);
                    isDescriptionOpen = true;
                    rootDescription.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewDescription.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewDescription.setTextColor(getResources().getColor(R.color.text_gray));
                    imgDescription.setImageResource(R.drawable.greyright_arrow);
                }

                break;
            case R.id.root_total_price:

                if (isTotalPriceOpen)
                {
//                    textViewTotalPriceDynamic.setVisibility(View.VISIBLE);
                    ll_price.setVisibility(View.VISIBLE);

                    isTotalPriceOpen = false;
                    rootTotalPrice.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewTotalPrice.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewTotalPrice.setTextColor(getResources().getColor(R.color.white));
                    imgTotalPrice.setImageResource(R.drawable.down_arrow);
                }
                else {
//                    textViewTotalPriceDynamic.setVisibility(View.GONE);
                    ll_price.setVisibility(View.GONE);

                    isTotalPriceOpen = true;
                    rootTotalPrice.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewTotalPrice.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewTotalPrice.setTextColor(getResources().getColor(R.color.text_gray));
                    imgTotalPrice.setImageResource(R.drawable.greyright_arrow);
                }

                break;
            case R.id.button_rate_us:
                if(bookingBean.getStatus().equalsIgnoreCase("billgenerate"))
                {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("bookingBean",bookingBean);
                    RateScreenFragment rateFragment = new RateScreenFragment();
                    rateFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer,rateFragment,"rateServiceProviderFragment").addToBackStack("rateserviceprovider").commit();
                }
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        try {

            if (isChecked) {
                if(!bookingBean.getWalletAmount().isEmpty())
                {
                payAmount = Double.parseDouble(bookingBean.getMinCharge()) - Double.parseDouble(bookingBean.getWalletAmount());
                textViewTotalPriceDynamic.setText("$" + payAmount);
                }
                else
                    Toast.makeText(getActivity(), "Wallet is Empty.", Toast.LENGTH_SHORT).show();
            }
            if (!isChecked) {
                payAmount = Double.parseDouble(bookingBean.getMinCharge());
                textViewTotalPriceDynamic.setText("$" + bookingBean.getMinCharge());
            }
        }
        catch (Exception e)
        {e.printStackTrace();}

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


    //    Services
    private void detailServiceHit()
    {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN));
        multipartEntityBuilder.addTextBody("request_id", bookingBean.getBookingid());

        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setActivity(getActivity());
        serviceBean.setFragment(BillPaymentFragment.this);
//        serviceBean.setMethodName("Consumers/request_details");
        serviceBean.setMethodName("Consumers/billdetails");
        serviceBean.setApilistener(this);

        CustomHandler customHandler = new CustomHandler(serviceBean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);
        ///Consumers/billdetails

    }

    @Override
    public void myServerResponse(JSONObject jsonObject)
    {
        try {
            if (jsonObject != null)
            {
                if (jsonObject.getString("status").equals("SUCCESS"))
                {
                    if(jsonObject.getString("requestKey").equalsIgnoreCase("billdetails"))
                    {
                        JSONObject j_object = jsonObject.getJSONObject("response").getJSONObject("user");
                        bookingBean.setRequestcategory(j_object.getString("category"));
                        bookingBean.setRequestdesc(j_object.getString("request_description"));
                        bookingBean.setUserName(j_object.getString("userName"));
                        bookingBean.setRequestname(j_object.getString("request_Title"));
                        bookingBean.setRequestDate(j_object.getString("requestDate"));
                        bookingBean.setWalletAmount(j_object.getString("Wallet_amount"));

                        JSONArray reqImgJsonArray=j_object.getJSONArray("request_image");
                        String array[]=new String[reqImgJsonArray.length()];
                        for(int j=0;j<reqImgJsonArray.length();j++)
                        {
                            array[j]=reqImgJsonArray.getString(j);

                        }
                        bookingBean.setRequestImgArr(array);
                        if(array.length>0)
                        {
                            bookingBean.setRequestImage(array[0]);
                        }

                        setFields();

                    }

                }
                else
                {

                }
                Log.e("JSON Response: ",""+jsonObject.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

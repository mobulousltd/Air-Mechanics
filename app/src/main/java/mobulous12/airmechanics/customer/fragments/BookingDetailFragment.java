package mobulous12.airmechanics.customer.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.List;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.BookingBean;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.databinding.BillPaymentBinding;
import mobulous12.airmechanics.databinding.BookingDetailBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingDetailFragment extends Fragment implements View.OnClickListener{




    private RelativeLayout rootServiceType;
    private RelativeLayout rootServiceProviderDetail;
    private RelativeLayout rootServiceProviderAddress;
    private RelativeLayout rootRating;

    private ImageView imgServiceType;
    private ImageView imgServiceProviderDetail;
    private ImageView imgServiceProviderAddress;
    private ImageView imgRating;

    private boolean isServiceTypeOpen = true;
    private boolean isServiceProviderDetailOpen = true;
    private boolean isServiceProviderAddressOpen = true;
    private boolean isRatingOpen = true;
    private boolean isDescpOpen = true;

    private TextView textViewServiceTypeDynamic;
    private TextView textViewServiceProviderDetailDynamic;
    private TextView textViewServiceProviderAddressDynamic;
    private LinearLayout rootStars;
    private TextView textViewServiceType;
    private TextView textViewServiceProviderDetail;
    private TextView textViewServiceProviderAddress;
    private TextView textViewRating;
    private TextView textViewServiceTypeDynamic2;

    private TextView title,descrip,tv_Descrip;
    private LinearLayout root_descrip;
    RelativeLayout sp_bookDetailDescrip;
    private ImageView img_Descrip,profile;
    View view;
    private  BookingBean  bookingBean;
    BookingDetailBinding binding;
    public BookingDetailFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState)
    {
        binding=DataBindingUtil.inflate(inflater, R.layout.booking_detail, container, false);
        view=binding.getRoot();
        ((HomeActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.headername_booking_detail));
        ((HomeActivity)getActivity()).setNavigationIcon();


        bookingBean=  getArguments().getParcelable("bookingBean");

//        Font.setFontHeader(HomeActivity.toolbar_title, getActivity()); //toolbar set font


        rootServiceType = (RelativeLayout) view.findViewById(R.id.root_service_type);
        rootServiceProviderDetail = (RelativeLayout) view.findViewById(R.id.root_service_provider_detail);
        rootServiceProviderAddress = (RelativeLayout) view.findViewById(R.id.root_service_provider_address);
        rootRating = (RelativeLayout) view.findViewById(R.id.root_booking_detail_rating);

        rootServiceType.setOnClickListener(this);
        rootServiceProviderDetail.setOnClickListener(this);
        rootServiceProviderAddress.setOnClickListener(this);
        rootRating.setOnClickListener(this);

        imgServiceType = (ImageView) view.findViewById(R.id.imageView_rightArrow_serviceType);
        imgServiceProviderDetail = (ImageView) view.findViewById(R.id.imageView_rightArrow_service_provider_detail);
        imgServiceProviderAddress = (ImageView) view.findViewById(R.id.imageView_rightArrow_service_provider_address);
        imgRating = (ImageView) view.findViewById(R.id.imageView_rightArrow_booking_detail_rating);

        textViewServiceTypeDynamic = (TextView) view.findViewById(R.id.textView_service_type_dynamic);
        textViewServiceTypeDynamic2 = (TextView) view.findViewById(R.id.textView_service_type_dynamic2);
        textViewServiceProviderDetailDynamic = (TextView) view.findViewById(R.id.textView_service_provider_detail_dynamic);
        textViewServiceProviderAddressDynamic = (TextView) view.findViewById(R.id.textView_service_provider_address_dynamic);
        rootStars = (LinearLayout) view.findViewById(R.id.linearlayout_starsFavorites_bookingDetail);

        view.findViewById(R.id.type_layout).setVisibility(View.GONE);
        textViewServiceProviderDetailDynamic.setVisibility(View.GONE);
        textViewServiceProviderAddressDynamic.setVisibility(View.GONE);
        rootStars.setVisibility(View.GONE);

        textViewServiceType = (TextView) view.findViewById(R.id.textView_serviceType_serviceProviderDetail);
        textViewServiceProviderDetail = (TextView) view.findViewById(R.id.textView_service_provider_detail);
        textViewServiceProviderAddress = (TextView) view.findViewById(R.id.textView_service_provider_address);
        textViewRating = (TextView) view.findViewById(R.id.textView_booking_detail_rating);


        tv_Descrip = (TextView) view.findViewById(R.id.tv_Descrip);
        title = (TextView) view.findViewById(R.id.tv_bookTitle);
        descrip = (TextView) view.findViewById(R.id.tv_bookDescrip);
        img_Descrip = (ImageView)view.findViewById(R.id.img_Descrip);
        profile = (ImageView)view.findViewById(R.id.img_bookSP);


        sp_bookDetailDescrip = (RelativeLayout) view.findViewById(R.id.sp_bookDetailDescrip);
        root_descrip = (LinearLayout) view.findViewById(R.id.root_descripSP);
        root_descrip.setVisibility(View.GONE);
        sp_bookDetailDescrip.setOnClickListener(this);
        /*set fonts*/

//        Font.setFontTextView(textViewServiceType, getActivity());
//        Font.setFontTextView(textViewServiceProviderDetail, getActivity());
//        Font.setFontTextView(textViewServiceProviderAddress, getActivity());
//        Font.setFontTextView(textViewRating, getActivity());
//
//        Font.setFontTextView(textViewServiceTypeDynamic, getActivity());
//        Font.setFontTextView(textViewServiceTypeDynamic2, getActivity());
//        Font.setFontTextView(textViewServiceProviderDetailDynamic, getActivity());
//        Font.setFontTextView(textViewServiceProviderAddressDynamic, getActivity());


        setFields();

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

// type
    textViewServiceTypeDynamic.setText(cat);
    textViewServiceTypeDynamic2.setText("Minimum Charges : "+bookingBean.getMinCharge());
//address
    textViewServiceProviderAddressDynamic.setText(bookingBean.getUseraddress());
//    title , description and profile
    title.setText("Title : "+bookingBean.getRequestname());
    String status;
    if (bookingBean.getStatus().equalsIgnoreCase("pending"))
    {
        status="Pending";
    }
    else if (bookingBean.getStatus().equalsIgnoreCase("process"))
    {
        status="In Progress";
    }
    else
    {
        status="Completed";
    }
    descrip.setText("Description : "+bookingBean.getRequestdesc()+"\nStatus : "+status);

    AQuery aQuery = new AQuery(profile);
    if(bookingBean.getRequestImage().isEmpty())
    {
        profile.setVisibility(View.GONE);
    }
    else
    {
        aQuery.id(profile).image(bookingBean.getRequestImage());
    }

//    details
    textViewServiceProviderDetailDynamic.setText("Name : "+bookingBean.getUserName()+"\n"+
                                                "Contact No. : "+bookingBean.getUsernumber()+"\n"+
                                                "Timing : "+bookingBean.getOpenTime()+" to "+bookingBean.getCloseTime());

//rating
    switch (bookingBean.getRating())
    {
        case "1":
            binding.bookingStar1.setImageResource(R.drawable.star_rate);
            break;
        case "2":
            binding.bookingStar1.setImageResource(R.drawable.star_rate);
            binding.bookingStar2.setImageResource(R.drawable.star_rate);
            break;
        case "3":
            binding.bookingStar1.setImageResource(R.drawable.star_rate);
            binding.bookingStar2.setImageResource(R.drawable.star_rate);
            binding.bookingStar3.setImageResource(R.drawable.star_rate);
            break;
        case "4":
            binding.bookingStar1.setImageResource(R.drawable.star_rate);
            binding.bookingStar2.setImageResource(R.drawable.star_rate);
            binding.bookingStar3.setImageResource(R.drawable.star_rate);
            binding.bookingStar4.setImageResource(R.drawable.star_rate);
            break;
        case "5":
            binding.bookingStar1.setImageResource(R.drawable.star_rate);
            binding.bookingStar2.setImageResource(R.drawable.star_rate);
            binding.bookingStar3.setImageResource(R.drawable.star_rate);
            binding.bookingStar4.setImageResource(R.drawable.star_rate);
            binding.bookingStar5.setImageResource(R.drawable.star_rate);
            break;

    }
}



    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.sp_bookDetailDescrip:
                if(isDescpOpen)
                {
                    root_descrip.setVisibility(View.VISIBLE);
                    isDescpOpen = false;
                    sp_bookDetailDescrip.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    tv_Descrip.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    tv_Descrip.setTextColor(getResources().getColor(R.color.white));
                    img_Descrip.setImageResource(R.drawable.down_arrow);

                }
                else {
                    root_descrip.setVisibility(View.GONE);
                    isDescpOpen = true;
                    sp_bookDetailDescrip.setBackgroundColor(getResources().getColor(R.color.white));
                    tv_Descrip.setBackgroundColor(getResources().getColor(R.color.white));
                    tv_Descrip.setTextColor(getResources().getColor(R.color.text_gray));
                    img_Descrip.setImageResource(R.drawable.greyright_arrow);
                }
                break;

            case R.id.root_service_type:

                if (isServiceTypeOpen)
                {
                    this.view.findViewById(R.id.type_layout).setVisibility(View.VISIBLE);
                    textViewServiceTypeDynamic2.setVisibility(View.VISIBLE);
                    isServiceTypeOpen = false;
                    rootServiceType.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewServiceType.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewServiceType.setTextColor(getResources().getColor(R.color.white));
                    imgServiceType.setImageResource(R.drawable.down_arrow);
                }
                else {
                    this.view.findViewById(R.id.type_layout).setVisibility(View.GONE);
                    isServiceTypeOpen = true;
                    rootServiceType.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewServiceType.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewServiceType.setTextColor(getResources().getColor(R.color.text_gray));
                    imgServiceType.setImageResource(R.drawable.greyright_arrow);
                }

                break;
            case R.id.root_service_provider_detail:

                if (isServiceProviderDetailOpen)
                {
                    textViewServiceProviderDetailDynamic.setVisibility(View.VISIBLE);
                    isServiceProviderDetailOpen = false;
                    rootServiceProviderDetail.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewServiceProviderDetail.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewServiceProviderDetail.setTextColor(getResources().getColor(R.color.white));
                    imgServiceProviderDetail.setImageResource(R.drawable.down_arrow);
                }
                else {
                    textViewServiceProviderDetailDynamic.setVisibility(View.GONE);
                    isServiceProviderDetailOpen = true;
                    rootServiceProviderDetail.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewServiceProviderDetail.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewServiceProviderDetail.setTextColor(getResources().getColor(R.color.text_gray));
                    imgServiceProviderDetail.setImageResource(R.drawable.greyright_arrow);
                }

                break;
            case R.id.root_service_provider_address:

                if (isServiceProviderAddressOpen)
                {
                    textViewServiceProviderAddressDynamic.setVisibility(View.VISIBLE);
                    isServiceProviderAddressOpen = false;
                    rootServiceProviderAddress.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewServiceProviderAddress.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewServiceProviderAddress.setTextColor(getResources().getColor(R.color.white));
                    imgServiceProviderAddress.setImageResource(R.drawable.down_arrow);
                }
                else {
                    textViewServiceProviderAddressDynamic.setVisibility(View.GONE);
                    isServiceProviderAddressOpen = true;
                    rootServiceProviderAddress.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewServiceProviderAddress.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewServiceProviderAddress.setTextColor(getResources().getColor(R.color.text_gray));
                    imgServiceProviderAddress.setImageResource(R.drawable.greyright_arrow);
                }

                break;
            case R.id.root_booking_detail_rating:

                if (isRatingOpen)
                {
                    rootStars.setVisibility(View.VISIBLE);
                    isRatingOpen = false;
                    rootRating.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewRating.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewRating.setTextColor(getResources().getColor(R.color.white));
                    imgRating.setImageResource(R.drawable.down_arrow);
                }
                else {
                    rootStars.setVisibility(View.GONE);
                    isRatingOpen = true;
                    rootRating.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewRating.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewRating.setTextColor(getResources().getColor(R.color.text_gray));
                    imgRating.setImageResource(R.drawable.greyright_arrow);
                }

                break;
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(bookingBean.getStatus().equalsIgnoreCase("billgenerate") || bookingBean.getStatus().equalsIgnoreCase("payment") )
        {
            inflater.inflate(R.menu.booking_detail_menu,menu);
        }
        else {
            inflater.inflate(R.menu.blank_at_right_menu,menu);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.payment:

                Bundle bundle = new Bundle();
                bundle.putParcelable("bookingBean",bookingBean);
                BillPaymentFragment paymentFragment = new BillPaymentFragment();
                paymentFragment.setArguments(bundle);
              getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer,paymentFragment,"billPaymentFragment").addToBackStack("payment").commit();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_show_service_provider).setVisible(false);

    }

}

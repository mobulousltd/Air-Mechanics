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

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.customer.activities.HomeActivity;
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

    private TextView textViewServiceTypeDynamic;
    private TextView textViewServiceProviderDetailDynamic;
    private TextView textViewServiceProviderAddressDynamic;
    private LinearLayout rootStars;
    private TextView textViewServiceType;
    private TextView textViewServiceProviderDetail;
    private TextView textViewServiceProviderAddress;
    private TextView textViewRating;
    private TextView textViewServiceTypeDynamic2;
    View view;

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
        BookingDetailBinding binding=DataBindingUtil.inflate(inflater, R.layout.booking_detail, container, false);
        view=binding.getRoot();
        ((HomeActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.headername_booking_detail));
        ((HomeActivity)getActivity()).setNavigationIcon();
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

        textViewServiceTypeDynamic.setVisibility(View.GONE);
        textViewServiceTypeDynamic2.setVisibility(View.GONE);
        textViewServiceProviderDetailDynamic.setVisibility(View.GONE);
        textViewServiceProviderAddressDynamic.setVisibility(View.GONE);
        rootStars.setVisibility(View.GONE);

        textViewServiceType = (TextView) view.findViewById(R.id.textView_serviceType_serviceProviderDetail);
        textViewServiceProviderDetail = (TextView) view.findViewById(R.id.textView_service_provider_detail);
        textViewServiceProviderAddress = (TextView) view.findViewById(R.id.textView_service_provider_address);
        textViewRating = (TextView) view.findViewById(R.id.textView_booking_detail_rating);

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
        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.root_service_type:

                if (isServiceTypeOpen)
                {
                    textViewServiceTypeDynamic.setVisibility(View.VISIBLE);
                    isServiceTypeOpen = false;
                    rootServiceType.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewServiceType.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewServiceType.setTextColor(getResources().getColor(R.color.white));
                    imgServiceType.setImageResource(R.drawable.down_arrow);
                }
                else {
                    textViewServiceTypeDynamic.setVisibility(View.GONE);
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
       inflater.inflate(R.menu.booking_detail_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.payment:
              getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer,new BillPaymentFragment(),"billPaymentFragment").addToBackStack("payment").commit();
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

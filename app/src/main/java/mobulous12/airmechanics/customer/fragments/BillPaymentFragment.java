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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.databinding.BillPaymentBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillPaymentFragment extends Fragment implements View.OnClickListener {


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
    View view;
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
        ((HomeActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.headername_billpayment));
        ((HomeActivity)getActivity()).setNavigationIcon();
//        Font.setFontHeader(HomeActivity.toolbar_title, getActivity());

        rootTypeOfService = (RelativeLayout) view.findViewById(R.id.root_type_of_vechile);
        rootDescription = (RelativeLayout) view.findViewById(R.id.root_description_bill_payment);
        rootTotalPrice = (RelativeLayout) view.findViewById(R.id.root_total_price);
        button_rate_us = (Button) view.findViewById(R.id.button_rate_us);

        rootTypeOfService.setOnClickListener(this);
        rootDescription.setOnClickListener(this);
        rootTotalPrice.setOnClickListener(this);
        button_rate_us.setOnClickListener(this);

        imgTypeOfService = (ImageView) view.findViewById(R.id.imageView_type_of_vechile_billPayment);
        imgDescription = (ImageView) view.findViewById(R.id.imageView_rightArrow_description_billPayment);
        imgTotalPrice = (ImageView) view.findViewById(R.id.imageView_total_price_billPayment);

        textViewTypeOfService = (TextView) view.findViewById(R.id.textView_type_of_vechile);
        textViewDescription = (TextView) view.findViewById(R.id.textView_description_billPayment);
        textViewTotalPrice = (TextView) view.findViewById(R.id.textView_total_price);

        textViewTypeOfServiceDynamic = (TextView) view.findViewById(R.id.textView_type_of_vechile_dynamic);
        textViewDescriptionDynamic = (TextView) view.findViewById(R.id.textView_description_dynamic_billPayment);
        textViewTotalPriceDynamic = (TextView) view.findViewById(R.id.textView_total_price_dynamic);

        textViewTypeOfServiceDynamic.setVisibility(View.GONE);
        textViewDescriptionDynamic.setVisibility(View.GONE);
        textViewTotalPriceDynamic.setVisibility(View.GONE);

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

        return view;
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
                    textViewDescriptionDynamic.setVisibility(View.VISIBLE);
                    isDescriptionOpen = false;
                    rootDescription.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewDescription.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewDescription.setTextColor(getResources().getColor(R.color.white));
                    imgDescription.setImageResource(R.drawable.down_arrow);
                }
                else {
                    textViewDescriptionDynamic.setVisibility(View.GONE);
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
                    textViewTotalPriceDynamic.setVisibility(View.VISIBLE);
                    isTotalPriceOpen = false;
                    rootTotalPrice.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewTotalPrice.setBackgroundColor(getResources().getColor(R.color.dodgerblue));
                    textViewTotalPrice.setTextColor(getResources().getColor(R.color.white));
                    imgTotalPrice.setImageResource(R.drawable.down_arrow);
                }
                else {
                    textViewTotalPriceDynamic.setVisibility(View.GONE);
                    isTotalPriceOpen = true;
                    rootTotalPrice.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewTotalPrice.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewTotalPrice.setTextColor(getResources().getColor(R.color.text_gray));
                    imgTotalPrice.setImageResource(R.drawable.greyright_arrow);
                }

                break;
            case R.id.button_rate_us:

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer,new RateScreenFragment(),"rateServiceProviderFragment").addToBackStack("rateserviceprovider").commit();

                break;
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

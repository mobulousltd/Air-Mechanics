package mobulous12.airmechanics.serviceprovider.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
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
import android.widget.Toast;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.databinding.BillPaymentSpBinding;
import mobulous12.airmechanics.fonts.Font;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillPaymentFragmentSp extends Fragment implements View.OnClickListener {

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

    private Button button_send;


    public BillPaymentFragmentSp() {
        // Required empty public constructor
    }

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BillPaymentSpBinding binding=DataBindingUtil.inflate(inflater, R.layout.bill_payment_sp, container, false);
        view=binding.getRoot();

        rootTypeOfService = (RelativeLayout) view.findViewById(R.id.root_type_of_vechile_sp);
        rootDescription = (RelativeLayout) view.findViewById(R.id.root_description_bill_payment_sp);
        rootTotalPrice = (RelativeLayout) view.findViewById(R.id.root_total_price_sp);
        button_send = (Button) view.findViewById(R.id.button_send_billPayment_sp);

        rootTypeOfService.setOnClickListener(this);
        rootDescription.setOnClickListener(this);
        rootTotalPrice.setOnClickListener(this);
        button_send.setOnClickListener(this);

        imgTypeOfService = (ImageView) view.findViewById(R.id.imageView_type_of_vechile_billPayment_sp);
        imgDescription = (ImageView) view.findViewById(R.id.imageView_rightArrow_description_billPayment_sp);
        imgTotalPrice = (ImageView) view.findViewById(R.id.imageView_total_price_billPayment_sp);

        textViewTypeOfService = (TextView) view.findViewById(R.id.textView_type_of_vechile_sp);
        textViewDescription = (TextView) view.findViewById(R.id.textView_description_billPayment_sp);
        textViewTotalPrice = (TextView) view.findViewById(R.id.textView_total_price_sp);

        textViewTypeOfServiceDynamic = (TextView) view.findViewById(R.id.textView_type_of_vechile_dynamic_sp);
        textViewDescriptionDynamic = (TextView) view.findViewById(R.id.textView_description_dynamic_billPayment_sp);
        textViewTotalPriceDynamic = (TextView) view.findViewById(R.id.textView_total_price_dynamic_sp);

        textViewTypeOfServiceDynamic.setVisibility(View.GONE);
        textViewDescriptionDynamic.setVisibility(View.GONE);
        textViewTotalPriceDynamic.setVisibility(View.GONE);


        imgTypeOfService.setImageResource(R.drawable.right_arrow);
        imgDescription.setImageResource(R.drawable.right_arrow);
        imgTotalPrice.setImageResource(R.drawable.right_arrow);


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
                    textViewTypeOfService.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewTypeOfService.setTextColor(getResources().getColor(R.color.text_gray));
                    imgTypeOfService.setImageResource(R.drawable.right_arrow);
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
                    imgDescription.setImageResource(R.drawable.right_arrow);
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
                    imgTotalPrice.setImageResource(R.drawable.right_arrow);
                }

                break;

            case R.id.button_send_billPayment_sp:
                Toast.makeText(getActivity(), "Bill Sent to Customer successfully!", Toast.LENGTH_SHORT).show();
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

package mobulous12.airmechanics.serviceprovider.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.databinding.FragmentMyEarningsSpBinding;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;
import mobulous12.airmechanics.fonts.Font;

public class MyEarningsFragment_SP extends Fragment {

    private View view;
    private TextView textView_nameMyEarning,textView_amountMyEarning,textView_dateEarning,descrip1_earning,descrip2_earning;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState)
    {
        FragmentMyEarningsSpBinding binding= DataBindingUtil.inflate(inflater, R.layout.fragment_my_earnings_sp, container, false);
        view=binding.getRoot();
        ((HomeActivityServicePro) getActivity()).setToolbarTitleSP("My Earning");
        ((HomeActivityServicePro) getActivity()).setNavigationIconSP();

        textView_nameMyEarning = (TextView) view.findViewById(R.id.textView_nameMyEarning);
        textView_amountMyEarning = (TextView) view.findViewById(R.id.textView_amountMyEarning);
        textView_dateEarning = (TextView) view.findViewById(R.id.textView_dateEarning);
        descrip1_earning = (TextView) view.findViewById(R.id.descrip1_earning);
        descrip2_earning = (TextView) view.findViewById(R.id.descrip2_earning);



        return view;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.blank_at_right_menu,menu);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_show_service_provider).setVisible(false);
//        menu.findItem(R.id.action_show_myJob_Orders).setVisible(false);


    }

}

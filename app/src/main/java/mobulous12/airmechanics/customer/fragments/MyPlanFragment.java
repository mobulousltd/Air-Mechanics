package mobulous12.airmechanics.customer.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.customer.adapters.MyPlanRecyclerAdapter;
import mobulous12.airmechanics.databinding.FragmentMyPlanBinding;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;
import mobulous12.airmechanics.fonts.Font;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPlanFragment extends Fragment {

    RecyclerView recyclerView_myPlan;
    MyPlanRecyclerAdapter myPlanRecyclerAdapter;
    private View view;

    public MyPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        FragmentMyPlanBinding binding=DataBindingUtil.inflate(inflater, R.layout.fragment_my_plan, container, false);
        view=binding.getRoot();

        if(SharedPreferenceWriter.getInstance(getActivity()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
        {
            ((HomeActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.headername_myplan));
            ((HomeActivity)getActivity()).setNavigationIcon();
        }
        else
        {
            ((HomeActivityServicePro)getActivity()).setToolbarTitleSP(getResources().getString(R.string.headername_myplan));
            ((HomeActivityServicePro)getActivity()).setNavigationIconSP();

        }
        recyclerView_myPlan = (RecyclerView) view.findViewById(R.id.recyclerView_myPlan);
        myPlanRecyclerAdapter = new MyPlanRecyclerAdapter(getActivity());
        recyclerView_myPlan.setAdapter(myPlanRecyclerAdapter);
        recyclerView_myPlan.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
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
//        if(SharedPreferenceWriter.getInstance(getActivity()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN)) {
//
//            menu.findItem(R.id.action_show_service_provider).setVisible(false);
//        }else {
//            menu.findItem(R.id.action_show_myJob_Orders).setVisible(false);
//        }
    }

}

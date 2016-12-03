package mobulous12.airmechanics.customer.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.databinding.ResendCodeScreenBinding;

public class ResendCode extends DialogFragment {
    View view;

    public ResendCode() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ResendCodeScreenBinding binding=DataBindingUtil.inflate(inflater, R.layout.resend_code_screen,container,false);
        view=binding.getRoot();
        return view;
    }

}

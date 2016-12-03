package mobulous12.airmechanics.serviceprovider.dialogs;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.databinding.FragmentForgotPassSendFragmentSpBinding;
import mobulous12.airmechanics.fonts.Font;

public class ForgotPassSendFragmentSP extends DialogFragment implements View.OnClickListener  {

    private View view;
    public ForgotPassSendFragmentSP() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        FragmentForgotPassSendFragmentSpBinding binding=DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_pass_send_fragment_sp, container, false);
        view=binding.getRoot();
        view.findViewById(R.id.button_okForgotPassFragSP).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view)
    {
        dismiss();
        getActivity().finish();
    }
}

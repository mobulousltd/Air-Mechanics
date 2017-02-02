package mobulous12.airmechanics.customer.fragments;


import android.content.Intent;
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
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.databinding.FragmentSubmitQuoteBinding;
import mobulous12.airmechanics.fonts.Font;

public class SubmitQuoteFragment extends DialogFragment implements View.OnClickListener {

    private View view;

    public SubmitQuoteFragment() {
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
        FragmentSubmitQuoteBinding binding=DataBindingUtil.inflate(inflater,R.layout.fragment_submit_quote, container, false);
        view =  binding.getRoot();
        view.findViewById(R.id.button_okSendReq_Quote).setOnClickListener(this);
        return view;
    }




    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.button_okSendReq_Quote:
                startActivity(new Intent(getActivity(), HomeActivity.class));
                getActivity().finishAffinity();
                dismiss();
                break;
        }
    }


}

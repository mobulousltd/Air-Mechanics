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
import mobulous12.airmechanics.databinding.FragmentSendCodeBinding;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;
import mobulous12.airmechanics.fonts.Font;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendCodeFragment extends DialogFragment implements View.OnClickListener{

    private View view;

    public SendCodeFragment() {
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
        FragmentSendCodeBinding binding=DataBindingUtil.inflate(inflater, R.layout.fragment_send_code, container, false);
        view = binding.getRoot();
        view.findViewById(R.id.button_okSendFrag).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.button_okSendFrag:
                Intent intent;
                if(SharedPreferenceWriter.getInstance(getActivity()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
                {
                    intent=new Intent(getActivity(), HomeActivity.class);
                    getActivity().finishAffinity();
                    dismiss();
                }
                else
                {
                    intent=new Intent(getActivity(), HomeActivityServicePro.class);
                    getActivity().finishAffinity();
                    dismiss();
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(intent);
                getActivity().finish();

              break;
        }
    }
}

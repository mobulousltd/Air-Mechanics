package mobulous12.airmechanics.serviceprovider.dialogs;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.airinterfaces.MyDialogListenerInterface;
import mobulous12.airmechanics.serviceprovider.activities.SignUpServiceProActivity;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;

/**
 * Created by mobulous11 on 10/11/16.
 */

public class SpecialityDialogFrag extends DialogFragment implements View.OnClickListener{

    private RadioGroup radioGroup;
    private TextView tvCancel;
    private TextView tvDone;
    private MyDialogListenerInterface listener;
    private String specialty="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_speciality_dialog, container, false);
        specialty=getArguments().getString("specialty");
        tvDone= (TextView) view.findViewById(R.id.speciality_done);
        tvDone.setOnClickListener(this);
        tvCancel = (TextView) view.findViewById(R.id.speciality_cancel);
        tvCancel.setOnClickListener(this);
        RadioButton radioButton;
        if(!specialty.isEmpty())
        {
            switch (specialty)
            {
                case "two":
                    radioButton=(RadioButton)view.findViewById(R.id.radio_btn_twoV);
                    radioButton.setChecked(true);
                    break;
                case "light":
                    radioButton=(RadioButton)view.findViewById(R.id.radio_btn_lightV);
                    radioButton.setChecked(true);
                    break;
                case "heavy":
                    radioButton=(RadioButton)view.findViewById(R.id.radio_btn_heavyV);
                    radioButton.setChecked(true);
                    break;
            }
        }

        radioGroup = (RadioGroup) view.findViewById(R.id.speciality_radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId)
                {
                    case R.id.radio_btn_twoV:
                        specialty = "two";
                        break;
                    case R.id.radio_btn_lightV:
                        specialty = "light";
                        break;
                    case R.id.radio_btn_heavyV:
                        specialty = "heavy";
                        break;
                }
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.speciality_done:

                if (getActivity() instanceof SignUpServiceProActivity)
                {
                    listener = (MyDialogListenerInterface) getActivity();
                    listener.onFinishDialog(specialty, "speciality");
                }
                dismiss();
                break;
            case R.id.speciality_cancel:
                dismiss();
                break;
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // super.onCreateDialog(savedInstanceState);
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.semitransparent)));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
    }
}

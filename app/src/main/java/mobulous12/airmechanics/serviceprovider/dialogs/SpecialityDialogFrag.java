package mobulous12.airmechanics.serviceprovider.dialogs;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.airinterfaces.MyDialogListenerInterface;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;
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
    private String specialty="",selectedCategories = "";
    private String cat = "";
    private String[] categories=new String[]{};
    private RadioButton radio_btn_twoV,radio_btn_lightV,radio_btn_heavyV;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_speciality_dialog, container, false);
        radio_btn_twoV = (RadioButton) view.findViewById(R.id.radio_btn_twoV);
        radio_btn_lightV = (RadioButton) view.findViewById(R.id.radio_btn_lightV);
        radio_btn_heavyV = (RadioButton) view.findViewById(R.id.radio_btn_heavyV);
        radio_btn_twoV.setVisibility(View.GONE);
        radio_btn_lightV.setVisibility(View.GONE);
        radio_btn_heavyV.setVisibility(View.GONE);

        specialty=getArguments().getString("specialty");
        selectedCategories=getArguments().getString("selectedCategories");
        if (selectedCategories != null) {
            categories = selectedCategories.split(",");
        }
        for(int i = 0;i<categories.length;i++)
        {
            if(categories[i].equals("two"))
            {
                cat = "two";
                radio_btn_twoV.setVisibility(View.VISIBLE);
                radio_btn_lightV.setVisibility(View.GONE);
                radio_btn_heavyV.setVisibility(View.GONE);

            }
            if(categories[i].equals("light"))
            {
                if(cat.isEmpty())
                {
                    cat = "light";
                    radio_btn_twoV.setVisibility(View.GONE);
                    radio_btn_lightV.setVisibility(View.VISIBLE);
                    radio_btn_heavyV.setVisibility(View.GONE);
                }
                else
                {
                    cat += "light";
                    radio_btn_lightV.setVisibility(View.VISIBLE);


                }
            }
            if(categories[i].equals("heavy"))
            {
                if(cat.isEmpty())

                {
                    cat = "heavy";
                    radio_btn_twoV.setVisibility(View.GONE);
                    radio_btn_lightV.setVisibility(View.GONE);
                    radio_btn_heavyV.setVisibility(View.VISIBLE);
                }
                else
                {
                    cat += "heavy";
                    radio_btn_heavyV.setVisibility(View.VISIBLE);

                }
            }
        }

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

                MyDialogListenerInterface listener;
                if(getActivity() instanceof HomeActivityServicePro)
                {
                    Fragment fragment=getTargetFragment();
                    listener=(MyDialogListenerInterface)fragment;
                }
                else
                {
                    listener=(MyDialogListenerInterface)getActivity();

                }
                listener.onFinishDialog(specialty, "speciality");

                categories=new String[]{};
                dismiss();
                break;
            case R.id.speciality_cancel:
                categories=new String[]{};
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

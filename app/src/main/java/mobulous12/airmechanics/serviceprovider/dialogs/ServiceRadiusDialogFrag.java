package mobulous12.airmechanics.serviceprovider.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.airinterfaces.MyDialogListenerInterface;
import mobulous12.airmechanics.databinding.FragmentServiceRadiusDialogBinding;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;

public class ServiceRadiusDialogFrag extends DialogFragment {

    View view;
    String radius="";
    RadioGroup radioGroup;
    RadioButton radioupto5, radioupto10, radioupto20;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState)
    {
        FragmentServiceRadiusDialogBinding binding=DataBindingUtil.inflate(inflater, R.layout.fragment_service_radius_dialog, container, false);
        view=binding.getRoot();

        radius=getArguments().getString("radius");
        radioGroup=(RadioGroup)view.findViewById(R.id.radiogroup);
        radioupto5=(RadioButton)view.findViewById(R.id.radioupto5);
        radioupto10=(RadioButton)view.findViewById(R.id.radioupto10);
        radioupto20=(RadioButton)view.findViewById(R.id.radioupto20);
        if(!radius.isEmpty())
        {
            switch (radius)
            {
                case "5":
                    radioupto5.setChecked(true);
                    break;
                case "10":
                    radioupto10.setChecked(true);
                    break;
                case "20":
                    radioupto20.setChecked(true);
                    break;
            }
        }
        view.findViewById(R.id.radius_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton radioButton = (RadioButton)view.findViewById(selectedId);
                if (radioButton.equals(radioupto5))
                {
                    radius="5";
                }
                else if (radioButton.equals(radioupto10))
                {
                    radius="10";
                }
                if (radioButton.equals(radioupto20))
                {
                    radius="20";
                }


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
                listener.onFinishDialog(radius, "radius");
                dismiss();

            }
        });
        view.findViewById(R.id.radius_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Dialog dialog = super.onCreateDialog(savedInstanceState);
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

package mobulous12.airmechanics.serviceprovider.dialogs;



import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.airinterfaces.MyDialogListenerInterface;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.databinding.FragmentCategoriesBinding;
import mobulous12.airmechanics.databinding.FragmentWorkingdaysDialogBinding;
import mobulous12.airmechanics.databinding.HomeScreenInfoWindowBinding;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkingDaysDialogFrag extends DialogFragment implements View.OnClickListener
{


    private TextView days_cancel,days_done;
    private View view;
    private String workdays="";
    private CheckBox chBox_Sun,chBox_Mon,chBox_Tue,chBox_Wed,chBox_Thur,chBox_Fri,chBox_Sat;
    public WorkingDaysDialogFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

         FragmentWorkingdaysDialogBinding daysBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_workingdays_dialog,container,false);
        view = daysBinding.getRoot();
        workdays = getArguments().getString("workdays");

//        Views
        chBox_Sun = (CheckBox) view.findViewById(R.id.chBox_Sun);
        chBox_Mon = (CheckBox) view.findViewById(R.id.chBox_Mon);
        chBox_Tue = (CheckBox) view.findViewById(R.id.chBox_Tue);
        chBox_Wed = (CheckBox) view.findViewById(R.id.chBox_Wed);
        chBox_Thur = (CheckBox) view.findViewById(R.id.chBox_Thur);
        chBox_Fri = (CheckBox) view.findViewById(R.id.chBox_Fri);
        chBox_Sat = (CheckBox) view.findViewById(R.id.chBox_Sat);

        days_done = (TextView) view.findViewById(R.id.days_done);
        days_cancel = (TextView) view.findViewById(R.id.days_cancel);

        /*Keeping checkbox state*/

        if(workdays.contains("0"))
        {
            chBox_Sun.setChecked(true);
        }
        if(workdays.contains("1"))
        {
            chBox_Mon.setChecked(true);
        }
        if(workdays.contains("2"))
        {
            chBox_Tue.setChecked(true);
        }
        if(workdays.contains("3"))
        {
            chBox_Wed.setChecked(true);
        }
        if(workdays.contains("4"))
        {
            chBox_Thur.setChecked(true);
        }
        if(workdays.contains("5"))
        {
            chBox_Fri.setChecked(true);
        }
        if(workdays.contains("6"))
        {
            chBox_Sat.setChecked(true);
        }

        days_done.setOnClickListener(this);
        days_cancel.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.days_done:

                workdays ="";
                if(chBox_Sun.isChecked())
                {
                    if(workdays.isEmpty())
                    {

                        workdays=chBox_Sun.getText().toString();
                    }
                }
                if(chBox_Mon.isChecked())
                {
                    if(workdays.isEmpty())
                    {
                        workdays =chBox_Mon.getText().toString();
                    }
                    else
                    {
                        workdays +=","+chBox_Mon.getText().toString();
                    }
                }
                if(chBox_Tue.isChecked())
                {
                    if(workdays.isEmpty())
                    {
                        workdays =chBox_Tue.getText().toString();
                    }
                    else
                    {
                        workdays +=","+chBox_Tue.getText().toString();
                    }
                }
                if(chBox_Wed.isChecked())
                {
                    if(workdays.isEmpty())
                    {
                        workdays =chBox_Wed.getText().toString();
                    }
                    else
                    {
                        workdays +=","+chBox_Wed.getText().toString();
                    }
                }if(chBox_Thur.isChecked())
                {
                    if(workdays.isEmpty())
                    {
                        workdays =chBox_Thur.getText().toString();
                    }
                    else
                    {
                        workdays +=","+chBox_Thur.getText().toString();
                    }
                }if(chBox_Fri.isChecked())
                {
                    if(workdays.isEmpty())
                    {
                        workdays =chBox_Fri.getText().toString();
                    }
                    else
                    {
                        workdays +=","+chBox_Fri.getText().toString();
                    }
                }if(chBox_Sat.isChecked())
                {
                    if(workdays.isEmpty())
                    {
                        workdays =chBox_Sat.getText().toString();
                    }
                    else
                    {
                        workdays +=","+chBox_Sat.getText().toString();
                    }
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
                listener.onFinishDialog(workdays, "workdays");
                dismiss();

                break;

            case R.id.days_cancel:
                dismiss();
                break;
        }
    }




        @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
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

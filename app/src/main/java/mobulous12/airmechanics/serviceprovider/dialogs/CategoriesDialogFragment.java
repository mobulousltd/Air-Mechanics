package mobulous12.airmechanics.serviceprovider.dialogs;



import android.app.Dialog;
import android.databinding.DataBindingUtil;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.airinterfaces.MyDialogListenerInterface;
import mobulous12.airmechanics.databinding.FragmentCategoriesBinding;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesDialogFragment extends DialogFragment implements View.OnClickListener
{

    private CheckBox chBox_twoV,chBox_lightV,chBox_heavyV;
    private TextView categories_cancel,categories_done;
    private View view;
    private String categories = "";

    private boolean isTwoChk=false,isLightChk=false,isHeavyChk=false,isAllChk=false;

    public CategoriesDialogFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setStyle(STYLE_NO_TITLE, 0);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        FragmentCategoriesBinding binding=DataBindingUtil.inflate(inflater,R.layout.fragment_categories, container, false);
        view = binding.getRoot();

        categories = getArguments().getString("categories");

        categories_done = (TextView) view.findViewById(R.id.categories_done);
        categories_cancel = (TextView) view.findViewById(R.id.categories_cancel);
        chBox_twoV = (CheckBox) view.findViewById(R.id.chBox_twoV);
        chBox_lightV = (CheckBox) view.findViewById(R.id.chBox_lightV);
        chBox_heavyV = (CheckBox) view.findViewById(R.id.chBox_heavyV);

        /* keeping chechbox state  */
        if(categories.contains("two"))
        {
            chBox_twoV.setChecked(true);
        }
        if(categories.contains("light"))
        {
            chBox_lightV.setChecked(true);
        }
        if(categories.contains("heavy"))
        {
            chBox_heavyV.setChecked(true);
        }


        categories_done.setOnClickListener(this);
        categories_cancel.setOnClickListener(this);



        return view;
    }

    //Dialog Fragment
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.categories_done:

                categories ="";
                if(chBox_twoV.isChecked())
                {
                    if(categories.isEmpty())
                    {
                        categories=chBox_twoV.getText().toString();
                    }
                }
                if(chBox_lightV.isChecked())
                {
                    if(categories.isEmpty())
                    {
                        categories =chBox_lightV.getText().toString();
                    }
                    else
                    {
                        categories +=","+chBox_lightV.getText().toString();
                    }
                }
                if(chBox_heavyV.isChecked())
                {
                    if(categories.isEmpty())
                    {
                        categories =chBox_heavyV.getText().toString();
                    }
                    else
                    {
                        categories +=","+chBox_heavyV.getText().toString();
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

                listener.onFinishDialog(categories, "categories");
                dismiss();

                break;

            case R.id.categories_cancel:
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

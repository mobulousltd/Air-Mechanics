package mobulous12.airmechanics.customer.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.databinding.MyPlanCardBinding;
import mobulous12.airmechanics.fonts.Font;

/**
 * Created by mobulous12 on 7/10/16.
 */

public class MyPlanRecyclerAdapter extends RecyclerView.Adapter<MyPlanRecyclerAdapter.MyPlanViewHolder>{

    private Context context;
    private LayoutInflater inflater;

    public MyPlanRecyclerAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public MyPlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyPlanCardBinding binding=DataBindingUtil.inflate(inflater, R.layout.my_plan_card, parent, false);
        MyPlanViewHolder holder = new MyPlanViewHolder(binding.getRoot());

        return holder;
    }

    @Override
    public void onBindViewHolder(MyPlanViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class  MyPlanViewHolder extends RecyclerView.ViewHolder
    {

        private TextView myPlanDuration;
        private TextView myPlanAmount;
        private TextView expiryDateText;
        private TextView expiryDate;
        private TextView benefitHeading;
        private TextView descriptionMyPlan;

        public MyPlanViewHolder(View itemView) {
            super(itemView);


//            myPlanDuration = (TextView) itemView.findViewById(R.id.textView_durationMyPlan);
//            myPlanAmount = (TextView) itemView.findViewById(R.id.textView_amountMyPlan);
//            expiryDateText = (TextView) itemView.findViewById(R.id.textView_expirydateTextMyPlan);
//            expiryDate = (TextView) itemView.findViewById(R.id.textView_expirydateMyPlan);
//            benefitHeading = (TextView) itemView.findViewById(R.id.textView_benefitsHeadingMyPlan);
//            descriptionMyPlan = (TextView) itemView.findViewById(R.id.textView_descriptionMyPlan);

//            Font.setFontRobotoRegular(myPlanDuration, (AppCompatActivity)context);
//            Font.setFontRobotoRegular(myPlanAmount, (AppCompatActivity)context);
//            Font.setFontTextView(expiryDateText, (AppCompatActivity)context);
//            Font.setFontTextView(expiryDate, (AppCompatActivity)context);
//            Font.setFontRobotoRegular(benefitHeading, (AppCompatActivity)context);
//            Font.setFontRalewayLight(descriptionMyPlan, (AppCompatActivity)context);
        }
    }
}

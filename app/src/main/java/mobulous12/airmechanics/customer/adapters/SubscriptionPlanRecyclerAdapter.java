package mobulous12.airmechanics.customer.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.databinding.SubscriptionPlanCustomCardsBinding;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;

/**
 * Created by mobulous12 on 7/10/16.
 */

public class SubscriptionPlanRecyclerAdapter extends RecyclerView.Adapter<SubscriptionPlanRecyclerAdapter.SubsPlanViewHolder> {


    private Context context;
    private LayoutInflater inflater;

    public SubscriptionPlanRecyclerAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public SubsPlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SubscriptionPlanCustomCardsBinding binding=DataBindingUtil.inflate(inflater, R.layout.subscription_plan_custom_cards,parent,false);
        SubsPlanViewHolder holder = new SubsPlanViewHolder(binding.getRoot());
        return holder;
    }

    @Override
    public void onBindViewHolder(SubsPlanViewHolder holder, int position) {
        holder.btn_MonthlySubscription_BuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Monthly Plan purchased successfully.", Toast.LENGTH_SHORT).show();
                if(context instanceof HomeActivity)
                {
                    ((HomeActivity) context).getSupportFragmentManager().popBackStack();
                }
                else
                {
                    ((HomeActivityServicePro) context).getSupportFragmentManager().popBackStack();
                }
            }
        });
        holder.btn_AnnualSubscription_BuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Annual Plan purchased successfully.", Toast.LENGTH_SHORT).show();
                if(context instanceof HomeActivity)
                {
                    ((HomeActivity) context).getSupportFragmentManager().popBackStack();
                }
                else
                {
                    ((HomeActivityServicePro) context).getSupportFragmentManager().popBackStack();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }


    class  SubsPlanViewHolder extends RecyclerView.ViewHolder
    {

        ///////////////////////
        private TextView monthlySubscriptionTitle;
        private TextView monthlySubscriptionAmount;
        private TextView monthlySubscription_benefit;
        private TextView monthlySubscription_description;
        private Button btn_MonthlySubscription_BuyNow;

        private TextView annualSubscriptionTitle;
        private TextView annualSubscriptionAmount;
        private TextView annualSubscription_benefit;
        private TextView annualSubscription_description;

        private Button btn_AnnualSubscription_BuyNow;
        //////////////////////

        public SubsPlanViewHolder(View itemView) {
            super(itemView);

   //////////
//            monthlySubscriptionTitle = (TextView) itemView.findViewById(R.id.textView_durationMonthlySubscriptionPlan);
//            monthlySubscriptionAmount = (TextView) itemView.findViewById(R.id.textView_monthlyAmountSubscriptionPlan);
//            monthlySubscription_benefit = (TextView) itemView.findViewById(R.id.textView1_benefitsHeadingSubcriptionPlan);
//            monthlySubscription_description = (TextView) itemView.findViewById(R.id.textView_upper_descriptionSubscriptionPlan);
//
//            Font.setFontRobotoRegular(monthlySubscriptionTitle,(AppCompatActivity)context);
//            Font.setFontRobotoRegular(monthlySubscriptionAmount,(AppCompatActivity)context);
//            Font.setFontRobotoRegular(monthlySubscription_benefit, (AppCompatActivity)context);
//            Font.setFontRalewayLight(monthlySubscription_description, (AppCompatActivity)context);
//
            btn_MonthlySubscription_BuyNow = (Button) itemView.findViewById(R.id.button_buyNow_monthlysubscription);
//            Font.setFontButton(btn_MonthlySubscription_BuyNow ,(AppCompatActivity)context);
//
//            annualSubscriptionTitle = (TextView) itemView.findViewById(R.id.textView_durationAnnualSubscriptionPlan);
//            annualSubscriptionAmount = (TextView) itemView.findViewById(R.id.textView_annualyAmountSubscriptionPlan);
//            annualSubscription_benefit = (TextView) itemView.findViewById(R.id.textView2_benefitsHeadingSubcriptionPlan);
//            annualSubscription_description = (TextView) itemView.findViewById(R.id.textView_lower_descriptionSubscriptionPlan);
//
//            Font.setFontRobotoRegular(annualSubscriptionTitle,(AppCompatActivity)context);
//            Font.setFontRobotoRegular(annualSubscriptionAmount, (AppCompatActivity)context);
//            Font.setFontRobotoRegular(annualSubscription_benefit, (AppCompatActivity)context);
//            Font.setFontRalewayLight(annualSubscription_description, (AppCompatActivity)context);
//
            btn_AnnualSubscription_BuyNow = (Button) itemView.findViewById(R.id.button_buyNow_annualSubscription);
//            Font.setFontButton(btn_AnnualSubscription_BuyNow ,(AppCompatActivity)context);
/////////////


        }
    }
}

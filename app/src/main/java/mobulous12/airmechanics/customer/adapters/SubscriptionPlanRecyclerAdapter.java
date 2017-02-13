package mobulous12.airmechanics.customer.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.SubscriptionPlanBean;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.databinding.SubsPlanCardBinding;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;

/**
 * Created by mobulous12 on 7/10/16.
 */

public class SubscriptionPlanRecyclerAdapter extends RecyclerView.Adapter<SubscriptionPlanRecyclerAdapter.SubsPlanViewHolder> {


    private Context context;
    private LayoutInflater inflater;
    private static MyClickListener listener;
    private ArrayList<SubscriptionPlanBean> arrayList;

    public SubscriptionPlanRecyclerAdapter(Context context, ArrayList<SubscriptionPlanBean> arrayList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.arrayList = arrayList;
    }

    @Override
    public SubsPlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SubsPlanCardBinding binding = DataBindingUtil.inflate(inflater,R.layout.subs_plan_card,parent,false);
        SubsPlanViewHolder holder = new SubsPlanViewHolder(binding.getRoot());
        return holder;
    }

    @Override
    public void onBindViewHolder(SubsPlanViewHolder holder, final int position)
    {
        final SubscriptionPlanBean bean = arrayList.get(position);

        if(SharedPreferenceWriter.getInstance(context.getApplicationContext()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
        {
            holder.root_heading1.setBackgroundColor(context.getResources().getColor(R.color.annual_pink_color));
            holder.root_heading2.setBackgroundColor(context.getResources().getColor(R.color.annual_pink_color));

            holder.tv_durationPlan1.setText("One Off");
            holder.tv_durationPlan2.setText("One Off");

            holder.tv_amountPlan1.setText("KES "+bean.getPlanPayAmountKES());
            holder.tv_amountPlan2.setText("$ "+bean.getPlanPayAmountUSD());

//            buy now button click
            holder.btn_buyNowSubsPlan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v,position);
                }
            });
        }
        else
        {
            CharSequence type = "Monthly";
            String plan_name = bean.getPlanName().toLowerCase();

            if(plan_name.contains(type.toString().toLowerCase()) )
            {
                holder.root_heading1.setBackgroundColor(context.getResources().getColor(R.color.monthly_orange_color));
                holder.root_heading2.setBackgroundColor(context.getResources().getColor(R.color.monthly_orange_color));
                holder.tv_durationPlan1.setText(bean.getPlanName());
                holder.tv_durationPlan2.setText(bean.getPlanName());
                holder.tv_amountPlan1.setText("KES "+bean.getPlanPayAmountKES());
                holder.tv_amountPlan2.setText("$ "+bean.getPlanPayAmountUSD());
            }
            else
            {
                holder.root_heading1.setBackgroundColor(context.getResources().getColor(R.color.annual_pink_color));
                holder.root_heading2.setBackgroundColor(context.getResources().getColor(R.color.annual_pink_color));
                holder.tv_durationPlan1.setText(bean.getPlanName());
                holder.tv_durationPlan2.setText(bean.getPlanName());
                holder.tv_amountPlan1.setText("KES "+bean.getPlanPayAmountKES());
                holder.tv_amountPlan2.setText("$ "+bean.getPlanPayAmountUSD());
            }

//              buy now button click
            holder.btn_buyNowSubsPlan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v,position);
                }
            });

        }

//
//
//                Toast.makeText(context, "Monthly Plan purchased successfully.", Toast.LENGTH_SHORT).show();
//                if(context instanceof HomeActivity)
//                {
//                    ((HomeActivity) context).getSupportFragmentManager().popBackStack();
//                }
//                else
//                {
//                    ((HomeActivityServicePro) context).getSupportFragmentManager().popBackStack();
//                }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void onItemClickListener(MyClickListener listener) {
        this.listener = listener;
    }

//  VIEWHOLDER CLASS
    class  SubsPlanViewHolder extends RecyclerView.ViewHolder
    {

        ///////////////////////
        private TextView tv_durationPlan1;
        private TextView tv_amountPlan1;
        private TextView tv_durationPlan2;
        private TextView tv_amountPlan2;
        private Button btn_buyNowSubsPlan;
        private RelativeLayout root_heading1,root_heading2;
        //////////////////////

        public SubsPlanViewHolder(View itemView) {
            super(itemView);

//      VIEWS
            root_heading1 = (RelativeLayout) itemView.findViewById(R.id.rel_headingPlan1);
            root_heading2 = (RelativeLayout) itemView.findViewById(R.id.rel_headingPlan2);

            tv_durationPlan1 = (TextView) itemView.findViewById(R.id.tv_durationPlan1);
            tv_amountPlan1 = (TextView) itemView.findViewById(R.id.tv_amountPlan1);

            tv_durationPlan2 = (TextView) itemView.findViewById(R.id.tv_durationPlan2);
            tv_amountPlan2 = (TextView) itemView.findViewById(R.id.tv_amountPlan2);

            btn_buyNowSubsPlan = (Button) itemView.findViewById(R.id.btn_buyNowSubsPlan);


//            Font.setFontRobotoRegular(monthlySubscriptionTitle,(AppCompatActivity)context);
//            Font.setFontRobotoRegular(monthlySubscriptionAmount,(AppCompatActivity)context);
//            Font.setFontRobotoRegular(monthlySubscription_benefit, (AppCompatActivity)context);
//            Font.setFontRalewayLight(monthlySubscription_description, (AppCompatActivity)context);
//
//            btn_MonthlySubscription_BuyNow = (Button) itemView.findViewById(R.id.button_buyNow_monthlysubscription);
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
//            btn_AnnualSubscription_BuyNow = (Button) itemView.findViewById(R.id.button_buyNow_annualSubscription);
//            Font.setFontButton(btn_AnnualSubscription_BuyNow ,(AppCompatActivity)context);



        }
    }

//    LISTENER
public interface MyClickListener
{
    void onItemClick(View v, int position);
}

}

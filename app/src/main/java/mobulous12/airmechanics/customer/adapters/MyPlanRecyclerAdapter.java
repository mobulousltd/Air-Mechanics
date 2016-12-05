package mobulous12.airmechanics.customer.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.PlanBean;
import mobulous12.airmechanics.beans.ServiceProviderBean;
import mobulous12.airmechanics.databinding.MyPlanCardBinding;
import mobulous12.airmechanics.fonts.Font;

/**
 * Created by mobulous12 on 7/10/16.
 */

public class MyPlanRecyclerAdapter extends RecyclerView.Adapter<MyPlanRecyclerAdapter.MyPlanViewHolder>{

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<PlanBean> planBeanArrayList;

    public MyPlanRecyclerAdapter(Context context, ArrayList<PlanBean> planBeanArrayList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.planBeanArrayList = planBeanArrayList;
    }
    @Override
    public MyPlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyPlanCardBinding binding=DataBindingUtil.inflate(inflater, R.layout.my_plan_card, parent, false);
        MyPlanViewHolder holder = new MyPlanViewHolder(binding.getRoot());

        return holder;
    }

    @Override
    public void onBindViewHolder(MyPlanViewHolder holder, int position)
    {
        final PlanBean planBean = planBeanArrayList.get(position);
        if(planBean.getPlanId().equalsIgnoreCase("1"))
        {
            holder.myPlanDuration.setText(context.getString(R.string.duration_myplan));
            holder.rootHeadingMyPlan.setBackgroundColor(context.getResources().getColor(R.color.booking_completed_color));
        }
        else {
            holder.myPlanDuration.setText(context.getString(R.string.duration_annual_subscription));
            holder.rootHeadingMyPlan.setBackgroundColor(context.getResources().getColor(R.color.annual_pink_color));
        }

        holder.myPlanAmount.setText("$"+planBean.getPlanAmount());
        holder.expiryDate.setText(planBean.getExpiryDate());

    }

    @Override
    public int getItemCount() {
        return planBeanArrayList.size();
    }

    class  MyPlanViewHolder extends RecyclerView.ViewHolder
    {

        private TextView myPlanDuration;
        private TextView myPlanAmount;
        private TextView expiryDateText;
        private TextView expiryDate;
        private TextView benefitHeading;
        private TextView descriptionMyPlan;
        private RelativeLayout rootHeadingMyPlan;

        public MyPlanViewHolder(View itemView) {
            super(itemView);


            myPlanDuration = (TextView) itemView.findViewById(R.id.textView_durationMyPlan);
            myPlanAmount = (TextView) itemView.findViewById(R.id.textView_amountMyPlan);
            expiryDateText = (TextView) itemView.findViewById(R.id.textView_expirydateTextMyPlan);
            expiryDate = (TextView) itemView.findViewById(R.id.textView_expirydateMyPlan);
            benefitHeading = (TextView) itemView.findViewById(R.id.textView_benefitsHeadingMyPlan);
            descriptionMyPlan = (TextView) itemView.findViewById(R.id.textView_descriptionMyPlan);
            rootHeadingMyPlan = (RelativeLayout) itemView.findViewById(R.id.relativelayout_headingMyPlan);

//            Font.setFontRobotoRegular(myPlanDuration, (AppCompatActivity)context);
//            Font.setFontRobotoRegular(myPlanAmount, (AppCompatActivity)context);
//            Font.setFontTextView(expiryDateText, (AppCompatActivity)context);
//            Font.setFontTextView(expiryDate, (AppCompatActivity)context);
//            Font.setFontRobotoRegular(benefitHeading, (AppCompatActivity)context);
//            Font.setFontRalewayLight(descriptionMyPlan, (AppCompatActivity)context);
        }
    }
}

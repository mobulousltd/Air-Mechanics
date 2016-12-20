package mobulous12.airmechanics.customer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.ArrayList;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.ServiceProviderBean;
import mobulous12.airmechanics.customer.activities.CalenderActivity;
import mobulous12.airmechanics.customer.activities.LoginActivity;
import mobulous12.airmechanics.customer.activities.ServiceProviderActivity;
import mobulous12.airmechanics.customer.activities.ServiceProviderDetailActivity;
import mobulous12.airmechanics.databinding.ServiceProviderCardBinding;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.utils.CircularImageView;
import mobulous12.airmechanics.volley.ServiceBean;

/**
 * Created by mobulous12 on 4/10/16.
 */

public class ServiceProviderRecyclerAdapter extends RecyclerView.Adapter<ServiceProviderRecyclerAdapter.ServiceProviderViewHolder> {

    private static MyClickListener listener;
    ArrayList<ServiceProviderBean> serviceProviderArrayList;
    Activity activity;
    private LayoutInflater inflater;
    private ServiceProviderActivity serviceProviderActivity;


    public ServiceProviderRecyclerAdapter(Activity activity, ArrayList<ServiceProviderBean> serviceProviderArrayList) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
        this.serviceProviderActivity= (ServiceProviderActivity) activity;
        this.serviceProviderArrayList=serviceProviderArrayList;


    }

    @Override
    public ServiceProviderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ServiceProviderCardBinding binding= DataBindingUtil.inflate(inflater, R.layout.service_provider_card,parent,false);
        ServiceProviderViewHolder holder = new ServiceProviderViewHolder(binding.getRoot());
        return holder;
    }

    @Override
    public void onBindViewHolder(ServiceProviderViewHolder holder, final int position) {

        final ServiceProviderBean providerBean = serviceProviderArrayList.get(position);
        holder.serviceProviderName.setText(providerBean.getName());
        String cat="";
        if(providerBean.getCategory().contains("two"))
        {
            cat=serviceProviderActivity.getString(R.string.two_wheeler);
        }
        if(providerBean.getCategory().contains("light"))
        {
            if(cat.isEmpty())
            {
                cat=serviceProviderActivity.getString(R.string.light_weight_vehicle);
            }
            else
            {
                cat+=", "+serviceProviderActivity.getString(R.string.light_weight_vehicle);
            }
        }
        if(providerBean.getCategory().contains("heavy"))
        {
            if(cat.isEmpty())
            {
                cat=serviceProviderActivity.getString(R.string.heavy_weight_vehicle);
            }
            else
            {
                cat+=", "+serviceProviderActivity.getString(R.string.heavy_weight_vehicle);
            }
        }
        if(providerBean.getWorkingdays().contains("0"))
        {
            holder.sun.setBackgroundResource(R.drawable.green_circle);
        }
        else
        {
            holder.sun.setBackgroundResource(R.drawable.gray_circle);
        }
        if(providerBean.getWorkingdays().contains("1"))
        {
            holder.mon.setBackgroundResource(R.drawable.green_circle);
        }
        else
        {
            holder.mon.setBackgroundResource(R.drawable.gray_circle);
        }
        if(providerBean.getWorkingdays().contains("2"))
        {
            holder.tue.setBackgroundResource(R.drawable.green_circle);
        }
        else
        {
            holder.tue.setBackgroundResource(R.drawable.gray_circle);
        }
        if (providerBean.getWorkingdays().contains("3")) {
            holder.wed.setBackgroundResource(R.drawable.green_circle);
        } else {
            holder.wed.setBackgroundResource(R.drawable.gray_circle);
        }
        if (providerBean.getWorkingdays().contains("4")) {
            holder.thur.setBackgroundResource(R.drawable.green_circle);
        } else {
            holder.thur.setBackgroundResource(R.drawable.gray_circle);
        }
        if (providerBean.getWorkingdays().contains("5")) {
            holder.fri.setBackgroundResource(R.drawable.green_circle);
        } else {
            holder.fri.setBackgroundResource(R.drawable.gray_circle);
        }
        if (providerBean.getWorkingdays().contains("6")) {
            holder.sat.setBackgroundResource(R.drawable.green_circle);
        } else {
            holder.sat.setBackgroundResource(R.drawable.gray_circle);
        }
        if(!cat.isEmpty())
        {
            holder.sp_owner.setText(cat);
        }
        holder.textView_ServiceProviderLocation.setText(providerBean.getAddress());
        AQuery aQuery=new AQuery(holder.circularImageView_ServiceProvider);
        if(providerBean.getProfile_thumb().isEmpty())
        {
            aQuery.id(holder.circularImageView_ServiceProvider).image(R.drawable.default_profile_pic);
        }
        else
        {
            aQuery.id(holder.circularImageView_ServiceProvider).image(providerBean.getProfile_thumb());
        }
        holder.askForQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!SharedPreferenceWriter.getInstance(activity).getBoolean(SPreferenceKey.LOGINKEY))
                {
                    Snackbar snackbar = Snackbar.make(v,"You must SignIn to avail exciting services!",Snackbar.LENGTH_LONG)
                            .setAction("SIGN IN", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent in1=new Intent(activity, LoginActivity.class);
                                    in1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    SharedPreferenceWriter.getInstance(activity).writeBooleanValue(SPreferenceKey.LOGINKEY, false);
                                    SharedPreferenceWriter.getInstance(activity).writeBooleanValue(SPreferenceKey.CUSTOMER_LOGIN, true);
                                    SharedPreferenceWriter.getInstance(activity).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, false);
                                    activity.startActivity(in1);
                                    activity.finishAffinity();
                                }
                            });
                    snackbar.setActionTextColor(activity.getResources().getColor(R.color.blue));
                    snackbar.show();
                }
                else {
                    Intent intent = new Intent(activity, CalenderActivity.class);
                    intent.putExtra("bean", providerBean);
                    activity.startActivity(intent);
                }
            }
        });
        holder.relative_ReviewAndRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceProviderArrayList.size();
    }

    class ServiceProviderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        CircularImageView circularImageView_ServiceProvider;
        RelativeLayout  relative_ReviewAndRating;
        TextView serviceProviderName,ratingServiceProvider,mon,tue,wed,thur,fri,sat,sun,
                sp_owner,textView_ServiceProviderLocation,askForQuote,review,reviewRating;

        public ServiceProviderViewHolder(View itemView)
        {
            super(itemView);
            relative_ReviewAndRating=(RelativeLayout)itemView.findViewById(R.id.relative_ReviewAndRating);
            circularImageView_ServiceProvider=(CircularImageView)itemView.findViewById(R.id.circularImageView_ServiceProvider);
            serviceProviderName = (TextView) itemView.findViewById(R.id.textView_ServiceProviderName);
            ratingServiceProvider = (TextView) itemView.findViewById(R.id.textView_RatingServiceProvider);
            mon = (TextView) itemView.findViewById(R.id.textView_mon_serviceProviderDetail);
            tue = (TextView) itemView.findViewById(R.id.textView_tue_serviceProviderDetail);
            wed = (TextView) itemView.findViewById(R.id.textView_wed_serviceProviderDetail);
            thur = (TextView) itemView.findViewById(R.id.textView_thur_serviceProviderDetail);
            fri = (TextView) itemView.findViewById(R.id.textView_fri_serviceProviderDetail);
            sat = (TextView) itemView.findViewById(R.id.textView_sat_serviceProviderDetail);
            sun = (TextView) itemView.findViewById(R.id.textView_sun_serviceProviderDetail);
            sp_owner = (TextView) itemView.findViewById(R.id.sp_owner);
            textView_ServiceProviderLocation = (TextView) itemView.findViewById(R.id.textView_ServiceProviderLocation);
            askForQuote = (TextView) itemView.findViewById(R.id.textView_askForQuote);
            review = (TextView) itemView.findViewById(R.id.button_Review);
            reviewRating = (TextView) itemView.findViewById(R.id.imageView_ReviewRating);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                default:
                    listener.onItemClick(getPosition(), v);
                    break;
            }
        }
    }
    public void onItemClickListener(MyClickListener listener) {
        this.listener = listener;
    }

    public interface MyClickListener
    {
        public void onItemClick(int position, View v);
    }


}



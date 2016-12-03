package mobulous12.airmechanics.customer.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
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
import mobulous12.airmechanics.customer.activities.ServiceProviderActivity;
import mobulous12.airmechanics.customer.activities.ServiceProviderDetailActivity;
import mobulous12.airmechanics.databinding.ServiceProviderCardBinding;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.utils.CircularImageView;
import mobulous12.airmechanics.volley.ServiceBean;

/**
 * Created by mobulous12 on 4/10/16.
 */

public class ServiceProviderRecyclerAdapter extends RecyclerView.Adapter<ServiceProviderRecyclerAdapter.ServiceProviderViewHolder> {


    ArrayList<ServiceProviderBean> serviceProviderArrayList;
    private Context context;
    private LayoutInflater inflater;
    private ServiceProviderActivity serviceProviderActivity;


    public ServiceProviderRecyclerAdapter(Context context, ArrayList<ServiceProviderBean> serviceProviderArrayList) {
      this.context = context;
        inflater = LayoutInflater.from(context);
      serviceProviderActivity= (ServiceProviderActivity) context;
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

        holder.mainRootCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(context,ServiceProviderDetailActivity.class);
                intent.putExtra("bean",providerBean);
                context.startActivity(intent);

            }
        });




    }



    @Override
    public int getItemCount() {
        return serviceProviderArrayList.size();
    }

    class ServiceProviderViewHolder extends RecyclerView.ViewHolder
    {
        CardView mainRootCardView;
        CircularImageView circularImageView_ServiceProvider;
        RelativeLayout relative_allDetailsServiceProvider;
        TextView serviceProviderName,ratingServiceProvider,mon,tue,wed,thur,fri,sat,sun,
                sp_owner,textView_ServiceProviderLocation,askForQuote,review,reviewRating,textView_AskForQuote;

        public ServiceProviderViewHolder(View itemView)
        {
            super(itemView);
            mainRootCardView=(CardView)itemView.findViewById(R.id.cardView_serviceprovider);
            circularImageView_ServiceProvider=(CircularImageView)itemView.findViewById(R.id.circularImageView_ServiceProvider);
            relative_allDetailsServiceProvider = (RelativeLayout) itemView.findViewById(R.id.relative_allDetailsServiceProvider);

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


        }
    }



}



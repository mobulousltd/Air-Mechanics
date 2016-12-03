package mobulous12.airmechanics.customer.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.ArrayList;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.ServiceProviderBean;
import mobulous12.airmechanics.customer.activities.ServiceProviderActivity;
import mobulous12.airmechanics.databinding.FavoritesCardBinding;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.utils.CircularImageView;

/**
 * Created by mobulous12 on 6/10/16.
 */

public class FavoritesRecyclerAdapter  extends RecyclerView.Adapter<FavoritesRecyclerAdapter.FavoritesViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ServiceProviderBean> serviceProviderArrayList;


    public FavoritesRecyclerAdapter(Context context,ArrayList<ServiceProviderBean> serviceProviderArrayList) {
        this.context = context;
        inflater = LayoutInflater.from(context);

        this.serviceProviderArrayList=serviceProviderArrayList;
    }

    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        FavoritesCardBinding binding=DataBindingUtil.inflate(inflater, R.layout.favorites_card, parent, false);
        View view=binding.getRoot();

        return new FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoritesViewHolder holder, final int position) {

        final ServiceProviderBean providerBean = serviceProviderArrayList.get(position);

        holder.serviceProviderName.setText(providerBean.getName());
        AQuery aQuery=new AQuery(holder.circImage_Favorites);
        if(providerBean.getProfile_thumb().isEmpty())
        {
            aQuery.id(holder.circImage_Favorites).image(R.drawable.default_profile_pic);
        }
        else
        {
            aQuery.id(holder.circImage_Favorites).image(providerBean.getProfile_thumb());
        }

        //work days
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
        }
        else
        {
            holder.wed.setBackgroundResource(R.drawable.gray_circle);
        }
        if (providerBean.getWorkingdays().contains("4")) {
            holder.thur.setBackgroundResource(R.drawable.green_circle);
        }
        else
        {
            holder.thur.setBackgroundResource(R.drawable.gray_circle);
        }
        if (providerBean.getWorkingdays().contains("5"))
        {
            holder.fri.setBackgroundResource(R.drawable.green_circle);
        }
        else
        {
            holder.fri.setBackgroundResource(R.drawable.gray_circle);
        }
        if (providerBean.getWorkingdays().contains("6"))
        {
            holder.sat.setBackgroundResource(R.drawable.green_circle);
        }
        else
        {
            holder.sat.setBackgroundResource(R.drawable.gray_circle);
        }


/*    Rating stars  */
        try {
            int r = Math.round(Float.parseFloat(providerBean.getRating()));
            switch (r)
            {
                case 1:
                    holder.star1.setImageResource(R.drawable.yellow_star);
                    break;
                case 2:
                    holder.star1.setImageResource(R.drawable.yellow_star);
                    holder.star2.setImageResource(R.drawable.yellow_star);
                    break;
                case 3:
                    holder.star1.setImageResource(R.drawable.yellow_star);
                    holder.star2.setImageResource(R.drawable.yellow_star);
                    holder.star3.setImageResource(R.drawable.yellow_star);
                    break;
                case 4:
                    holder.star1.setImageResource(R.drawable.yellow_star);
                    holder.star2.setImageResource(R.drawable.yellow_star);
                    holder.star3.setImageResource(R.drawable.yellow_star);
                    holder.star4.setImageResource(R.drawable.yellow_star);
                    break;
                case 5:
                    holder.star1.setImageResource(R.drawable.yellow_star);
                    holder.star2.setImageResource(R.drawable.yellow_star);
                    holder.star3.setImageResource(R.drawable.yellow_star);
                    holder.star4.setImageResource(R.drawable.yellow_star);
                    holder.star5.setImageResource(R.drawable.yellow_star);
                    break;
                default:
                    holder.star1.setImageResource(R.drawable.gray_star);
                    holder.star2.setImageResource(R.drawable.gray_star);
                    holder.star3.setImageResource(R.drawable.gray_star);
                    holder.star4.setImageResource(R.drawable.gray_star);
                    holder.star5.setImageResource(R.drawable.gray_star);
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        holder.tv_price.setText("Price $"+providerBean.getMin_charge());

    }

    @Override
    public int getItemCount() {
        return serviceProviderArrayList.size();
    }


    class FavoritesViewHolder extends RecyclerView.ViewHolder
    {

        private TextView serviceProviderName;
        private TextView mon, tue, wed, thur, fri, sat, sun;
        private ImageView star1,star2,star3,star4,star5;
        private TextView tv_price;
        private TextView review;
        private TextView reviewRating;
        private CircularImageView circImage_Favorites;


        public FavoritesViewHolder(View itemView)
        {
            super(itemView);

            serviceProviderName = (TextView) itemView.findViewById(R.id.textView_serviceProviderName_favorites);
//            Font.setFontTextView(serviceProviderName, (AppCompatActivity)context);
//
            circImage_Favorites = (CircularImageView) itemView.findViewById(R.id.circularImageView_Favorites);
            star1 = (ImageView) itemView.findViewById(R.id.imageView_star1);
            star2 = (ImageView) itemView.findViewById(R.id.imageView_star2);
            star3 = (ImageView) itemView.findViewById(R.id.imageView_star3);
            star4 = (ImageView) itemView.findViewById(R.id.imageView_star4);
            star5 = (ImageView) itemView.findViewById(R.id.imageView_star5);

            mon = (TextView) itemView.findViewById(R.id.textView_mon_serviceProviderDetail);
//            Font.setFontTextView(mon, (AppCompatActivity)context);
//
            tue = (TextView) itemView.findViewById(R.id.textView_tue_serviceProviderDetail);
//            Font.setFontTextView(tue, (AppCompatActivity)context);
//
            wed = (TextView) itemView.findViewById(R.id.textView_wed_serviceProviderDetail);
//            Font.setFontTextView(wed, (AppCompatActivity)context);
//
            thur = (TextView) itemView.findViewById(R.id.textView_thur_serviceProviderDetail);
//            Font.setFontTextView(thur, (AppCompatActivity)context);
//
            fri = (TextView) itemView.findViewById(R.id.textView_fri_serviceProviderDetail);
//            Font.setFontTextView(fri, (AppCompatActivity)context);
//
            sat = (TextView) itemView.findViewById(R.id.textView_sat_serviceProviderDetail);
//            Font.setFontTextView(sat, (AppCompatActivity)context);
//
            sun = (TextView) itemView.findViewById(R.id.textView_sun_serviceProviderDetail);
//            Font.setFontTextView(sun, (AppCompatActivity)context);
//
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
//            Font.setFontTextView(askForQuote, (AppCompatActivity)context);
//
            review = (TextView) itemView.findViewById(R.id.button_Review);
//            Font.setFontTextView(review, (AppCompatActivity)context);
//
            reviewRating = (TextView) itemView.findViewById(R.id.imageView_ReviewRating);
//            Font.setFontTextView(reviewRating, (AppCompatActivity)context);
        }
    }
}

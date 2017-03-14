package mobulous12.airmechanics.customer.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
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
import mobulous12.airmechanics.databinding.MyBookingCardBinding;
import mobulous12.airmechanics.databinding.RatingScreenListingRowBinding;
import mobulous12.airmechanics.utils.CircleImageView;


/**
 * Created by mobulous11 on 22/12/16.
 */

public class RatingScreenListingAdapter extends RecyclerView.Adapter<RatingScreenListingAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<ServiceProviderBean> list;

    public RatingScreenListingAdapter(Context context, ArrayList<ServiceProviderBean> list)
    {
        inflater = LayoutInflater.from(context);
        this.list =list;
    }

    @Override
    public RatingScreenListingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        RatingScreenListingRowBinding binding=DataBindingUtil.inflate(inflater, R.layout.rating_screen_listing_row, parent, false);
        MyViewHolder holder = new MyViewHolder(binding.getRoot());
        return holder;
    }

    @Override
    public void onBindViewHolder(RatingScreenListingAdapter.MyViewHolder holder, int position) {

        ServiceProviderBean bean = list.get(position);

        holder.name.setText(bean.getName());
        holder.review.setText(bean.getReviews());
        AQuery aQuery = new AQuery(holder.image);
        aQuery.id(holder.image).image(bean.getProfile());
        switch (bean.getRating())
        {
            case "1":
                holder.star1.setImageResource(R.drawable.star_rate);
                break;
            case "2":
                holder.star1.setImageResource(R.drawable.star_rate);
                holder.star2.setImageResource(R.drawable.star_rate);
                break;
            case "3":
                holder.star1.setImageResource(R.drawable.star_rate);
                holder.star2.setImageResource(R.drawable.star_rate);
                holder.star3.setImageResource(R.drawable.star_rate);
                break;
            case "4":
                holder.star1.setImageResource(R.drawable.star_rate);
                holder.star2.setImageResource(R.drawable.star_rate);
                holder.star3.setImageResource(R.drawable.star_rate);
                holder.star4.setImageResource(R.drawable.star_rate);
                break;
            case "5":
                holder.star1.setImageResource(R.drawable.star_rate);
                holder.star2.setImageResource(R.drawable.star_rate);
                holder.star3.setImageResource(R.drawable.star_rate);
                holder.star4.setImageResource(R.drawable.star_rate);
                holder.star5.setImageResource(R.drawable.star_rate);
                break;

        }

    }

    @Override
    public int getItemCount() {
//        return list.size();
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private CircleImageView image;
        private TextView name;
        private TextView review;
        private ImageView star1, star2, star3, star4, star5;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (CircleImageView) itemView.findViewById(R.id.image_rating_screen_listing);
            name = (TextView) itemView.findViewById(R.id.title_rating_screen_listing);
            review = (TextView) itemView.findViewById(R.id.review_rating_screen_listing);
            star1 = (ImageView) itemView.findViewById(R.id.imageView_star1_rating_screen_listing);
            star2 = (ImageView) itemView.findViewById(R.id.imageView_star2_rating_screen_listing);
            star3 = (ImageView) itemView.findViewById(R.id.imageView_star3_rating_screen_listing);
            star4 = (ImageView) itemView.findViewById(R.id.imageView_star4_rating_screen_listing);
            star5 = (ImageView) itemView.findViewById(R.id.imageView_star5_rating_screen_listing);
        }
    }
}

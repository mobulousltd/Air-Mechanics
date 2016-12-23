package mobulous12.airmechanics.customer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.ArrayList;
import java.util.List;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.ServiceProviderBean;
import mobulous12.airmechanics.utils.CircularImageView;

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
    public RatingScreenListingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = inflater.inflate(R.layout.rating_screen_listing_row, parent, false);
        MyViewHolder holder = new MyViewHolder(row);
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
                holder.star1.setImageResource(R.drawable.star);
                break;
            case "2":
                holder.star1.setImageResource(R.drawable.star);
                holder.star2.setImageResource(R.drawable.star);
                break;
            case "3":
                holder.star1.setImageResource(R.drawable.star);
                holder.star2.setImageResource(R.drawable.star);
                holder.star3.setImageResource(R.drawable.star);
                break;
            case "4":
                holder.star1.setImageResource(R.drawable.star);
                holder.star2.setImageResource(R.drawable.star);
                holder.star3.setImageResource(R.drawable.star);
                holder.star4.setImageResource(R.drawable.star);
                break;
            case "5":
                holder.star1.setImageResource(R.drawable.star);
                holder.star2.setImageResource(R.drawable.star);
                holder.star3.setImageResource(R.drawable.star);
                holder.star4.setImageResource(R.drawable.star);
                holder.star5.setImageResource(R.drawable.star);
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
        private CircularImageView image;
        private TextView name;
        private TextView review;
        private ImageView star1, star2, star3, star4, star5;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (CircularImageView) itemView.findViewById(R.id.image_rating_screen_listing);
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

package mobulous12.airmechanics.customer.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.ArrayList;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.ServiceProviderBean;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.beans.BookingBean;
import mobulous12.airmechanics.customer.fragments.BookingDetailFragment;
import mobulous12.airmechanics.databinding.MyBookingCardBinding;
import mobulous12.airmechanics.utils.CircularImageView;

/**
 * Created by mobulous12 on 6/10/16.
 */

public class MyBookingsRecyclerAdapter extends RecyclerView.Adapter<MyBookingsRecyclerAdapter.BookingsViewHolder> {
    private static MyClickListener listener;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<BookingBean> arrayList;


    public MyBookingsRecyclerAdapter(Context context, ArrayList<BookingBean> arrayList)
    {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.arrayList=arrayList;
    }

    @Override
    public BookingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyBookingCardBinding binding=DataBindingUtil.inflate(inflater,R.layout.my_booking_card,parent,false);
        BookingsViewHolder holder = new BookingsViewHolder(binding.getRoot());

        return holder;
    }

    @Override
    public void onBindViewHolder(BookingsViewHolder holder, int position)
    {
        final   BookingBean bookingBean = arrayList.get(position);

        if(bookingBean.getStatus().equalsIgnoreCase("pending"))
        {
            holder.bookingStatus.setText("Pending");
            holder.bookingStatus.setBackgroundColor(context.getResources().getColor(R.color.booking_pending_color));
        }
        else if(bookingBean.getStatus().equalsIgnoreCase("complete"))
        {
            holder.bookingStatus.setText("Completed!");
            holder.bookingStatus.setBackgroundColor(context.getResources().getColor(R.color.booking_completed_color));

        }
        else
        {
            holder.bookingStatus.setText("Inprogress");
            holder.bookingStatus.setBackgroundColor(context.getResources().getColor(R.color.booking_inprogress_color));
        }

        holder.myBookingName.setText(bookingBean.getUserName());
        holder.bookingAmount.setText(bookingBean.getMinCharge());
        holder.bookingDate.setText(bookingBean.getRequestDate());
        holder.bookingTime.setText(bookingBean.getOpenTime()+" - "+bookingBean.getCloseTime());

        AQuery aQuery=new AQuery(holder.circularImageView_myBooking);
        if(bookingBean.getProfile_thumb().isEmpty())
        {
            aQuery.id(holder.circularImageView_myBooking).image(R.drawable.default_profile_pic);
        }
        else
        {
            aQuery.id(holder.circularImageView_myBooking).image(bookingBean.getProfile_thumb());
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class BookingsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private final CircularImageView circularImageView_myBooking;
        private TextView myBookingName;
        private TextView bookingAmount;
        private TextView bookingDate;
        private TextView bookingTime;
        private TextView bookingStatus;
        ImageView imageView_myBookingRightArrow;

        public BookingsViewHolder(View itemView) {
            super(itemView);

            imageView_myBookingRightArrow = (ImageView) itemView.findViewById(R.id.imageView_myBookingRightArrow);
            circularImageView_myBooking = (CircularImageView) itemView.findViewById(R.id.circularImageView_myBooking);

            myBookingName = (TextView) itemView.findViewById(R.id.textView_myBookingName);
            bookingAmount = (TextView) itemView.findViewById(R.id.textView_myBookingAmount);
            bookingDate= (TextView) itemView.findViewById(R.id.textView_myBookingDate);
            bookingTime= (TextView) itemView.findViewById(R.id.textView_myBookingTime);
            bookingStatus= (TextView) itemView.findViewById(R.id.textView_bookingStatus);
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

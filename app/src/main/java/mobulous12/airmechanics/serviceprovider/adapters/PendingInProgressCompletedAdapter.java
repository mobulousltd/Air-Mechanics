package mobulous12.airmechanics.serviceprovider.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.ArrayList;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.BookingBean;
import mobulous12.airmechanics.serviceprovider.fragments.CompletedFragment;
import mobulous12.airmechanics.serviceprovider.fragments.InProgressFragment;
import mobulous12.airmechanics.serviceprovider.fragments.PendingFragment;
import mobulous12.airmechanics.utils.CircularImageView;

/**
 * Created by mobulous11 on 6/2/17.
 */

public class PendingInProgressCompletedAdapter extends RecyclerView.Adapter<PendingInProgressCompletedAdapter.PendingInProgressCompletedHolder> {

    private Context context;
    private LayoutInflater inflater;
    private static PendingInProgressCompletedAdapter.ClickListener listener;
    private ArrayList<BookingBean> beanArrayList;

    public PendingInProgressCompletedAdapter(Context context, ArrayList<BookingBean> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        beanArrayList = list;
    }

    @Override
    public PendingInProgressCompletedAdapter.PendingInProgressCompletedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = inflater.inflate(R.layout.in_progress_frag_cards, parent, false);
        return new PendingInProgressCompletedHolder(row);
    }

    @Override
    public void onBindViewHolder(PendingInProgressCompletedAdapter.PendingInProgressCompletedHolder holder, int position) {

        BookingBean bean = beanArrayList.get(position);
        holder.jobOrderName_1_inProg.setText(bean.getUserName());
        holder.jobOrderAmount_1_inProg.setText(bean.getMinCharge());
        holder.jobOrderDate_1_inProg.setText(bean.getRequestDate());
        holder.jobOrderTime_1_inProg.setText(bean.getOpenTime()+" - "+bean.getCloseTime());

        AQuery aQuery = new AQuery(holder.circularImageView_1_inProgressFrag);
        if(bean.getUserImage().isEmpty())
        {
            aQuery.id(holder.circularImageView_1_inProgressFrag).image(R.drawable.default_profile_pic);
        }
        else
        {
            aQuery.id(holder.circularImageView_1_inProgressFrag).image(bean.getUserImage());
        }

        String status = bean.getStatus();

        if (status != null)
        {
            if (status.equalsIgnoreCase("pending"))
            {
                holder.jobOrderStatus_1_inProg.setBackgroundColor(context.getResources().getColor(R.color.booking_pending_color));
                holder.jobOrderStatus_1_inProg.setText("Pending");
            }
            else if (status.equalsIgnoreCase("process"))
            {
                holder.jobOrderStatus_1_inProg.setBackgroundColor(context.getResources().getColor(R.color.booking_inprogress_color));
                holder.jobOrderStatus_1_inProg.setText("InProgress");
            }
            else if (status.equalsIgnoreCase("billgenerate") || status.equalsIgnoreCase("complete"))
            {
                holder.jobOrderStatus_1_inProg.setBackgroundColor(context.getResources().getColor(R.color.booking_completed_color));
                holder.jobOrderStatus_1_inProg.setText("Completed");
            }
        }

    }

    @Override
    public int getItemCount() {
        return beanArrayList.size();
    }

protected class PendingInProgressCompletedHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView jobOrderName_1_inProg;
        private TextView jobOrderAmount_1_inProg;
        private TextView jobOrderDate_1_inProg;
        private TextView jobOrderTime_1_inProg;
        private TextView jobOrderStatus_1_inProg;
        CircularImageView circularImageView_1_inProgressFrag;

        public PendingInProgressCompletedHolder(View itemView) {
            super(itemView);
            circularImageView_1_inProgressFrag = (CircularImageView) itemView.findViewById(R.id.circularImageView_1_inProgressFrag);
            jobOrderName_1_inProg = (TextView) itemView.findViewById(R.id.textView_jobOrderName_1_inProgress);
            jobOrderAmount_1_inProg = (TextView) itemView.findViewById(R.id.textView_jobOrderAmount_1_inProgress);
            jobOrderDate_1_inProg= (TextView) itemView.findViewById(R.id.textView_jobOrderDate_1_inProgress);
            jobOrderTime_1_inProg= (TextView) itemView.findViewById(R.id.textView_jobOrderTime_1_inProgress);
            jobOrderStatus_1_inProg= (TextView) itemView.findViewById(R.id.textView_jobOrderStatus_1_inProgress);

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
                    listener.onItemClick(getPosition(), v);
        }

    }
    public void onItemClickListener(PendingInProgressCompletedAdapter.ClickListener listener) {
        this.listener = listener;
    }

    public interface ClickListener
    {
        void onItemClick(int position, View v);
    }
}

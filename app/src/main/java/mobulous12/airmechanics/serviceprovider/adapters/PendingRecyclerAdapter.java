package mobulous12.airmechanics.serviceprovider.adapters;

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

import java.lang.reflect.Array;
import java.util.ArrayList;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.BookingBean;
import mobulous12.airmechanics.databinding.PendingFragCardsBinding;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.utils.CircularImageView;

/**
 * Created by mobulous12 on 18/10/16.
 */

public class PendingRecyclerAdapter extends RecyclerView.Adapter<PendingRecyclerAdapter.PendingViewHolder>{

    private Context context;
    private LayoutInflater inflater;
    private static MyClickListener listener;
    private ArrayList<BookingBean> beanArrayList;

    public PendingRecyclerAdapter(Context context, ArrayList<BookingBean> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        beanArrayList = list;
    }

    @Override
    public PendingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PendingFragCardsBinding binding=DataBindingUtil.inflate(inflater,R.layout.pending_frag_cards,parent,false);
        PendingViewHolder holder = new PendingViewHolder(binding.getRoot());
        return holder;
    }

    @Override
    public void onBindViewHolder(PendingViewHolder holder, int position) {

        BookingBean bean = beanArrayList.get(position);
        holder.jobOrderName_1.setText(bean.getUserName());
        holder.jobOrderAmount_1.setText("$"+bean.getMinCharge());
        holder.jobOrderDate_1.setText(bean.getRequestDate());
        holder.jobOrderTime_1.setText(bean.getOpenTime()+" - "+bean.getCloseTime());


        AQuery aQuery = new AQuery(holder.circularImageView);
        if(bean.getUserImage().isEmpty())
        {
            aQuery.id(holder.circularImageView).image(R.drawable.default_profile_pic);
        }
        else
        {
            aQuery.id(holder.circularImageView).image(bean.getUserImage());
        }

    }

    @Override
    public int getItemCount() {
        return beanArrayList.size();
    }


    class PendingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircularImageView circularImageView;
        private TextView jobOrderName_1;
        private TextView jobOrderAmount_1;
        private TextView jobOrderDate_1;
        private TextView jobOrderTime_1;
        private TextView jobOrderStatus_1;

        public PendingViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            circularImageView = (CircularImageView) itemView.findViewById(R.id.circularImageView_1_pendingFrag);
            jobOrderName_1 = (TextView) itemView.findViewById(R.id.textView_jobOrderName_1);
            jobOrderAmount_1 = (TextView) itemView.findViewById(R.id.textView_jobOrderAmount_1);
            jobOrderDate_1 = (TextView) itemView.findViewById(R.id.textView_jobOrderDate_1);
            jobOrderTime_1 = (TextView) itemView.findViewById(R.id.textView_jobOrderTime_1);
            jobOrderStatus_1 = (TextView) itemView.findViewById(R.id.textView_jobOrderStatus_1);

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

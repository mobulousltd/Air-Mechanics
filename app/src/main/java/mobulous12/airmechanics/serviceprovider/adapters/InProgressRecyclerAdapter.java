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

import java.util.ArrayList;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.BookingBean;
import mobulous12.airmechanics.databinding.InProgressFragCardsBinding;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.utils.CircularImageView;

/**
 * Created by mobulous12 on 18/10/16.
 */

public class InProgressRecyclerAdapter extends RecyclerView.Adapter<InProgressRecyclerAdapter.InProgressViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private static MyClickListener listener;
    private ArrayList<BookingBean> beanArrayList;

    public InProgressRecyclerAdapter(Context context, ArrayList<BookingBean> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        beanArrayList = list;
    }

    @Override
    public InProgressViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        InProgressFragCardsBinding binding=DataBindingUtil.inflate(inflater, R.layout.in_progress_frag_cards,parent,false);
        InProgressViewHolder holder = new InProgressViewHolder(binding.getRoot());
        return holder;
    }

    @Override
    public void onBindViewHolder(InProgressViewHolder holder, int position) {

        BookingBean bean = beanArrayList.get(position);
        holder.jobOrderName_1_inProg.setText(bean.getUserName());
        holder.jobOrderAmount_1_inProg.setText("$"+bean.getMinCharge());
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


    }

    @Override
    public int getItemCount() {
        return beanArrayList.size();
    }

   public class InProgressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView jobOrderName_1_inProg;
        private TextView jobOrderAmount_1_inProg;
        private TextView jobOrderDate_1_inProg;
        private TextView jobOrderTime_1_inProg;
        private TextView jobOrderStatus_1_inProg;
        CircularImageView circularImageView_1_inProgressFrag;


        public InProgressViewHolder(View itemView) {
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

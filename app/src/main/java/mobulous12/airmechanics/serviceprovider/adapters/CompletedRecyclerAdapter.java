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
import mobulous12.airmechanics.databinding.CompletedFragCardsBinding;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.utils.CircularImageView;

/**
 * Created by mobulous12 on 18/10/16.
 */

public class CompletedRecyclerAdapter extends RecyclerView.Adapter<CompletedRecyclerAdapter.CompletedViewHolder>{
    private static MyClickListener listener;

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<BookingBean> beanArrayList;

    public CompletedRecyclerAdapter(Context context, ArrayList<BookingBean> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        beanArrayList = list;
    }

    @Override
    public CompletedViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        CompletedFragCardsBinding binding=DataBindingUtil.inflate(inflater,R.layout.completed_frag_cards,parent,false);
        CompletedViewHolder holder = new CompletedViewHolder(binding.getRoot());
        return holder;
    }

    @Override
    public void onBindViewHolder(CompletedViewHolder holder, int position)
    {
        BookingBean bean = beanArrayList.get(position);
        holder.jobOrderName_1_Comp.setText(bean.getUserName());
        holder.jobOrderAmount_1_Comp.setText("$"+bean.getMinCharge());
        holder.jobOrderDate_1_Comp.setText(bean.getRequestDate());
        holder.jobOrderTime_1_Comp.setText(bean.getOpenTime()+" - "+bean.getCloseTime());

        AQuery aQuery = new AQuery(holder.circularImageView_1_completedFrag);
        if(bean.getUserImage().isEmpty())
        {
            aQuery.id(holder.circularImageView_1_completedFrag).image(R.drawable.default_profile_pic);
        }
        else
        {
            aQuery.id(holder.circularImageView_1_completedFrag).image(bean.getUserImage());
        }
    }

    @Override
    public int getItemCount() {
        return beanArrayList.size();
    }


    public class CompletedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        private TextView jobOrderName_1_Comp;
        private TextView jobOrderAmount_1_Comp;
        private TextView jobOrderDate_1_Comp;
        private TextView jobOrderTime_1_Comp;
        private TextView jobOrderStatus_1_Comp;
        CircularImageView circularImageView_1_completedFrag;

        public CompletedViewHolder(View itemView) {
            super(itemView);
            circularImageView_1_completedFrag = (CircularImageView) itemView.findViewById(R.id.circularImageView_1_completedFrag);

            jobOrderName_1_Comp = (TextView) itemView.findViewById(R.id.textView_jobOrderName_1_completed);

            jobOrderAmount_1_Comp = (TextView) itemView.findViewById(R.id.textView_jobOrderAmount_1_completed);

            jobOrderDate_1_Comp= (TextView) itemView.findViewById(R.id.textView_jobOrderDate_1_completed);

            jobOrderTime_1_Comp= (TextView) itemView.findViewById(R.id.textView_jobOrderTime_1_completed);

            jobOrderStatus_1_Comp= (TextView) itemView.findViewById(R.id.textView_jobOrderStatus_1_completed);

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

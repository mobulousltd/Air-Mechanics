package mobulous12.airmechanics.customer.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.BookingBean;
import mobulous12.airmechanics.databinding.RejectedScreenCardsBinding;
import mobulous12.airmechanics.fonts.Font;

/**
 * Created by mobulous12 on 12/10/16.
 */

public class RejectedRecyclerAapter extends RecyclerView.Adapter<RejectedRecyclerAapter.RejectedViewHolder>{


    private Context context;
    private LayoutInflater inflater;
    private ArrayList<BookingBean> bookingBeanArrayList;



    public RejectedRecyclerAapter(Context context, ArrayList<BookingBean> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        bookingBeanArrayList = list;
    }
    @Override
    public RejectedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RejectedScreenCardsBinding binding=DataBindingUtil.inflate(inflater, R.layout.rejected_screen_cards,parent,false);
        RejectedViewHolder holder = new RejectedViewHolder(binding.getRoot());
        return holder;
    }

    @Override
    public void onBindViewHolder(RejectedViewHolder holder, int position) {

        if (bookingBeanArrayList != null) {
            BookingBean bean = bookingBeanArrayList.get(position);
            holder.textView_1_rejectedName.setText(bean.getUserName());
            holder.textView_1_rejectedAmount.setText(bean.getMinCharge());
            holder.textView_1_rejectedDate.setText(bean.getRequestDate());
//            holder.textView_1_rejectedStatus.setText(bean.getStatus());
            holder.textView_1_rejectedTime.setText(bean.getRequestTime());
        }
    }

    @Override
    public int getItemCount() {
        return bookingBeanArrayList.size();
    }

    class  RejectedViewHolder extends RecyclerView.ViewHolder {

        private TextView textView_1_rejectedName,textView_1_rejectedAmount,
                textView_1_rejectedDate,textView_1_rejectedTime,textView_1_rejectedStatus;


        public RejectedViewHolder(View itemView) {
            super(itemView);

            textView_1_rejectedName = (TextView) itemView.findViewById(R.id.textView_1_rejectedName);
            textView_1_rejectedAmount = (TextView) itemView.findViewById(R.id.textView_1_rejectedAmount);
            textView_1_rejectedDate = (TextView) itemView.findViewById(R.id.textView_1_rejectedDate);
            textView_1_rejectedTime = (TextView) itemView.findViewById(R.id.textView_1_rejectedTime);
            textView_1_rejectedStatus = (TextView) itemView.findViewById(R.id.textView_1_rejectedStatus);
//
//            textView_2_rejectedName = (TextView) itemView.findViewById(R.id.textView_2_rejectedName);
//            textView_2_rejectedAmount = (TextView) itemView.findViewById(R.id.textView_2_rejectedAmount);
//            textView_2_rejectedDate = (TextView) itemView.findViewById(R.id.textView_2_rejectedDate);
//            textView_2_rejectedTime = (TextView) itemView.findViewById(R.id.textView_2_rejectedTime);
//            textView_2_rejectedStatus = (TextView) itemView.findViewById(R.id.textView_2_rejectedStatus);
//
//
//            /* set Fonts*/
//
//            Font.setFontTextView(textView_1_rejectedName,(AppCompatActivity)context);
//            Font.setFontTextView(textView_1_rejectedAmount,(AppCompatActivity)context);
//            Font.setFontTextView(textView_1_rejectedDate,(AppCompatActivity)context);
//            Font.setFontTextView(textView_1_rejectedTime,(AppCompatActivity)context);
//            Font.setFontTextView(textView_1_rejectedStatus,(AppCompatActivity)context);
//
//            Font.setFontTextView(textView_2_rejectedName,(AppCompatActivity)context);
//            Font.setFontTextView(textView_2_rejectedAmount,(AppCompatActivity)context);
//            Font.setFontTextView(textView_2_rejectedDate,(AppCompatActivity)context);
//            Font.setFontTextView(textView_2_rejectedTime,(AppCompatActivity)context);
//            Font.setFontTextView(textView_2_rejectedStatus,(AppCompatActivity)context);

        }
    }
}

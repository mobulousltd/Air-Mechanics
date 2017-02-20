package mobulous12.airmechanics.serviceprovider.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.EarningsBean;
import mobulous12.airmechanics.databinding.FragmentMyEarningsSpBinding;


/**
 * Created by mobulous12 on 20/2/17.
 */

public class MyEarningsAdapter extends RecyclerView.Adapter<MyEarningsAdapter.EarningsViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<EarningsBean> arrayList;

    public MyEarningsAdapter(Context context,ArrayList<EarningsBean> arrayList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.arrayList = arrayList;

    }

    @Override
    public EarningsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FragmentMyEarningsSpBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_earnings_sp,parent,false);
        EarningsViewHolder  holder = new EarningsViewHolder(binding.getRoot());
        return holder;
    }

    @Override
    public void onBindViewHolder(EarningsViewHolder holder, int position) {

        final EarningsBean bean = arrayList.get(position);

        holder.customerName.setText(bean.getCustomerName());
        holder.paidAmount.setText(bean.getPaidAmount());
        holder.date.setText(bean.getDate());
        holder.address.setText(bean.getAddress());

    }

    @Override
    public int getItemCount() {
        return (this.arrayList).size();
    }


//    VIEW HOLDER CLASS
    class EarningsViewHolder extends RecyclerView.ViewHolder {

        private TextView customerName,date,address,paidAmount;

        public EarningsViewHolder(View itemView) {
            super(itemView);

            customerName = (TextView) itemView.findViewById(R.id.textView_nameMyEarning);
            paidAmount = (TextView) itemView.findViewById(R.id.textView_amountMyEarning);
            date = (TextView) itemView.findViewById(R.id.textView_dateEarning);
            address = (TextView) itemView.findViewById(R.id.tv_addressEarnings);
        }
    }
}

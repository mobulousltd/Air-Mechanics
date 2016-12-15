package mobulous12.airmechanics.customer.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.ServiceProviderBean;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;
import mobulous12.airmechanics.databinding.*;

/**
 * Created by mobulous12 on 14/12/16.
 */

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SPListViewHolder>
{

    private ArrayList<ServiceProviderBean> spArrayList;
    public static MyListener listener;
    private Context context;
    private LayoutInflater inflater;

    public SearchListAdapter(Context context, ArrayList<ServiceProviderBean> spArrayList) {
        this.context = context;
        this.spArrayList = spArrayList;
        this.inflater = LayoutInflater.from(context);
    }
    public void setArraList(ArrayList<ServiceProviderBean> spArrayList)
    {
        this.spArrayList = spArrayList;
        notifyDataSetChanged();
    }
    @Override
    public SPListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        SPListViewHolder holder = new SPListViewHolder((DataBindingUtil.inflate(inflater, R.layout.single_searchrow,parent,false)).getRoot());
        return holder;
    }

    @Override
    public void onBindViewHolder(SPListViewHolder holder, int position) {

        final ServiceProviderBean serviceProviderBean = spArrayList.get(position);

        holder.tv_spName.setText(serviceProviderBean.getName());

    }

    @Override
    public int getItemCount() {
        return spArrayList.size();
    }

    public void onItemClickListener(MyListener listener)
    {
        this.listener = listener;
    }


    class SPListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_spName;
        public SPListViewHolder(View itemView) {
            super(itemView);

            tv_spName = (TextView) itemView.findViewById(R.id.tv_spName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                default:
                    listener.onItemClick(getPosition(), v);
                    break;
            }

        }
    }

   public interface MyListener
    {
        void onItemClick(int position , View view);
    }
}

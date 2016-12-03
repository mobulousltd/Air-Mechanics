package mobulous12.airmechanics.customer.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.databinding.NewJobRequestBinding;

/**
 * Created by mobulous12 on 18/11/16.
 */

public class NewJobReqRecyclerAdapter extends RecyclerView.Adapter<NewJobReqRecyclerAdapter.JobReqViewHolder> {

    public static JobReqClickListener listener;
    private LayoutInflater inflater;
    private Context context;
    JSONArray jsonArray;
    String status;

    public NewJobReqRecyclerAdapter(Context context, JSONArray jsonArray)
    {
        this.context = context;
        this.jsonArray=jsonArray;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public JobReqViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NewJobRequestBinding binding  = DataBindingUtil.inflate(inflater,R.layout.new_job_request,parent,false);
        JobReqViewHolder holder = new JobReqViewHolder(binding.getRoot());
        return holder;
    }

    @Override
    public void onBindViewHolder(JobReqViewHolder holder, int position) {
        try {
            JSONObject jsonObject=jsonArray.getJSONObject(position);
                status = jsonObject.getString("status");
            holder.tv_title.setText(jsonObject.getString("request_Title"));
            holder.tv_date.setText(jsonObject.getString("requestDate"));
            holder.tv_description.setText(jsonObject.getString("request_description"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public void onItemClickListener(JobReqClickListener listener)
    {
        this.listener = listener;
    }

    class JobReqViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView tv_title;
        private TextView tv_date;
        private TextView tv_description;

        public JobReqViewHolder(View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.tv_title_newJobRequest);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date_newJobRequest);
            tv_description = (TextView) itemView.findViewById(R.id.tv_descrip_newJobRequest);
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

    public interface JobReqClickListener
    {
        void onItemClick(int position, View v);
    }
}

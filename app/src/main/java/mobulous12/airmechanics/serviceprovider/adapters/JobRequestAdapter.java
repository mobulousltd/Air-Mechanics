package mobulous12.airmechanics.serviceprovider.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.databinding.JobRequestBinding;
import mobulous12.airmechanics.beans.JobRequestModel;
import mobulous12.airmechanics.serviceprovider.fragments.JobRequestDetailFragment;
import mobulous12.airmechanics.fonts.Font;

/**
 * Created by mobulous11 on 15/10/16.
 */

public class JobRequestAdapter extends RecyclerView.Adapter<JobRequestAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<JobRequestModel> modelList = Collections.emptyList();

    private JobRequestInterface requestInterface;

    public JobRequestAdapter(Context context, List<JobRequestModel> list)
    {
        this.context = context;
        inflater = LayoutInflater.from(context);
        modelList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        JobRequestBinding binding=DataBindingUtil.inflate(inflater, R.layout.job_request, parent, false);
        MyViewHolder holder = new MyViewHolder(binding.getRoot());
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        JobRequestModel model = modelList.get(position);

        holder.tv_title.setText(model.getRequestTitle());
        holder.tv_description.setText(model.getRequestDescription());
        holder.tv_date.setText(model.getDate());


        // calling onItemClick()
        //requestInterface.onItemClick(position, (View) holder);

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_title;
        private TextView tv_date;
        private TextView tv_description;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.textView_title_job_request);
            tv_date = (TextView) itemView.findViewById(R.id.textView_date_job_request);
            tv_description = (TextView) itemView.findViewById(R.id.textView_description_job_request);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                default:
                    requestInterface.onItemClick(getPosition(), v);
            }
        }
    }


    public void onItemClickListener_JobRequest_SP(JobRequestInterface requestInterface)
    {
        this.requestInterface = requestInterface;
    }

    public interface JobRequestInterface
    {
        public void onItemClick(int position, View view);
    }

}

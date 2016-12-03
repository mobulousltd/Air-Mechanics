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

import org.json.JSONArray;
import org.json.JSONObject;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.databinding.NotificationScreenCardBinding;
import mobulous12.airmechanics.databinding.NotificationScreenCardSpBinding;
import mobulous12.airmechanics.serviceprovider.fragments.JobRequestFragment;
import mobulous12.airmechanics.fonts.Font;

/**
 * Created by mobulous11 on 15/10/16.
 */

public class NotificationsRecyclerAdapter_SP extends RecyclerView.Adapter<NotificationsRecyclerAdapter_SP.MyViewHolder> {
    private static MyClickListener listener;
    private LayoutInflater inflater;
    private Context context;
    JSONArray jsonArray;

    public NotificationsRecyclerAdapter_SP(Context context, JSONArray jsonArray)
    {
        this.jsonArray=jsonArray;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        NotificationScreenCardSpBinding binding=DataBindingUtil.inflate(inflater, R.layout.notification_screen_card_sp, parent, false);
        MyViewHolder holder = new MyViewHolder(binding.getRoot());
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        try {
            JSONObject jsonObject=jsonArray.getJSONObject(position);
            holder.tv_content.setText(jsonObject.getString("message"));
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

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        private TextView tv_content;
        private TextView tv_time;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_content = (TextView) itemView.findViewById(R.id.textView_content_notificationScreen_sp);
            tv_time = (TextView) itemView.findViewById(R.id.textView_time_notificationScreen_sp);
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

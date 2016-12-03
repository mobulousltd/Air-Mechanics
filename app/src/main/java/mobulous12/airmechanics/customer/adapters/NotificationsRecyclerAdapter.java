package mobulous12.airmechanics.customer.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.databinding.NotificationScreenCardBinding;
import mobulous12.airmechanics.fonts.Font;


public class NotificationsRecyclerAdapter extends RecyclerView.Adapter<NotificationsRecyclerAdapter.MyViewHolder> {

    private static MyClickListener listener;
    private LayoutInflater inflater;
    private Context context;
    JSONArray jsonArray;

    public NotificationsRecyclerAdapter(Context context, JSONArray jsonArray)
    {
        this.context = context;
        this.jsonArray=jsonArray;
        inflater = LayoutInflater.from(context);
    }
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView textView_content_notificationScreen;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView_content_notificationScreen=(TextView)itemView.findViewById(R.id.textView_content_notificationScreen);
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
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NotificationScreenCardBinding binding= DataBindingUtil.inflate(inflater, R.layout.notification_screen_card, parent, false);
       MyViewHolder holder = new MyViewHolder(binding.getRoot());
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            JSONObject jsonObject=jsonArray.getJSONObject(position);
            holder.textView_content_notificationScreen.setText(jsonObject.getString("message"));
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


    public void onItemClickListener(MyClickListener listener) {
        this.listener = listener;
    }

    public interface MyClickListener
    {
        public void onItemClick(int position, View v);
    }
}

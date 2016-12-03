package mobulous12.airmechanics.serviceprovider.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

import mobulous12.airmechanics.R;

/**
 * Created by mobulous12 on 18/10/16.
 */

public class DocumentsAdapter extends RecyclerView.Adapter<DocumentsAdapter.DocumnentsViewholder>{
    private static MyClickListener listener;
    ArrayList<String> arrayList;
    private Context context;
    private LayoutInflater inflater;



    public DocumentsAdapter(Context context, ArrayList<String>  arrayList)
    {
        this.context = context;
        this.arrayList=arrayList;
        inflater = LayoutInflater.from(context);
    }
    public void setArrayList(ArrayList<String> arrayList)
    {
        this.arrayList=arrayList;
        notifyDataSetChanged();
    }
    @Override
    public DocumnentsViewholder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.documents_item,parent,false);
        DocumnentsViewholder holder = new DocumnentsViewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DocumnentsViewholder holder, final int position)
    {
        String path=arrayList.get(position);
        File imgFile = new File(path);
        if(imgFile.exists())
        {
            holder.imageView.setImageURI(Uri.fromFile(imgFile));
        }
//        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v)
//            {
//                arrayList.remove(position);
//                notifyDataSetChanged();
//                return false;
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class DocumnentsViewholder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener
    {
        ImageView imageView;
        public DocumnentsViewholder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.image);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View v)
        {
            switch (v.getId())
            {
                default:
                    listener.OnLongPress(getPosition(), v);
                    break;
            }
            return false;
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
        public void OnLongPress(int position, View v);
    }
}

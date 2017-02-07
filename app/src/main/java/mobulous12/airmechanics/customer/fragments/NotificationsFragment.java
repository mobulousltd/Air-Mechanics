package mobulous12.airmechanics.customer.fragments;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.customer.activities.AcceptRejectDetailActivity;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.customer.adapters.NotificationsRecyclerAdapter;
import mobulous12.airmechanics.databinding.FragmentNotificationsBinding;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.serviceprovider.adapters.NotificationsRecyclerAdapter_SP;
import mobulous12.airmechanics.serviceprovider.fragments.JobRequestDetailFragment;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment implements ApiListener{

    RecyclerView recyclerView;
    NotificationsRecyclerAdapter notificationsRecyclerAdapter;
    private View view;
    JSONArray jsonArray;
    private TextView tv_newNotifi;
    private SwipeRefreshLayout swipeRefresh;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.blank_at_right_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_show_service_provider).setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState)
    {
        FragmentNotificationsBinding binding=DataBindingUtil.inflate(inflater,R.layout.fragment_notifications, container, false);
        view =  binding.getRoot();
        ((HomeActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.headername_notifications));
        ((HomeActivity)getActivity()).setNavigationIcon();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_notifications);
        swipeRefresh = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefresh);
        tv_newNotifi = (TextView) view.findViewById(R.id.tv_newNotifi);

        notificationServiceHit();

 //      Load items again
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//              Refresh items
                notificationServiceHit();
            }
        });

        return view;
    }


    //services
    private void notificationServiceHit()
    {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getActivity().getApplicationContext()).getString(SPreferenceKey.TOKEN));
        ServiceBean bean = new ServiceBean();
        bean.setActivity(getActivity());
        bean.setMethodName("Consumers/NotificationList");
        bean.setApilistener(NotificationsFragment.this);

        CustomHandler customHandler = new CustomHandler(bean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);
    }


    @Override
    public void myServerResponse(final JSONObject jsonObject) {
        try {
            if (jsonObject != null) {
                if (jsonObject.getString("status").equalsIgnoreCase("SUCCESS"))
                {
                    if (jsonObject.getString("requestKey").equalsIgnoreCase("NotificationList"))
                    {
                        jsonArray=jsonObject.getJSONArray("response");
                        if(jsonArray.length() == 0)
                        {
                            tv_newNotifi.setVisibility(View.VISIBLE);
                        }
                        else {
                            tv_newNotifi.setVisibility(View.GONE);

                            notificationsRecyclerAdapter = new NotificationsRecyclerAdapter(getActivity(), jsonArray);
                            recyclerView.setAdapter(notificationsRecyclerAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            notificationsRecyclerAdapter.onItemClickListener(new NotificationsRecyclerAdapter.MyClickListener() {
                                @Override
                                public void onItemClick(int position, View v) {
                                    try {
//                                    JSONObject jsonObject1 = jsonArray.getJSONObject(position);
//                                    Intent intent=new Intent(getActivity(), AcceptRejectDetailActivity.class);
//                                    intent.putExtra("requestid", jsonObject1.getString("request_id"));
//                                    getActivity().startActivity(intent);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            // Load complete
                            swipeRefresh.setRefreshing(false);
                        }
                    }
                }
                else
                {

                }
                Log.e("JSON Response", ""+jsonObject.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

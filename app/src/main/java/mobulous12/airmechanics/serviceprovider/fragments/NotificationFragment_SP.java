package mobulous12.airmechanics.serviceprovider.fragments;


import android.app.Notification;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.databinding.FragmentNotificationSpBinding;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;
import mobulous12.airmechanics.serviceprovider.adapters.NotificationsRecyclerAdapter_SP;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;


public class NotificationFragment_SP extends Fragment implements ApiListener {

    View view;
    JSONArray jsonArray;
    RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private FragmentNotificationSpBinding binding;

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
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_show_service_provider).setVisible(false);
//        menu.findItem(R.id.action_show_myJob_Orders).setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification_sp, container, false);
        view=binding.getRoot();

        ((HomeActivityServicePro) getActivity()).setToolbarTitleSP("Notifications");
        ((HomeActivityServicePro) getActivity()).setNavigationIconSP();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_notifications_sp);
        swipeRefresh = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefresh);

        notificationServiceHit();

//       Load items again
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
        bean.setMethodName("Services/notification_list");
        bean.setApilistener(NotificationFragment_SP.this);

        CustomHandler customHandler = new CustomHandler(bean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);
    }


    @Override
    public void myServerResponse(final JSONObject jsonObject) {
        try {
            if (jsonObject != null) {
                if (jsonObject.getString("status").equalsIgnoreCase("SUCCESS"))
                {
                    if (jsonObject.getString("requestKey").equalsIgnoreCase("notification_list"))
                    {
                        jsonArray=jsonObject.getJSONArray("response");
                        if (jsonArray.length() == 0)
                        {
                            binding.tvNotificationSp.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            NotificationsRecyclerAdapter_SP adapter=new NotificationsRecyclerAdapter_SP(getActivity(), jsonArray);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            adapter.onItemClickListener(new NotificationsRecyclerAdapter_SP.MyClickListener() {
                                @Override
                                public void onItemClick(int position, View v)
                                {
                                    try
                                    {
//                                    JSONObject jsonObject1 = jsonArray.getJSONObject(position);
//                                    Fragment fragment = new JobRequestDetailFragment();
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("custid", jsonObject1.getString("customer_id"));
//                                    bundle.putString("reqid", jsonObject1.getString("request_id"));
//                                    fragment.setArguments(bundle);
//                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_serviceHomeContainer, fragment).addToBackStack("jobReqFrag").commit();
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        }

                        // Load complete
                        swipeRefresh.setRefreshing(false);
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

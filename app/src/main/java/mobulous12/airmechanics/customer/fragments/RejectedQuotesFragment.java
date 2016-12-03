package mobulous12.airmechanics.customer.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.BookingBean;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.customer.adapters.RejectedRecyclerAapter;
import mobulous12.airmechanics.databinding.FragmentRejectedQuotesBinding;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

public class RejectedQuotesFragment extends Fragment implements ApiListener {


    private RecyclerView recyclerView_rejected;
    private RejectedRecyclerAapter rejectedRecyclerAapter;
    private View view;
    private ArrayList<BookingBean> beanArrayList;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        FragmentRejectedQuotesBinding binding=DataBindingUtil.inflate(inflater, R.layout.fragment_rejected_quotes, container, false);
        view=binding.getRoot();
        ((HomeActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.headername_rejected));
        ((HomeActivity)getActivity()).setNavigationIcon();


        // HITTING REJECTED SERVICE
        rejectedJobRequestHit();

        recyclerView_rejected = (RecyclerView) view.findViewById(R.id.recyclerView_rejected);



        return view;
    }


    private void rejectedJobRequestHit()
    {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN));

        ServiceBean bean = new ServiceBean();
        bean.setActivity(getActivity());
        bean.setMethodName("Consumers/reject_job_request");
        bean.setApilistener(this);

        CustomHandler customHandler = new CustomHandler(bean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);

    }

    @Override
    public void myServerResponse(JSONObject jsonObject) {

        Log.e("REJECTED_SCREEN", jsonObject+"");

        if (jsonObject != null)
        {

            try {
                if (jsonObject.getString("status").equals("SUCCESS")&&jsonObject.getString("requestKey").equalsIgnoreCase("reject_job_request"))
                {
                    JSONObject response = jsonObject.getJSONObject("response");
                    JSONArray userArray = response.getJSONArray("user");

                    beanArrayList = new ArrayList<>();

                    for (int i=0; i<userArray.length(); i++)
                    {
                        JSONObject obj = userArray.getJSONObject(i);

                        BookingBean bean = new BookingBean();

                        bean.setUserName(obj.getString("request_Title"));
                        bean.setMinCharge(obj.getString("minCharge"));
                        bean.setRequestDate(obj.getString("requestDate"));
                        bean.setStatus(obj.getString("status"));
                        bean.setRequestTime(obj.getString("Request_time"));
                        beanArrayList.add(bean);
                    }

                    rejectedRecyclerAapter = new RejectedRecyclerAapter(getActivity(), beanArrayList);
                    recyclerView_rejected.setAdapter(rejectedRecyclerAapter);
                    recyclerView_rejected.setLayoutManager(new LinearLayoutManager(getActivity()));

                }
                else
                {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}

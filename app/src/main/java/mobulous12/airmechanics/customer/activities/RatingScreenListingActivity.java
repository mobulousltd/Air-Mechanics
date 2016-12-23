package mobulous12.airmechanics.customer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.ServiceProviderBean;
import mobulous12.airmechanics.customer.adapters.RatingScreenListingAdapter;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

public class RatingScreenListingActivity extends AppCompatActivity implements ApiListener, View.OnClickListener {

    private RecyclerView recyclerView;
    private ServiceProviderBean bean;
    private ArrayList<ServiceProviderBean> list;
    private ImageView back_ratingScreenListing;

    private TextView negativeScreenText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_screen_listing);

        back_ratingScreenListing = (ImageView) findViewById(R.id.back_ratingScreenListing);
        back_ratingScreenListing.setOnClickListener(this);

        negativeScreenText = (TextView) findViewById(R.id.tv_rating_screen_listing);

        bean = getIntent().getParcelableExtra("bean");
        list = new ArrayList<>();

        reviewListServiceHit();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_ratingScreenListing);

    }

    private void reviewListServiceHit()
    {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        multipartEntityBuilder.addTextBody("service_id", bean.getId());

        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setActivity(this);
        serviceBean.setMethodName("Consumers/reviewList");
        serviceBean.setApilistener(this);

        CustomHandler customHandler  = new CustomHandler(serviceBean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);
    }

    @Override
    public void myServerResponse(JSONObject jsonObject) {
        Log.e("RATING_SCREEN", jsonObject+"");
        try {
        if (jsonObject != null) {
            if (jsonObject.getString("status").equals("SUCCESS")) {

                JSONArray responseArray = jsonObject.getJSONArray("response");
                for (int i=0; i<responseArray.length(); i++)
                {
                    JSONObject object = responseArray.getJSONObject(i);
                    ServiceProviderBean bean = new ServiceProviderBean();

                    bean.setId(object.getString("customer_id"));
                    bean.setName(object.getString("full_name"));
                    bean.setRating(object.getString("rating"));
                    bean.setReviews(object.getString("review"));
                    bean.setProfile(object.getString("profile"));
                    bean.setProfile_thumb(object.getString("profile_thumb"));

                    list.add(bean);
                }
                if (list.size()>0)
                {
                    recyclerView.setAdapter(new RatingScreenListingAdapter(this, list));
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                }
                else {
                    negativeScreenText.setVisibility(View.VISIBLE);
                }


            }
        }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_ratingScreenListing:
                finish();
                break;
        }
    }
}


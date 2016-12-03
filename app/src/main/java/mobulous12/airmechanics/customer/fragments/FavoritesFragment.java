package mobulous12.airmechanics.customer.fragments;


import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.beans.ServiceProviderBean;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.customer.adapters.FavoritesRecyclerAdapter;
import mobulous12.airmechanics.customer.adapters.ServiceProviderRecyclerAdapter;
import mobulous12.airmechanics.databinding.FragmentFavoritesBinding;
import mobulous12.airmechanics.fonts.Font;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment  implements View.OnClickListener, ApiListener {

    private RecyclerView recyclerView_myFavorites;
    private FavoritesRecyclerAdapter favoritesRecyclerAdapter;
    private  TextView textView_sortingFavorites,textView_filterFavorites;
    public AlertDialog.Builder alertbuilder_sortFavorites,alertbuilder_filterFavorites;
    private View view;ViewGroup viewGroup;
    private   LayoutInflater myinflater;

    private TextView headername_favoritesFrag;
    private ImageView back_favoritesFrag;
    private int d=0,r=0,p=0;
    private String filter="";
    private ArrayList<ServiceProviderBean> serviceProviderArrayList;

    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        FragmentFavoritesBinding binding=DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false);
        view=binding.getRoot();


        ((HomeActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.headername_favourites));
        ((HomeActivity) getActivity()).toolbarGone();
        ((HomeActivity)getActivity()).setNavigationIcon();

        myinflater = inflater;

        /*Views*/
        textView_sortingFavorites  = (TextView) view.findViewById(R.id.textView_sortingFavorites);
        textView_filterFavorites  = (TextView) view.findViewById(R.id.textView_filterFavorites);
        headername_favoritesFrag  = (TextView) view.findViewById(R.id.headername_favoritesFrag);
        back_favoritesFrag  = (ImageView) view.findViewById(R.id.back_favoritesFrag);
        recyclerView_myFavorites = (RecyclerView) view.findViewById(R.id.recyclerView_myFavorites);



         /*CLick listeneres*/
        textView_sortingFavorites.setOnClickListener(this);
        textView_filterFavorites.setOnClickListener(this);
        back_favoritesFrag.setOnClickListener(this);


        myFavouritesService();


        return  view;
    }



    private void showSortDialog()
    {

        alertbuilder_sortFavorites = new AlertDialog.Builder(getActivity());
        alertbuilder_sortFavorites.setCancelable(false);

        //  ALertDialog - title
        View view1=myinflater.inflate(R.layout.popup_title_sort, null);
        TextView textView_titleSortList = (TextView) view1.findViewById(R.id.textView_titleSortList);
        Font.setFontHeader(textView_titleSortList,getActivity());
        alertbuilder_sortFavorites.setCustomTitle(view1);

        //  ALertDialog - content View
        View view2 = myinflater.inflate(R.layout.sort_your_list,null);
        final RadioGroup rdGrp_Sorting = (RadioGroup) view2.findViewById(R.id.radioGroup_Sorting);

        alertbuilder_sortFavorites.setView(view2);

        alertbuilder_sortFavorites.setPositiveButton(getResources().getString(R.string.popup_done), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                rdGrp_Sorting.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId)
                    {

                        switch (checkedId)
                        {
                            case R.id.radio1_byDistance:
                                d=1;
                                r=0;
                                p=0;
                                break;
                            case R.id.radio2_byRating:
                                d=0;
                                r=1;
                                p=0;
                                break;
                            case R.id.radio3_byPrice:
                                d=0;
                                r=0;
                                p=1;
                                break;
                        }
                    }
                });

                myFavouritesService();

            }
        });
        alertbuilder_sortFavorites.setNegativeButton(getResources().getString(R.string.popup_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = alertbuilder_sortFavorites.create();
        alertDialog.show();

        //Buttons
        Button positive_button =  alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negative_button =  alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        setPositiveNegativeButtonColor(positive_button,negative_button);

    }


    private void showFilterDialog()
    {
        alertbuilder_filterFavorites = new AlertDialog.Builder(getActivity());
        alertbuilder_filterFavorites.setCancelable(false);

        //  ALertDialog - title
        View view1=myinflater.inflate(R.layout.popup_title_filter, null);
        TextView textView_titleFilter = (TextView) view1.findViewById(R.id.textView_titleFilter);
        Font.setFontHeader(textView_titleFilter,getActivity());
        alertbuilder_filterFavorites.setCustomTitle(view1);

        //  ALertDialog - content view

        View view2 = myinflater.inflate(R.layout.filter_option,null);
        RadioButton radio1 = (RadioButton) view2.findViewById(R.id.radio1_twoWheeler);
        RadioButton radio2 = (RadioButton) view2.findViewById(R.id.radio2_lightWeight);
        RadioButton radio3 = (RadioButton) view2.findViewById(R.id.radio3_heavyWeight);

        switch (filter)
        {
            case "two":
                radio1.setChecked(true);
                break;
            case "light":
                radio2.setChecked(true);
                break;
            case "heavy":
                radio3.setChecked(true);
                break;
        }
        alertbuilder_filterFavorites.setView(view2);

        final RadioGroup rdGrp_Filter = (RadioGroup) view2.findViewById(R.id.radioGroup_Filter);
        rdGrp_Filter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId)
                {
                    case R.id.radio1_twoWheeler:
                        filter = "two";
                        break;
                    case R.id.radio2_lightWeight:
                        filter = "light";
                        break;
                    case R.id.radio3_heavyWeight:
                        filter = "heavy";
                        break;
                }
            }
        });

        alertbuilder_filterFavorites.setPositiveButton(getResources().getString(R.string.popup_done), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myFavouritesService();

            }
        });
        alertbuilder_filterFavorites.setNegativeButton(getResources().getString(R.string.popup_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = alertbuilder_filterFavorites.create();
        alertDialog.show();

        //Buttons
        Button positive_button =  alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negative_button =  alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        setPositiveNegativeButtonColor(positive_button,negative_button);

    }

    private void setPositiveNegativeButtonColor(Button positive,Button negative)
    {
        Font.setFontButton(positive,getActivity());
        positive.setTextColor(getResources().getColor(R.color.blue));

        Font.setFontButton(negative,getActivity());
        negative.setTextColor(getResources().getColor(R.color.black));
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id)
        {
            case R.id.textView_sortingFavorites:
                showSortDialog();
                break;

            case R.id.textView_filterFavorites:
                showFilterDialog();
                break;
            case R.id.back_favoritesFrag:
                getActivity().getSupportFragmentManager().popBackStack();
                break;


        }

    }
    private void myFavouritesService()
    {
        try{
//            token,lat,long,distance,rating,price     my_favourites
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartEntityBuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN));
            multipartEntityBuilder.addTextBody("lat", SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.LATITUDE));
            multipartEntityBuilder.addTextBody("long", SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.LONGITUDE));
            multipartEntityBuilder.addTextBody("distance",String.valueOf(d));
            multipartEntityBuilder.addTextBody("rating",String.valueOf(r));
            multipartEntityBuilder.addTextBody("price",String.valueOf(p));

            ServiceBean serviceBean = new ServiceBean();
            serviceBean.setMethodName("Consumers/my_favourites");
            serviceBean.setActivity(getActivity());
            serviceBean.setApilistener(this);

            CustomHandler customHandler = new CustomHandler(serviceBean);
            customHandler.makeMultipartRequest(multipartEntityBuilder);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void myServerResponse(JSONObject responseObj) {
        try {
            if (responseObj != null)
            {
                if (responseObj.getString("status").equalsIgnoreCase("SUCCESS") && responseObj.getString("requestKey").equalsIgnoreCase("my_favourites")) {
                    JSONArray jsonArray = responseObj.getJSONArray("response");
                    serviceProviderArrayList = new ArrayList<ServiceProviderBean>();
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                        ServiceProviderBean serviceproviderbean = new ServiceProviderBean();

                        serviceproviderbean.setCategory(jsonobject.getString("work_category"));
                        serviceproviderbean.setId(jsonobject.getString("id"));
                        serviceproviderbean.setName(jsonobject.getString("name"));
                        serviceproviderbean.setAddress(jsonobject.getString("address"));
                        serviceproviderbean.setEmail(jsonobject.getString("email"));
                        serviceproviderbean.setContact_no(jsonobject.getString("contact_no"));
                        serviceproviderbean.setLat(jsonobject.getString("lat"));
                        serviceproviderbean.setLng(jsonobject.getString("long"));
                        serviceproviderbean.setProfile(jsonobject.getString("profile"));
                        serviceproviderbean.setProfile_thumb(jsonobject.getString("profile_thumb"));
                        serviceproviderbean.setWorkingdays(jsonobject.getString("workingDays"));
                        serviceproviderbean.setRating(jsonobject.getString("rating"));
                        serviceproviderbean.setMin_charge(jsonobject.getString("st_charge"));
                        serviceproviderbean.setDistance(jsonobject.getString("distance"));
                        serviceProviderArrayList.add(serviceproviderbean);

                        /*Recycler view*/
                        favoritesRecyclerAdapter = new FavoritesRecyclerAdapter(getActivity(),serviceProviderArrayList);
                        recyclerView_myFavorites.setAdapter(favoritesRecyclerAdapter);
                        recyclerView_myFavorites.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                }
                else
                {
                    Toast toast = Toast.makeText(getActivity(),responseObj.getString("message"), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                Log.v("JsonResponse", ""+responseObj.toString());
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

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


    }


}

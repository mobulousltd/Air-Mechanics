package mobulous12.airmechanics.customer.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONObject;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.RoleSelectionActivity;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.databinding.FragmentSettingsBinding;
import mobulous12.airmechanics.fonts.FontBinding;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

public class SettingsFragment extends Fragment implements View.OnClickListener, ApiListener {


    private View view;
    private ImageView imgView_pushNotify;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        FragmentSettingsBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_settings, container, false);

        view =  binding.getRoot();

        ((HomeActivity) getActivity()).toolbarVisible();
        ((HomeActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.settings_home));
        ((HomeActivity)getActivity()).setNavigationIcon();

       /*Views*/
        RelativeLayout root_pushnotify = (RelativeLayout) view.findViewById(R.id.root_pushnotify_settingsFrag);
        RelativeLayout root_changePass = (RelativeLayout) view.findViewById(R.id.root_changePassword_settingsFrag);
        RelativeLayout root_aboutUs = (RelativeLayout) view.findViewById(R.id.root_aboutUs_settingsFrag);
        RelativeLayout root_logout = (RelativeLayout) view.findViewById(R.id.root_logout_settingsFrag);
        imgView_pushNotify = (ImageView) view.findViewById(R.id.imgView_pushNotify);


       if(SharedPreferenceWriter.getInstance(getActivity()).getBoolean(SPreferenceKey.ISPUSHON))
           imgView_pushNotify.setImageResource(R.drawable.on_btn);

        else
           imgView_pushNotify.setImageResource(R.drawable.off_btn);


         /*CLick listeneres*/
        root_pushnotify.setOnClickListener(this);
        root_changePass.setOnClickListener(this);
        root_aboutUs.setOnClickListener(this);
        root_logout.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.root_pushnotify_settingsFrag:

                if(SharedPreferenceWriter.getInstance(getActivity()).getBoolean(SPreferenceKey.ISPUSHON)) {
                    imgView_pushNotify.setImageResource(R.drawable.off_btn);
                    SharedPreferenceWriter.getInstance(getActivity()).writeBooleanValue(SPreferenceKey.ISPUSHON,false);
                }
                else
                {
                    imgView_pushNotify.setImageResource(R.drawable.on_btn);
                    SharedPreferenceWriter.getInstance(getActivity()).writeBooleanValue(SPreferenceKey.ISPUSHON,true);
                }
                break;

            case R.id.root_changePassword_settingsFrag:

                if (SharedPreferenceWriter.getInstance(getActivity()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
                {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer, new ChangePasswordFragment(), "changePasswordFragment").addToBackStack("changePassword").commit();

                }
//                else {
//                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_serviceHomeContainer, new ChangePasswordFragment(), "changePasswordFragSP").addToBackStack("changePasswordSP").commit();
//                }

                break;
            case R.id.root_aboutUs_settingsFrag:
                SharedPreferenceWriter.getInstance(getActivity()).writeBooleanValue(SPreferenceKey.CUSTOMER_LOGIN, true);

                Bundle bundle=new Bundle();
                bundle.putString("page_type", "aboutus");
                WebViewsFragment webViewsFragment =new WebViewsFragment();
                webViewsFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_homeContainer, webViewsFragment, "webViewsFragment").addToBackStack("webViews").commit();

                break;
            case R.id.root_logout_settingsFrag:
                logoutPopUp();
                break;

        }

    }



    private void logoutPopUp()
    {
        AlertDialog.Builder logout =  new AlertDialog.Builder(getActivity());
        logout.setCancelable(false);
        logout.setTitle("Logout");
        logout.setIcon(R.drawable.logo);
        logout.setMessage("Do you want to logout ?");
        logout.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logoutServiceHit();
            }
        });
        logout.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = logout.create();
        alertDialog.show();
        TextView msgTxt = (TextView) alertDialog.findViewById(android.R.id.message);
        FontBinding.setFont(msgTxt, "regular");
        //Buttons
        Button positive_button =  alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negative_button =  alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        setPositiveNegativeButtonColor(positive_button,negative_button);
    }

    private void setPositiveNegativeButtonColor(Button positive,Button negative)
    {
//        Font.setFontButton(positive,this);
        positive.setTextColor(getResources().getColor(R.color.blue));
//        Font.setFontButton(negative,this);
        negative.setTextColor(getResources().getColor(R.color.black));
    }

        /*services*/

    private void logoutServiceHit() {
        try {

            MultipartEntityBuilder multipartbuilder = MultipartEntityBuilder.create();
            multipartbuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            multipartbuilder.addTextBody("token",SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN));

            ServiceBean serviceBean = new ServiceBean();
            serviceBean.setMethodName("Consumers/logout");
            serviceBean.setActivity( getActivity());
            serviceBean.setApilistener(this);


            CustomHandler customHandler = new CustomHandler(serviceBean);
            customHandler.makeMultipartRequest(multipartbuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void myServerResponse(JSONObject responseObj)
    {
        try {
            if (responseObj != null)
            {
                if (responseObj.getString("status").equalsIgnoreCase("SUCCESS"))
                {
                    if (responseObj.getString("requestKey").equals("logout"))
                    {
                        Log.i("JsonResponse", ""+responseObj.toString());

                        Intent intent=new Intent(getActivity(), RoleSelectionActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        SharedPreferenceWriter.getInstance(getActivity()).writeBooleanValue(SPreferenceKey.LOGINKEY, false);
                        SharedPreferenceWriter.getInstance(getActivity()).writeBooleanValue(SPreferenceKey.CUSTOMER_LOGIN, false);
                        SharedPreferenceWriter.getInstance(getActivity()).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, false);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }
                else {
                    Log.i("JsonResponse", ""+responseObj.toString());
                    Toast.makeText(getActivity(),""+responseObj.getString("message"),Toast.LENGTH_LONG).show();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

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

}

package mobulous12.airmechanics.customer.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.databinding.FragmentWebViewsBinding;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.fonts.Font;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewsFragment extends Fragment {

    private View view;
    private WebView webView_custom;

    public WebViewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        FragmentWebViewsBinding binding=DataBindingUtil.inflate(inflater, R.layout.fragment_web_views, container, false);
        view=binding.getRoot();
        webView_custom = (WebView) view.findViewById(R.id.webView_custom);
        String type = getArguments().getString("page_type");

        /*For Guest user*/
        if(!SharedPreferenceWriter.getInstance(getActivity()).getBoolean(SPreferenceKey.LOGINKEY))
        {
            if(type.equalsIgnoreCase("aboutus"))
            {
                ((HomeActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.about_us_home));
                ((HomeActivity)getActivity()).setNavigationIcon();
                webView_custom.loadUrl("http://mobulous.co.in/design/airMechaniks/about.html");
                webView_custom.getSettings().setJavaScriptEnabled(true);
            }
            if(type.equalsIgnoreCase("contactus"))
            {
                ((HomeActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.contact_us_homesp));
                ((HomeActivity)getActivity()).setNavigationIcon();

            }
            if(type.equalsIgnoreCase("t_and_c"))
            {
                ((HomeActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.tc_homesp));
                ((HomeActivity)getActivity()).setNavigationIcon();
                webView_custom.loadUrl("http://mobulous.co.in/design/airMechaniks/terms.html");
                webView_custom.getSettings().setJavaScriptEnabled(true);
            }
        }

        if(SharedPreferenceWriter.getInstance(getActivity()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
        {
            if(type.equalsIgnoreCase("aboutus"))
            {
                ((HomeActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.about_us_home));
                ((HomeActivity)getActivity()).setNavigationIcon();
                webView_custom.loadUrl("http://mobulous.co.in/design/airMechaniks/about.html");
                webView_custom.getSettings().setJavaScriptEnabled(true);
            }
        }
        else
        {
            if(type.equalsIgnoreCase("aboutus"))
            {
                ((HomeActivityServicePro)getActivity()).setToolbarTitleSP(getResources().getString(R.string.about_us_home));
                ((HomeActivityServicePro)getActivity()).setNavigationIconSP();

                webView_custom.loadUrl("http://mobulous.co.in/design/airMechaniks/about.html");
                webView_custom.getSettings().setJavaScriptEnabled(true);
            }
            if(type.equalsIgnoreCase("t_and_c"))
            {
                ((HomeActivityServicePro)getActivity()).setToolbarTitleSP(getResources().getString(R.string.tc_homesp));
                ((HomeActivityServicePro)getActivity()).setNavigationIconSP();

                webView_custom.loadUrl("http://mobulous.co.in/design/airMechaniks/terms.html");
                webView_custom.getSettings().setJavaScriptEnabled(true);
            }
            if (type.equalsIgnoreCase("contactus"))
            {
                ((HomeActivityServicePro)getActivity()).setToolbarTitleSP(getResources().getString(R.string.contact_us_homesp));
                ((HomeActivityServicePro)getActivity()).setNavigationIconSP();
            }
        }
        return view;
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
//        if(SharedPreferenceWriter.getInstance(getActivity()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN)) {
//
//            menu.findItem(R.id.action_show_service_provider).setVisible(false);
//        }else {
//            menu.findItem(R.id.action_show_myJob_Orders).setVisible(false);
//        }
    }

}

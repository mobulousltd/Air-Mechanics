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
import android.webkit.WebViewClient;

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

//        http://mobulous.co.in/design/airMechaniks/termsandcondition.html
//        http://mobulous.co.in/design/airMechaniks/aboutUs.html
        /*For Guest user*/
        if(!SharedPreferenceWriter.getInstance(getActivity()).getBoolean(SPreferenceKey.LOGINKEY))
        {
            if(type.equalsIgnoreCase("aboutus"))
            {
                ((HomeActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.about_us_home));
                ((HomeActivity)getActivity()).setNavigationIcon();
                webView_custom.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
                webView_custom.getSettings().setJavaScriptEnabled(true);
                webView_custom.setWebViewClient(new MyWebViewClient());
                webView_custom.loadUrl("http://airmechaniks.com/home/about");
                webView_custom.requestFocus();

            }
            if(type.equalsIgnoreCase("t_and_c"))
            {
                ((HomeActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.tc_homesp));
                ((HomeActivity)getActivity()).setNavigationIcon();
                webView_custom.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
                webView_custom.getSettings().setJavaScriptEnabled(true);
                webView_custom.setWebViewClient(new MyWebViewClient());
                webView_custom.loadUrl("http://airmechaniks.com/home/terms");
                webView_custom.requestFocus();

            }
        }
      /*For CUSTOMER user*/
        if(SharedPreferenceWriter.getInstance(getActivity()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN))
        {
            if(type.equalsIgnoreCase("aboutus"))
            {
                ((HomeActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.about_us_home));
                ((HomeActivity)getActivity()).setNavigationIcon();

                webView_custom.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
                webView_custom.getSettings().setJavaScriptEnabled(true);
                webView_custom.setWebViewClient(new MyWebViewClient());
                webView_custom.loadUrl("http://airmechaniks.com/home/about");
                webView_custom.requestFocus();

            }
        }
        /*For SERVICE PROVIDER user*/
        else
        {
            if(type.equalsIgnoreCase("aboutus"))
            {
                ((HomeActivityServicePro)getActivity()).setToolbarTitleSP(getResources().getString(R.string.about_us_home));
                ((HomeActivityServicePro)getActivity()).setNavigationIconSP();

                webView_custom.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
                webView_custom.getSettings().setJavaScriptEnabled(true);
                webView_custom.setWebViewClient(new MyWebViewClient());
                webView_custom.loadUrl("http://airmechaniks.com/home/about");
                webView_custom.requestFocus();

            }
            if(type.equalsIgnoreCase("t_and_c"))
            {
                ((HomeActivityServicePro)getActivity()).setToolbarTitleSP(getResources().getString(R.string.tc_homesp));
                ((HomeActivityServicePro)getActivity()).setNavigationIconSP();

                webView_custom.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
                webView_custom.getSettings().setJavaScriptEnabled(true);
                webView_custom.setWebViewClient(new MyWebViewClient());
                webView_custom.loadUrl("http://airmechaniks.com/home/terms");
                webView_custom.requestFocus();
            }

        }
        return view;
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
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
//        if(SharedPreferenceWriter.getInstance(getActivity()).getBoolean(SPreferenceKey.CUSTOMER_LOGIN)) {
//
//            menu.findItem(R.id.action_show_service_provider).setVisible(false);
//        }else {
//            menu.findItem(R.id.action_show_myJob_Orders).setVisible(false);
//        }
    }

}

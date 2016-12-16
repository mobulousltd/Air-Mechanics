package mobulous12.airmechanics.serviceprovider.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mobulous12.airmechanics.R;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.databinding.ContactUsSpBinding;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.volley.ApiListener;
import mobulous12.airmechanics.volley.CustomHandler;
import mobulous12.airmechanics.volley.ServiceBean;

/**
 * Created by mobulous11 on 14/12/16.
 */

public class ContactUsFragment extends Fragment implements View.OnClickListener, ApiListener {

    private EditText fullName;
    private EditText email;
    private EditText contactNumber;
    private EditText message;
    private Button submitBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ContactUsSpBinding binding = DataBindingUtil.inflate(inflater,R.layout.contact_us_sp,container,false);
        View view = binding.getRoot();

        if(!SharedPreferenceWriter.getInstance(getActivity()).getBoolean(SPreferenceKey.LOGINKEY))
        {
            ((HomeActivity)getActivity()).setToolbarTitle(getResources().getString(R.string.contact_us_homesp));
            ((HomeActivity)getActivity()).setNavigationIcon();
        }
        else
        {
            ((HomeActivityServicePro)getActivity()).setToolbarTitleSP(getResources().getString(R.string.contact_us_homesp));
            ((HomeActivityServicePro)getActivity()).setNavigationIconSP();

        }

        fullName = (EditText) view.findViewById(R.id.editText_fullName_contact_us);
        email = (EditText) view.findViewById(R.id.editText_email_contact_us);
        contactNumber = (EditText) view.findViewById(R.id.editText_contactNumber_contact_us);
        message = (EditText) view.findViewById(R.id.editText_message_servicePro);
        submitBtn = (Button) view.findViewById(R.id.button_contact_us_submit);
        submitBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_contact_us_submit)
        {
//            SUBMIT BUTTON FUNCTIONALITY
            if (validateCustomerData())
            {
                contactUsServiceHit();
            }
        }
    }

    private boolean validateCustomerData()
    {
        if (fullName.getText().toString().trim().isEmpty())
        {
            showToast("Please enter Full name");
            return false;
        }
        else if(!validateName(fullName))
        {
            showToast("Please enter a valid Full name");
            return false;
        }
        else if(email.getText().toString().isEmpty())
        {
            showToast("Please enter Email Address");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            showToast("Please enter valid Email Address");
            return false;
        }

        else if (contactNumber.getText().toString().isEmpty())
        {
            showToast("Please enter your Contact Number.");
            return false;
        }
        else if(contactNumber.getText().toString().length()<8 || contactNumber.getText().toString().length()>15)
        {

            showToast( "Please enter a valid Contact  Number.");

            return false;
        }
        else if (!Patterns.PHONE.matcher(contactNumber.getText().toString()).matches()) {
            showToast("Please enter valid Contact Number");
            return false;
        }
        else if (message.getText().toString().isEmpty())
        {
            showToast("Please Enter Message");
            return false;
        }
        else
        {
            return true;
        }
    }

    private void showToast(String msg)
    {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private boolean validateName(EditText editText)
    {
        final String regex = "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$";
        final String string = editText.getText().toString().trim();

        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);

        if (matcher.find())
        {
            for (int i = 1; i <= matcher.groupCount(); i++)
            {
                Log.d("Name Matching","Group " + i + ": " + matcher.group(i));
            }
            return true;
        }
        else
            return false;

    }

    private void contactUsServiceHit()
    {
        /*token,name,phone,email,message*/
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.addTextBody("token", SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN));
        multipartEntityBuilder.addTextBody("name", fullName.getText().toString());
        multipartEntityBuilder.addTextBody("phone", contactNumber.getText().toString());
        multipartEntityBuilder.addTextBody("email", email.getText().toString());
        multipartEntityBuilder.addTextBody("message", message.getText().toString());

        ServiceBean bean = new ServiceBean();
        bean.setActivity(getActivity());
        bean.setFragment(ContactUsFragment.this);
        bean.setMethodName("Consumers/contactUs");
        bean.setApilistener(this);

        CustomHandler customHandler = new CustomHandler(bean);
        customHandler.makeMultipartRequest(multipartEntityBuilder);

    }

    @Override
    public void myServerResponse(JSONObject jsonObject)
    {
        if (jsonObject != null)
        {
          try {
                if (jsonObject.getString("status").equalsIgnoreCase("SUCCESS") && jsonObject.getString("requestKey").equalsIgnoreCase("contactUs"))
                {
                    Toast.makeText(getActivity(), "Your query has been Submitted", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();

                Log.e("CONTACT_US", jsonObject.toString());
                }
              } catch (JSONException e) {
                e.printStackTrace();
            }
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

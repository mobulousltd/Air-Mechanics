package mobulous12.airmechanics.serviceprovider.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mobulous12.airmechanics.R;

/**
 * Created by mobulous11 on 14/12/16.
 */

public class ContactUsFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private EditText fullName;
    private EditText email;
    private EditText contactNumber;
    private EditText message;

    private Button submitBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.contact_us_sp, container, false);

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
            showToast("Please enter your contact number.");
            return false;
        }
        else if(contactNumber.getText().toString().length()<8 || contactNumber.getText().toString().length()>15)
        {

            showToast( "Please enter a valid Contact  number.");

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
        Pattern ps = Pattern.compile("[a-zA-Z ]+");
        Matcher ms = ps.matcher(editText.getText().toString());
        boolean bs = ms.matches();
        return bs;
    }
}

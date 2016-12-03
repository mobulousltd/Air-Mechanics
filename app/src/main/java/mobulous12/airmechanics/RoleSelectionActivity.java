package mobulous12.airmechanics;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import mobulous12.airmechanics.customer.activities.LoginActivity;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;
import mobulous12.airmechanics.fonts.Font;

public class RoleSelectionActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_role_selection);

        findViewById(R.id.circularImageView_customer).setOnClickListener(this);
        findViewById(R.id.circularImageView_service_provider).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.circularImageView_customer:
                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.CUSTOMER_LOGIN, true);
                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, false);
                startActivity(new Intent(this, LoginActivity.class));
                break;

            case R.id.circularImageView_service_provider:
                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.SERVICE_PROVIDER_LOGIN, true);
                SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.CUSTOMER_LOGIN, false);
                startActivity(new Intent(this, LoginActivity.class));
                break;

        }

    }
}




package mobulous12.airmechanics.volley;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import mobulous12.airmechanics.R;

/**
 * Created by mobulous2 on 10/11/16.
 */
public class CustomDialog extends Dialog {
    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static CustomDialog show(Context context, boolean indeterminate, boolean cancelable,
                                   OnCancelListener cancelListener) {
        CustomDialog dialog = new CustomDialog(context, R.style.Progress);
        dialog.setTitle(context.getResources().getString(R.string.loading));
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DataBindingUtil.inflate(inflater,R.layout.custom_progress, null, false);
        dialog.setContentView(R.layout.custom_progress);
        ProgressBar progressWithoutBg = (ProgressBar) dialog.findViewById(R.id.progressBar);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        dialog.getWindow().getAttributes().gravity= Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount=0.2f;
        dialog.getWindow().setAttributes(lp);
        //dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();
        return dialog;
    }
}

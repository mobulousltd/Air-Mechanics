package mobulous12.airmechanics.receivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import mobulous12.airmechanics.R;
import mobulous12.airmechanics.customer.activities.AcceptDialogAct;



public class AcceptRejectReceiver extends BroadcastReceiver {
    private String requestid="";
    private Context context;
    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context = context;
        requestid = intent.getStringExtra("requestid");
        String status = intent.getStringExtra("status");
        String notify = intent.getStringExtra("notify");
        String login_type = intent.getStringExtra("login_type");
        String spName = intent.getStringExtra("spName");
        String address = intent.getStringExtra("address");
        String exdate = intent.getStringExtra("exdate");
        String minCh1 = intent.getStringExtra("minCh1");
        String minCh2 = intent.getStringExtra("minCh2");
        String message = intent.getStringExtra("message");
        String title = intent.getStringExtra("title");


        if (status.equalsIgnoreCase("priceupdate"))
        {
            Intent i  = new Intent(context,AcceptDialogAct.class);
            i.putExtra("requestid",requestid);
            i.putExtra("status",status);
            i.putExtra("notify",notify);
            i.putExtra("login_type",login_type);
            i.putExtra("spName",spName);
            i.putExtra("exdate",exdate);
            i.putExtra("address",address);
            i.putExtra("minCh2",minCh2);
            i.putExtra("minCh1",minCh1);
            i.putExtra("status",status);
            i.putExtra("message",message);
            i.putExtra("title",title);

            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getApplicationContext().startActivity(i);

//            final Dialog dialog = new Dialog(context.getApplicationContext(),R.style.ThemeDialogCustom);
//            dialog.setContentView(R.layout.pop_up_accept_reject);
//
//
//            if(Build.VERSION.SDK_INT >= 23) {
//                if (!Settings.canDrawOverlays(context.getApplicationContext())) {
//                    Intent i = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                            Uri.parse("package:" + context.getApplicationContext().getPackageName()));
//                context.getApplicationContext().startActivity(i);
//
//                }
//            }
//            else {
//                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//            }
//
////            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//
//            TextView accept = (TextView) dialog.findViewById(R.id.tv_accept_Quote);
//            TextView reject = (TextView) dialog.findViewById(R.id.tv_reject_Quote);
//            TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
//            TextView tv_Name = (TextView) dialog.findViewById(R.id.tv_Name);
//            TextView tv_Date = (TextView) dialog.findViewById(R.id.tv_Date);
//            TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
//            TextView tv_minchrge = (TextView) dialog.findViewById(R.id.tv_minchrge);
//            TextView tv_address = (TextView) dialog.findViewById(R.id.tv_address);
//            tv_msg.setText(message);
//            tv_Name.setText(spName);
//            tv_Date.setText(exdate);
//            tv_title.setText("Title : "+title);
//            tv_minchrge.setText(minCh2);
//            tv_address.setText(address);
//            dialog.show();
//
////            ACCEPT AND REJECT FUNCTIONALITY
//            accept.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                    launchActivityViaService("accept");
//                }
//            });
//            reject.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                    launchActivityViaService("reject");
//                }
//            });
//
//        }

        }
        else {
            Toast.makeText(context, "Air Mechaniks notification error occured.", Toast.LENGTH_SHORT).show();
        }
    }

//    private void launchActivityViaService(String s)
//    {
//        Intent myIntent = new Intent(context, IntentServiceForAccept.class);
//        myIntent.putExtra("key", s);
//        myIntent.putExtra("reqid", requestid);
//        context.startService(myIntent);  // starting an Intent Service
//
//    }

}
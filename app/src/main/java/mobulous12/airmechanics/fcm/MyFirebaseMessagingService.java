package mobulous12.airmechanics.fcm;

/**
 * Created by mobulous2 on 9/9/16.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;

import mobulous12.airmechanics.R;
import mobulous12.airmechanics.customer.activities.HomeActivity;
import mobulous12.airmechanics.customer.fragments.MyBookingsFragment;
import mobulous12.airmechanics.customer.fragments.NewJobRequest;
import mobulous12.airmechanics.serviceprovider.activities.HomeActivityServicePro;
import mobulous12.airmechanics.serviceprovider.fragments.JobRequestFragment;
import mobulous12.airmechanics.sharedprefrences.SPreferenceKey;
import mobulous12.airmechanics.sharedprefrences.SharedPreferenceWriter;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String requestid="";
    private String status = "";
    private static final String TAG = "MyFirebaseMsgService";

    // Called when message is received.@param remoteMessage Object representing the message received from Firebase Cloud Messaging.

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) // Check if message contains a data payload.
        {
            try
            {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
                Map<String, String> data=remoteMessage.getData();
                JSONObject jsonObject=new JSONObject(data);
                boolean login= SharedPreferenceWriter.getInstance(MyFirebaseMessagingService.this).getBoolean(SPreferenceKey.LOGINKEY);
                if(login)
                {
                    if(SharedPreferenceWriter.getInstance(MyFirebaseMessagingService.this).getBoolean(SPreferenceKey.SERVICE_PROVIDER_LOGIN))
                    {
                        if(HomeActivityServicePro.homeact)
                        {
                            if(jsonObject.getString("status").equals("request"))
                            {
                                if(JobRequestFragment.jobRequestFrag)
                                {
                                    Intent intent = new Intent("NOTIFYSP");
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                                }
                                else
                                {
                                    requestid=jsonObject.getString("request_id");
                                    sendNotification(jsonObject.getString("message"), jsonObject.getString("status"), "serviceprovider", "");
                                }
                            }
                            else
                            {
                                sendNotification(jsonObject.getString("message"), jsonObject.getString("status"), "serviceprovider", "");
                            }
                        }
                        else
                        {
                            sendNotification(jsonObject.getString("message"), jsonObject.getString("status"), "serviceprovider", "");
                        }
                    }
                    else
                    {
                        if(HomeActivity.homeact)
                        {
                            if(jsonObject.getString("status").equals("priceupdate"))
                            {
                                if(NewJobRequest.jobRequest)
                                {
                                    Intent intent = new Intent("NOTIFYCUST");
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                                }
                                else
                                {
                                    requestid=jsonObject.getString("request_id");
                                    sendNotification(jsonObject.getString("message"), jsonObject.getString("status"), "customer", jsonObject.getString("request_id"));
                                }
                            }
                            else if(jsonObject.getString("status").equals("billgenerate"))
                            {
/*Message data payload: {status=billgenerate, type=billgenerate, request_id=395, price=600, message=Your bill recieved from Anil Sharma New.}*/
                                requestid=jsonObject.getString("request_id");
                                status=jsonObject.getString("status");
                                sendNotification(jsonObject.getString("message"), jsonObject.getString("status"), "customer",jsonObject.getString("request_id"));
//                                if(NewJobRequest.jobRequest)
//                                {
//                                    Intent intent = new Intent("NOTIFYCUST");
//                                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//                                }
//                                else
//                                {
//                                    requestid=jsonObject.getString("request_id");
//                                    sendNotification(jsonObject.getString("message"), jsonObject.getString("status"), "customer");
//                                }
                            }
                            else
                            {
                                if(MyBookingsFragment.bookingsFrag)
                                {
                                    Intent intent = new Intent("NOTIFYCUSTBOOK");
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                                }
                                else
                                {
                                    sendNotification(jsonObject.getString("message"), jsonObject.getString("status"), "customer", "");
                                }
                            }
                        }
                        else
                        {
                            sendNotification(jsonObject.getString("message"), jsonObject.getString("status"), "customer", jsonObject.getString("request_id"));
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if (remoteMessage.getNotification() != null) // Check if message contains a notification payload.
        {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void sendNotification(String message, String type, String login, String id)
    {

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon);
        Intent intent;
        if(login.equals("customer"))
        {
            intent = new Intent(this, HomeActivity.class);
        }
        else
        {
            intent = new Intent(this, HomeActivityServicePro.class);
        }
        intent.putExtra("requestid", id);
        intent.putExtra("status", type);
        intent.putExtra("type", "notify");
        intent.putExtra("notify", type);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notifi_icon)
//                .setLargeIcon(icon)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setColor(ContextCompat.getColor(MyFirebaseMessagingService.this, R.color.blue))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
//AIzaSyA0-bZUagt89GhxVnjLMP9PX3stw1bz0Y8
//Message data payload: {status=request, request_id=133, message=New job request recieved from Customer Six.}//requesting
//Message data payload: {minimum_charge=, address=B 23, Block B, Industrial Area, Sector 62, Noida, Uttar Pradesh 201309, status=priceupdate, service_provider_name=Service Provider Three, minCharge=58809, id=, date=1 January, 1970, category=, title=, message=Your Job price Updated by Service Provider Three, category_id=, service_provider_Id=}
//Message data payload: {customer_name=Customer Six, customer_Id=70, minimum_charge=58809, address=C-13, C Block, Sector 59, Noida, Uttar Pradesh 201301, status=pending, id=133, date=20 December, 2016, category=heavy, title=fcbc, message=Your Job request  accepted by Customer Six, category_id=heavy}

//{minimum_charge=, address=3, D Block, Sector 59, Noida, Uttar Pradesh 201301, mesage=Your Job price Updated  Reject by Service Provider 3, status=priceupdate, service_provider_name=Service Provider 3, minCharge=27889, id=, date=1 January, 1970, category=, title=, category_id=, service_provider_Id=}
//{customer_name=Customer Four, customer_Id=214, minimum_charge=27889, address=FNG Expressway Service Road, Amrapali Zodiac, Sector 120, Noida, Uttar Pradesh 201304, status=pending, id=64, date=25 November, 2016, category=light, title=fjfcjjffu, message=Your Job request  accepted by Customer Four, category_id=light}
//{minimum_charge=, address=3, D Block, Sector 59, Noida, Uttar Pradesh 201301, status=priceupdate, service_provider_name=Service Provider 3, minCharge=27884, id=, date=1 January, 1970, category=, title=, message=Your Job price Updated by Service Provider 3, category_id=, service_provider_Id=}
//'request','reject','process','pending','complete','cancel','payment','priceupdate'
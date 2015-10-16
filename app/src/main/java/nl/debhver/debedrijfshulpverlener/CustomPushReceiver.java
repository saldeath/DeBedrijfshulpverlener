package nl.debhver.debedrijfshulpverlener;

/**
 * Created by Koen on 15-10-2015.
 */

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class CustomPushReceiver extends ParsePushBroadcastReceiver {
    public static final String PARSE_DATA_KEY = "com.parse.Data";
    public static final String EXTRA_INCIDENTID = "incident_id";

    @Override
    protected Notification getNotification(Context context, Intent intent) {
        Log.e("Push", "getNotificationCalled");
        // deactivate standard notification
        return null;
    }
   /* @Override
    public void onPushOpen(Context context, Intent intent) {
        Log.e("Push", "Clicked");
        Intent i = new Intent(context, IncidentOpener.class);
        i.putExtras(intent.getExtras());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }*/

    @Override
    public void onPushReceive(Context context, Intent intent){
        super.onPushReceive(context, intent);
        Log.e("Push", "Received");
        if (intent == null){
            return;
        }

        String title = "";
        String description = "";
        String location = "";

        JSONObject data = getDataFromIntent(intent);

        Intent notificationIntent = new Intent(context, IncidentOpener.class);


        Log.d("getDesc", data.toString());
        try {
            title = data.getString("title");
            description = data.getString("description");
            location = data.getString("location");
            String incidentId = data.getString("incidentId");
            Log.d("getDesc", "Extra Put " + incidentId);
            notificationIntent.putExtra(EXTRA_INCIDENTID, incidentId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PendingIntent contentIntent = PendingIntent.getActivity(context,0, notificationIntent,PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentIntent(contentIntent);
        builder.setContentTitle(title);
        builder.setContentText(description + " @ " + location);
        builder.setSmallIcon(R.mipmap.ic_launcher); // change image
        builder.setAutoCancel(true);

        // OPTIONAL create soundUri and set sound:
        builder.setSound(Uri.parse("android.resource://nl.debhver.debedrijfshulpverlener/raw/alarm_sound"));

        notificationManager.notify("MyTag", 0, builder.build());
    }

    private JSONObject getDataFromIntent(Intent intent) {
        JSONObject data = null;
        try {
            data = new JSONObject(intent.getExtras().getString(PARSE_DATA_KEY));
        } catch (JSONException e) {
            Log.d("JSONException", e.toString());
        }
        return data;
    }

    public void isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        if(isInBackground)
        {
            Log.d("APP", "IN BACKGROUND");
        }
        else {
            Log.d("APP", "NOT BACKGROUND");
        }
    }
}

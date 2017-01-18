package com.example.faizrehman.geofence_example;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by faizrehman on 1/3/17.
 */

public class GeofenceTransitionIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    protected static final String TAG = "Worker Thread";
    private DatabaseReference mDatabase;
// ...

    public GeofenceTransitionIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //get geofence data from the intent
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if(geofencingEvent.hasError()){
            String erorMsg = GeofenceErrorMsg.getErrorMessage(this,geofencingEvent.getErrorCode());
            Log.e(TAG, "onHandleIntent: "+erorMsg);
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){

            List<Geofence> geoFencingList = geofencingEvent.getTriggeringGeofences();

            String geofenceTransitionDetail = getTransitionDetail(this,geofenceTransition,geoFencingList);
            sendNotification(geofenceTransitionDetail);


         double Latitude =  geofencingEvent.getTriggeringLocation().getLatitude();

         double Longitude =  geofencingEvent.getTriggeringLocation().getLongitude();
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, ''yy", Locale.ENGLISH);
            String st_date = format.format(date);
            mDatabase.child("Geofence").push().setValue(new Model(Longitude,Latitude,geofenceTransitionDetail,st_date));

            Log.i(TAG, "onHandleIntent: "+geofenceTransitionDetail);
        }else{
            Log.e(TAG, "onHandleIntent: "+getString(R.string.geofence_transition_invalid_type) );
        }


    }

    private void sendNotification(String geofenceTransitionDetail) {
                Intent intentNoti = new Intent(getApplicationContext(),MainActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        // Add the main Activity to the task stack as the parent.
        taskStackBuilder.addParentStack(MainActivity.class);

        // Push the content Intent onto the stack.
        taskStackBuilder.addNextIntent(intentNoti);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // Define the notification settings.
        builder.setSmallIcon(R.mipmap.ic_launcher)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                .setColor(Color.RED)
                .setContentTitle(geofenceTransitionDetail)
                .setContentText(getString(R.string.geofence_transition_notification_text))
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());







    }

    public String getTransitionDetail(Context context,int geoTransition,List<Geofence> triggerGeoList){

        String transitionDetailString = getTransitionString(geoTransition);
        ArrayList triggerList = new ArrayList();
        for(Geofence geo:triggerGeoList){
            triggerList.add(geo.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ", triggerList);



        return transitionDetailString + ": " + triggeringGeofencesIdsString;
    }

    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return getString(R.string.geofence_transition_entered);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return getString(R.string.geofence_transition_exited);
            default:
                return getString(R.string.unknown_geofence_transition);
        }
    }
}

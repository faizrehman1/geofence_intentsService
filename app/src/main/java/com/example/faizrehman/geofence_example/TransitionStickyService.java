package com.example.faizrehman.geofence_example;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

/**
 * Created by faizrehman on 1/6/17.
 */

public class TransitionStickyService extends Service implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks,ResultCallback<Status> {

    GoogleApiClient mGoogleApiClient;
    PendingIntent mGeofencePendingIntent;
    protected ArrayList<Geofence> mGeofenceList;




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mGeofenceList= new ArrayList<>();

        mGeofencePendingIntent= null;

        if(!mGeofenceList.isEmpty()) {

            buildGoogleApiClient();

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mGoogleApiClient== null) {

            buildGoogleApiClient();

        }

        if(!mGoogleApiClient.isConnected()) {

            mGoogleApiClient.connect();

        }

        return super.onStartCommand(intent, flags, startId);
    }

    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient= new GoogleApiClient.Builder(this)

                .addConnectionCallbacks(this)

                .addOnConnectionFailedListener(this)

                .addApi(LocationServices.API)

                .build();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull Status status) {

    }
}


package com.iteyes.placesproject;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyLocationServices extends BroadcastReceiver {

    String msg;
    Double lat;
    Double lon;
    String deneme;
    public List<Place> findPlaces;

public static final String Action_Process_Update = "com.iteyes.placesproject.UPDATE_LOCATION";
    @Override
    public void onReceive(Context context, Intent intent) {
    if(intent != null)
    {
        final String action = intent.getAction();
        if(Action_Process_Update.equals(action))
        {
            LocationResult result = LocationResult.extractResult(intent);
            if(result != null)
            {
                Location location = result.getLastLocation();
                String location_string = new StringBuilder(" "+location.getLatitude())
                        .append("/")
                        .append(location.getLongitude())
                        .toString();

                lat=location.getLatitude();
                lon = location.getLongitude();


                try {

               //     MapActivity.getInstance().updateTextView(location_string,location.getLatitude(),location.getLongitude());

                } catch (Exception ex)
                {

                    Toast.makeText(context, location_string, Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(context, "null deperibiz", Toast.LENGTH_SHORT).show();
            }

        }
    }
    }

          }





package com.iteyes.placesproject;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyBackgroundServices extends Service {
    private static final String CHANNEL_ID = "my_channel";

    private static final String CHANNEL_IDs = "my_channels";
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = "com.iteyes.placesproject"
            + ".started_from_notification";
    private final  IBinder mBinder = new LocalBinder();
    Boolean kontrol =false;
    private static  final  long UPDATE_INTERVAL_IN_MIL = 10000;
    private static final  long  FASTES_UPDATE_INTERVAL_IN_MIL = UPDATE_INTERVAL_IN_MIL/2;
    private  static final  int NOTI_ID = 1223;
    private  static final  int NOTI_IDs = 1224;
    private  boolean mChangingConfiguration=false;
    private NotificationManager mNotificationManager;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private Handler mServiceHandler;
    private Location mLocation;
    public String isim;
    public  int sayactut=0;
    public String adress;
    public String latt;
    public int min=500; //mesafe
    public String longi;

    public  MyBackgroundServices()
    {

    }

    @Override
    public void onCreate() {
       fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback =new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());

            }
        };
        createLocationRequest();
        getLastLocation();

        HandlerThread handlerThread = new HandlerThread( "EDMTDev");
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
        mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID,
                    getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationChannel mChannels = new NotificationChannel(CHANNEL_IDs,
                    getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(mChannel);


        }



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION, false);
        if (startedFromNotification)
        {
            removeLocationUpdates();
            stopSelf();

        }
        return START_NOT_STICKY;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mChangingConfiguration = true;
    }

    private void removeLocationUpdates() {
        try {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            Common.setRequestingLocationUodates(this,false);
            stopSelf();
        }catch (SecurityException ex)
        {
            Common.setRequestingLocationUodates(this,true);
            Log.e("EDMT_DEV","Lost location permission. Could not remove updates "+ex);
        }
    }

    private void getLastLocation() {
        try {
            fusedLocationProviderClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if(task.isSuccessful() && task.getResult() != null)
                                mLocation = task.getResult();
                            else
                                Log.e("EDMT_DEV","failend to get location");
                        }
                    });
        }
        catch (SecurityException ex)
        {
           Log.e("EDMT_DEV","Lost location permission. "+ex);
        }
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MIL);
        locationRequest.setFastestInterval(FASTES_UPDATE_INTERVAL_IN_MIL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }
    String arananmallar;
    double distance;
    int sayac=0;
    double oncekiinstance;
    private static FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private static DatabaseReference mref = mDatabase.getReference("restaurant");
    private static DatabaseReference last = mDatabase.getReference("mylastlocation");
    DatabaseReference feri = mDatabase.getReference("sonreklam");
    private static List<veriisim> data = new ArrayList<>();

    VTDatabase vt = new VTDatabase();
    private void onNewLocation(final Location lastLocation) {

        feri.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                oncekiinstance = Double.valueOf(dataSnapshot.child("distance").getValue(String.class));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        mLocation = lastLocation;
        EventBus.getDefault().postSticky(new SendLocationToActivity(mLocation));
        //update etme yeri
        if(serviceIsRunningInForeGround(this)) {
            mNotificationManager.notify(NOTI_ID, getNotification());
        //    vt.lastlatlng_add(mLocation);
            mref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    List<String> keys = new ArrayList<>();
                    for(DataSnapshot keyNode : dataSnapshot.getChildren())
                    {
                        sayac++;

                        keys.add(keyNode.getKey());
                        veriisim verifies = keyNode.getValue(veriisim.class);
                           Location startPoint=new Location("locationA");
                        startPoint.setLatitude(Location.convert(verifies.getRestaurant_lat()));
                        startPoint.setLongitude(Location.convert(verifies.getRestaurant_lng()));

                        Location endPoint=new Location("locationA");
                        endPoint.setLatitude(lastLocation.getLatitude());
                        endPoint.setLongitude(lastLocation.getLongitude());

                  distance=startPoint.distanceTo(endPoint);
                        if (distance < min && distance>oncekiinstance) {
                            kontrol=true;
                            min = Integer.valueOf((int)distance);
                            vt.sonreklam(String.valueOf(distance));
                  sayactut=sayac;
                      isim = verifies.getRestaurant_name();
                      adress = verifies.getRestaurant_adress();
                      latt = verifies.getRestaurant_lat();
                      longi = verifies.getRestaurant_lng();


                  }


                    }
                    if(kontrol == false)
                        vt.sonreklam("0.0");

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MyBackgroundServices.this)
                            .setSmallIcon(R.drawable.ic_baseline_message_24)
                            .setContentTitle("Reklam")
                            .setContentText("Leziz restoran "+isim +" gelin :)")
                            .setAutoCancel(true);
                    Intent intent = new Intent(MyBackgroundServices.this,banner.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("isim",isim);
                    intent.putExtra("latitude",lastLocation.getLatitude());
                    intent.putExtra("longitude",lastLocation.getLatitude());
                    intent.putExtra("sayac",  sayactut);
                    intent.putExtra("distance",Integer.valueOf((int) min));

                    PendingIntent pendingIntent = PendingIntent.getActivity(MyBackgroundServices.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);
                    NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(0,builder.build());
                    sayac=0;
                    sayactut = 0;
                    min=500;;
                    kontrol = false;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

    private Notification getNotification() {
        Intent intent = new Intent(this,MyBackgroundServices.class);
        String text = Common.getLocationText(mLocation);
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION,true);
        PendingIntent servicePendingIntent = PendingIntent.getService(this,0,
               intent,PendingIntent.FLAG_UPDATE_CURRENT);
///////////////
        PendingIntent activityPendingIntent = PendingIntent.getActivity(this,
                0,new Intent(this,MapActivity.class),0);
        NotificationCompat.Builder builder= new NotificationCompat
                .Builder(this)
                .addAction(R.drawable.ic_baseline_launch_24,"Launch",activityPendingIntent)
                .addAction(R.drawable.ic_baseline_cancel_24,"Cancel",servicePendingIntent)
                .setContentText(text)
                .setContentTitle(Common.getLocationTitle(this))
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(text)
                .setWhen(System.currentTimeMillis());
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            builder.setChannelId(CHANNEL_ID);
            builder.setChannelId(CHANNEL_IDs);
        }
        return  builder.build();

    }

    private boolean serviceIsRunningInForeGround(Context context)  {
        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
    for(ActivityManager.RunningServiceInfo service:manager.getRunningServices(Integer.MAX_VALUE))
        if(getClass().getName().equals(service.service.getClassName()))
            if(service.foreground)
                return true;
              return false;
    }

    public void requestLocationUpdates() {
        Common.setRequestingLocationUodates(this,true);
        startService(new Intent(getApplicationContext(),MyBackgroundServices.class));
        try {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
        }
        catch (SecurityException ex)
        {
            Log.e("EDMT_DEV","lost location permission could not reuquest"+ex);
        }
    }

    public class LocalBinder extends Binder {
        MyBackgroundServices getService()
        {return  MyBackgroundServices.this;}
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        stopForeground(true);
        mChangingConfiguration = false;
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent)  {
        stopForeground(true);
        mChangingConfiguration =false;
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if(!mChangingConfiguration && Common.requestingLocationUodates(this)) {
            startForeground(NOTI_ID, getNotification());
        }


        return true;
    }

    @Override
    public void onDestroy() {
        mServiceHandler.removeCallbacks(null);
        super.onDestroy();
    }
}

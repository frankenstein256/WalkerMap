package com.iteyes.placesproject;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

import androidx.annotation.NonNull;

public class Common extends Activity {
   static Location mkLocation;
    public static final String KEY_REQUESTING_LOCATION_UPDATE = "LocationUpdateEnable";
     static VTDatabase vt = new VTDatabase();

    private static FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private static DatabaseReference mref = mDatabase.getReference("restaurant");
    private static List<veriisim> data = new ArrayList<>();


    public static String getLocationText(Location mLocation) {
       // vt.lastlatlng_add(mLocation);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                data.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren())
                {
                    keys.add(keyNode.getKey());
                    veriisim verifies = keyNode.getValue(veriisim.class);
                  //  data.add(verifies);
                    //burada lat ve lng ler kıyslanacak lanet reklam buraya gelecek amık he he
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return mLocation == null ? "Unknown Location" : new StringBuilder()
                .append(mLocation.getLatitude())
                .append("/")
                .append(mLocation.getLongitude())
                .toString();

    }



    public static CharSequence getLocationTitle(MyBackgroundServices myBackgroundServices) {
        return String.format("Location Update: %1$s", DateFormat.getDateInstance().format(new Date()));
    }

    public static void setRequestingLocationUodates(Context context, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATE,value)
                .apply();
    }

    public static boolean requestingLocationUodates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATE,false);
    }
}

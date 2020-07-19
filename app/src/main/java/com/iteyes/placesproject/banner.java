package com.iteyes.placesproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class banner extends AppCompatActivity {
    String words;
    TextView text;
    ProgressDialog progressDialog;
Bitmap bitmap;
ImageView resim;
String title;
double mesafe;
String mylatitudete;
String mylongitude;
String isim;
int sayac=0;
int saydir=0;
int distances =0;
int min=500;
    private static FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private static DatabaseReference mref ;
    private  List<herhangiideneme> data ;
    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner2);
        text = (TextView) findViewById(R.id.title1);
        mref =mDatabase.getReference("restadd");

        InterstitialAd interstitial;
        interstitial = new InterstitialAd(banner.this);
        interstitial.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        AdView adView = (AdView) this.findViewById(R.id.adView);


        AdRequest adRequest = new AdRequest.Builder()

                // Add a test device to show Test Ads
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addKeyword("Flowers")

                .build();
                adView.loadAd(adRequest);



        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {
            // String j =(String) b.get("name");
            isim = (String) b.get("isim");
            sayac=(int) b.get("sayac");
            distances = (int) b.get("distance");

        //    editor.putString("sayac", String.valueOf(sayac));

        }

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren())
                {
                    saydir++;
                    keys.add(keyNode.getKey());
                    herhangiideneme verifies = keyNode.getValue(herhangiideneme.class);
                  //  Toast.makeText(banner.this, lat+"mm"+lnt, Toast.LENGTH_SHORT).show();
                            if(sayac==saydir) {
                                text.setText("Yakınlarınızda ki restorana "+distances+" metre mesafeniz olan"+" "+isim+"Adres : "+verifies.getAdress());

                                break;
                            }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
       // new doo().execute();
    }







    }





/*
    class doo extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(banner.this);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //Connect to the website
                Document document = Jsoup.connect("https://www.zomato.com/tr/istanbul/durak-pastane-cafe-sultangazi-merkez-istanbul/photos").get();

                //Get the logo source of the website
                Element img = document.select("img").first();
                // Locate the src attribute
                String imgSrc = img.absUrl("src");
                // Download image from URL
                InputStream input = new java.net.URL(imgSrc).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);

                //Get the title of the website
                title = document.title();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            resim.setImageBitmap(bitmap);
            text.setText(lnt+ eee);
            progressDialog.dismiss();
        }
    }*/

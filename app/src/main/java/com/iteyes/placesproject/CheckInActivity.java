package com.iteyes.placesproject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CheckInActivity extends ListActivity {
    VTDatabase vt = new VTDatabase();

String msg;
    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        new GetPlaces(this, getListView()).execute();

    }


    class GetPlaces extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog;
        private Context context;
        private String[] placeName;
        private  double[] Placekoordinatelat;
        private  double[] Placekoordinatelong;
        private String[] imageUrl;
        private ListView listView;
String words;
        public GetPlaces(Context context, ListView listView) {
            // TODO Auto-generated constructor stub
            this.context = context;
            this.listView = listView;

        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dialog.dismiss();
            listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_expandable_list_item_1,placeName));
            //listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_expandable_list_item_1,imageUrl));

        }


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setCancelable(true);
            dialog.setMessage("Loading..");
            dialog.isIndeterminate();
            dialog.show();
        }
       public List<Place> findPlaces;
        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            Intent intent = getIntent();
            String msg = intent.getStringExtra("keyMessage");
            String searchplace = intent.getStringExtra("ss");
            Double lat = intent.getExtras().getDouble("lat");
            Double lon = intent.getExtras().getDouble("lon");


                PlaceService service = new PlaceService("AIzaSyBhs2LXC9uNJZJNR9I9KiR_B0ZD8YIisM0");
                //burası girdiğimiz arama cubundaki mekanın çevresindeki
            if(!searchplace.equals("")) {
                if (Geocoder.isPresent()) {
                    try {
                        String location = searchplace;
                        Geocoder gc = new Geocoder(CheckInActivity.this);
                        List<Address> addresses = gc.getFromLocationName(location, 5); // get the found Address Objects

                        List<LatLng> ll = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
                        for (Address a : addresses) {
                            if (a.hasLatitude() && a.hasLongitude()) {
                                ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
                                //Toast.makeText(getApplicationContext(), a.getLatitude()+" "+a.getLongitude(), Toast.LENGTH_LONG).show();
                             findPlaces = service.findPlaces(a.getLatitude(), a.getLongitude(), msg);  // hospiral for hospital
                            }
                        }
                    } catch (IOException e) {
                        // handle the exception
                    }
                }
            }
            else
            {
                findPlaces = service.findPlaces(lat,lon, msg);  // hospiral for hospital

            }



            // atm for ATM

            placeName = new String[findPlaces.size()];
            imageUrl = new String[findPlaces.size()];
            Placekoordinatelat = new double[findPlaces.size()];
            Placekoordinatelong = new double[findPlaces.size()];
            for (int i = 0; i < findPlaces.size(); i++) {

                Place placeDetail = findPlaces.get(i);
                placeDetail.getIcon();

                System.out.println(placeDetail.getName());
                placeName[i] = placeDetail.getName();;
                Placekoordinatelat[i]=placeDetail.getLatitude();
                Placekoordinatelong[i]=placeDetail.getLongitude();

                imageUrl[i] =placeDetail.getIcon();
               if(msg.equals("restaurant")) {
                    vt.setDTDeg(msg);
                    vt.set_adress(placeDetail.getVicinity());
                    vt.set_id(placeDetail.getId());
                    vt.set_lat(placeDetail.getLatitude().toString());
                    vt.set_lng(placeDetail.getLongitude().toString());
                    vt.set_name(placeDetail.getName());
                    vt.gonder(vt.get_id(), vt.get_name(), vt.get_lat(), vt.get_lng(), vt.get_adress(), vt.getDTDeg());
                    vt.gonders(vt.get_adress(), vt.get_name(), vt.get_lng(), vt.get_lng(), vt.get_id());
                }//sadece restorantları ekle vt ye veri fazla olmasın
            }
            return null;

        }


        {

        }

    }

}

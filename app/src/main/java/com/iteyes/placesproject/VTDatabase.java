package com.iteyes.placesproject;

import android.location.Location;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.interfaces.DSAPublicKey;

public class VTDatabase {
    private  String _name;
    private  String  _lat;
    private  String _lng;


    public double getLastlt() {
        return lastlt;
    }

    public void setLastlt(double lastlt) {
        this.lastlt = lastlt;
    }

    public double getLastlg() {
        return lastlg;
    }

    public void setLastlg(double lastlg) {
        this.lastlg = lastlg;
    }

    private String DTDeg ;
    private double lastlt;
    private double lastlg;


    public String getDTDeg() {
        return DTDeg;
    }

    public void setDTDeg(String DTDeg) {
        this.DTDeg = DTDeg;
    }


    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        private String _id;
        private String  _adress;

    public String get_adress() {
        return _adress;
    }

    public void set_adress(String _adress) {
        this._adress = _adress;
    }

    DatabaseReference dtreferencesname;

    public void gonder(String ic_id, String ic_name,String ic_lat,String ic_lng,String ic_adress,String vtisim)
        {

             dtreferencesname = mDatabase.getReference(vtisim).child(ic_id);
            dtreferencesname.child( vtisim+"_address").setValue(ic_adress);
            dtreferencesname.child(vtisim+"_ID").setValue(ic_id);
            dtreferencesname.child(vtisim+"_name").setValue(ic_name);
            dtreferencesname.child( vtisim+"_lat").setValue(ic_lat);
            dtreferencesname.child( vtisim+"_lng").setValue(ic_lng);

                dtreferencesname.push();




        }
    public void gonders(String ic_adress ,String ic_name,String ic_lat,String ic_lng,String ic_id )
    {
        dtreferencesname = mDatabase.getReference("restadd").child(ic_id);
        dtreferencesname.child("adress").setValue(ic_adress);
        dtreferencesname.child("name").setValue(ic_name);
        dtreferencesname.child("latitude").setValue(ic_lat);
        dtreferencesname.child("longitude").setValue(ic_lng);
        dtreferencesname.push();
    }
    public void sonreklam(String value)
    {
        dtreferencesname = mDatabase.getReference("sonreklam");
        dtreferencesname.child("distance").setValue(value);
    }

        public void lastlatlng_add(Location lastlng)
        {
            dtreferencesname = mDatabase.getReference("mylastlocation");
            dtreferencesname.child("lat").setValue(lastlng.getLatitude());
            dtreferencesname.child("lon").setValue(lastlng.getLongitude());


        }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_lat() {
        return _lat;
    }

    public void set_lat(String _lat) {
        this._lat = _lat;
    }

    public String get_lng() {
        return _lng;
    }

    public void set_lng(String _lng) {
        this._lng = _lng;
    }





}

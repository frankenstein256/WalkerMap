package com.iteyes.placesproject;

public class veriisim  {
        private String  restaurant_adress;
        private String restaurant_lat;
        private String restaurant_lng;
        private String restaurant_name;
        private  String id;

    public  veriisim()
    {}

    public veriisim( String id, String restaurant_lat, String restaurant_lng, String restaurant_name,String restaurant_adress) {
        this.id = id;
        this.restaurant_adress = restaurant_adress;
        this.restaurant_lat = restaurant_lat;
        this.restaurant_lng = restaurant_lng;
        this.restaurant_name = restaurant_name;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getRestaurant_adress() { return restaurant_adress; }

    public void setRestaurant_adress(String restaurant_adress) {
        this.restaurant_adress = restaurant_adress;
    }

    public String getRestaurant_lat() {
        return restaurant_lat;
    }

    public void setRestaurant_lat(String restaurant_lat) {
        this.restaurant_lat = restaurant_lat;
    }

    public String getRestaurant_lng() {
        return restaurant_lng;
    }

    public void setRestaurant_lng(String restaurant_lng) {
        this.restaurant_lng = restaurant_lng;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

}

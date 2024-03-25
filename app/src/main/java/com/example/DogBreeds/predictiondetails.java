package com.example.DogBreeds;

public class predictiondetails {
    String image;
    double lat,lan;

    predictiondetails(String image,double lat,double lan){
        this.image=image;
        this.lat=lat;
        this.lan=lan;

    }


    public String getImage() {
        return image;
    }

    public double getLat() {
        return lat;
    }

    public double getLan() {
        return lan;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLan(double lan) {
        this.lan = lan;
    }
}

package com.geekworkx.hellocab.Model;

/**
 * Created by parag on 29/01/18.
 */

public class Latlong {

    public String Mobile;
    public double Lat;
    public double Long;
    public double Latn;
    public double Longn;



    public Latlong() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Latlong(String Mobile,double Lat,double Long,double Latn,double Longn){
       this.Mobile=Mobile;
        this.Lat=Lat;
        this.Long=Long;
        this.Latn=Latn;
        this.Longn=Longn;
    }

    public double getLatn(int position) {
        return Latn;
    }

    public void setLatn(double latn) {
        Latn = latn;
    }

    public double getLongn() {
        return Longn;
    }

    public void setLongn(double longn) {
        Longn = longn;
    }

    public String getMobile(int position) {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public double getLat(int position) {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLong(int postion) {
        return Long;
    }

    public void setLong(double aLong) {
        Long = aLong;
    }
}

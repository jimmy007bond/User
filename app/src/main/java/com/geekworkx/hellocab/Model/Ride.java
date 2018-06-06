package com.geekworkx.hellocab.Model;

/**
 * Created by parag on 13/09/17.
 */

public class Ride  {

    private double Lat;
    private double Long;
    private double Latp;
    private double Longp;
    private double Latn;
    private double Longn;
    private String Vehicle;
    private String Mobile;
    private int Driver_ID;


    public Ride() {
    }


    public Ride( int Driver_ID,double Lat, double Long,double Latp, double Longp,double Latn, double Longn,String Vehicle,String Mobile) {


        this.Lat=Lat;
        this.Long=Long;
        this.Latp=Latp;
        this.Longp=Longp;
        this.Latn=Latn;
        this.Longn=Longn;
        this.Vehicle=Vehicle;
        this.Mobile=Mobile;
        this.Driver_ID=Driver_ID;
    }

    public int getDriver_ID(int position) {
        return Driver_ID;
    }

    public void setDriver_ID(int driver_ID) {
        this.Driver_ID = driver_ID;
    }

    public String getMobile(int position) {
        return Mobile;
    }

    public void setMobile(String imei) {
        Mobile = imei;
    }

    public String getVehicle(int position) {
        return Vehicle;
    }

    public void setVehicle(String vehicle) {
        Vehicle = vehicle;
    }

    public double getLat(int position) {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLong(int position) {
        return Long;
    }

    public void setLong(double aLong) {
        Long = aLong;
    }

    public double getLatp(int position) {
        return Latp;
    }

    public void setLatp(double latp) {
        Latp = latp;
    }

    public double getLongp(int position) {
        return Longp;
    }

    public void setLongp(double longp) {
        Longp = longp;
    }

    public double getLatn(int position) {
        return Latn;
    }

    public void setLatn(double latn) {
        Latn = latn;
    }

    public double getLongn(int position) {
        return Longn;
    }

    public void setLongn(double longn) {
        Longn = longn;
    }
}

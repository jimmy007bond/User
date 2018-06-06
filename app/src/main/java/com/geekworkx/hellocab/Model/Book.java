package com.geekworkx.hellocab.Model;

/**
 * Created by parag on 02/01/18.
 */

public class Book
{

    private double From_Lat;
    private double From_Long;
    private double To_Lat;
    private double To_Long;
    private String Vehicle;
    private double Distance;
    private String Owner;
    private String UserMobile;
    private String DriverMobile;
    private String OTP;
    private String UserAccept;
    private String DriverAccept;
    private String Stop;
    private String Paid;
    private String Rate_user;

    public Book() {
    }


    public Book( double From_lat, double From_Long,double To_Lat,
                 double To_Long,String UserMobile,String DriverMobile,
                 String Vehicle,double Distance,String Owner,String OTP,
                 String UserAccept,String DriverAccept,String Stop,String Paid,String Rate_user) {


        this.From_Lat=From_lat;
        this.From_Long=From_Long;
        this.To_Lat=To_Lat;
        this.To_Long=To_Long;
        this.Vehicle=Vehicle;
        this.Distance=Distance;
        this.Owner=Owner;
        this.UserMobile=UserMobile;
        this.DriverMobile=DriverMobile;
        this.OTP=OTP;
        this.UserAccept=UserAccept;
        this.DriverAccept=DriverAccept;
        this.Stop=Stop;
        this.Paid=Paid;
        this.Rate_user=Rate_user;
    }

    public String getRate_user() {
        return Rate_user;
    }

    public void setRate_user(String rate_user) {
        Rate_user = rate_user;
    }

    public String getPaid() {
        return Paid;
    }

    public void setPaid(String paid) {
        Paid = paid;
    }

    public String getStop() {
        return Stop;
    }

    public void setStop(String stop) {
        Stop = stop;
    }

    public String getUserMobile() {
        return UserMobile;
    }

    public void setUserMobile(String userMobile) {
        UserMobile = userMobile;
    }

    public String getDriverMobile() {
        return DriverMobile;
    }

    public void setDriverMobile(String driverMobile) {
        DriverMobile = driverMobile;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public double getFrom_Lat() {
        return From_Lat;
    }

    public void setFrom_Lat(double from_Lat) {
        From_Lat = from_Lat;
    }

    public double getFrom_Long() {
        return From_Long;
    }

    public void setFrom_Long(double from_Long) {
        From_Long = from_Long;
    }

    public double getTo_Lat() {
        return To_Lat;
    }

    public void setTo_Lat(double to_Lat) {
        To_Lat = to_Lat;
    }

    public double getTo_Long() {
        return To_Long;
    }

    public void setTo_Long(double to_Long) {
        To_Long = to_Long;
    }

    public String getVehicle() {
        return Vehicle;
    }

    public void setVehicle(String vehicle) {
        Vehicle = vehicle;
    }


    public double getDistance() {
        return Distance;
    }

    public void setDistance(double dist) {
        Distance = dist;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public String getUserAccept() {
        return UserAccept;
    }

    public void setUserAccept(String userAccept) {
        UserAccept = userAccept;
    }

    public String getDriverAccept() {
        return DriverAccept;
    }

    public void setDriverAccept(String driverAccept) {
        DriverAccept = driverAccept;
    }
}


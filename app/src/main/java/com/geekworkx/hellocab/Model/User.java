package com.geekworkx.hellocab.Model;

/**
 * Created by parag on 04/11/17.
 */

public class User {

   public String from;
    public String to;
    public String date;
    public String time;
    public int OTP;
    public String Snapshot;
    private String DriverImage;
    private String Unique_id;
    private String Vehicle;
    private String Drivername;
    private String DriverRate;
    private String Cost;
    private String Start_time;
    private String End_time;
    private String Review;
    private String Vehicle_choosen;
    private double To_Lat;
    private double To_Long;
    private int _exp;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public User(double To_Lat,
                double To_Long,String from,String to,String date,String time,int OTP,
                String Snapshot,String DriverImage,String Unique_id,String Vehicle
    ,String Drivername,String DriverRate,String Cost,String Start_time,String End_time,
                String Review,String Vehicle_choosen,int _exp){
        this.from=from;
        this.to=to;
        this.To_Lat=To_Lat;
        this.To_Long=To_Long;
        this.date=date;
        this.time=time;
        this.OTP=OTP;
        this.Snapshot=Snapshot;
        this.DriverImage=DriverImage;
        this.Unique_id=Unique_id;
        this.Vehicle=Vehicle;
        this.Drivername=Drivername;
        this.DriverRate=DriverRate;
        this.Cost=Cost;
        this.Start_time=Start_time;
        this.End_time=End_time;
        this.Review=Review;
        this.Vehicle_choosen=Vehicle_choosen;
        this._exp=_exp;
    }

    public int get_exp(int position) {
        return _exp;
    }

    public void set_exp(int _exp) {
        this._exp = _exp;
    }

    public double getTo_Lat(int position) {
        return To_Lat;
    }

    public void setTo_Lat(double to_Lat) {
        To_Lat = to_Lat;
    }

    public double getTo_Long(int position) {
        return To_Long;
    }

    public void setTo_Long(double to_Long) {
        To_Long = to_Long;
    }
    public String getVehicle_choosen(int position) {
        return Vehicle_choosen;
    }

    public void setVehicle_choosen(String vehicle_choosen) {
        Vehicle_choosen = vehicle_choosen;
    }

    public String getReview(int position) {
        return Review;
    }

    public void setReview(String review) {
        Review = review;
    }

    public String getStart_time(int position) {
        return Start_time;
    }

    public void setStart_time(String start_time) {
        Start_time = start_time;
    }

    public String getEnd_time(int position) {
        return End_time;
    }

    public void setEnd_time(String end_time) {
        End_time = end_time;
    }

    public String getCost(int position) {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }

    public String getDriverRate(int position) {
        return DriverRate;
    }

    public void setDriverRate(String driverRate) {
        DriverRate = driverRate;
    }

    public String getDrivername(int position) {
        return Drivername;
    }

    public void setDrivername(String drivername) {
        Drivername = drivername;
    }

    public String getVehicle(int position) {
        return Vehicle;
    }

    public void setVehicle(String vehicle) {
        Vehicle = vehicle;
    }

    public String getUnique_id(int position) {
        return Unique_id;
    }

    public void setUnique_id(String unique_id) {
        Unique_id = unique_id;
    }

    public String getDriverImage(int position) {
        return DriverImage;
    }

    public void setDriverImage(String driverImage) {
        DriverImage = driverImage;
    }

    public String getSnapshot(int position) {
        return Snapshot;
    }

    public void setSnapshot(String snapshot) {
        Snapshot = snapshot;
    }

    public int getOTP(int position) {
        return OTP;
    }

    public void setOTP(int OTP) {
        this.OTP = OTP;
    }

    public String getFrom(int position) {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo(int position) {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDate(int position) {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime(int position) {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
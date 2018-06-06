package com.geekworkx.hellocab.Model;

/**
 * Created by parag on 16/03/18.
 */

public class Rate  {

    private  String Vehicle_Type;
    private  int  Minimum_Rate,Hourly_Rate;

    public Rate() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Rate(String Vehicle_Type,int Minimum_Rate,int Hourly_Rate) {
        this.Vehicle_Type=Vehicle_Type;
        this.Minimum_Rate=Minimum_Rate;
        this.Hourly_Rate=Hourly_Rate;
    }


    public String getVehicle_Type(int position) {
        return Vehicle_Type;
    }

    public void setVehicle_Type(String vehicle_Type) {
        Vehicle_Type = vehicle_Type;
    }

    public int getMinimum_Rate(int position) {
        return Minimum_Rate;
    }

    public void setMinimum_Rate(int minimum_Rate) {
        Minimum_Rate = minimum_Rate;
    }

    public int getHourly_Rate(int position) {
        return Hourly_Rate;
    }

    public void setHourly_Rate(int hourly_Rate) {
        Hourly_Rate = hourly_Rate;
    }
}

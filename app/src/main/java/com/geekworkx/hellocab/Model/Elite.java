package com.geekworkx.hellocab.Model;

/**
 * Created by parag on 18/02/18.
 */

public class Elite {

    public String Plane_Name;
    public int Amount;
    public int Validity_in_Days;
    public String Description;




    public Elite() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Elite(String Plane_Name,int Amount,int Validity_in_Days,String Description){
        this.Plane_Name=Plane_Name;
        this.Amount=Amount;
        this.Validity_in_Days=Validity_in_Days;
        this.Description=Description;

    }

    public String getPlane_Name(int position) {
        return Plane_Name;
    }

    public void setPlane_Name(String plane_Name) {
        Plane_Name = plane_Name;
    }

    public int getAmount(int position) {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public int getValidity_in_Days(int position) {
        return Validity_in_Days;
    }

    public void setValidity_in_Days(int validity_in_Days) {
        Validity_in_Days = validity_in_Days;
    }

    public String getDescription(int position) {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}

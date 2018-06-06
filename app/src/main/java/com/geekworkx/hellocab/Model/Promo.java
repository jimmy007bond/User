package com.geekworkx.hellocab.Model;

/**
 * Created by parag on 02/01/18.
 */

public class Promo
{


    private String Promo_Code;
    private double Discount_Value;
    private String Drop_Location;
    private String App_Invitation;
    private String Applicable_Place;

  

    public Promo() {
    }


    public Promo(
                 String App_Invitation,String Applicable_Place,
                 String Promo_Code,double Discount_Value,String Drop_Location) {


    
        this.Promo_Code=Promo_Code;
        this.Discount_Value=Discount_Value;
        this.Drop_Location=Drop_Location;
        this.App_Invitation=App_Invitation;
        this.Applicable_Place=Applicable_Place;

        
    }

 
    public String getApp_Invitation(int position) {
        return App_Invitation;
    }

    public void setApp_Invitation(String App_Invitation) {
        this.App_Invitation =App_Invitation;
    }

    public String getApplicable_Place(int position) {
        return Applicable_Place;
    }

    public void setApplicable_Place(String Applicable_Place) {
        this.Applicable_Place = Applicable_Place;
    }

    public String getDrop_Location(int position) {
        return Drop_Location;
    }

    public void setDrop_Location(String Drop_Location) {
        this.Drop_Location = Drop_Location;
    }



    public String getPromo_Code(int position) {
        return Promo_Code;
    }

    public void setPromo_Code(String Promo_Code) {
        this.Promo_Code = Promo_Code;
    }


    public double getDiscount_Value(int position) {
        return Discount_Value;
    }

    public void setDiscount_Value(double Discount_Value) {
        this.Discount_Value =Discount_Value;
    }


 
}


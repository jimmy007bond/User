package com.geekworkx.hellocab.Model;

/**
 * Created by parag on 17/03/18.
 */

public class Taxes  {

    private  String Tax_name;
    private  double  Tax_Rate;

    public Taxes() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Taxes(String Tax_name,double Tax_Rate) {
        this.Tax_name=Tax_name;
        this.Tax_Rate=Tax_Rate;

    }


    public String getTax_name(int position) {
        return Tax_name;
    }

    public void setTax_name(String tax_name) {
        Tax_name = tax_name;
    }

    public double getTax_Rate(int position) {
        return Tax_Rate;
    }

    public void setTax_Rate(double tax_Rate) {
        Tax_Rate = tax_Rate;
    }
}

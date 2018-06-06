package com.geekworkx.hellocab.Model;

/**
 * Created by parag on 06/03/18.
 */

public class Date_ {

    public String date;
    public String time;
    public Boolean Selected;
    public String Date_selected;

    public Date_() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Date_(String date,String time,Boolean Selected,String Date_selected){
        this.date=date;
        this.time=time;
        this.Selected=Selected;
        this.Date_selected=Date_selected;
    }

    public String getDate_selected(int position) {
        return Date_selected;
    }

    public void setDate_selected(String date_selected) {
        Date_selected = date_selected;
    }

    public Boolean getSelected(int position) {
        return Selected;
    }

    public void setSelected(Boolean selected) {
        Selected = selected;
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

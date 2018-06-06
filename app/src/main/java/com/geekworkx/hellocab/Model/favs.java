package com.geekworkx.hellocab.Model;

/**
 * Created by parag on 18/02/18.
 */

public class favs {

    public String from;
    public String to;
    public String home;
    public String work;
    public String other;



    public favs() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public favs(String from,String to,String home,String work,String other){
        this.from=from;
        this.to=to;
        this.home=home;
        this.work=work;
        this.other=other;
    }

    public String getother(int position) {
        return other;
    }

    public void setother(String other) {
        this.other = other;
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

    public String getHome(int position) {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getWork(int postion) {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }
}

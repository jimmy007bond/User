package com.geekworkx.hellocab.Model;

/**
 * Created by parag on 28/02/18.
 */

public class Contacts {

    public String Name;
    public String Contacts;

    public Contacts() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Contacts(String Name,String Contacts){
        this.Name=Name;
        this.Contacts=Contacts;

    }

    public String getName(int position) {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getContacts(int position) {
        return Contacts;
    }

    public void setContacts(String contacts) {
        Contacts = contacts;
    }
}

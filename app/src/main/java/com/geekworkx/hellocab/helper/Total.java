package com.geekworkx.hellocab.helper;

/**
 * Created by parag on 12/11/16.
 */
public class Total {

    private String name;
    private String thumbnailUrl;
    private String mapUrl;
    private String from_address;
    private String via1;
    private String via2;
    private String mobile;
    private String to_address;
    private String vehicle_no;
    private String vehicle_company;
    private String vehicle_model;
    private String vehicle_color;
    private String date;
    private String rate;
    private String unique_id;
    private String hour;
    private String minute;
    private String vehicletype;
    private String message;
    private String idfacebook;
    private String vehicleImage;
    private double from_lat;
    private double from_long;
    private double to_lat;
    private double to_long;
    private double Via1_lat;
    private double Via1_long;
    private double Via2_lat;
    private double Via2_long;
    private double dist;
    private String Confirm;
    private String Reason;
    private String Ridermobile;
    private String Usermobile;
    private String Progress;
    private double Rating,Nop;
    private String Amount;
    public Total() {
    }



    public Total(String name, String thumbnailUrl, String mapUrl,String from_address, String to_address, String via1,
                 String via2, String mobile, String vehicle_no,String Progress,double Rating,double Nop,
                 String vehicle_model, String vehicle_company, String vehicle_color, String date,String Amount,
                 String rate, String unique_id, String hour, String minute , String vehicletype,
                 String message,String idfacebook,String vehicleImage,double from_lat,double from_long,double to_lat,double to_long,
                 double Via1_lat,double Via1_long,double Via2_lat,double Via2_long,double dist,String Confirm,String Reason,String Ridermobile,String Usermobile) {
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.from_address=from_address;
        this.to_address=to_address;
        this.via1=via1;
        this.via2=via2;
        this.mobile=mobile;
        this.vehicle_no=vehicle_no;
        this.vehicle_color=vehicle_color;
        this.vehicle_model=vehicle_model;
        this.vehicle_company=vehicle_company;
        this.date=date;
        this.rate=rate;
        this.unique_id=unique_id;
        this.hour = hour;
        this.minute=minute;
        this.vehicletype=vehicletype;
        this.message=message;
        this.idfacebook=idfacebook;
        this.vehicleImage=vehicleImage;
        this.from_lat=from_lat;
        this.from_long=from_long;
        this.to_lat=to_lat;
        this.to_long=to_long;
        this.Via1_lat=Via1_lat;
        this.Via1_long=Via1_long;
        this.Via2_lat=Via2_lat;
        this.Via2_long=Via2_long;
        this.dist=dist;
        this.Confirm=Confirm;
        this.Reason=Reason;
        this.Ridermobile=Ridermobile;
        this.Usermobile=Usermobile;
        this.mapUrl=mapUrl;
        this.Progress=Progress;
        this.Rating=Rating;
        this.Nop=Nop;
        this.Amount=Amount;
    }

    public String getAmount(int position) {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public double getRating(int position) {
        return Rating;
    }

    public void setRating(double rating) {
        Rating = rating;
    }

    public double getNop(int position) {
        return Nop;
    }

    public void setNop(double nop) {
        Nop = nop;
    }

    public String getProgress(int position) {
        return Progress;
    }

    public void setProgress(String progress) {
        Progress = progress;
    }

    public String getMapUrl(int position) {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public String getRidermobile(int position) {
        return Ridermobile;
    }

    public void setRidermobile(String ridermobile) {
        Ridermobile = ridermobile;
    }

    public String getUsermobile(int position) {
        return Usermobile;
    }

    public void setUsermobile(String usermobile) {
        Usermobile = usermobile;
    }

    public String getReason(int position) {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getConfirm(int position) {
        return Confirm;
    }

    public void setConfirm(String confirm) {
        Confirm = confirm;
    }

    public double getDist(int position) {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public double getFrom_lat(int position) {
        return from_lat;
    }

    public void setFrom_lat(double from_lat) {
        this.from_lat = from_lat;
    }

    public double getFrom_long(int position) {
        return from_long;
    }

    public void setFrom_long(double from_long) {
        this.from_long = from_long;
    }

    public double getTo_lat(int position) {
        return to_lat;
    }

    public void setTo_lat(double to_lat) {
        this.to_lat = to_lat;
    }

    public double getTo_long(int position) {
        return to_long;
    }

    public void setTo_long(double to_long) {
        this.to_long = to_long;
    }

    public double getVia1_lat(int position) {
        return Via1_lat;
    }

    public void setVia1_lat(double via1_lat) {
        Via1_lat = via1_lat;
    }

    public double getVia1_long(int position) {
        return Via1_long;
    }

    public void setVia1_long(double via1_long) {
        Via1_long = via1_long;
    }

    public double getVia2_lat(int position) {
        return Via2_lat;
    }

    public void setVia2_lat(double via2_lat) {
        Via2_lat = via2_lat;
    }

    public double getVia2_long(int position) {
        return Via2_long;
    }

    public void setVia2_long(double via2_long) {
        Via2_long = via2_long;
    }

    public String getVehicleImage(int position) {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public String getIdfacebook(int position) {
        return idfacebook;
    }

    public void setIdfacebook(String idfacebook) {
        this.idfacebook = idfacebook;
    }

    public String getMessage(int position) {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVehicletype(int position) {
        return vehicletype;
    }

    public void setVehicletype(String vehicletype) {
        this.vehicletype = vehicletype;
    }

    public String getName(int position) {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnailUrl(int position) {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getFrom_address(int position) {
        return from_address;
    }

    public void setFrom_address(String from_address) {
        this.from_address = from_address;
    }

    public String getTo_address(int position) {
        return to_address;
    }

    public void setTo_address(String to_address) {
        this.to_address = to_address;
    }

    public String getVehicle_no(int position) {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public String getVehicle_company(int position) {
        return vehicle_company;
    }

    public void setVehicle_company(String vehicle_company) {
        this.vehicle_company = vehicle_company;
    }

    public String getVehicle_model(int position) {
        return vehicle_model;
    }

    public void setVehicle_model(String vehicle_model) {
        this.vehicle_model = vehicle_model;
    }

    public String getVehicle_color(int position) {
        return vehicle_color;
    }

    public void setVehicle_color(String vehicle_color) {
        this.vehicle_color = vehicle_color;
    }

    public String getDate(int position) {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRate(int position) {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getUnique_id(int position) {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public String getHour(int position) {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute(int position) {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getVia1(int position) {
        return via1;
    }

    public void setVia1(String via1) {
        this.via1 = via1;
    }

    public String getVia2(int position) {
        return via2;
    }

    public void setVia2(String via2) {
        this.via2 = via2;
    }

    public String getMobile(int position) {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}


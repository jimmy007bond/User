package com.geekworkx.hellocab.URLS;

//This class is for storing all URLs as a model of URLs
public class Config_URL
{
    public static final String URL = "https://geekworkx.app/IndCab";

    public static final String URL_STORE_ALL_LATLNG =URL + "/store_lat_user.php";
    public static final String URL_ADD_DRIVER = URL + "/app_add_driver.php";
    public static final String URL_COMMENT = URL + "/user_comment.php";
    public static final String GET_ALL_DATA = URL + "/get_all_data.php";
    public static final String GET_ALL_BOOKING = URL + "/get_all_booking.php";
    public static final String GET_STOP_BOOKING = URL + "/get_stop_booking.php";
    public static final String URL_REQUEST_SMS =URL + "/user_profile.php" ;
    public static final String URL_VERIFY_OTP = URL + "/verify_otp.php";
    public static final String URL_STORE_USER_LATLNG =URL + "/store_user_lat_long.php";
    public static final String SMS_ORIGIN = "BDLIFT";
    public static final String OTP_DELIMITER = ":";
    public static final String USER_BOOKING_RIDE = URL + "/Booking_ride_user.php";
    public static final String STOP_BOOKING = URL + "/stop_ride_user.php";
    public static final String ADD_FAVOURITE = URL + "/add_fav.php";
    public static final String ADD_LATER_RIDE = URL + "/add_later_ride.php";
    public static final String ADD_LATER_RIDE_PLACE = URL + "/add_later_ride_place.php";
    public static final String DELETE_RIDE =URL + "/delete_ride.php";
    public static final String DELETE_RIDE_ALL =URL + "/delete_ride_all.php";
    public static final String URL_EMERGENCY =URL + "/emergency_contacts.php";
    public static final String GET_CONTACTS = URL + "/contacts.php";
    public static final String GET_ALL_USER =URL + "/get_refer_user.php";
    public static final String GET_SUPPORT_RIDE =URL + "/get_support_booking.php";
    public static final String BOOKING_RIDE_ANOTHER_CAR = URL + "/another_car.php";
    public static final String ADD_LATER_OUTSTATION =URL + "/add_later_ride_outstation.php";
    public static final String GET_SETTINGS =URL + "/get_settings.php";
    public static final String URL_VERIFY_USER_OTP =URL + "/verify_user_otp.php";
    public static final String URL_STORE_USER_PROFILE =URL + "/store_user_profile_data.php";
    public static final String ADD_LATER_CHANGE_TIME = URL + "/change_ride_later_time.php";
    public static final String URL_REQUEST_MOBILE=URL + "/User_registration_with_OTP.php";
    public static final String URL_EMERGENCY_MOBILE=URL + "/User_emergency_no.php";
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String TOPIC_GLOBAL = "global";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final String SHARED_PREF = "ah_firebase";
    public static final String URL_FCM=URL + "/fcm_user.php";
    public static final String FCM_SENT =URL + "/fcm_sent.php" ;
    public static final String BOOKING_WEEK_DATA =URL + "/book_ride_week.php" ;
    public static final String ADD_LATER_RIDE_DELETE=URL + "/book_ride_delete.php" ;
    public static final String CONTACT_DELETE=URL + "/contact_delete.php" ;
    public static final String GET_ALL_DRIVERS = URL + "/get_all_drivers.php";

    public static String URL_COUPON_ADD= URL + "/add_coupon.php";
}

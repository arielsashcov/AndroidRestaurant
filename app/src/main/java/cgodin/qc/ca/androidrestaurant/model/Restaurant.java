package cgodin.qc.ca.androidrestaurant.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mohamed on 13/11/2017.
 */

public class Restaurant implements Parcelable{

    private String formatted_address;
    private String name;
    private double rating;
    private String place_id;
    private String photo_reference;
    private String img_link;
    private String place_url;
    private String formatted_phone_number;
    private String lat;
    private String lng;

    public Restaurant() {
    }



    public Restaurant(String formatted_address, String name, double rating, String place_id, String photo_reference, String img_link, String place_url, String formatted_phone_number, String lat, String lng) {
        this.formatted_address = formatted_address;
        this.name = name;
        this.rating = rating;
        this.place_id = place_id;
        this.photo_reference = photo_reference;
        this.img_link = img_link;
        this.place_url = place_url;
        this.formatted_phone_number = formatted_phone_number;
        this.lat = lat;
        this.lng = lng;
    }

    protected Restaurant(Parcel in) {
        formatted_address = in.readString();
        name = in.readString();
        rating = in.readDouble();
        place_id = in.readString();
        photo_reference = in.readString();
        img_link = in.readString();
        place_url = in.readString();
        formatted_phone_number = in.readString();
        lat = in.readString();
        lng = in.readString();
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getPhoto_reference() {
        return photo_reference;
    }

    public void setPhoto_reference(String photo_reference) {
        this.photo_reference = photo_reference;
    }

    public String getImg_link() {
        return img_link;
    }

    public void setImg_link(String img_link) {
        this.img_link = img_link;
    }

    public String getPlace_url() {
        return place_url;
    }

    public void setPlace_url(String place_url) {
        this.place_url = place_url;
    }

    public String getFormatted_phone_number() {
        return formatted_phone_number;
    }

    public void setFormatted_phone_number(String formatted_phone_number) {
        this.formatted_phone_number = formatted_phone_number;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "formatted_address='" + formatted_address + '\'' +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", place_id='" + place_id + '\'' +
                ", photo_reference='" + photo_reference + '\'' +
                ", img_link='" + img_link + '\'' +
                ", place_url='" + place_url + '\'' +
                ", formatted_phone_number='" + formatted_phone_number + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(formatted_address);
        dest.writeString(name);
        dest.writeDouble(rating);
        dest.writeString(place_id);
        dest.writeString(photo_reference);
        dest.writeString(img_link);
        dest.writeString(place_url);
        dest.writeString(formatted_phone_number);
        dest.writeString(lat);
        dest.writeString(lng);
    }
}

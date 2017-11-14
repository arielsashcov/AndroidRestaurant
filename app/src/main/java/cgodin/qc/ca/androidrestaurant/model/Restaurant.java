package cgodin.qc.ca.androidrestaurant.model;

/**
 * Created by Mohamed on 13/11/2017.
 */

public class Restaurant {

    private String formatted_address;
    private String name;
    private double rating;
    private String place_id;
    private String photo_reference;
    private String img_link;
    private String place_url;
    private String formatted_phone_number;

    public Restaurant() {
    }

    public Restaurant(String formatted_address, String name, double rating, String place_id, String photo_reference, String img_link, String place_url, String formatted_phone_number) {
        this.formatted_address = formatted_address;
        this.name = name;
        this.rating = rating;
        this.place_id = place_id;
        this.photo_reference = photo_reference;
        this.img_link = img_link;
        this.place_url = place_url;
        this.formatted_phone_number = formatted_phone_number;
    }

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
                '}';
    }
}

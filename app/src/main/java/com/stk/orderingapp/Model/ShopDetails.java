package com.stk.orderingapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by JARVIS on 30-Mar-18.
 */

public class ShopDetails extends DefaultResponse implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("shop_name")
    @Expose
    private String shop_name;

    @SerializedName("route_id")
    @Expose
    private String route_id;

    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("shop_id")
    @Expose
    private String enterable_shop_id;


    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @SerializedName("image_url")
    @Expose
    private String image_url;


    public String getEnterable_shop_id() {
        return enterable_shop_id;
    }

    public void setEnterable_shop_id(String enterable_shop_id) {
        this.enterable_shop_id = enterable_shop_id;
    }

    @Override
    public String toString() {
        return "ShopDetails{" +
                "id='" + id + '\'' +
                ", shop_name='" + shop_name + '\'' +
                ", route_id='" + route_id + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", distributor_id='" + distributor_id + '\'' +
                ", image_url='" + image_url + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("distributor_id")
    @Expose
    private String distributor_id;


    @SerializedName("latitude")
    @Expose
    private String latitude;

    @SerializedName("longitude")
    @Expose
    private String longitude;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistributor_id() {
        return distributor_id;
    }

    public void setDistributor_id(String distributor_id) {
        this.distributor_id = distributor_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    /*@SerializedName("shop_image")
    @Expose
    private String shop_image;

    public String getShop_image() {
        return shop_image;
    }

    public void setShop_image(String shop_image) {
        this.shop_image = shop_image;
    }*/

}

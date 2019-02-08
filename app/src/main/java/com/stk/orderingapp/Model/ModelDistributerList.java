package com.stk.orderingapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by JARVIS on 28-Mar-18.
 */

public class ModelDistributerList implements Serializable {


    @SerializedName("distributer_name")
    @Expose
    private String distributer_name;

    @SerializedName("dist_id")
    @Expose
    private String dist_id;

    @SerializedName("dist_address")
    @Expose
    private String dist_address;

    @SerializedName("last_visit")
    @Expose
    private String last_visit;

    @SerializedName("shop_image")
    @Expose
    private String shop_image;


    @SerializedName("mobile_no")
    @Expose
    private String mobile_no;

    @SerializedName("imageSaved")
    @Expose
    private String imageSaved;


    @SerializedName("remark")
    @Expose
    private String remark;


    @SerializedName("latitude")
    @Expose
    private String latitude;

    @SerializedName("longitude")
    @Expose
    private String longitude;

    @SerializedName("id")
    @Expose
    private String shop_id;



    @SerializedName("email")
    @Expose
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @SerializedName("city")
    @Expose
    private String city;


    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }




    public ModelDistributerList() {

    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getDistributer_name() {
        return distributer_name;
    }

    public void setDistributer_name(String distributer_name) {
        this.distributer_name = distributer_name;
    }

    public String getDist_id() {
        return dist_id;
    }

    public void setDist_id(String dist_id) {
        this.dist_id = dist_id;
    }

    public String getDist_address() {
        return dist_address;
    }

    public void setDist_address(String dist_address) {
        this.dist_address = dist_address;
    }

    public String getLast_visit() {
        return last_visit;
    }

    public void setLast_visit(String last_visit) {
        this.last_visit = last_visit;
    }

    public String getShop_image() {
        return shop_image;
    }

    public void setShop_image(String shop_image) {
        this.shop_image = shop_image;
    }

    public String getImageSaved() {
        return imageSaved;
    }

    public void setImageSaved(String imageSaved) {
        this.imageSaved = imageSaved;
    }


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude =  latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    @Override
    public String toString() {
        return "ModelDistributerList{" +
                "distributer_name='" + distributer_name + '\'' +
                ", dist_id='" + dist_id + '\'' +
                ", dist_address='" + dist_address + '\'' +
                ", last_visit='" + last_visit + '\'' +
                ", shop_image='" + shop_image + '\'' +
                ", mobile_no='" + mobile_no + '\'' +
                ", imageSaved='" + imageSaved + '\'' +
                ", remark='" + remark + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", shop_id='" + shop_id + '\'' +
                '}';
    }

}

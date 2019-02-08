package com.stk.orderingapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shashi on 03/05/2018.
 */

public class NotificationMessage extends DefaultResponse {

    @SerializedName("notification_title")
    @Expose
    private String  notificationTitle;

    @SerializedName("notifiaction_message")
    @Expose
    private String notificationMessage;

    @SerializedName("expire_date")
    @Expose
    private String expireDate;

    @SerializedName("rec_date")
    @Expose
    private String rec_date;

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getRec_date() {
        return rec_date;
    }

    public void setRec_date(String rec_date) {
        this.rec_date = rec_date;
    }

    @Override
    public String toString() {
        return "NotificationMessage{" +
                "notificationTitle='" + notificationTitle + '\'' +
                ", notificationMessage='" + notificationMessage + '\'' +
                ", expireDate='" + expireDate + '\'' +
                ", rec_date='" + rec_date + '\'' +
                '}';
    }
}

package com.stk.orderingapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Shashi on 03/05/2018.
 */

public class NotificationResponse extends DefaultResponse {

    @SerializedName("notification_flag")
    @Expose
    private String notificationFlag;


    @SerializedName("notification_message")
    @Expose
    private ArrayList<NotificationMessage> notificationMessage  = new ArrayList<>();;


    public String getNotificationFlag() {
        return notificationFlag;
    }

    public void setNotificationFlag(String notificationFlag) {
        this.notificationFlag = notificationFlag;
    }

    public ArrayList<NotificationMessage> getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(ArrayList<NotificationMessage> notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    @Override
    public String toString() {
        return "NotificationResponse{" +
                "notificationFlag='" + notificationFlag + '\'' +
                ", notificationMessage=" + notificationMessage +
                '}';
    }
}

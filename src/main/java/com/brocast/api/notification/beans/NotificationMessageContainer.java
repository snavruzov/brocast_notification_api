package com.brocast.api.notification.beans;

import com.dgtz.mcache.api.utils.GsonInsta;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by sardor on 4/7/16.
 */
public class NotificationMessageContainer implements Serializable{
    private static final long serialVersionUID = 1L;


    private Notificator notificator;
    private String deviceId;

    public NotificationMessageContainer() {
    }

    public NotificationMessageContainer(Notificator notificator, String deviceId) {
        this.notificator = notificator;
        this.deviceId = deviceId;
    }

    public Notificator getNotificator() {
        return notificator;
    }

    public void setNotificator(Notificator notificator) {
        this.notificator = notificator;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        Gson gson = GsonInsta.getInstance();
        return gson.toJson(this);

    }
}

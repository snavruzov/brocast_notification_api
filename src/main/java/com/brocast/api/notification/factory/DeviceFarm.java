package com.brocast.api.notification.factory;

import com.brocast.api.notification.beans.NotificationMessageContainer;
import com.brocast.api.notification.constants.Notification;
import com.brocast.api.notification.enums.EnumErrors;
import com.dgtz.mcache.api.factory.Constants;
import com.dgtz.mcache.api.factory.RMemoryAPI;

/**
 * BroCast.
 * Copyright: Sardor Navruzov
 * 2013-2016.
 */
public class DeviceFarm implements Notification {

    private String token;

    public DeviceFarm() {
    }

    public DeviceFarm(String token){
        this.token = token;
    }

    @Override
    public EnumErrors pushNotification() {
        return null;
    }

    @Override
    public EnumErrors pushLiveNotification() {
        return null;
    }

    @Override
    public EnumErrors pushDelayNotification() {
        return null;
    }

    @Override
    public void removeNotificationDevice() {
        String endpointArn = RMemoryAPI.getInstance().pullElemFromMemory(Constants.DEVICE_KEY + "arn:" + token);
        if(endpointArn!=null) {
            AmazonSNSSender.getInstance().removeDevice(endpointArn);
            RMemoryAPI.getInstance().delFromMemory(Constants.DEVICE_KEY + "arn:"+token);
        }
    }

    @Override
    public EnumErrors htmlNotification() {
        return null;
    }

    @Override
    public EnumErrors htmlDelayNotification() {
        return null;
    }
}

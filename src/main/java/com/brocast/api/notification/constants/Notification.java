package com.brocast.api.notification.constants;


import com.brocast.api.notification.enums.EnumErrors;

/**
 * Created by Sardor Navruzov on 7/23/15.
 * Copyrights BroCast Co.
 */
public interface Notification {

    EnumErrors pushNotification();

    EnumErrors pushLiveNotification();

    EnumErrors pushDelayNotification();

    void removeNotificationDevice();

    EnumErrors htmlNotification();

    EnumErrors htmlDelayNotification();
}

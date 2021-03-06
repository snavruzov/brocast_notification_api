package com.brocast.api.notification.factory;

import com.brocast.api.notification.beans.NotificationMessageContainer;
import com.brocast.api.notification.beans.Notificator;
import com.brocast.api.notification.constants.Notification;
import com.brocast.api.notification.enums.EnumErrors;
import com.dgtz.mcache.api.factory.Constants;
import com.dgtz.mcache.api.factory.RMemoryAPI;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Created by Sardor Navruzov on 7/23/15.
 * Copyrights BroCast Co.
 */
public class AndroidNotification implements Notification {

    private String key;
    private static final int MULTICAST_SIZE = 1000;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(AndroidNotification.class);


    public AndroidNotification() {

    }

    public AndroidNotification(String key) {
        this.key = key;
    }

    @Override
    public EnumErrors pushDelayNotification() {
        return EnumErrors.NO_ERRORS;
    }

    @Override
    public void removeNotificationDevice() {

    }

    @Override
    public EnumErrors pushNotification() {

        Set<String> tokens = RMemoryAPI.getInstance()
                .pullSetElemFromMemory(Constants.NOTIFICATION_KEY + "gcm:device:" + key);

        try {

            Notificator notif = (Notificator) RMemoryAPI.getInstance().pullElemFromMemory(key, Notificator.class);

            log.debug("NOTIFICATION: {}", notif);
            if (tokens == null || tokens.isEmpty()) {
                log.debug("Message ignored as there is no device registered or off notice!");
            } else {
                log.debug("NOTIFICATION GCM DEVICES: {}:::: KEY - {}", tokens.toString(), key);
                Gson gson = new Gson();
                Set<NotificationMessageContainer> devices = gson.fromJson(tokens.toString(),
                        new TypeToken<Set<NotificationMessageContainer>>() {
                        }.getType());

                devices.forEach(container -> AmazonSNSSender.getInstance().sendGCM(container));

                RMemoryAPI.getInstance()
                        .delFromMemory(Constants.NOTIFICATION_KEY + notif.getIdMedia()
                                + ":" + notif.getToId());
            }
        } catch (Exception e) {
            log.error("GCM Error ", e);
        }

        return EnumErrors.NO_ERRORS;
    }

    @Override
    public EnumErrors pushLiveNotification() {
        return null;
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

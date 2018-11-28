package com.brocast.api.notification.factory;

import com.brocast.api.notification.constants.Notification;
import com.brocast.api.notification.enums.EnumErrors;
import com.dgtz.mcache.api.factory.Constants;
import com.dgtz.mcache.api.factory.RMemoryAPI;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Set;

/**
 * BroCast.
 * Copyright: Sardor Navruzov
 * 2013-2016.
 */
public class LiveNotification implements Notification {

    private String key;
    private RabbitTemplate template;

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(LiveNotification.class);


    public LiveNotification() {

    }

    public LiveNotification(String key, RabbitTemplate template) {
        this.key = key;
        this.template = template;
    }

    @Override
    public EnumErrors pushNotification() {
        return null;
    }

    @Override
    public EnumErrors pushLiveNotification() {
        String idUserFrom = RMemoryAPI.getInstance().pullHashFromMemory(Constants.NOTIFICATION_KEY + key, "idfrom");

        try {
            if (idUserFrom == null) {
                log.info("Message ignored as there is no users registered or off notice!");
            } else {
                Set<String> followers = RMemoryAPI.getInstance().pullSetElemFromMemory(Constants.FOLLOWERS+idUserFrom);
                if(followers!=null && !followers.isEmpty()){
                    followers.forEach(idto -> template.convertAndSend("liveLNQ", key+"≈"+idto));
                }else {
                    log.info("Message ignored no followers! {}", idUserFrom);
                }

            }
        } catch (Exception e) {
            log.error("GCM Error ", e);
        }

        return EnumErrors.NO_ERRORS;
    }

    @Override
    public EnumErrors pushDelayNotification() {
        return null;
    }

    @Override
    public void removeNotificationDevice() {
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

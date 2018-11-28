package com.brocast.api.notification.factory;

import com.brocast.api.notification.constants.Notification;
import com.brocast.api.notification.enums.EnumErrors;
import com.dgtz.mcache.api.factory.Constants;
import com.dgtz.mcache.api.factory.RMemoryAPI;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;


/**
 * Created by Sardor Navruzov on 7/23/15.
 * Copyrights BroCast Co.
 */
public class EmailNotification implements Notification {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(EmailNotification.class);

    private String key;
    private RabbitTemplate template;

    public EmailNotification() {
    }

    public EmailNotification(String key, RabbitTemplate template) {
        this.key = key;
        this.template = template;
    }

    @Override
    public EnumErrors htmlNotification() {
        return null;
    }

    @Override
    public EnumErrors pushDelayNotification() {
        return null;
    }

    @Override
    public void removeNotificationDevice() {

    }

    @Override
    public EnumErrors pushNotification() {
        String idUserFrom = RMemoryAPI.getInstance().pullHashFromMemory(Constants.NOTIFICATION_KEY + key, "idfrom");

        try {
            if (idUserFrom == null) {
                log.info("Message ignored as there is no users registered or off notice!");
            } else {

                String idto = RMemoryAPI.getInstance().pullHashFromMemory(Constants.NOTIFICATION_KEY + key, "idto");
                template.convertAndSend("liveUMAILNQ", key + "≈" + idto);
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
    public EnumErrors htmlDelayNotification() {
        return null;
    }

}

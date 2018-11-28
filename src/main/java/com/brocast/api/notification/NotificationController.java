package com.brocast.api.notification;

import com.brocast.api.notification.beans.BasicResponse;
import com.brocast.api.notification.constants.Notification;
import com.brocast.api.notification.enums.DeliverMethod;
import com.brocast.api.notification.enums.EnumErrors;
import com.brocast.api.notification.factory.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BroCast.
 * Copyright: Sardor Navruzov
 * 2013-2016.
 */
@RestController
public class NotificationController {
    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private RabbitTemplate template;

    @RequestMapping(value = "/private/pass/restore", method = RequestMethod.GET)
    public BasicResponse restorePasswordSend() {
        return new BasicResponse();
    }

    @RequestMapping(value = "/private/account/confirm", method = RequestMethod.GET)
    public BasicResponse accountConfirmSend() {
        return new BasicResponse();
    }

    @RequestMapping(value = "/private/send/notice", method = RequestMethod.GET)
    public BasicResponse sendNotificationByEmail() {
        return new BasicResponse();
    }

    @RequestMapping(value = "/private/push/notice", method = RequestMethod.GET)
    public void sendNotification(@RequestParam("key") String key) {
        log.info("Notification variable: {}", key);
        try {
            Notification notifier = new CommonNotification(key, template);
            EnumErrors errors = notifier.pushNotification();
            log.info("LIVE NOTIFICATION key {}, {}", key, errors.name());

        } catch (Exception e) {
            log.error("ERROR IN BOOT API ", e);
        }
    }

    @RequestMapping(value = "/private/email/notice", method = RequestMethod.GET)
    public void sendEmailNotification(@RequestParam("key") String key) {
        log.info("Notification variable: {}", key);
        try {
            Notification notifier = new EmailNotification(key, template);
            EnumErrors errors = notifier.pushNotification();
            log.info("EMAIL NOTIFICATION KEY {}, {}", key, errors.name());

        } catch (Exception e) {
            log.error("ERROR IN BOOT API ", e);
        }
    }

    @RequestMapping(value = "/private/del/deviceid", method = RequestMethod.GET)
    public void removeSNSDevice(@RequestParam("key") String key) {
        log.info("Endpoint Token variable: {}", key);
        try {
            Notification notifier = new DeviceFarm(key);
            notifier.removeNotificationDevice();
        } catch (Exception e) {
            log.error("ERROR IN BOOT API ", e);
        }
    }

}

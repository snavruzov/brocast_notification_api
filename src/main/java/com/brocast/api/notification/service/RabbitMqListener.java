package com.brocast.api.notification.service;

import com.brocast.api.notification.beans.NotificationMessageContainer;
import com.brocast.api.notification.beans.Notificator;
import com.brocast.api.notification.factory.AmazonSNSSender;
import com.brocast.api.notification.tools.HTMLReader;
import com.brocast.riak.api.beans.DcRoomEntity;
import com.brocast.riak.api.beans.DcUserNotificationSettings;
import com.brocast.riak.api.dao.RiakAPI;
import com.brocast.riak.api.dao.RiakTP;
import com.brocast.riak.api.factory.IRiakQueryFactory;
import com.brocast.riak.api.factory.RiakQueryFactory;
import com.dgtz.api.contents.UsersShelf;
import com.dgtz.api.feature.SystemDelivery;
import com.dgtz.api.settings.ISystemDelivery;
import com.dgtz.db.api.beans.DcDeviceType;
import com.dgtz.db.api.domain.Notification;
import com.dgtz.db.api.enums.EnumNotification;
import com.dgtz.db.api.enums.EnumOperationSystem;
import com.dgtz.mcache.api.factory.Constants;
import com.dgtz.mcache.api.factory.RMemoryAPI;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * BroCast.
 * Copyright: Sardor Navruzov
 * 2013-2016.
 */

@Component
public class RabbitMqListener {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RabbitMqListener.class);

    @RabbitListener(queues = "liveLNQ")
    public void processQueue1(String message) throws Exception {
        logger.info("Received from live notification queue 1: " + message);
        try {
            sendToMultiNPProc(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "liveLNQ")
    public void processQueue2(String message) throws Exception {
        logger.info("Received from live notification queue 2: " + message);
        try {
            sendToMultiNPProc(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "liveLNQ")
    public void processQueue3(String message) throws Exception {
        logger.info("Received from live notification queue 3: " + message);
        try {
            sendToMultiNPProc(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*New content Notification*/
    @RabbitListener(queues = "liveMNQ")
    public void processQueueMedia1(String message) throws Exception {
        logger.info("Received from media notification queue 1: " + message);
        try {
            sendToUnoNPProc(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "liveMNQ")
    public void processQueueMedia2(String message) throws Exception {
        logger.info("Received from media notification queue 2: " + message);
        try {
            sendToUnoNPProc(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "liveMNQ")
    public void processQueueMedia3(String message) throws Exception {
        logger.info("Received from media notification queue 3: " + message);
        try {
            sendToUnoNPProc(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Email notification*/
    @RabbitListener(queues = "liveMMAILNQ")
    public void processQueueMailMulti(String message) throws Exception {
        logger.info("Received from mail notification queue 3: " + message);
        try {
            sendToMultiMailPProc(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Email notification*/
    @RabbitListener(queues = "liveUMAILNQ")
    public void processQueueMailUno(String message) throws Exception {
        logger.info("Received from mail notification queue 3: " + message);
        try {
            sendToUnoMailPProc(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendToMultiNPProc(String message) throws Exception {

        String[] dt = message.split("≈");
        String idUserTo = dt[1];
        String key = dt[0];
        logger.info("Message info {}", message);
        RiakTP transport = RiakAPI.getInstance();
        IRiakQueryFactory queryFactory = new RiakQueryFactory(transport);
        DcUserNotificationSettings usettings =
                queryFactory.queryUserNotificationSettings(Long.valueOf(idUserTo), "push");

        if (usettings != null && usettings.follower) {

            String cod = RMemoryAPI.getInstance().pullHashFromMemory(Constants.USER_KEY + idUserTo, "language");
            cod = (cod == null || cod.isEmpty()) ? "en" : cod;

            Notificator note = setNotificatorValues(key);
            note.setToId(idUserTo);

            /*String val_note = RMemoryAPI.getInstance()
                    .pullHashFromMemory(Constants.TRANSLATION + "apn:notification:" + note.type, "language" + ":" + cod);*/


            switch (note.type) {
                case "2": {
                    note.title = note.username + " uploaded a new video " + note.title + ", check it out!";
                    break;
                }
                case "19": {
                    note.title = note.username + " created a new event " + note.title;
                    break;
                }
                case "6": {
                    note.title = note.username + " started a new LIVE stream " + note.title;
                    break;
                }
                case "18": {
                    note.title = note.username + " published a new post to the channel – " + note.title;
                    break;
                }

            }

            sendPushAWSNotification(note, idUserTo);
        }
    }

    private void sendToUnoNPProc(String message) throws Exception {

        String[] dt = message.split("≈");
        String idUserTo = dt[1];
        String key = dt[0];
        boolean sendPush = true;
        logger.info("Message info {}", message);
        RiakTP transport = RiakAPI.getInstance();
        IRiakQueryFactory queryFactory = new RiakQueryFactory(transport);
        DcUserNotificationSettings usettings =
                queryFactory.queryUserNotificationSettings(Long.valueOf(idUserTo), "push");

        if (usettings != null) {

            String cod = RMemoryAPI.getInstance().pullHashFromMemory(Constants.USER_KEY + idUserTo, "language");
            cod = (cod == null || cod.isEmpty()) ? "en" : cod;

            Notificator note = setNotificatorValues(key);
            note.setToId(idUserTo);

            /*String val_note = RMemoryAPI.getInstance()
                    .pullHashFromMemory(Constants.TRANSLATION + "apn:notification:" + note.type, "language" + ":" + cod);*/

            switch (note.type) {
                case "0": {
                    note.title = note.username + " commented your video – " + note.title + " at " + note.duration + ".";
                    sendPush = usettings.comment;
                    break;
                }
                case "20": {
                    note.title = " is now following you!";
                    sendPush = usettings.follower;
                    break;
                }

                case "1": {
                    note.title = note.username + " asking for permission to publish posts into your channel–" + note.title;
                    break;
                }
                case "3": {
                    note.title = note.username + " invites you to follow the channel – " + note.title;
                    break;
                }
                case "21": {
                    note.title = note.username + " subscribed to your channel – " + note.title;
                    break;
                }
                case "8": {
                    note.title = note.username + " invites you to the LIVE debate mode – " + note.title;
                    break;
                }
                case "24": {
                    sendPush = usettings.inbox;
                    if (sendPush) {
                        DcRoomEntity roomEntity = queryFactory.queryRoomByID(note.id_room);
                        note.msg_count = roomEntity.msg_count.intValue();
                    }
                    break;
                }
            }

            if (sendPush) {
                sendPushAWSNotification(note, idUserTo);
            }
        }
    }

    private void sendToMultiMailPProc(String message) throws Exception {

        String[] dt = message.split("≈");
        String idUserTo = dt[1];
        String key = dt[0];
        logger.info("Message info {}", message);
        RiakTP transport = RiakAPI.getInstance();
        IRiakQueryFactory queryFactory = new RiakQueryFactory(transport);
        DcUserNotificationSettings usettings =
                queryFactory.queryUserNotificationSettings(Long.valueOf(idUserTo), "email");

        if (usettings != null && usettings.follower) {

            String cod = RMemoryAPI.getInstance().pullHashFromMemory(Constants.USER_KEY + idUserTo, "language");
            cod = (cod == null || cod.isEmpty()) ? "en" : cod;

            Notificator note = setNotificatorValues(key);
            note.setToId(idUserTo);

            /*String val_note = RMemoryAPI.getInstance()
                    .pullHashFromMemory(Constants.TRANSLATION + "apn:notification:" + note.type, "language" + ":" + cod);*/


            switch (note.type) {
                case "2": {
                    note.title = note.username + " uploaded a new video " + note.title + ", check it out!";
                    break;
                }
                case "19": {
                    Notification notification = new Notification();
                    notification.setType(EnumNotification.SCHEDULED_EVENT.value);
                    notification.setIdFrom(Long.valueOf(note.fromId));
                    notification.setIdUser(Long.valueOf(idUserTo));
                    notification.setIdMedia(Long.valueOf(note.idMedia));
                    notification.setText(note.title);
                    notification.setUsername(note.username);
                    ISystemDelivery systemDelivery = SystemDelivery
                            .builder(notification).system();
                    note.title = note.username + " created a new event " + note.title;
                    break;
                }
                case "6": {
                    note.title = note.username + " started a new LIVE stream " + note.title;
                    Notification notification = new Notification();
                    notification.setType(EnumNotification.LIVE_STARTED.value);
                    notification.setIdFrom(Long.valueOf(note.fromId));
                    notification.setIdUser(Long.valueOf(idUserTo));
                    notification.setIdMedia(Long.valueOf(note.idMedia));
                    notification.setText(note.title);
                    notification.setUsername(note.username);
                    ISystemDelivery systemDelivery = SystemDelivery.builder(notification).system();
                    break;
                }
                case "18": {
                    note.title = note.username + " published a new post to the channel – " + note.title;
                    break;
                }
                case "23": {
                    note.title = note.username + " started an event – " + note.title;
                    break;
                }

            }
            sendPushAWSNotification(note, idUserTo);
        }
    }

    private void sendToUnoMailPProc(String message) throws Exception {

        String[] dt = message.split("≈");
        String idUserTo = dt[1];
        String key = dt[0];
        logger.info("Mail Message info {}", message);
//        RiakTP transport = RiakAPI.getInstance();
//        IRiakQueryFactory queryFactory = new RiakQueryFactory(transport);
//        DcUserNotificationSettings usettings =
//                queryFactory.queryUserNotificationSettings(Long.valueOf(idUserTo), "email");


        Notificator note = setNotificatorValues(key);
        if (note.email != null && !note.email.isEmpty() && !note.email.contains("brocast.com")) {

            String[] body = new HTMLReader().htmlAccount(note);

            MailNotifier.sendMailBox(body[0], body[1], note.email);
        }

    }

    private Notificator setNotificatorValues(String key) {
        Notificator note = new Notificator();
        note.type = RMemoryAPI.getInstance().pullHashFromMemory(Constants.NOTIFICATION_KEY + key, "type");
        note.fromId = RMemoryAPI.getInstance().pullHashFromMemory(Constants.NOTIFICATION_KEY + key, "idfrom");
        note.title = RMemoryAPI.getInstance().pullHashFromMemory(Constants.NOTIFICATION_KEY + key, "title");
        note.avatar = RMemoryAPI.getInstance().pullHashFromMemory(Constants.NOTIFICATION_KEY + key, "avatar");
        note.idChannel = RMemoryAPI.getInstance().pullHashFromMemory(Constants.NOTIFICATION_KEY + key, "idChannel");
        note.idMedia = RMemoryAPI.getInstance().pullHashFromMemory(Constants.NOTIFICATION_KEY + key, "idMedia");
        note.username = RMemoryAPI.getInstance().pullHashFromMemory(Constants.NOTIFICATION_KEY + key, "username");
        note.activation = RMemoryAPI.getInstance().pullHashFromMemory(Constants.NOTIFICATION_KEY + key, "activation");
        note.email = RMemoryAPI.getInstance().pullHashFromMemory(Constants.NOTIFICATION_KEY + key, "email");
        note.id_room = RMemoryAPI.getInstance().pullHashFromMemory(Constants.NOTIFICATION_KEY + key, "id_room");

        return note;
    }

    private void sendPushAWSNotification(Notificator note, String idUserTo) {
        Set<String> dvsIOS = RMemoryAPI.getInstance().pullSetElemFromMemory(Constants.USER_KEY + "device:ios:" + idUserTo);
        for (String idDev : dvsIOS) {
            String dev = RMemoryAPI.getInstance().pullElemFromMemory(Constants.DEVICE_KEY + idDev);
            if (dev != null) {
                AmazonSNSSender.getInstance().sendAPN(new NotificationMessageContainer(note, idDev));
            } else {
                RMemoryAPI.getInstance().delFromSetElem(Constants.USER_KEY + "device:ios:" + idUserTo, idDev);
            }
        }

        Set<String> dvsANDROID = RMemoryAPI.getInstance().pullSetElemFromMemory(Constants.USER_KEY + "device:android:" + idUserTo);
        for (String idDev : dvsANDROID) {
            String dev = RMemoryAPI.getInstance().pullElemFromMemory(Constants.DEVICE_KEY + idDev);
            if (dev != null) {
                AmazonSNSSender.getInstance().sendGCM(new NotificationMessageContainer(note, idDev));
            } else {
                RMemoryAPI.getInstance().delFromSetElem(Constants.USER_KEY + "device:android:" + idUserTo, idDev);
            }
        }
    }
}

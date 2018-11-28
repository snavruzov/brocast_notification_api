package com.brocast.api.notification.tools;

/*
 * Copyright 2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

import com.brocast.api.notification.beans.NotificationMessageContainer;
import com.brocast.api.notification.beans.Notificator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;


public class MobilePushMessageGenerator {

    /*
     * This message is delivered if a platform specific message is not specified
     * for the end point. It must be set. It is received by the device as the
     * value of the key "default".
     */

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public enum Platform {
        APNS,
        GCM
    }

    public static String jsonify(Object message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw (RuntimeException) e;
        }
    }

    private static Map<String, String> getAndroidData(NotificationMessageContainer container) {
        Map<String, String> payload = new HashMap<String, String>();
        Notificator notif = container.getNotificator();

        payload.put("type", notif.getType()+"");
        payload.put("id_user", notif.getFromId()+"");
        payload.put("id_channel", notif.getIdChannel()+"");
        payload.put("id_media", notif.getIdMedia()+"");
        payload.put("username", notif.getUsername());
        payload.put("title", notif.getTitle());
        payload.put("avatar", notif.getAvatar());
        payload.put("id_room", notif.getId_room());
        payload.put("msg_count", notif.getMsg_count()+"");
        return payload;
    }

    public static String getAppleMessage(NotificationMessageContainer container) {

        Notificator notif = container.getNotificator();

        Map<String, Object> appleMessageMap = new HashMap<String, Object>();
        Map<String, Object> appMessageMap = new HashMap<String, Object>();

        appMessageMap.put("alert", notif.getTitle());
        appMessageMap.put("badge", 1);
        appMessageMap.put("sound", "bingbong.aiff");
        appMessageMap.put("type", notif.getType());
        appMessageMap.put("username", notif.getUsername());
        appMessageMap.put("id_media", notif.getIdMedia());
        appMessageMap.put("id_channel", notif.getIdChannel());
        appMessageMap.put("id_user", notif.getFromId());
        appMessageMap.put("avatar", notif.getAvatar());
        appMessageMap.put("id_room", notif.getId_room());
        appMessageMap.put("msg_count", notif.getMsg_count()+"");

        appleMessageMap.put("aps", appMessageMap);
        return jsonify(appleMessageMap);
    }

    public static String getAndroidMessage(NotificationMessageContainer container) {
        Map<String, Object> androidMessageMap = new HashMap<String, Object>();

        androidMessageMap.put("collapse_key", "BroCast");
        androidMessageMap.put("data", getAndroidData(container));
        androidMessageMap.put("delay_while_idle", true);
        androidMessageMap.put("time_to_live", 60);
        androidMessageMap.put("dry_run", false);

        androidMessageMap.put("priority", "high");
        return jsonify(androidMessageMap);
    }

}
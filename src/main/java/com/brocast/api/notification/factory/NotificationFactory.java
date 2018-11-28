package com.brocast.api.notification.factory;


import com.brocast.api.notification.constants.Notification;
import com.brocast.api.notification.enums.DeliverMethod;
import com.dgtz.db.api.enums.EnumNotification;

/**
 * Created by Sardor Navruzov on 7/23/15.
 * Copyrights BroCast Co.
 */
final public class NotificationFactory {

    public static synchronized void buildAndSend(Notification notify, int noticeType, DeliverMethod method) {
        switch (method) {
            case APPLE:
            case ANDROID:
            case EMAIL:
                if (noticeType == EnumNotification.COMMENTED.value || noticeType == EnumNotification.LIKED.value) {
                    DelayedScheduler.queueJobRun(notify);
                } else if (noticeType == EnumNotification.LIVE_STARTED.value) {
                    InstantScheduler.queueJobRun(notify);
                } else {
                    InstantScheduler.queueJobRun(notify);
                }
                break;
        }

    }

}

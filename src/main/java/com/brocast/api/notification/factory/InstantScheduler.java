package com.brocast.api.notification.factory;


import com.brocast.api.notification.constants.Notification;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sardor Navruzov on 8/8/15.
 * Copyrights BroCast Co.
 */
public abstract class InstantScheduler {

    protected static void queueJobRun(Notification notif) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
        Runnable task = notif::pushNotification;

        executor.schedule(task, 5, TimeUnit.SECONDS);
        executor.shutdown();
    }

    protected static void rabbitMQRun(Notification notif) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
        Runnable task = notif::pushNotification;

        executor.schedule(task, 5, TimeUnit.SECONDS);
        executor.shutdown();
    }
}

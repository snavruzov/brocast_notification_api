package com.brocast.api.notification.factory;

import com.brocast.api.notification.constants.Notification;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sardor Navruzov on 8/8/15.
 * Copyrights BroCast Co.
 */
public abstract class DelayedScheduler {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(DelayedScheduler.class);

    protected static void queueJobRun(Notification notif) {
        log.debug("DELAYED SCHEDULER: 2 MINUTE");
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
        Runnable task = notif::pushNotification;

        executor.schedule(task, 2, TimeUnit.MINUTES);
        executor.shutdown();
    }
}

package com.brocast.api.notification.factory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.brocast.api.notification.service.AmazonSNSClientWrapper;
import org.slf4j.LoggerFactory;

/**
 * Created by sardor on 4/7/16.
 */
public final class AmazonSNSSender {
    private static AmazonSNSClientWrapper snsClientWrapper = null;
    private final static AmazonSNS sns;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(AmazonSNSSender.class);


    static {
        AWSCredentials credentials = new BasicAWSCredentials("XXXXXXXXXXXXXXx", "XXXXXXXXXXXXXXXXXXXXXXXX");
        sns = new AmazonSNSClient(credentials);
        sns.setEndpoint("https://sns.eu-central-1.amazonaws.com");

        log.info("===========================================\n");
        log.info("Initializing Amazon SNS");
        log.info("===========================================\n");

    }
    public AmazonSNSSender(){}

    public static AmazonSNSClientWrapper getInstance(){
        if(snsClientWrapper==null)
                snsClientWrapper = new AmazonSNSClientWrapper(sns);

        return snsClientWrapper;
    }
}

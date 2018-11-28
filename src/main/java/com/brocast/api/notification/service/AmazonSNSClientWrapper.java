package com.brocast.api.notification.service;

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

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.*;
import com.brocast.api.notification.beans.NotificationMessageContainer;
import com.brocast.api.notification.tools.MobilePushMessageGenerator;
import com.brocast.api.notification.tools.MobilePushMessageGenerator.Platform;
import com.dgtz.mcache.api.factory.Constants;
import com.dgtz.mcache.api.factory.RMemoryAPI;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmazonSNSClientWrapper {

    private final AmazonSNS snsClient;
    private String endpointArn = "";

    public AmazonSNSClientWrapper(AmazonSNS client) {
        this.snsClient = client;
    }
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(AmazonSNSClientWrapper.class);

    public void removeDevice(String endpointArn){
        snsClient.deleteEndpoint(deleteEndpointRequest(endpointArn));
    }

    public void sendAPN(NotificationMessageContainer container) {
        String endpointArn = prepareEndpointArn(Platform.APNS, container);
        appNotification(Platform.APNS, container, endpointArn);
    }
    public void sendGCM(NotificationMessageContainer container) {
        String endpointArn = prepareEndpointArn(Platform.GCM, container);
        appNotification(Platform.GCM, container, endpointArn);
    }

    private String createPlatformEndpoint(String customData, String platformToken,
            String applicationArn) {
        CreatePlatformEndpointRequest platformEndpointRequest = new CreatePlatformEndpointRequest();
        platformEndpointRequest.setCustomUserData(customData);

        platformEndpointRequest.setToken(platformToken);
        platformEndpointRequest.setPlatformApplicationArn(applicationArn);
        try {
            this.endpointArn = snsClient
                    .createPlatformEndpoint(platformEndpointRequest)
                    .getEndpointArn();
        } catch (InvalidParameterException e) {
            log.error("WARNING TOKEN EXISTS, TRYING TO UPGRADE ", e);
            Pattern p = Pattern
                    .compile(".*Endpoint (arn:aws:sns[^ ]+) already exists " +
                            "with the same Token.*");
            Matcher m = p.matcher(e.getErrorMessage());
            if (m.matches()) {
                snsClient.deleteEndpoint(deleteEndpointRequest(m.group(1)));
                this.endpointArn = snsClient
                        .createPlatformEndpoint(platformEndpointRequest)
                        .getEndpointArn();
            } else {
                throw e;
            }
        }
        return this.endpointArn;
    }

    private PublishResult publish(String endpointArn, Platform platform, NotificationMessageContainer container) {
        PublishRequest publishRequest = new PublishRequest();

        publishRequest.setMessageStructure("json");
        String message = getPlatformMessage(platform, container);

        Map<String, String> messageMap = new HashMap<String, String>();
        messageMap.put(platform.name(), message);
        message = MobilePushMessageGenerator.jsonify(messageMap);
        // For direct publish to mobile end points, topicArn is not relevant.
        publishRequest.setTargetArn(endpointArn);

        // Display the message that will be sent to the endpoint/
        log.info("{Message Body: " + message + "}");

        publishRequest.setMessage(message);
        return snsClient.publish(publishRequest);
    }

    private void appNotification(Platform platform, NotificationMessageContainer container, String endpointArn) {
        try {
            // Publish a push notification to an Endpoint.
            PublishResult publishResult = publish(endpointArn, platform, container);
            log.info("Published to {} deviceID: {} for platform! \n{Platform="
                    + platform.name() + "}", container.getNotificator().getToId(), container.getDeviceId());
        } catch (AmazonServiceException ase) {
            log.info("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon SNS, but was rejected with an error response for some reason.");
            log.info("Error Message:    " + ase.getMessage());
            log.info("HTTP Status Code: " + ase.getStatusCode());
            log.info("AWS Error Code:   " + ase.getErrorCode());
            log.info("Error Type:       " + ase.getErrorType());
            log.info("Request ID:       " + ase.getRequestId());
            if(ase.getErrorCode().equals("EndpointDisabled")){
                RMemoryAPI.getInstance()
                        .delFromSetElem(Constants.USER_KEY + "device:ios:" + container.getNotificator().getToId(), container.getDeviceId());
                RMemoryAPI.getInstance()
                        .delFromSetElem(Constants.USER_KEY + "device:android:" + container.getNotificator().getToId(), container.getDeviceId());
                snsClient.deleteEndpoint(deleteEndpointRequest(endpointArn));
            }
        } catch (AmazonClientException ace) {
            log.info("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with SNS, such as not "
                    + "being able to access the network.");
            log.info("Error Message: " + ace.getMessage());
        }
    }

    private String prepareEndpointArn(Platform platform, NotificationMessageContainer container){
        String platformApplicationArn = "arn:aws:sns:eu-central-1:812805977885:app/GCM/gcmbrocast";
        if(platform==Platform.APNS){
            platformApplicationArn = "arn:aws:sns:eu-central-1:812805977885:app/APNS/apnbrocast";
        }

        // Create an Endpoint. This corresponds to an app on a device.
        String endpointArn = createPlatformEndpoint(
                "ID_USER - "+container.getNotificator().getToId(), container.getDeviceId(), platformApplicationArn);
        log.info(endpointArn);

        RMemoryAPI.getInstance().pushElemToMemory(Constants.DEVICE_KEY + "arn:"+container.getDeviceId(), -1, endpointArn);
        return endpointArn;

    }

    private String getPlatformMessage(Platform platform, NotificationMessageContainer container) {
        switch (platform) {
            case APNS:
                return MobilePushMessageGenerator.getAppleMessage(container);
            case GCM:
                return MobilePushMessageGenerator.getAndroidMessage(container);
            default:
                throw new IllegalArgumentException("Platform not supported : "
                        + platform.name());
        }
    }

    private DeleteEndpointRequest deleteEndpointRequest(String endpointArn){
        return new DeleteEndpointRequest().withEndpointArn(endpointArn);
    }
}

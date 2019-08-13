package com.witcurve.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.simpleworkflow.flow.annotations.Asynchronous;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.*;
import com.witcurve.config.ApplicationProperties;
import com.witcurve.config.Constants;
import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.TopicType;
import com.witcurve.repository.StudentStandardRepository;
import com.witcurve.repository.TopicRecordRepository;
import com.witcurve.repository.UserMobileEndPointRepository;
import com.witcurve.service.util.WitCurveConstants;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class SnsService {

    private static final Logger log = LoggerFactory.getLogger(SnsService.class);

    @Autowired
    AmazonSNS amazonSNS;

    @Autowired
    UserMobileEndPointRepository userMobileEndPointRepository;

    @Autowired
    ApplicationProperties applicationProperties;

    @Autowired
    TopicRecordRepository topicRecordRepository;

    @Autowired
    private StudentStandardRepository studentStandardRepository;

   @Autowired
   private MessageSource messageSource;



    public void sendMobilePushNotification() {

//        GetPlatformApplicationAttributesRequest getPlatformApplicationAttributesRequest = new GetPlatformApplicationAttributesRequest();
//        getPlatformApplicationAttributesRequest.setPlatformApplicationArn(applicationProperties.getAws().getSnsApplicationArn());
//        GetPlatformApplicationAttributesResult platformApplicationAttributesResult = amazonSNS.getPlatformApplicationAttributes(getPlatformApplicationAttributesRequest);
//
//        log.info("Platform application : {}", platformApplicationAttributesResult);
//        PublishRequest publishRequest = new PublishRequest();
//        publishRequest.setTargetArn("arn:aws:sns:us-east-1:373531857223:endpoint/GCM/WitcurveTestNotification/65ca0975-4e4f-3243-b4d9-1793d6e94d53");
//        publishRequest.setMessage("{\n" +
//            "  \"GCM\": \"{ \\\"data\\\": { \\\"message\\\": \\\" Api Sample message for Android endpoints\\\" } }\"\n" +
//            "}");
//        publishRequest.setMessageStructure("json");
//        PublishResult publishResult = amazonSNS.publish(publishRequest);
//
//        log.info("Publish Result : {}", publishResult);
        String token = "ePor1oIw2Ew:APA91bEqRxUQuyEeKaH_AyA6CMQV_BiIBfykB5_9buwCVtEY1RUJ4MLBqPEoqi78fPAua01OtrNJREvmgGPkATQx6mf-iQY-CX3IB2mzLM8ShdSZwpBp9ad67Wi7rdq6Zc5i8gJhv98K";
        String endPointArn = "arn:aws:sns:us-east-1:373531857223:endpoint/GCM/WitcurveTestNotification/5064d17c-44ba-3e4e-ad95-5fab7ada5719";
        deleteEndpoint(endPointArn);



    }

    public String createEndPointWithToken(String token) throws WitcurveException {
        log.info("Create Platform end point with token : {}", token);
        try {
            CreatePlatformEndpointRequest createPlatformEndpointRequest = new CreatePlatformEndpointRequest();
            createPlatformEndpointRequest.setPlatformApplicationArn(applicationProperties.getAws().getSnsApplicationArn());
            createPlatformEndpointRequest.setToken(token);
            CreatePlatformEndpointResult createPlatformEndpointResult = amazonSNS.createPlatformEndpoint(createPlatformEndpointRequest);
            log.info("Platform end point result : {}", createPlatformEndpointResult);
            return createPlatformEndpointResult.getEndpointArn();
        } catch (SdkClientException e) {
            log.info("AWS Exception : {}",e.getMessage());
            throw new WitcurveException("There was a problem while creating end point");
        }
    }

    public void deleteEndpoint(String endPointArn) throws WitcurveException {
        try {
            DeleteEndpointRequest deleteEndpointRequest = new DeleteEndpointRequest();
            deleteEndpointRequest.setEndpointArn(endPointArn);
            DeleteEndpointResult deleteEndpointResult = amazonSNS.deleteEndpoint(deleteEndpointRequest);
            log.info("delete result : {}", deleteEndpointResult);
        } catch (SdkClientException e) {
            log.info("AWS Exception : {}",e.getMessage());
            throw new WitcurveException("There was a problem while deleting end point");
        }

    }

    public String addSubscription(String topicEndPoint, String platformEndPoint)  throws WitcurveException {
        try {
            SubscribeRequest subscribeRequest = new SubscribeRequest(topicEndPoint, "application", platformEndPoint);
            SubscribeResult subscribeResult = amazonSNS.subscribe(subscribeRequest);
            return subscribeResult.getSubscriptionArn();
        }  catch (SdkClientException e) {
            log.info("AWS Exception : {}",e.getMessage());
            throw new WitcurveException("There was a problem while adding subscription");
        }
    }

    public void unSubscribe(String subscriptionArn)  throws WitcurveException {
        try {
            amazonSNS.unsubscribe(subscriptionArn);
        }  catch (SdkClientException e) {
            log.info("AWS Exception : {}",e.getMessage());
            throw new WitcurveException("There was a problem while un-subscribing");
        }
    }

    public String createTopic(String name) {
        try {
            CreateTopicRequest createTopicRequest = new CreateTopicRequest(name);
            CreateTopicResult createTopicResult = amazonSNS.createTopic(createTopicRequest);
            return createTopicResult.getTopicArn();
        } catch (SdkClientException e) {
            log.info("AWS Exception : {}",e.getMessage());
            throw new WitcurveException("There was a problem creating topic");
        }
    }

    public void publishMessage(String message, String url, String endPoint) {
        try {
            PublishRequest publishRequest = new PublishRequest();
            publishRequest.setTargetArn(endPoint);
            publishRequest.setMessage(getPublishMessage(message, url));
            publishRequest.setMessageStructure("json");
            amazonSNS.publish(publishRequest);
        } catch (SdkClientException e) {
            log.info("AWS Exception : {}",e.getMessage());
            throw new WitcurveException("There was a problem sending this message");
        }

    }

    public void publishBulkMessage(String message, String url, TopicType type, Long schoolInfoId) {
        String topicArn = null;
        if(type.equals(TopicType.GLOBAL)) {
            List<TopicRecord> topicRecords = topicRecordRepository.findByType(TopicType.GLOBAL);
            if(topicRecords.size() ==1) {
                topicArn = topicRecords.get(0).getTopicEndPoint();
            }
        } else {
            if(schoolInfoId == null) {
                throw new WitcurveException("School Info Id is required for publishing bulk message");
            }
            TopicRecord topicRecord = topicRecordRepository.findBySchoolInfoId(schoolInfoId);
            topicArn = topicRecord.getTopicEndPoint();
        }
        if(topicArn != null) {
            try {
                PublishRequest publishRequest = new PublishRequest();
                publishRequest.setTopicArn(topicArn);
                publishRequest.setMessage(getPublishMessage(message, url));
                publishRequest.setMessageStructure("json");
                amazonSNS.publish(publishRequest);
            } catch (SdkClientException e) {
                log.info("AWS Exception : {}",e.getMessage());
                throw new WitcurveException("There was a problem sending this message");
            }
        } else {
            throw new WitcurveException("There was a problem getting topic arn links");
        }

    }


    private String getPublishMessage(String message, String url) {
        if (url == null) {
            return "{ \n" +
                "\"default\": \"" + message + "\",\n" +
                "\"APNS\": \"{\\\"aps\\\":{\\\"alert\\\": \\\"" + message + "\\\"} }\",\n" +
                "\"GCM\":\"{\\\"data\\\":{\\\"message\\\":\\\"" + message + "\\\"} }\"\n" +
                "}  ";
        } else {
            return "{ \n" +
                "\"default\": \"" + message + "\",\n" +
                "\"APNS\": \"{\\\"aps\\\":{\\\"alert\\\": \\\"" + message + "\\\",\\\"url\\\":\\\"" + url + "\\\"} }\",\n" +
                "\"GCM\":\"{\\\"data\\\":{\\\"message\\\":\\\"" + message + "\\\",\\\"url\\\":\\\"" + url + "\\\"}}\"\n" +
                "}  ";
        }
    }


    public void sendPushNotification(List<Event> listOfEvent) {
        for (Event event : listOfEvent) {

            switch (event.getType()){

                case TEST: List<UserMobileEndPoint> listOfUserMobileEndPoints=userMobileEndPointRepository.findByStandardId(event.getStandard().getId());

                            if(listOfUserMobileEndPoints==null){
                                throw new WitcurveException("No User found related to thid standard Id");
                            }

                            String message= getMessage(event.getName());
                            System.out.println(message);
                            for(UserMobileEndPoint userMobileEndPoint:listOfUserMobileEndPoints ){
                                String url="?userId="+userMobileEndPoint.getUser().getId()+"&event=true&date="+event.getDate();
                                System.out.println(url);
                                publishMessage(message,url,userMobileEndPoint.getEndPoint());
                           }

            }


        }
    }

    private String getMessage(String subject){
        String message=WitCurveConstants.TEST_PUSH_NOTIFICATION;
        if(message.contains("{{subject}}")){
            return message.replace("{{subject}}",subject);
        }
        return message;
    }


}

package com.witcurve.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.*;
import com.witcurve.config.ApplicationProperties;
import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.AttendanceType;
import com.witcurve.domain.enumeration.ExamStatus;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.domain.enumeration.TopicType;
import com.witcurve.repository.*;
import com.witcurve.service.dto.*;
import com.witcurve.service.util.WitCurveConstants;
import com.witcurve.service.util.WitcurveUtil;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
    CourseRepository courseRepository;

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StandardRepository standardRepository;

    @Autowired
    CourseTeacherRepository courseTeacherRepository;

    @Autowired
    MessageThreadRepository messageThreadRepository;

    @Autowired
    EventRepository eventRepository;

    public String createEndPointWithToken(String token) throws WitcurveException {
        log.debug("Create Platform end point with token : {}", token);
        try {
            CreatePlatformEndpointRequest createPlatformEndpointRequest = new CreatePlatformEndpointRequest();
            createPlatformEndpointRequest.setPlatformApplicationArn(applicationProperties.getAws().getSnsApplicationArn());
            createPlatformEndpointRequest.setToken(token);
            CreatePlatformEndpointResult createPlatformEndpointResult = amazonSNS.createPlatformEndpoint(createPlatformEndpointRequest);
            log.info("Platform end point result : {}", createPlatformEndpointResult);
            return createPlatformEndpointResult.getEndpointArn();
        } catch (SdkClientException e) {
            log.debug("AWS Exception : {}", e.getMessage());
            throw new WitcurveException("There was a problem while deleting end point");
        }
    }

    public void deleteEndpoint(String endPointArn) throws WitcurveException {
        try {
            DeleteEndpointRequest deleteEndpointRequest = new DeleteEndpointRequest();
            deleteEndpointRequest.setEndpointArn(endPointArn);
            DeleteEndpointResult deleteEndpointResult = amazonSNS.deleteEndpoint(deleteEndpointRequest);
            log.info("delete result : {}", deleteEndpointResult);
        } catch (SdkClientException e) {
            log.debug("AWS Exception : {}", e.getMessage());
            throw new WitcurveException("There was a problem while deleting end point");
        }

    }

    public String addSubscription(String topicEndPoint, String platformEndPoint) throws WitcurveException {
        try {
            SubscribeRequest subscribeRequest = new SubscribeRequest(topicEndPoint, "application", platformEndPoint);
            SubscribeResult subscribeResult = amazonSNS.subscribe(subscribeRequest);
            return subscribeResult.getSubscriptionArn();
        } catch (SdkClientException e) {
            log.debug("AWS Exception : {}", e.getMessage());
            throw new WitcurveException("There was a problem while deleting end point");
        }
    }

    public void unSubscribe(String subscriptionArn) throws WitcurveException {
        try {
            amazonSNS.unsubscribe(subscriptionArn);
        } catch (SdkClientException e) {
            log.debug("AWS Exception : {}", e.getMessage());
            throw new WitcurveException("There was a problem while deleting end point");
        }
    }

    public String createTopic(String name) {
        try {
            CreateTopicRequest createTopicRequest = new CreateTopicRequest(name);
            CreateTopicResult createTopicResult = amazonSNS.createTopic(createTopicRequest);
            return createTopicResult.getTopicArn();
        } catch (SdkClientException e) {
            log.debug("AWS Exception : {}", e.getMessage());
            throw new WitcurveException("There was a problem creating topic");
        }
    }

    public void publishMessage(String message, String url, String endPoint) {
        try {
            PublishRequest publishRequest = new PublishRequest();
            publishRequest.setTargetArn(endPoint);
            publishRequest.setMessage(getPublishMessage(message, url));
            publishRequest.setMessageStructure("json");
            log.info("\n\n publish request created \n\n");
            amazonSNS.publish(publishRequest);
            log.info("\n\n  request pulished  \n\n");
        } catch (SdkClientException e) {
            log.info("\n---AWS Exception : {} ---\n", e.getMessage());
        }

    }

    public void publishBulkMessage(String message, String url, TopicType type, Long schoolInfoId, Long standardId) {
        String topicArn = null;
        if (type.equals(TopicType.GLOBAL)) {
            List<TopicRecord> topicRecords = topicRecordRepository.findByType(TopicType.GLOBAL);
            if (topicRecords.size() == 1) {
                topicArn = topicRecords.get(0).getTopicEndPoint();
            }
        } else if (type.equals(TopicType.SCHOOL_INFO)) {
            if (schoolInfoId == null) {
                log.info("School Info Id is required for publishing bulk message");
            }
            List<TopicRecord> topicRecord = topicRecordRepository.findByTypeAndSchoolInfoId(TopicType.SCHOOL_INFO, schoolInfoId);
            topicArn = topicRecord.get(0).getTopicEndPoint();
        } else if (type.equals(TopicType.STAFF_SCHOOL_INFO)) {
            if (schoolInfoId == null) {
                log.info("School Info Id is required for publishing bulk message");
            }
            List<TopicRecord> topicRecord = topicRecordRepository.findByTypeAndSchoolInfoId(TopicType.STAFF_SCHOOL_INFO, schoolInfoId);
            topicArn = topicRecord.get(0).getTopicEndPoint();
        } else if (type.equals(TopicType.STANDARD)) {
            if (standardId == null) {
                log.info("School Info Id is required for publishing bulk message");
            }
            List<TopicRecord> topicRecord = topicRecordRepository.findByStandardId(standardId);
            topicArn = topicRecord.get(0).getTopicEndPoint();
        }
        if (topicArn != null) {
            try {
                PublishRequest publishRequest = new PublishRequest();
                publishRequest.setTopicArn(topicArn);
                publishRequest.setMessage(getPublishMessage(message, url));
                publishRequest.setMessageStructure("json");
                amazonSNS.publish(publishRequest);
            } catch (SdkClientException e) {
                log.info("AWS Exception : {}", e.getMessage());
                log.info("There was a problem sending this message");
            }
        } else {
            log.info("There was a problem getting topic arn links");
        }
    }

    private String getPublishMessage(String message, String url) {
        if (url == null) {
            return "{ \n" +
                "\"default\": \"" + message + "\",\n" +
                "\"APNS\": \"{\\\"aps\\\":{\\\"alert\\\": \\\"" + message + "\\\"} }\",\n" +
                "\"GCM\": \"{ \\\"notification\\\" : { \\\"body\\\" : \\\"" + message + "\\\", \\\"click_action\\\" : \\\"FCM_PLUGIN_ACTIVITY\\\" } }\"\n" +
                "}  ";
        } else {
            return "{ \n" +
                "\"default\": \"" + message + "\",\n" +
                "\"APNS\": \"{\\\"aps\\\":{\\\"alert\\\": \\\"" + message + "\\\",\\\"url\\\":\\\"" + url + "\\\"} }\",\n" +
                "\"GCM\": \"{ \\\"notification\\\" : { \\\"body\\\" : \\\"" + message + "\\\", \\\"click_action\\\" : \\\"FCM_PLUGIN_ACTIVITY\\\"}, \\\"data\\\" : { \\\"url\\\" : \\\"" + url + "\\\" } }\"\n" +
                "}  ";
        }
    }

    @Async
    @Transactional
    public void sendPushNotification(List<EventDTO> listOfEventDTO, Map<Long, LocalDate> map) {
        Set<Long> keys = map.keySet();
        String url;
        String message;
        String stringOfUserIds;
        Standard standard;
        Course course;
        for (EventDTO eventDTO : listOfEventDTO) {
            Map<String, String> variableMap;
            switch (eventDTO.getType()) {

                case TEST:
                    List<UserMobileEndPoint> listOfUserMobileEndPointsRelatedToTest = userMobileEndPointRepository.findByStandardId(eventDTO.getStandardId());
                    course = courseRepository.findBySlotCourseDetailId(eventDTO.getScd().getId());
                    standard = standardRepository.getOne(eventDTO.getStandardId());

                    variableMap = new HashMap<>();
                    variableMap.put("subject", course.getMasterSubject().getName());
                    variableMap.put("date", WitcurveUtil.format(eventDTO.getDate()));
                    variableMap.put("class", "'" + standard.getGrade().toString() + "-" + standard.getSection() + "'");

                    if (keys.contains(eventDTO.getId())) {
                        if (!eventDTO.getDate().equals(map.get(eventDTO.getId()))) {
                            variableMap.put("fromDate", WitcurveUtil.format(map.get(eventDTO.getId())));
                            message = WitcurveUtil.replacePlaceHolder(variableMap, WitCurveConstants.UPDATED_TEST_PUSH_NOTIFICATION);
                            stringOfUserIds = convertToCommaSeparatedStringOfUserIds(listOfUserMobileEndPointsRelatedToTest);
                            url = "?userId=" + stringOfUserIds + "&event=true&date=" + eventDTO.getDate();
                            publishBulkMessage(message, url, TopicType.STANDARD, null, eventDTO.getStandardId());
                        }
                    } else {
                        message = WitcurveUtil.replacePlaceHolder(variableMap, WitCurveConstants.TEST_PUSH_NOTIFICATION);
                        stringOfUserIds = convertToCommaSeparatedStringOfUserIds(listOfUserMobileEndPointsRelatedToTest);
                        url = "?userId=" + stringOfUserIds + "&event=true&date=" + eventDTO.getDate();
                        publishBulkMessage(message, url, TopicType.STANDARD, null, eventDTO.getStandardId());
                    }

                    break;
                case ASSIGNMENT:
                    List<UserMobileEndPoint> listOfUserMobileEndPointsRelatedToAssignment = userMobileEndPointRepository.findByStandardId(eventDTO.getStandardId());
                    //
                    Course cName = courseRepository.findByCourseTeacherId(eventDTO.getCourseTeacher().getId());
                    standard = standardRepository.getOne(eventDTO.getStandardId());

                    variableMap = new HashMap<>();
                    variableMap.put("subject", cName.getMasterSubject().getName());
                    variableMap.put("date", WitcurveUtil.format(eventDTO.getDate()));
                    variableMap.put("class", "'" + standard.getGrade().toString() + "-" + standard.getSection() + "'");

                    if (keys.contains(eventDTO.getId())) {
                        if (!eventDTO.getDate().equals(map.get(eventDTO.getId()))) {
                            variableMap.put("fromDate", WitcurveUtil.format(map.get(eventDTO.getId())));
                            message = WitcurveUtil.replacePlaceHolder(variableMap, WitCurveConstants.UPDATED_ASSIGNMENT_PUSH_NOTIFICATION);
                            stringOfUserIds = convertToCommaSeparatedStringOfUserIds(listOfUserMobileEndPointsRelatedToAssignment);
                            url = "?userId=" + stringOfUserIds + "&event=true&date=" + eventDTO.getDate();
                            publishBulkMessage(message, url, TopicType.STANDARD, null, eventDTO.getStandardId());
                        }
                    } else {
                        message = WitcurveUtil.replacePlaceHolder(variableMap, WitCurveConstants.ASSIGNMENT_PUSH_NOTIFICATION);
                        stringOfUserIds = convertToCommaSeparatedStringOfUserIds(listOfUserMobileEndPointsRelatedToAssignment);
                        url = "?userId=" + stringOfUserIds + "&event=true&date=" + eventDTO.getDate();
                        publishBulkMessage(message, url, TopicType.STANDARD, null, eventDTO.getStandardId());
                    }

                    break;
                case DAILY_UPDATE:
                    List<UserMobileEndPoint> listOfUserMobileEndPointsRelatedToDailyUpdate = userMobileEndPointRepository.findByStandardId(eventDTO.getStandardId());
                    course = courseRepository.findBySlotCourseDetailId(eventDTO.getScd().getId());

                    variableMap = new HashMap<>();
                    variableMap.put("subject", course.getMasterSubject().getName());

                    message = WitcurveUtil.replacePlaceHolder(variableMap, WitCurveConstants.DAILY_UPDATE);
                    stringOfUserIds = convertToCommaSeparatedStringOfUserIds(listOfUserMobileEndPointsRelatedToDailyUpdate);
                    //we need to change it later
                    url = "?diary=true&userId=" + stringOfUserIds;
                    publishBulkMessage(message, url, TopicType.STANDARD, null, eventDTO.getStandardId());
                    break;
                case STAFF_NOTICE:
                    List<UserMobileEndPoint> listOfUserMobileEndPointsRelatedToStaffNotice = userMobileEndPointRepository.findStaffBySchoolInfoId(eventDTO.getSchoolInfoId());
                    stringOfUserIds = convertToCommaSeparatedStringOfUserIds(listOfUserMobileEndPointsRelatedToStaffNotice);
                    url = "?userId=" + stringOfUserIds + "&notice=true";
                    publishBulkMessage(WitCurveConstants.STAFF_NOTICE, url, TopicType.STAFF_SCHOOL_INFO, eventDTO.getSchoolInfoId(), 0l);
                    break;
                case NOTICE://Notice For Particular standard
                    if (eventDTO.getStandardId() != null) {
                        variableMap = new HashMap<>();
                        standard = standardRepository.getOne(eventDTO.getStandardId());
                        variableMap.put("class", "'" + standard.getGrade().toString() + "-" + standard.getSection() + "'");
                        message = WitcurveUtil.replacePlaceHolder(variableMap, WitCurveConstants.NOTICE_FOR_CLASS);

                        List<UserMobileEndPoint> listOfUserMobileEndPointsRelatedToNoticeOfStd = userMobileEndPointRepository.findByStandardId(eventDTO.getStandardId());
                        stringOfUserIds = convertToCommaSeparatedStringOfUserIds(listOfUserMobileEndPointsRelatedToNoticeOfStd);
                        url = "?userId=" + stringOfUserIds + "&notice=true";
                        publishBulkMessage(message, url, TopicType.STANDARD, null, eventDTO.getStandardId());
                    } else {//Notice For All
                        url = "?notice=true";
                        publishBulkMessage(WitCurveConstants.NOTICE_FOR_All, url, TopicType.SCHOOL_INFO, eventDTO.getSchoolInfoId(), 0l);
                    }
                    break;
                case HOLIDAY:
                    variableMap = new HashMap<>();
                    variableMap.put("date", WitcurveUtil.format(eventDTO.getDate()));
                    message = WitcurveUtil.replacePlaceHolder(variableMap, WitCurveConstants.HOLIDAY);
                    url = "?event=true&date=" + eventDTO.getDate();
                    publishBulkMessage(message, url, TopicType.SCHOOL_INFO, eventDTO.getSchoolInfoId(), null);
                    break;
                case SCHOOL_EVENT:
                    variableMap = new HashMap<>();
                    variableMap.put("date", WitcurveUtil.format(eventDTO.getDate()));

                    if (eventDTO.getGrade() != null) {
                        variableMap.put("class", "'" + eventDTO.getGrade().toString() + "'");
                        message = WitcurveUtil.replacePlaceHolder(variableMap, WitCurveConstants.SCHOOL_EVENT_FOR_CLASS);
                        List<Standard> listOfStandard = standardRepository.findByGradeAndSchoolInfoId(eventDTO.getGrade(), eventDTO.getSchoolInfoId());

                        for (Standard std : listOfStandard) {
                            List<UserMobileEndPoint> listOfUserMobileEndPointsRelatedToNoticeOfStd = userMobileEndPointRepository.findByStandardId(std.getId());
                            stringOfUserIds = convertToCommaSeparatedStringOfUserIds(listOfUserMobileEndPointsRelatedToNoticeOfStd);
                            url = "?" + stringOfUserIds + "&event=true&date=" + eventDTO.getDate();
                            publishBulkMessage(message, url, TopicType.STANDARD, null, std.getId());
                        }
                    } else { // For All
                        message = WitcurveUtil.replacePlaceHolder(variableMap, WitCurveConstants.SCHOOL_EVENT_FOR_ALL);
                        url = "?event=true&date=" + eventDTO.getDate();
                        publishBulkMessage(message, url, TopicType.SCHOOL_INFO, eventDTO.getSchoolInfoId(), null);
                    }

                    break;
                case ATTENDANCE:
                    variableMap = new HashMap<>();
                    variableMap.put("date", WitcurveUtil.format(eventDTO.getDate()));

                    message = WitcurveUtil.replacePlaceHolder(variableMap, WitCurveConstants.ATTENDANCE);
                    //To send notification when student is absent
                    if (eventDTO.getAttendanceType().equals(AttendanceType.ABSENT)) {
                        if (eventDTO.getStudentId() != null) {
                            List<UserMobileEndPoint> userMobileEndPointsRelatedToAbsentStudent = userMobileEndPointRepository.findStudentEndPointByStudentId(eventDTO.getStudentId());
                            for (UserMobileEndPoint userMobileEndPoint : userMobileEndPointsRelatedToAbsentStudent) {
                                url = "?userId=" + userMobileEndPoint.getUser().getId() + "&absent=true";
                                publishMessage(message, url, userMobileEndPoint.getEndPoint());
                            }
                        }
                        //To send notification when staff is absent
                        else if (eventDTO.getStaffId() != null) {
                            List<UserMobileEndPoint> userMobileEndPointsRelatedToStaffAttendance = userMobileEndPointRepository.findStaffEndPointByStaffId(eventDTO.getStaffId());
                            for (UserMobileEndPoint userMobileEndPoint : userMobileEndPointsRelatedToStaffAttendance) {
                                url = "?userId=" + userMobileEndPoint.getUser().getId() + "&absent=true";
                                publishMessage(message, url, userMobileEndPoint.getEndPoint());
                            }
                        }
                    }
                    break;
                case PERIODIC_TEST:
                    if (eventDTO.getCourse() != null) {
                        course = courseRepository.getOne(eventDTO.getCourse().getId());
                        variableMap = new HashMap<>();
                        variableMap.put("subject", course.getMasterSubject().getName());
                        variableMap.put("date", WitcurveUtil.format(eventDTO.getDate()));

                        List<Standard> listOfStandard = standardRepository.findByGradeAndSchoolInfoId(eventDTO.getGrade(), eventDTO.getSchoolInfoId());
                        for (Standard std : listOfStandard) {
                            List<UserMobileEndPoint> listOfUserMobileEndPointsRelatedToPeriodicTest = userMobileEndPointRepository.findByStandardId(std.getId());
                            variableMap.put("class", "'" + std.getGrade().toString() + "-" + std.getSection() + "'");
                            if (keys.contains(eventDTO.getId())) {
                                if (!eventDTO.getDate().equals(map.get(eventDTO.getId()))) {
                                    variableMap.put("fromDate", WitcurveUtil.format(map.get(eventDTO.getId())));
                                    message = WitcurveUtil.replacePlaceHolder(variableMap, WitCurveConstants.UPDATED_PERIODIC_TEST_PUSH_NOTIFICATION);
                                    stringOfUserIds = convertToCommaSeparatedStringOfUserIds(listOfUserMobileEndPointsRelatedToPeriodicTest);
                                    url = "?userId=" + stringOfUserIds + "&event=true&date=" + eventDTO.getDate();
                                    publishBulkMessage(message, url, TopicType.STANDARD, null, std.getId());
                                }
                            } else {
                                message = WitcurveUtil.replacePlaceHolder(variableMap, WitCurveConstants.PERIODIC_TEST_PUSH_NOTIFICATION);
                                stringOfUserIds = convertToCommaSeparatedStringOfUserIds(listOfUserMobileEndPointsRelatedToPeriodicTest);
                                url = "?userId=" + stringOfUserIds + "&event=true&date=" + eventDTO.getDate();
                                publishBulkMessage(message, url, TopicType.STANDARD, null, std.getId());
                            }

                        }
                    }
                    break;
            }
        }
    }

    @Async
    public void sendPushNotificationOfLeaveApplicationStatusChange(LeaveApplicationDTO leaveApplicationDTO) {
        String message;
        Map<String, String> varMap = new HashMap();
        varMap.put("fromDate", WitcurveUtil.format(leaveApplicationDTO.getFromLeaveDate()));
        varMap.put("toDate", WitcurveUtil.format(leaveApplicationDTO.getToLeaveDate()));
        varMap.put("status", leaveApplicationDTO.getStatus().toString().toLowerCase());

        if (leaveApplicationDTO.getFromLeaveDate().equals(leaveApplicationDTO.getToLeaveDate())) {
            message = WitcurveUtil.replacePlaceHolder(varMap, WitCurveConstants.LEAVE_APPLICATION_STATUS_FOR_ONE_DAY);
        } else {
            message = WitcurveUtil.replacePlaceHolder(varMap, WitCurveConstants.LEAVE_APPLICATION_STATUS);
        }
        List<UserMobileEndPoint> userMobileEndPointsRelatedToLeaveStatus;
        String url;
        if (leaveApplicationDTO.getAppliedStaffId() != null) { //To send notification to staff
            userMobileEndPointsRelatedToLeaveStatus = userMobileEndPointRepository.findStaffEndPointByStaffId(leaveApplicationDTO.getAppliedStaffId());
            for (UserMobileEndPoint userMobileEndPoint : userMobileEndPointsRelatedToLeaveStatus) {
                url = "?userId=" + userMobileEndPoint.getUser().getId() + "&leave=true";
                publishMessage(message, url, userMobileEndPoint.getEndPoint());
            }
        } else {//To send notification for student
            userMobileEndPointsRelatedToLeaveStatus = userMobileEndPointRepository.findStudentEndPointByStudentId(leaveApplicationDTO.getAppliedStudentId());
            for (UserMobileEndPoint userMobileEndPoint : userMobileEndPointsRelatedToLeaveStatus) {
                url = "?userId=" + userMobileEndPoint.getUser().getId() + "&leave=true";
                publishMessage(message, url, userMobileEndPoint.getEndPoint());
            }
        }
    }

    @Async
    public void sendPushNotificationOnStatusOfMeetingRequestChange(MessageThreadDTO messageThreadDTO) {
        Map<String, String> varMap = new HashMap<>();
        if (messageThreadDTO.getStatus() != null) {
            varMap.put("status", messageThreadDTO.getStatus().toString().toLowerCase());
            varMap.put("date", WitcurveUtil.format(messageThreadDTO.getMeetingDate()));
            varMap.put("time", WitcurveUtil.timeFormat(messageThreadDTO.getMeetingTime()));

            String message = WitcurveUtil.replacePlaceHolder(varMap, WitCurveConstants.MEETING_REQUEST_STATUS_CHANGE);
            List<UserMobileEndPoint> listOfUserMobileEndPointsRelatedToStatusChangeOfMeetingReq = userMobileEndPointRepository.findByUserId(messageThreadDTO.getFromUserId());
            for (UserMobileEndPoint userMobileEndPoint : listOfUserMobileEndPointsRelatedToStatusChangeOfMeetingReq) {
                String urlOfMeetingReq = "?userId=" + userMobileEndPoint.getUser().getId() + "&meeting=true";
                publishMessage(message, urlOfMeetingReq, userMobileEndPoint.getEndPoint());
            }
        }
    }

    @Async
    public void sendPushNotification(ExamDTO examDTO) {
        String message;
        Map<String, String> map = new HashMap();
        map.put("examName", examDTO.getName());

        if (examDTO.getStatus().equals(ExamStatus.PUBLISHED)) {
            map.put("fromDate", WitcurveUtil.format(examDTO.getStartDate()));
            map.put("toDate", WitcurveUtil.format(examDTO.getEndDate()));

            message = WitcurveUtil.replacePlaceHolder(map, WitCurveConstants.EXAM_PUBLISHED);
        } else
            message = WitcurveUtil.replacePlaceHolder(map, WitCurveConstants.EXAM_RESULT_DECLARED);

        Set<Grade> grades = examDTO.getGrades();
        for (Grade grade : grades) {
            List<Standard> listOfStandard = standardRepository.findByGradeAndSchoolInfoId(grade, examDTO.getSchoolInfoId());
            for (Standard standard : listOfStandard) {
                List<UserMobileEndPoint> listOfUserMobileEndPointsRelatedToNoticeOfStd = userMobileEndPointRepository.findByStandardId(standard.getId());
                String listOfUserIds = convertToCommaSeparatedStringOfUserIds(listOfUserMobileEndPointsRelatedToNoticeOfStd);
                String url = "?userId=" + listOfUserIds + "&event=true&date=" + examDTO.getStartDate();
                publishBulkMessage(message, url, TopicType.STANDARD, null, standard.getId());
            }
        }
    }


    @Async
    public void sendPushNotification(MessageDTO messageDTO, MessageThreadDTO messageThreadDTO) {
        log.info("\n\npush notification started\n\n");
        switch (messageThreadDTO.getMessageType()) {
            case SUBJECT_NOTE:
                List<UserMobileEndPoint> listOfUserMobileEndPointsRelatedGroupMessage = userMobileEndPointRepository.findStudentEndPointByCourseTeacherId(messageThreadDTO.getCourseTeacherDTO().getId());
                Optional<CourseTeacher> courseTeacher = courseTeacherRepository.findById(messageThreadDTO.getCourseTeacherDTO().getId());
                String stringOfUserIds = convertToCommaSeparatedStringOfUserIds(listOfUserMobileEndPointsRelatedGroupMessage);
                String urlOfSubjectNote = "?userId=" + stringOfUserIds + "&group=true";
                publishBulkMessage(WitCurveConstants.GROUP_MESSAGE, urlOfSubjectNote, TopicType.STANDARD, null, courseTeacher.get().getStandard().getId());
                break;
            case PERSONAL:
                log.info("\n\ncategory personal started\n\n");
                if (messageThreadDTO.getSchoolBoardAdminMessage() == true || messageThreadDTO.getSuperAdminMessage() == true) {
                    List<UserMobileEndPoint> listOfUserMobileEndPointsRelatedToAdminMessage = userMobileEndPointRepository.findByUserId(messageDTO.getToUserId());
                    for (UserMobileEndPoint userMobileEndPoint : listOfUserMobileEndPointsRelatedToAdminMessage) {
                        String urlOfAdminMessage = "?userId=" + userMobileEndPoint.getUser().getId() + "&admin=true";
                        publishMessage(WitCurveConstants.ADMIN_MESSAGE, urlOfAdminMessage, userMobileEndPoint.getEndPoint());
                    }
                } else {
                    List<UserMobileEndPoint> listOfUserMobileEndPointsRelatedToPersonalMessage = userMobileEndPointRepository.findByUserId(messageDTO.getToUserId());
                    for (UserMobileEndPoint userMobileEndPoint : listOfUserMobileEndPointsRelatedToPersonalMessage) {
                        String urlOfPersonalMsg = "?userId=" + userMobileEndPoint.getUser().getId() + "&direct=true";
                        log.info("\n\nEnd point --- {}\n\n", userMobileEndPoint.getEndPoint());
                        publishMessage(WitCurveConstants.PERSONAL_MESSAGE, urlOfPersonalMsg, userMobileEndPoint.getEndPoint());
                    }
                }
                break;
            case MEETING_REQUEST:
                List<UserMobileEndPoint> listOfUserMobileEndPointsRelatedMeetingReq = userMobileEndPointRepository.findByUserId(messageDTO.getToUserId());
                for (UserMobileEndPoint userMobileEndPoint : listOfUserMobileEndPointsRelatedMeetingReq) {
                    String urlOfMeetingReq = "?userId=" + userMobileEndPoint.getUser().getId() + "&meeting=true";
                    publishMessage(WitCurveConstants.MEETING_REQUEST, urlOfMeetingReq, userMobileEndPoint.getEndPoint());
                }
                break;
        }
    }

    @Async
    public void sendPushNotificationWhenLeaveApplicationCreated(LeaveApplicationDTO leaveApplicationDTO, Boolean update) {
        Map<String, String> varMap = new HashMap<>();
        String message;
        if (leaveApplicationDTO.getAppliedStudentId() != null) {
            Optional<Student> studentObj = studentRepository.findById(leaveApplicationDTO.getAppliedStudentId());
            Student student = studentObj.get();
            varMap.put("studentName", student.getFirstName() + " " + student.getLastName());
            varMap.put("fromDate", WitcurveUtil.format(leaveApplicationDTO.getFromLeaveDate()));
            varMap.put("toDate", WitcurveUtil.format(leaveApplicationDTO.getToLeaveDate()));
            if (!update) {
                if (leaveApplicationDTO.getFromLeaveDate().equals(leaveApplicationDTO.getToLeaveDate())) {
                    message = WitcurveUtil.replacePlaceHolder(varMap, WitCurveConstants.LEAVE_APPLICATION_CREATED_FOR_ONE_DAY);
                } else {
                    message = WitcurveUtil.replacePlaceHolder(varMap, WitCurveConstants.LEAVE_APPLICATION_CREATED);
                }
            } else {
                if (leaveApplicationDTO.getFromLeaveDate().equals(leaveApplicationDTO.getToLeaveDate())) {
                    message = WitcurveUtil.replacePlaceHolder(varMap, WitCurveConstants.UPDATED_LEAVE_APPLICATION_FOR_ONE_DAY);
                } else {
                    message = WitcurveUtil.replacePlaceHolder(varMap, WitCurveConstants.UPDATED_LEAVE_APPLICATION);
                }
            }
            List<UserMobileEndPoint> userMobileEndPointOfClassTeacher = userMobileEndPointRepository.findClassTeacherEndPointByStudentId(leaveApplicationDTO.getAppliedStudentId());
            for (UserMobileEndPoint userMobileEndPoint : userMobileEndPointOfClassTeacher) {
                String urlOfLeaveApp = "?userId=" + userMobileEndPoint.getUser().getId() + "&leave=true";
                publishMessage(message, urlOfLeaveApp, userMobileEndPoint.getEndPoint());
            }
        }
    }

    private String convertToCommaSeparatedStringOfUserIds(List<UserMobileEndPoint> listOfUserMobileEndPoint) {
        List<String> listOfUserIds = new ArrayList<>();
        for (UserMobileEndPoint userMobileEndPoint : listOfUserMobileEndPoint) {
            if(!listOfUserIds.contains(userMobileEndPoint.getUser().getId())) {
                listOfUserIds.add(userMobileEndPoint.getUser().getId().toString());
            }
        }
        return listOfUserIds.stream().collect(Collectors.joining(","));
    }
}








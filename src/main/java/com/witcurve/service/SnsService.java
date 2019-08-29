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
import com.witcurve.service.dto.EventDTO;
import com.witcurve.service.dto.ExamDTO;
import com.witcurve.service.dto.LeaveApplicationDTO;
import com.witcurve.service.dto.MessageThreadDTO;
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
            amazonSNS.publish(publishRequest);
        } catch (SdkClientException e) {
            log.debug("AWS Exception : {}", e.getMessage());
            throw new WitcurveException("There was a problem sending this message");
        }

    }

    public void publishBulkMessage(String message, String url, TopicType type, Long schoolInfoId) {
        String topicArn = null;
        if (type.equals(TopicType.GLOBAL)) {
            List<TopicRecord> topicRecords = topicRecordRepository.findByType(TopicType.GLOBAL);
            if (topicRecords.size() == 1) {
                topicArn = topicRecords.get(0).getTopicEndPoint();
            }
        } else {
            if (schoolInfoId == null) {
                throw new WitcurveException("School Info Id is required for publishing bulk message");
            }
            TopicRecord topicRecord = topicRecordRepository.findBySchoolInfoId(schoolInfoId);
            topicArn = topicRecord.getTopicEndPoint();
        }
        if (topicArn != null) {
            try {
                PublishRequest publishRequest = new PublishRequest();
                publishRequest.setTopicArn(topicArn);
                publishRequest.setMessage(getPublishMessage(message, url));
                publishRequest.setMessageStructure("json");
                amazonSNS.publish(publishRequest);
            } catch (SdkClientException e) {
                log.debug("AWS Exception : {}", e.getMessage());
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

    @Async
    @Transactional
    public void sendPushNotification(List<EventDTO> listOfEventDTO, Map<Long, LocalDate> map) {
        Set<Long> keys = map.keySet();
        for (EventDTO eventDTO : listOfEventDTO) {
            Map<String, String> variableMap;
            switch (eventDTO.getType()) {

                case TEST:
                    String finalMessage;
                    List<UserMobileEndPoint> listOfUserMobileEndPointsRelatedToTest = userMobileEndPointRepository.findByStandardId(eventDTO.getStandardId());
                    Course course = courseRepository.findBySlotCourseDetailId(eventDTO.getScd().getId());
                    variableMap = new HashMap<>();
                    variableMap.put("subject", course.getMasterSubject().getName());
                    variableMap.put("date", WitcurveUtil.format(eventDTO.getDate()));

                    if (keys.contains(eventDTO.getId())) {
                        variableMap.put("fromDate", WitcurveUtil.format(map.get(eventDTO.getId())));
                        finalMessage = WitcurveUtil.replacePlaceHolder(variableMap, WitCurveConstants.UPDATED_TEST_PUSH_NOTIFICATION);
                    } else {
                        finalMessage = WitcurveUtil.replacePlaceHolder(variableMap, WitCurveConstants.TEST_PUSH_NOTIFICATION);
                    }
                    for (UserMobileEndPoint userMobileEndPoint : listOfUserMobileEndPointsRelatedToTest) {
                        String urlOfTest = "?userId=" + userMobileEndPoint.getUser().getId() + "&event=true&date=" + eventDTO.getDate();
                        publishMessage(finalMessage, urlOfTest, userMobileEndPoint.getEndPoint());
                    }
                    break;
                case ASSIGNMENT:
                    String message;
                    List<UserMobileEndPoint> listOfUserMobileEndPointsRelatedToAssignment = userMobileEndPointRepository.findByStandardId(eventDTO.getStandardId());
                    Course cName = courseRepository.findByCourseTeacherId(eventDTO.getCourseTeacher().getId());
                    variableMap = new HashMap<>();
                    variableMap.put("subject", cName.getMasterSubject().getName());
                    variableMap.put("date", WitcurveUtil.format(eventDTO.getDate()));

                    if (keys.contains(eventDTO.getId())) {
                        variableMap.put("fromDate", WitcurveUtil.format(map.get(eventDTO.getId())));
                        message = WitcurveUtil.replacePlaceHolder(variableMap, WitCurveConstants.UPDATED_ASSIGNMENT_PUSH_NOTIFICATION);
                    } else {
                        message = WitcurveUtil.replacePlaceHolder(variableMap, WitCurveConstants.ASSIGNMENT_PUSH_NOTIFICATION);
                    }
                    for (UserMobileEndPoint userMobileEndPoint : listOfUserMobileEndPointsRelatedToAssignment) {
                        String urlOfAssignment = "?userId=" + userMobileEndPoint.getUser().getId() + "&event=true&date=" + eventDTO.getDate();
                        publishMessage(message, urlOfAssignment, userMobileEndPoint.getEndPoint());
                    }
                    break;

                case STAFF_NOTICE:
                    List<UserMobileEndPoint> listOfUserMobileEndPointsRelatedToStaffNotice = userMobileEndPointRepository.findStaffBySchoolInfoId(eventDTO.getSchoolInfoId());
                    for (UserMobileEndPoint userMobileEndPoint : listOfUserMobileEndPointsRelatedToStaffNotice) {
                        String url = "?userId=" + userMobileEndPoint.getUser().getId() + "&notice=true";
                        publishMessage(WitCurveConstants.STAFF_NOTICE, url, userMobileEndPoint.getEndPoint());
                    }
                    break;

                case NOTICE://Notice For Particular standard
                    if (eventDTO.getStandardId() != null) {
                        variableMap = new HashMap<>();
                        Standard standard = standardRepository.getOne(eventDTO.getStandardId());
                        variableMap.put("className", standard.getGrade().toString() + " " + standard.getSection());
                        String notice = WitcurveUtil.replacePlaceHolder(variableMap, WitCurveConstants.NOTICE_FOR_CLASS);

                        List<UserMobileEndPoint> listOfUserMobileEndPointsRelatedToNoticeOfStd = userMobileEndPointRepository.findByStandardId(eventDTO.getStandardId());
                        for (UserMobileEndPoint userMobileEndPoint : listOfUserMobileEndPointsRelatedToNoticeOfStd) {
                            String url = "?userId=" + userMobileEndPoint.getUser().getId() + "&notice=true";
                            publishMessage(notice, url, userMobileEndPoint.getEndPoint());
                        }
                    } else {//Notice For All
                        TopicRecord topicRecord = topicRecordRepository.findBySchoolInfoId(eventDTO.getSchoolInfoId());
                        if (topicRecord != null) {
                            String url = "?notice=true";
                            publishBulkMessage(WitCurveConstants.NOTICE_FOR_All, url, topicRecord.getType(), eventDTO.getSchoolInfoId());
                        }
                    }
                    break;
                case HOLIDAY:
                    variableMap = new HashMap<>();
                    variableMap.put("date", WitcurveUtil.format(eventDTO.getDate()));
                    String holidayMessage = WitcurveUtil.replacePlaceHolder(variableMap, WitCurveConstants.HOLIDAY);

                    TopicRecord topicRecord = topicRecordRepository.findBySchoolInfoId(eventDTO.getSchoolInfoId());
                    if (topicRecord != null) {
                        String url = "?event=true&date=" + eventDTO.getDate();
                        publishBulkMessage(holidayMessage, url, topicRecord.getType(), eventDTO.getSchoolInfoId());
                    }
                    break;

                case SCHOOL_EVENT:
                    variableMap = new HashMap<>();
                    variableMap.put("date", WitcurveUtil.format(eventDTO.getDate()));

                    if (eventDTO.getGrade() != null) {
                        variableMap.put("className", eventDTO.getGrade().toString());
                        String messageOfSchoolEvent = WitcurveUtil.replacePlaceHolder(variableMap, WitCurveConstants.SCHOOL_EVENT_FOR_CLASS);

                        List<UserMobileEndPoint> listOfUserMobileEndPointsRelatedToNoticeOfStd = userMobileEndPointRepository.findStudentEndPointByGradeAndSchoolInfoId(eventDTO.getGrade(), eventDTO.getSchoolInfoId());
                        for (UserMobileEndPoint userMobileEndPoint : listOfUserMobileEndPointsRelatedToNoticeOfStd) {
                            String urlOfSchoolEvent = "?userId=" + userMobileEndPoint.getUser().getId() + "&event=true&date=" + eventDTO.getDate();
                            publishMessage(messageOfSchoolEvent, urlOfSchoolEvent, userMobileEndPoint.getEndPoint());
                        }
                    } else { // For All
                        String messageOfSchoolEvent = WitcurveUtil.replacePlaceHolder(variableMap, WitCurveConstants.SCHOOL_EVENT_FOR_ALL);
                        TopicRecord topicRecordObj = topicRecordRepository.findBySchoolInfoId(eventDTO.getSchoolInfoId());
                        if (topicRecordObj != null) {
                            String urlOfSchoolEvent = "?event=true&date=" + eventDTO.getDate();
                            publishBulkMessage(messageOfSchoolEvent, urlOfSchoolEvent, topicRecordObj.getType(), eventDTO.getSchoolInfoId());
                        }
                    }
                    break;

                case ATTENDANCE:
                    variableMap = new HashMap<>();
                    variableMap.put("date", WitcurveUtil.format(eventDTO.getDate()));

                    String finalMessageContent = WitcurveUtil.replacePlaceHolder(variableMap, WitCurveConstants.ATTENDANCE);
                    //To send notification when student is absent
                    if (eventDTO.getAttendanceType().equals(AttendanceType.ABSENT)) {
                        if (eventDTO.getStudentId() != null) {
                            List<UserMobileEndPoint> userMobileEndPointsRelatedToAbsentStudent = userMobileEndPointRepository.findStudentEndPointByStudentId(eventDTO.getStudentId());
                            for(UserMobileEndPoint userMobileEndPoint:userMobileEndPointsRelatedToAbsentStudent) {
                                String urlOfStudentAttendance = "?userId=" + userMobileEndPoint.getUser().getId() + "&absent=true";
                                publishMessage(finalMessageContent, urlOfStudentAttendance, userMobileEndPoint.getEndPoint());
                            }
                        }
                        //To send notification when staff is absent
                        else if (eventDTO.getStaffId() != null) {
                            List<UserMobileEndPoint> userMobileEndPointsRelatedToStaffAttendance = userMobileEndPointRepository.findStaffEndPointByStaffId(eventDTO.getStaffId());
                            for(UserMobileEndPoint userMobileEndPoint:userMobileEndPointsRelatedToStaffAttendance){
                                String urlOfStaffAttendance = "?userId=" + userMobileEndPoint.getUser().getId() + "&absent=true";
                                publishMessage(finalMessageContent, urlOfStaffAttendance, userMobileEndPoint.getEndPoint());
                            }
                        }
                    }
                    break;
                case PERIODIC_TEST:
                    if (eventDTO.getCourse() != null) {
                        Course courseName = courseRepository.getOne(eventDTO.getCourse().getId());
                        variableMap = new HashMap<>();
                        variableMap.put("subject", courseName.getMasterSubject().getName());
                        variableMap.put("date", WitcurveUtil.format(eventDTO.getDate()));

                        String periodicTestMessage;
                        if (keys.contains(eventDTO.getId())) {
                            variableMap.put("fromDate", WitcurveUtil.format(map.get(eventDTO.getId())));
                            periodicTestMessage = WitcurveUtil.replacePlaceHolder(variableMap, WitCurveConstants.UPDATED_PERIODIC_TEST_PUSH_NOTIFICATION);
                        } else {
                            periodicTestMessage = WitcurveUtil.replacePlaceHolder(variableMap, WitCurveConstants.PERIODIC_TEST_PUSH_NOTIFICATION);
                        }
                        List<UserMobileEndPoint> userMobileEndPointListRelatedToPeriodicTest = userMobileEndPointRepository.findStudentEndPointByGradeAndSchoolInfoId(eventDTO.getGrade(), eventDTO.getSchoolInfoId());
                        for (UserMobileEndPoint userMobileEndPoint : userMobileEndPointListRelatedToPeriodicTest) {
                            String urlOfTest = "?userId=" + userMobileEndPoint.getUser().getId() + "&event=true&date=" + eventDTO.getDate();
                            publishMessage(periodicTestMessage, urlOfTest, userMobileEndPoint.getEndPoint());
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
            for(UserMobileEndPoint userMobileEndPoint:userMobileEndPointsRelatedToLeaveStatus) {
                url = "?userId=" + userMobileEndPoint.getUser().getId() + "&leave=true";
                publishMessage(message, url, userMobileEndPoint.getEndPoint());
            }
        } else {//To send notification for student
            userMobileEndPointsRelatedToLeaveStatus = userMobileEndPointRepository.findStudentEndPointByStudentId(leaveApplicationDTO.getAppliedStudentId());
            for(UserMobileEndPoint userMobileEndPoint:userMobileEndPointsRelatedToLeaveStatus){
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
            List<UserMobileEndPoint> listOfUserMobileEndPointsRelatedToStatusChangeOfMeetingReq = userMobileEndPointRepository.findByUserId(messageThreadDTO.getToUserId());
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
            List<UserMobileEndPoint> listOfUserMobileEndPointsRelatedToExamNotification = userMobileEndPointRepository.findStudentEndPointByGradeAndSchoolInfoId(grade, examDTO.getSchoolInfoId());
            for (UserMobileEndPoint userMobileEndPoint : listOfUserMobileEndPointsRelatedToExamNotification) {
                String urlOfExam = "?userId=" + userMobileEndPoint.getUser().getId() + "&event=true&date=" + examDTO.getStartDate();
                publishMessage(message, urlOfExam, userMobileEndPoint.getEndPoint());
            }
        }
    }

    @Async
    public void sendPushNotification(MessageThreadDTO messageThreadDTO) {
        switch (messageThreadDTO.getMessageType()) {

            case SUBJECT_NOTE:
                List<UserMobileEndPoint> listOfUserMobileEndPointsRelatedGroupMessage = userMobileEndPointRepository.findStudentEndPointByCourseTeacherId(messageThreadDTO.getCourseTeacherDTO().getId());
                for (UserMobileEndPoint userMobileEndPoint : listOfUserMobileEndPointsRelatedGroupMessage) {
                    String urlOfSubjectNote = "?userId=" + userMobileEndPoint.getUser().getId() + "&group=true";
                    publishMessage(WitCurveConstants.GROUP_MESSAGE, urlOfSubjectNote, userMobileEndPoint.getEndPoint());
                }
                break;
            case PERSONAL:
                if (messageThreadDTO.getSchoolBoardAdminMessage() == true || messageThreadDTO.getSuperAdminMessage() == true) {
                    List<UserMobileEndPoint> listOfUserMobileEndPointsRelatedToAdminMessage = userMobileEndPointRepository.findByUserId(messageThreadDTO.getToUserId());
                    for (UserMobileEndPoint userMobileEndPoint : listOfUserMobileEndPointsRelatedToAdminMessage) {
                        String urlOfAdminMessage = "?userId=" + userMobileEndPoint.getUser().getId() + "&admin=true";
                        publishMessage(WitCurveConstants.ADMIN_MESSAGE, urlOfAdminMessage, userMobileEndPoint.getEndPoint());
                    }
                } else {
                    List<UserMobileEndPoint> listOfUserMobileEndPointsRelatedToPersonalMessage = userMobileEndPointRepository.findByUserId(messageThreadDTO.getToUserId());
                    for (UserMobileEndPoint userMobileEndPoint : listOfUserMobileEndPointsRelatedToPersonalMessage) {
                        String urlOfPersonalMsg = "?userId=" + userMobileEndPoint.getUser().getId() + "&direct=true";
                        publishMessage(WitCurveConstants.PERSONAL_MESSAGE, urlOfPersonalMsg, userMobileEndPoint.getEndPoint());
                    }
                }
                break;
            case MEETING_REQUEST:
                Map<String, String> varMap = new HashMap<>();
                varMap.put("date", WitcurveUtil.format(messageThreadDTO.getMeetingDate()));
                varMap.put("time", WitcurveUtil.timeFormat(messageThreadDTO.getMeetingTime()));

                String message = WitcurveUtil.replacePlaceHolder(varMap, WitCurveConstants.MEETING_REQUEST);

                List<UserMobileEndPoint> listOfUserMobileEndPointsRelatedMeetingReq = userMobileEndPointRepository.findByUserId(messageThreadDTO.getToUserId());
                for (UserMobileEndPoint userMobileEndPoint : listOfUserMobileEndPointsRelatedMeetingReq) {
                    String urlOfMeetingReq = "?userId=" + userMobileEndPoint.getUser().getId() + "&meeting=true";
                    publishMessage(message, urlOfMeetingReq, userMobileEndPoint.getEndPoint());
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
}







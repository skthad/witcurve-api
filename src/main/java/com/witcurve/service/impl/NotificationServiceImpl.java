package com.witcurve.service.impl;

import com.google.common.base.Strings;
import com.witcurve.config.Constants;
import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.ConfigFieldName;
import com.witcurve.domain.enumeration.ConfigType;
import com.witcurve.repository.*;
import com.witcurve.service.MailService;
import com.witcurve.service.NotificationService;
import com.witcurve.service.SmsService;
import com.witcurve.service.dto.LeaveApplicationDTO;
import com.witcurve.service.dto.NotificationInfo;
import com.witcurve.service.mapper.LeaveApplicationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    ConfigSettingsRepository configSettingsRepository;

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentStandardRepository studentStandardRepository;

    @Autowired
    CourseTeacherRepository courseTeacherRepository;

    @Autowired
    LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    SchoolInfoRepository schoolInfoRepository;

    @Autowired
    LeaveApplicationMapper leaveApplicationMapper;

    @Autowired
    SmsService smsService;

    @Autowired
    MailService mailService;

    @Override
    @Async
    public void sendAbsentNotification(SchoolInfo schoolInfo, Map<Long, Set<LocalDate>> studentIdAndDatesMap, Map<Long, Set<LocalDate>> staffIdAndDatesMap) throws UnsupportedEncodingException {
        NotificationInfo info = getNotificationInfo(schoolInfo, ConfigFieldName.STAFF_OR_STUDENT_ABSENT);

        if (info.getSendSMS() || info.getSendEmail() || info.getSendPush()) {
            sendStudentAbsentNotification(studentIdAndDatesMap, info);
            sendStaffAbsentNotification(staffIdAndDatesMap, info);
        }
    }

    @Override
    @Async
    public void sendSchoolEventNotification(SchoolInfo schoolInfo, List<Event> schoolEvents) throws UnsupportedEncodingException {
        NotificationInfo info = getNotificationInfo(schoolInfo, ConfigFieldName.SCHOOL_EVENT_POSTED);
        if (info.getSendSMS() || info.getSendEmail() || info.getSendPush()) {
            sendSchoolEventNotification(schoolInfo, schoolEvents, info);
        }
    }

    @Override
    @Async
    public void sendLeaveApplicationSaveOrUpdateNotification(LeaveApplication leaveApplication, Boolean update) throws UnsupportedEncodingException {

        leaveApplication = leaveApplicationRepository.getOne(leaveApplication.getId());
        NotificationInfo info = getNotificationInfo(leaveApplication, update);
        if (info.getSendSMS() || info.getSendEmail() || info.getSendPush()) {
            String tinyUrl = "http://witcurve.com/tiny";
            String[] subjectParamArray = new String[]{info.getSmsSignature(), ""};
            String titlePlaceholder = "";
            Map paramsMap = getParamsMapForLeaveApplication(leaveApplicationMapper.toDto(leaveApplication), leaveApplication.getSchoolInfo(), info);
            paramsMap.put(Constants.PARAM_TINY_URL, tinyUrl);
            if (leaveApplication.getAppliedStudent() != null) {
                List<String> emailIds = new ArrayList<>();
                List<StudentStandard> studentStandards = studentStandardRepository.getByStudentId(leaveApplication.getAppliedStudent().getId());
                if (studentStandards.size() == 0) {
                    return;
                }
                Standard standard = studentStandards.get(0).getStandard();
                emailIds.add(standard.getClassTeacher().getEmail());
                emailIds.add(standard.getSchoolInfo().getSchool().getPrimaryEmail());
                if (update) {
                    titlePlaceholder = "Student";
                    subjectParamArray[1] = titlePlaceholder;
                    paramsMap.put(Constants.PARAM_TITLE_PLACEHOLDER, titlePlaceholder);
                    paramsMap.put(Constants.PARAM_MAIL_SUBJECT, subjectParamArray);
                    String smsBody = String.format("This is to inform you that %s has modified leave application details. Visit %s for more.", paramsMap.get(Constants.PARAM_FULL_NAME), tinyUrl);
                    if (info.getSendSMS()) {
                        smsService.sendSms(standard.getClassTeacher().getPrimaryPhone(), smsBody, info.getSmsSignature());
                    }
                    if (info.getSendEmail()) {
                        for (String email: emailIds) {
                            mailService.sendEmailFromTemplate(email, paramsMap, "mail/notification/leaveApplicationModificationEmail", "email.notification.leave.application.modification.title", info.getSmsSignature());
                        }
                    }
                    if (info.getSendPush()) {
                        // TODO
                    }
                } else {
                    titlePlaceholder = "student";
                    subjectParamArray[1] = titlePlaceholder;
                    paramsMap.put(Constants.PARAM_TITLE_PLACEHOLDER, titlePlaceholder);
                    paramsMap.put(Constants.PARAM_MAIL_SUBJECT, subjectParamArray);
                    String smsBody = String.format("This is to inform you that %s has applied for leave. Visit %s for more.", paramsMap.get(Constants.PARAM_FULL_NAME), tinyUrl);
                    if (info.getSendSMS()) {
                        smsService.sendSms(standard.getClassTeacher().getPrimaryPhone(), smsBody, info.getSmsSignature());
                    }
                    if (info.getSendEmail()) {
                        for (String email: emailIds) {
                            mailService.sendEmailFromTemplate(email, paramsMap, "mail/notification/leaveApplicationCreationEmail", "email.notification.leave.application.creation.title", info.getSmsSignature());
                        }
                    }
                    if (info.getSendPush()) {
                        // TODO
                    }
                }
            } else if (leaveApplication.getAppliedStaff() != null) {
                String email = leaveApplication.getSchoolInfo().getSchool().getPrimaryEmail();
                String phone = leaveApplication.getSchoolInfo().getSchool().getPrimaryPhone();
                if (update) {
                    titlePlaceholder = "Staff";
                    subjectParamArray[1] = titlePlaceholder;
                    paramsMap.put(Constants.PARAM_TITLE_PLACEHOLDER, titlePlaceholder);
                    paramsMap.put(Constants.PARAM_MAIL_SUBJECT, subjectParamArray);
                    String smsBody = String.format("This is to inform you that %s has modified leave application details. Visit %s for more.", paramsMap.get(Constants.PARAM_FULL_NAME), tinyUrl);
                    if (info.getSendSMS()) {
                        smsService.sendSms(phone, smsBody, info.getSmsSignature());
                    }
                    if (info.getSendEmail()) {
                        mailService.sendEmailFromTemplate(email, paramsMap, "mail/notification/leaveApplicationModificationEmail", "email.notification.leave.application.modification.title", info.getSmsSignature());
                    }
                    if (info.getSendPush()) {
                        // TODO
                    }
                } else {
                    titlePlaceholder = "staff";
                    subjectParamArray[1] = titlePlaceholder;
                    paramsMap.put(Constants.PARAM_TITLE_PLACEHOLDER, titlePlaceholder);
                    paramsMap.put(Constants.PARAM_MAIL_SUBJECT, subjectParamArray);
                    String smsBody = String.format("This is to inform you that %s has applied for leave. Visit %s for more.", paramsMap.get(Constants.PARAM_FULL_NAME), tinyUrl);
                    if (info.getSendSMS()) {
                        smsService.sendSms(phone, smsBody, info.getSmsSignature());
                    }
                    if (info.getSendEmail()) {
                        mailService.sendEmailFromTemplate(email, paramsMap, "mail/notification/leaveApplicationCreationEmail", "email.notification.leave.application.creation.title", info.getSmsSignature());
                    }
                    if (info.getSendPush()) {
                        // TODO
                    }
                }
            }
        }
    }

    @Override
    @Async
    public void sendLeaveApplicationStatusNotification(LeaveApplicationDTO leaveApplication) throws UnsupportedEncodingException {
        NotificationInfo info;
        SchoolInfo schoolInfo = schoolInfoRepository.getOne(leaveApplication.getSchoolInfoId());
        if (leaveApplication.getAppliedStudentId() != null) {
            info = getNotificationInfo(schoolInfo, ConfigFieldName.STUDENT_LEAVE_APPLICATION_STATUS_UPDATED);
        } else {
            info = getNotificationInfo(schoolInfo, ConfigFieldName.STAFF_LEAVE_APPLICATION_STATUS_UPDATED);
        }
        if (info.getSendSMS() || info.getSendEmail() || info.getSendPush()) {
            String tinyUrl = "http://witcurve.com/tiny";
            Map paramsMap = new HashMap();
            paramsMap.put(Constants.PARAM_INSTITUTE_NAME, info.getInstituteName());
            paramsMap.put(Constants.PARAM_TINY_URL, tinyUrl);
            String[] subjectParamArray = new String[]{info.getSmsSignature()};
            paramsMap.put(Constants.PARAM_MAIL_SUBJECT, subjectParamArray);
            String smsBody = String.format("This is to notify you that your leave application status has been updated. Visit %s for more.", tinyUrl);
            if (leaveApplication.getAppliedStudentId() != null) {
                paramsMap.put(Constants.PARAM_GREETING_PLACEHOLDER, "Parent");
                Student student = studentRepository.getOne(leaveApplication.getAppliedStudentId());
                if (info.getSendSMS()) {
                    smsService.sendSms(student.getRegisteredMobileNumber(), smsBody, info.getSmsSignature());
                }
                if (info.getSendEmail() && !Strings.isNullOrEmpty(student.getEmail())) {
                    mailService.sendEmailFromTemplate(student.getEmail(), paramsMap, "mail/notification/leaveApplicationStatusEmail", "email.notification.leave.application.status.title", info.getSmsSignature());
                }
                if (info.getSendPush()) {
                    // TODO
                }
            } else if (leaveApplication.getAppliedStaffId() != null) {
                Staff staff = staffRepository.getOne(leaveApplication.getAppliedStaffId());
                paramsMap.put(Constants.PARAM_GREETING_PLACEHOLDER, "Staff");
                if (info.getSendSMS()) {
                    smsService.sendSms(staff.getPrimaryPhone(), smsBody, info.getSmsSignature());
                }
                if (info.getSendEmail()) {
                    mailService.sendEmailFromTemplate(staff.getEmail(), paramsMap, "mail/notification/leaveApplicationStatusEmail", "email.notification.leave.application.status.title", info.getSmsSignature());
                }
                if (info.getSendPush()) {
                    // TODO
                }
            }
        }
    }

    @Override
    @Async
    public void sendLeaveApplicationDeletionNotification(LeaveApplicationDTO leaveApplication) throws UnsupportedEncodingException {
        NotificationInfo info;
        SchoolInfo schoolInfo = schoolInfoRepository.getOne(leaveApplication.getSchoolInfoId());
        if (leaveApplication.getAppliedStudentId() != null) {
            info = getNotificationInfo(schoolInfo, ConfigFieldName.STUDENT_LEAVE_APPLICATION_DELETED);
        } else {
            info = getNotificationInfo(schoolInfo, ConfigFieldName.STAFF_LEAVE_APPLICATION_DELETED);
        }
        if (info.getSendSMS() || info.getSendEmail() || info.getSendPush()) {
            String tinyUrl = "http://witcurve.com/tiny";
            Map paramsMap = getParamsMapForLeaveApplication(leaveApplication, schoolInfo, info);
            paramsMap.put(Constants.PARAM_TINY_URL, tinyUrl);
            String[] subjectParamArray = new String[]{info.getSmsSignature(), ""};
            String titlePlaceholder = "";
            if (leaveApplication.getAppliedStudentId() != null) {
                titlePlaceholder = "Student";
                subjectParamArray[1] = titlePlaceholder;
                paramsMap.put(Constants.PARAM_TITLE_PLACEHOLDER, titlePlaceholder);
                paramsMap.put(Constants.PARAM_MAIL_SUBJECT, subjectParamArray);
                List<String> emailIds = new ArrayList<>();
                List<StudentStandard> studentStandards = studentStandardRepository.getByStudentId(leaveApplication.getAppliedStudentId());
                if (studentStandards.size() == 0) {
                    return;
                }
                Standard standard = studentStandards.get(0).getStandard();
                emailIds.add(standard.getClassTeacher().getEmail());
                emailIds.add(standard.getSchoolInfo().getSchool().getPrimaryEmail());
                String smsBody = String.format("This is to inform you that %s has canceled the leave application. Visit %s for more.", paramsMap.get(Constants.PARAM_FULL_NAME), tinyUrl);
                if (info.getSendSMS()) {
                    smsService.sendSms(standard.getClassTeacher().getPrimaryPhone(), smsBody, info.getSmsSignature());
                }
                if (info.getSendEmail()) {
                    for (String email: emailIds) {
                        mailService.sendEmailFromTemplate(email, paramsMap, "mail/notification/leaveApplicationDeletionEmail", "email.notification.leave.application.deletion.title", info.getSmsSignature());
                    }
                }
                if (info.getSendPush()) {
                    // TODO
                }
            } else {
                titlePlaceholder = "Staff";
                subjectParamArray[1] = titlePlaceholder;
                paramsMap.put(Constants.PARAM_TITLE_PLACEHOLDER, titlePlaceholder);
                paramsMap.put(Constants.PARAM_MAIL_SUBJECT, subjectParamArray);
                String email = schoolInfo.getSchool().getPrimaryEmail();
                String phone = schoolInfo.getSchool().getPrimaryPhone();
                String smsBody = String.format("This is to inform you that %s has applied for leave. Visit %s for more.", paramsMap.get(Constants.PARAM_FULL_NAME), tinyUrl);
                if (info.getSendSMS()) {
                    smsService.sendSms(phone, smsBody, info.getSmsSignature());
                }
                if (info.getSendEmail()) {
                    mailService.sendEmailFromTemplate(email, paramsMap, "mail/notification/leaveApplicationDeletionEmail", "email.notification.leave.application.deletion.title", info.getSmsSignature());
                }
                if (info.getSendPush()) {
                    // TODO
                }
            }
        }
    }

    private void sendStaffAbsentNotification(Map<Long, Set<LocalDate>> staffIdAndDatesMap, NotificationInfo info) throws UnsupportedEncodingException {

        if (staffIdAndDatesMap != null) {
            Map paramsMap = new HashMap();
            paramsMap.put(Constants.PARAM_INSTITUTE_NAME, info.getInstituteName());
            paramsMap.put(Constants.PARAM_GREETING_PLACEHOLDER, "Staff");
            String[] subjectParamArray = new String[]{info.getSmsSignature(), ""};
            Map<Long, Staff> staffIdMap = new HashMap<>();
            List<Staff> staffList = staffRepository.findAllById(staffIdAndDatesMap.keySet());
            for (Staff staff: staffList) {
                if (staffIdMap == null) {
                    staffIdMap = new HashMap<>();
                }
                staffIdMap.put(staff.getId(), staff);
            }
            String smsBody = "This is to notify you that your attendance is marked absent on %s. Visit %s for more.";
            for (Long staffId: staffIdAndDatesMap.keySet()) {
                Staff staff = staffIdMap.get(staffId);
                String fullName = staff.getFirstName() + " " + staff.getLastName();
                paramsMap.put(Constants.PARAM_FULL_NAME, fullName);
                //TODO: add logic for tiny Urls
                String tinyUrl = "http://witcurve.com/tiny";
                paramsMap.put(Constants.PARAM_TINY_URL, tinyUrl);
                for (LocalDate date: staffIdAndDatesMap.get(staffId)) {
                    paramsMap.put(Constants.PARAM_DATE, date);
                    subjectParamArray[1] = date.toString();
                    paramsMap.put(Constants.PARAM_MAIL_SUBJECT, subjectParamArray);
                    if (info.getSendSMS()) {
                        smsService.sendSms(staff.getPrimaryPhone(), String.format(smsBody, date, tinyUrl), info.getSmsSignature());
                    }
                    if (info.getSendEmail()) {
                        mailService.sendEmailFromTemplate(staff.getEmail(), paramsMap, "mail/notification/staffAbsentNotificationEmail", "email.notification.staff.absent.title", info.getSmsSignature());
                    }
                    if (info.getSendPush()) {
                        //TODO:
                    }
                }
            }
        }
    }

    private void sendStudentAbsentNotification(Map<Long, Set<LocalDate>> studentIdAndDatesMap, NotificationInfo info) throws UnsupportedEncodingException {

        if (studentIdAndDatesMap != null) {
            Map paramsMap = new HashMap();
            paramsMap.put(Constants.PARAM_INSTITUTE_NAME, info.getInstituteName());
            paramsMap.put(Constants.PARAM_GREETING_PLACEHOLDER, "Parent");
            String[] subjectParamArray = new String[]{info.getSmsSignature(), "", ""};
            Map<Long, Student> studentIdMap = new HashMap<>();
            List<Student> studentList = studentRepository.findAllById(studentIdAndDatesMap.keySet());
            for (Student student: studentList) {
                studentIdMap.put(student.getId(), student);
            }
            String smsBody = "This is to notify you that your ward %s is absent on %s. Visit %s for more.";
            for (Long studentId: studentIdAndDatesMap.keySet()) {
                Student student = studentIdMap.get(studentId);
                if (Strings.isNullOrEmpty(student.getEmail())) {
                    continue;
                }
                String fullName = student.getFirstName() + " " + student.getLastName();
                paramsMap.put(Constants.PARAM_FULL_NAME, fullName);
                //TODO: add logic for tiny Urls
                String tinyUrl = "http://witcurve.com/tiny";
                paramsMap.put(Constants.PARAM_TINY_URL, tinyUrl);
                subjectParamArray[1] = fullName;
                for (LocalDate date: studentIdAndDatesMap.get(studentId)) {
                    paramsMap.put(Constants.PARAM_DATE, date);
                    subjectParamArray[2] = date.toString();
                    paramsMap.put(Constants.PARAM_MAIL_SUBJECT, subjectParamArray);
                    if (info.getSendSMS()) {
                        smsService.sendSms(student.getRegisteredMobileNumber(), String.format(smsBody, fullName, date, tinyUrl), info.getSmsSignature());
                    }
                    if (info.getSendEmail()) {
                        mailService.sendEmailFromTemplate(student.getEmail(), paramsMap, "mail/notification/studentAbsentNotificationEmail", "email.notification.student.absent.title", info.getSmsSignature());
                    }
                    if (info.getSendPush()) {
                        //TODO:
                    }
                }
            }
        }
    }

    private void sendSchoolEventNotification(SchoolInfo schoolInfo, List<Event> schoolEvents, NotificationInfo info) throws UnsupportedEncodingException {

        if (schoolEvents != null) {
            Map paramsMap = new HashMap();
            paramsMap.put(Constants.PARAM_INSTITUTE_NAME, info.getInstituteName());
            String[] subjectParamArray = new String[]{info.getSmsSignature()};
            paramsMap.put(Constants.PARAM_MAIL_SUBJECT, subjectParamArray);

            Set<String> allStudentMobileNos = studentStandardRepository.getActiveStudentPhoneNumbersBySchoolInfoId(schoolInfo.getId());
            Set<String> allStaffMobileNos = staffRepository.findActiveStaffPhoneNumbersInSchoolInfoId(schoolInfo.getId());
            Set<String> allStudentEmailIds = studentStandardRepository.getActiveStudentEmailsBySchoolInfoId(schoolInfo.getId());
            Set<String> allStaffEmailIds = staffRepository.findActiveStaffEmailsInSchoolInfoId(schoolInfo.getId());

            Map<Long, Set<String>> studentStandardMobileNosMap = null;
            Map<Long, Set<String>> studentStandardEmailIdsMap = null;
            Map<Long, Set<String>> staffStandardMobileNosMap = null;
            Map<Long, Set<String>> staffStandardEmailIdsMap = null;
            for (Event event: schoolEvents) {

                if (event.getStandard() != null) {
                    if (studentStandardMobileNosMap == null) {
                        studentStandardMobileNosMap = new HashMap<>();
                    }
                    if (studentStandardMobileNosMap.get(event.getStandard().getId()) == null) {
                        studentStandardMobileNosMap.put(event.getStandard().getId(), studentStandardRepository.getActiveStudentEmailsBySchoolInfoIdAndStandardIds(schoolInfo.getId(), Arrays.asList(event.getStandard().getId())));
                    }
                    if (studentStandardEmailIdsMap == null) {
                        studentStandardEmailIdsMap = new HashMap<>();
                    }
                    if (studentStandardEmailIdsMap.get(event.getStandard().getId()) == null) {
                        studentStandardEmailIdsMap.put(event.getStandard().getId(), studentStandardRepository.getActiveStudentEmailsBySchoolInfoIdAndStandardIds(schoolInfo.getId(), Arrays.asList(event.getStandard().getId())));
                    }
                    if (staffStandardMobileNosMap == null) {
                        staffStandardMobileNosMap = new HashMap<>();
                    }
                    if (staffStandardMobileNosMap.get(event.getStandard().getId()) == null) {
                        staffStandardMobileNosMap.put(event.getStandard().getId(), courseTeacherRepository.findActiveCourseTeacherPhoneNumbersByStandardId(event.getStandard().getId()));
                    }
                    if (staffStandardEmailIdsMap == null) {
                        staffStandardEmailIdsMap = new HashMap<>();
                    }
                    if (staffStandardEmailIdsMap.get(event.getStandard().getId()) == null) {
                        staffStandardEmailIdsMap.put(event.getStandard().getId(), courseTeacherRepository.findActiveCourseTeacherEmailIdsByStandardId(event.getStandard().getId()));
                    }
                }
                paramsMap.put(Constants.PARAM_EVENT_NAME, event.getName());
                paramsMap.put(Constants.PARAM_DATE, event.getDate());
                paramsMap.put(Constants.PARAM_GREETING_PLACEHOLDER, "User");
                String smsBody = "This is to notify you that %s is scheduled on %s. Visit %s for more.";
                if (info.getSendSMS()) {
                    for (String studentMobileNumber: (event.getStandard() == null ? allStudentMobileNos : studentStandardMobileNosMap.get(event.getStandard().getId()))) {
                        String tinyUrl = "http://witcurve.com/tiny";
                        smsService.sendSms(studentMobileNumber, String.format(smsBody, event.getName(), event.getDate(), tinyUrl), info.getSmsSignature());
                    }
                    for (String staffMobileNumber: (event.getStandard() == null ? allStaffMobileNos : staffStandardMobileNosMap.get(event.getStandard().getId()))) {
                        String tinyUrl = "http://witcurve.com/tiny";
                        smsService.sendSms(staffMobileNumber, String.format(smsBody, event.getName(), event.getDate(), tinyUrl), info.getSmsSignature());
                    }
                }
                if (info.getSendEmail()) {
                    for (String studentEmailId: (event.getStandard() == null ? allStudentEmailIds : studentStandardEmailIdsMap.get(event.getStandard().getId()))) {
                        String tinyUrl = "http://witcurve.com/tiny";
                        paramsMap.put(Constants.PARAM_TINY_URL, tinyUrl);
                        mailService.sendEmailFromTemplate(studentEmailId, paramsMap, "mail/notification/schoolEventNotificationEmail", "email.notification.school.event.title", info.getSmsSignature());
                    }
                    for (String staffEmailId: (event.getStandard() == null ? allStaffEmailIds : staffStandardEmailIdsMap.get(event.getStandard().getId()))) {
                        String tinyUrl = "http://witcurve.com/tiny";
                        paramsMap.put(Constants.PARAM_TINY_URL, tinyUrl);
                        mailService.sendEmailFromTemplate(staffEmailId, paramsMap, "mail/notification/schoolEventNotificationEmail", "email.notification.school.event.title", info.getSmsSignature());
                    }
                }
                if (info.getSendPush()) {
                    //TODO:
                }
            }
        }
    }

    private NotificationInfo getNotificationInfo(LeaveApplication leaveApplication, Boolean update) {
        if (leaveApplication.getAppliedStudent() != null) {
            if (update) {
                return getNotificationInfo(leaveApplication.getSchoolInfo(), ConfigFieldName.STUDENT_LEAVE_APPLICATION_MODIFIED);
            } else {
                return getNotificationInfo(leaveApplication.getSchoolInfo(), ConfigFieldName.STUDENT_LEAVE_APPLICATION_POSTED);
            }
        } else {
            if (update) {
                return getNotificationInfo(leaveApplication.getSchoolInfo(), ConfigFieldName.STAFF_LEAVE_APPLICATION_MODIFIED);
            } else {
                return getNotificationInfo(leaveApplication.getSchoolInfo(), ConfigFieldName.STAFF_LEAVE_APPLICATION_POSTED);
            }
        }
    }

    private Map getParamsMapForLeaveApplication(LeaveApplicationDTO leaveApplication, SchoolInfo schoolInfo, NotificationInfo info) {
        Map paramsMap = new HashMap();
        paramsMap.put(Constants.PARAM_INSTITUTE_NAME, info.getInstituteName());
        paramsMap.put(Constants.PARAM_GREETING_PLACEHOLDER, "Staff");
        String fullName = "";
        if (leaveApplication.getAppliedStudentId() != null) {
            fullName = leaveApplication.getStudentName();
            List<StudentStandard> studentStandards = studentStandardRepository.getByStudentId(leaveApplication.getAppliedStudentId());
            if (studentStandards.size() == 0) {
                return paramsMap;
            }
            Standard standard = studentStandards.get(0).getStandard();
            fullName += " of " + standard.getGrade() + " " + standard.getSection();
        } else if (leaveApplication.getAppliedStaffId() != null) {
            fullName = leaveApplication.getStaffName();
        }
        paramsMap.put(Constants.PARAM_FULL_NAME, fullName);
        return paramsMap;
    }

    private NotificationInfo getNotificationInfo(SchoolInfo schoolInfo, ConfigFieldName fieldName) {
        NotificationInfo notificationInfo = new NotificationInfo();
        List<ConfigSettings> configSettings = configSettingsRepository.getConfigSettingsBySchoolIdAndFieldName(schoolInfo.getSchool().getId(), fieldName);
        for (ConfigSettings configSetting: configSettings) {
            if (configSetting.getConfigType().equals(ConfigType.SMS_NOTIFICATION) && configSetting.getFieldValue().equals("TRUE")) {
                notificationInfo.setSendSMS(true);
            }
            if (configSetting.getConfigType().equals(ConfigType.EMAIL_NOTIFICATION) && configSetting.getFieldValue().equals("TRUE")) {
                notificationInfo.setSendEmail(true);
            }
            if (configSetting.getConfigType().equals(ConfigType.PUSH_NOTIFICATION) && configSetting.getFieldValue().equals("TRUE")) {
                notificationInfo.setSendPush(true);
            }
        }
        notificationInfo.setInstituteName(schoolInfo.getSchool().getInstitute().getName());
        notificationInfo.setSmsSignature(schoolInfo.getSchool().getInstitute().getSmsSignature());
        return notificationInfo;
    }
}

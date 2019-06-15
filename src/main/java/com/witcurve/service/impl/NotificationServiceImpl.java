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
    SmsService smsService;

    @Autowired
    MailService mailService;

    @Override
    @Async
    public void sendAbsentNotification(SchoolInfo schoolInfo, Map<Long, Set<LocalDate>> studentIdAndDatesMap, Map<Long, Set<LocalDate>> staffIdAndDatesMap) throws UnsupportedEncodingException {
        String instituteName = schoolInfo.getSchool().getInstitute().getName();
        String smsSignature = schoolInfo.getSchool().getInstitute().getSmsSignature();
        List<ConfigSettings> absentNotificationSetting = configSettingsRepository.getConfigSettingsBySchoolIdAndFieldName(schoolInfo.getSchool().getId(), ConfigFieldName.STAFF_OR_STUDENT_ABSENT);
        Boolean sendSMS = false;
        Boolean sendEmail = false;
        Boolean sendPush = false;
        for (ConfigSettings configSettings: absentNotificationSetting) {
            if (configSettings.getConfigType().equals(ConfigType.SMS_NOTIFICATION) && configSettings.getFieldValue().equals("TRUE")) {
                sendSMS = true;
            }
            if (configSettings.getConfigType().equals(ConfigType.EMAIL_NOTIFICATION) && configSettings.getFieldValue().equals("TRUE")) {
                sendEmail = true;
            }
            if (configSettings.getConfigType().equals(ConfigType.PUSH_NOTIFICATION) && configSettings.getFieldValue().equals("TRUE")) {
                sendPush = true;
            }
        }

        if (sendSMS || sendEmail || sendPush) {
            sendStudentAbsentNotification(studentIdAndDatesMap, instituteName, smsSignature, sendSMS, sendEmail, sendPush);
            sendStaffAbsentNotification(staffIdAndDatesMap, instituteName, smsSignature, sendSMS, sendEmail, sendPush);
        }
    }

    @Override
    @Async
    public void sendSchoolEventNotification(SchoolInfo schoolInfo, List<Event> schoolEvents) throws UnsupportedEncodingException {
        String instituteName = schoolInfo.getSchool().getInstitute().getName();
        String smsSignature = schoolInfo.getSchool().getInstitute().getSmsSignature();
        List<ConfigSettings> schoolEventNotificationSetting = configSettingsRepository.getConfigSettingsBySchoolIdAndFieldName(schoolInfo.getSchool().getId(), ConfigFieldName.SCHOOL_EVENT_POSTED);
        Boolean sendSMS = false;
        Boolean sendEmail = false;
        Boolean sendPush = false;
        for (ConfigSettings configSettings: schoolEventNotificationSetting) {
            if (configSettings.getConfigType().equals(ConfigType.SMS_NOTIFICATION) && configSettings.getFieldValue().equals("TRUE")) {
                sendSMS = true;
            }
            if (configSettings.getConfigType().equals(ConfigType.EMAIL_NOTIFICATION) && configSettings.getFieldValue().equals("TRUE")) {
                sendEmail = true;
            }
            if (configSettings.getConfigType().equals(ConfigType.PUSH_NOTIFICATION) && configSettings.getFieldValue().equals("TRUE")) {
                sendPush = true;
            }
        }
        if (sendSMS || sendEmail || sendPush) {
            sendSchoolEventNotification(schoolInfo, schoolEvents, instituteName, smsSignature, sendSMS, sendEmail, sendPush);
        }
    }

    @Override
    @Async
    public void sendLeaveApplicationSaveOrUpdateNotification(Long leaveApplicationId, Boolean update) throws UnsupportedEncodingException {
        LeaveApplication leaveApplication = leaveApplicationRepository.getOne(leaveApplicationId);
        String instituteName = leaveApplication.getSchoolInfo().getSchool().getInstitute().getName();
        String smsSignature = leaveApplication.getSchoolInfo().getSchool().getInstitute().getSmsSignature();
        Boolean sendSMS = false;
        Boolean sendEmail = false;
        Boolean sendPush = false;
        List<ConfigSettings> leaveApplicationNotificationSetting = null;
        if (leaveApplication.getAppliedStudent() != null) {
            if (update) {
                leaveApplicationNotificationSetting = configSettingsRepository.getConfigSettingsBySchoolIdAndFieldName(leaveApplication.getSchoolInfo().getSchool().getId(), ConfigFieldName.STUDENT_LEAVE_APPLICATION_MODIFIED);
            } else {
                leaveApplicationNotificationSetting = configSettingsRepository.getConfigSettingsBySchoolIdAndFieldName(leaveApplication.getSchoolInfo().getSchool().getId(), ConfigFieldName.STUDENT_LEAVE_APPLICATION_POSTED);
            }
        } else if (leaveApplication.getAppliedStaff() != null) {
            if (update) {
                leaveApplicationNotificationSetting = configSettingsRepository.getConfigSettingsBySchoolIdAndFieldName(leaveApplication.getSchoolInfo().getSchool().getId(), ConfigFieldName.STAFF_LEAVE_APPLICATION_MODIFIED);
            } else {
                leaveApplicationNotificationSetting = configSettingsRepository.getConfigSettingsBySchoolIdAndFieldName(leaveApplication.getSchoolInfo().getSchool().getId(), ConfigFieldName.STAFF_LEAVE_APPLICATION_POSTED);
            }
        }
        for (ConfigSettings configSettings: leaveApplicationNotificationSetting) {
            if (configSettings.getConfigType().equals(ConfigType.SMS_NOTIFICATION) && configSettings.getFieldValue().equals("TRUE")) {
                sendSMS = true;
            }
            if (configSettings.getConfigType().equals(ConfigType.EMAIL_NOTIFICATION) && configSettings.getFieldValue().equals("TRUE")) {
                sendEmail = true;
            }
            if (configSettings.getConfigType().equals(ConfigType.PUSH_NOTIFICATION) && configSettings.getFieldValue().equals("TRUE")) {
                sendPush = true;
            }
        }
        if (sendSMS || sendEmail || sendPush) {
            String tinyUrl = "http://witcurve.com/tiny";
            Map paramsMap = new HashMap();
            paramsMap.put(Constants.PARAM_INSTITUTE_NAME, instituteName);
            paramsMap.put(Constants.PARAM_GREETING_PLACEHOLDER, "Staff");
            paramsMap.put(Constants.PARAM_TINY_URL, tinyUrl);
            String[] subjectParamArray = new String[]{smsSignature, ""};
            if (leaveApplication.getAppliedStudent() != null) {
                List<String> emailIds = new ArrayList<>();
                String fullName = leaveApplication.getAppliedStudent().getFirstName() + " " + leaveApplication.getAppliedStudent().getLastName();
                List<StudentStandard> studentStandards = studentStandardRepository.getByStudentId(leaveApplication.getAppliedStudent().getId());
                if (studentStandards.size() == 0) {
                    return;
                }
                Standard standard = studentStandards.get(0).getStandard();
                emailIds.add(standard.getClassTeacher().getEmail());
                emailIds.add(standard.getSchoolInfo().getSchool().getPrimaryEmail());
                String phone = standard.getClassTeacher().getPrimaryPhone();
                fullName += " of " + standard.getGrade() + " " + standard.getSection();
                paramsMap.put(Constants.PARAM_FULL_NAME, fullName);
                if (update) {
                    String smsBody = String.format("This is to inform you that %s has modified leave application details. Visit %s for more.", fullName, tinyUrl);
                    subjectParamArray[1] = "Student";
                    paramsMap.put(Constants.PARAM_MAIL_SUBJECT, subjectParamArray);
                    if (sendSMS) {
                        smsService.sendSms(phone, smsBody, smsSignature);
                    }
                    if (sendEmail) {
                        for (String email: emailIds) {
                            mailService.sendEmailFromTemplate(email, paramsMap, "mail/notification/leaveApplicationModificationEmail", "email.notification.leave.application.modification.title", smsSignature);
                        }
                    }
                    if (sendPush) {
                        // TODO
                    }
                } else {
                    String smsBody = String.format("This is to inform you that %s has applied for leave. Visit %s for more.", fullName, tinyUrl);
                    subjectParamArray[1] = "student";
                    paramsMap.put(Constants.PARAM_MAIL_SUBJECT, subjectParamArray);
                    if (sendSMS) {
                        smsService.sendSms(phone, smsBody, smsSignature);
                    }
                    if (sendEmail) {
                        for (String email: emailIds) {
                            mailService.sendEmailFromTemplate(email, paramsMap, "mail/notification/leaveApplicationCreationEmail", "email.notification.leave.application.creation.title", smsSignature);
                        }
                    }
                    if (sendPush) {
                        // TODO
                    }
                }
            } else if (leaveApplication.getAppliedStaff() != null) {
                String fullName = leaveApplication.getAppliedStaff().getFirstName() + " " + leaveApplication.getAppliedStaff().getLastName();
                String email = leaveApplication.getSchoolInfo().getSchool().getPrimaryEmail();
                String phone = leaveApplication.getSchoolInfo().getSchool().getPrimaryPhone();
                paramsMap.put(Constants.PARAM_FULL_NAME, fullName);
                if (update) {
                    String smsBody = String.format("This is to inform you that %s has modified leave application details. Visit %s for more.", fullName, tinyUrl);
                    subjectParamArray[1] = "Staff";
                    paramsMap.put(Constants.PARAM_MAIL_SUBJECT, subjectParamArray);
                    if (sendSMS) {
                        smsService.sendSms(phone, smsBody, smsSignature);
                    }
                    if (sendEmail) {
                        mailService.sendEmailFromTemplate(email, paramsMap, "mail/notification/leaveApplicationModificationEmail", "email.notification.leave.application.modification.title", smsSignature);
                    }
                    if (sendPush) {
                        // TODO
                    }
                } else {
                    String smsBody = String.format("This is to inform you that %s has applied for leave. Visit %s for more.", fullName, tinyUrl);
                    subjectParamArray[1] = "staff";
                    paramsMap.put(Constants.PARAM_MAIL_SUBJECT, subjectParamArray);
                    if (sendSMS) {
                        smsService.sendSms(phone, smsBody, smsSignature);
                    }
                    if (sendEmail) {
                        mailService.sendEmailFromTemplate(email, paramsMap, "mail/notification/leaveApplicationCreationEmail", "email.notification.leave.application.creation.title", smsSignature);
                    }
                    if (sendPush) {
                        // TODO
                    }
                }
            }
        }
    }

    @Override
    @Async
    public void sendLeaveApplicationStatusNotification() throws UnsupportedEncodingException {

    }

    @Override
    @Async
    public void sendLeaveApplicationDeletionNotification() throws UnsupportedEncodingException {

    }

    private void sendStaffAbsentNotification(Map<Long, Set<LocalDate>> staffIdAndDatesMap, String instituteName, String smsSignature, Boolean sendSMS, Boolean sendEmail, Boolean sendPush) throws UnsupportedEncodingException {

        if (staffIdAndDatesMap != null) {
            Map paramsMap = new HashMap();
            paramsMap.put(Constants.PARAM_INSTITUTE_NAME, instituteName);
            paramsMap.put(Constants.PARAM_GREETING_PLACEHOLDER, "Staff");
            String[] subjectParamArray = new String[]{smsSignature, ""};
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
                    if (sendSMS) {
                        smsService.sendSms(staff.getPrimaryPhone(), String.format(smsBody, date, tinyUrl), smsSignature);
                    }
                    if (sendEmail) {
                        mailService.sendEmailFromTemplate(staff.getEmail(), paramsMap, "mail/notification/staffAbsentNotificationEmail", "email.notification.staff.absent.title", smsSignature);
                    }
                    if (sendPush) {
                        //TODO:
                    }
                }
            }
        }
    }

    private void sendStudentAbsentNotification(Map<Long, Set<LocalDate>> studentIdAndDatesMap, String instituteName, String smsSignature, Boolean sendSMS, Boolean sendEmail, Boolean sendPush) throws UnsupportedEncodingException {

        if (studentIdAndDatesMap != null) {
            Map paramsMap = new HashMap();
            paramsMap.put(Constants.PARAM_INSTITUTE_NAME, instituteName);
            paramsMap.put(Constants.PARAM_GREETING_PLACEHOLDER, "Parent");
            String[] subjectParamArray = new String[]{smsSignature, "", ""};
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
                    if (sendSMS) {
                        smsService.sendSms(student.getRegisteredMobileNumber(), String.format(smsBody, fullName, date, tinyUrl), smsSignature);
                    }
                    if (sendEmail) {
                        mailService.sendEmailFromTemplate(student.getEmail(), paramsMap, "mail/notification/studentAbsentNotificationEmail", "email.notification.student.absent.title", smsSignature);
                    }
                    if (sendPush) {
                        //TODO:
                    }
                }
            }
        }
    }

    private void sendSchoolEventNotification(SchoolInfo schoolInfo, List<Event> schoolEvents, String instituteName, String smsSignature, Boolean sendSMS, Boolean sendEmail, Boolean sendPush) throws UnsupportedEncodingException {

        if (schoolEvents != null) {
            Map paramsMap = new HashMap();
            paramsMap.put(Constants.PARAM_INSTITUTE_NAME, instituteName);
            String[] subjectParamArray = new String[]{smsSignature};
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
                if (sendSMS) {
                    for (String studentMobileNumber: (event.getStandard() == null ? allStudentMobileNos : studentStandardMobileNosMap.get(event.getStandard().getId()))) {
                        String tinyUrl = "http://witcurve.com/tiny";
                        smsService.sendSms(studentMobileNumber, String.format(smsBody, event.getName(), event.getDate(), tinyUrl), smsSignature);
                    }
                    for (String staffMobileNumber: (event.getStandard() == null ? allStaffMobileNos : staffStandardMobileNosMap.get(event.getStandard().getId()))) {
                        String tinyUrl = "http://witcurve.com/tiny";
                        smsService.sendSms(staffMobileNumber, String.format(smsBody, event.getName(), event.getDate(), tinyUrl), smsSignature);
                    }
                }
                if (sendEmail) {
                    for (String studentEmailId: (event.getStandard() == null ? allStudentEmailIds : studentStandardEmailIdsMap.get(event.getStandard().getId()))) {
                        String tinyUrl = "http://witcurve.com/tiny";
                        paramsMap.put(Constants.PARAM_TINY_URL, tinyUrl);
                        mailService.sendEmailFromTemplate(studentEmailId, paramsMap, "mail/notification/schoolEventNotificationEmail", "email.notification.school.event.title", smsSignature);
                    }
                    for (String staffEmailId: (event.getStandard() == null ? allStaffEmailIds : staffStandardEmailIdsMap.get(event.getStandard().getId()))) {
                        String tinyUrl = "http://witcurve.com/tiny";
                        paramsMap.put(Constants.PARAM_TINY_URL, tinyUrl);
                        mailService.sendEmailFromTemplate(staffEmailId, paramsMap, "mail/notification/schoolEventNotificationEmail", "email.notification.school.event.title", smsSignature);
                    }
                }
                if (sendPush) {
                    //TODO:
                }
            }
        }
    }
}

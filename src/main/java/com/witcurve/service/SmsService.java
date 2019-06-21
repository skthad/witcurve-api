package com.witcurve.service;

import com.google.common.base.Strings;
import com.witcurve.config.ApplicationProperties;
import com.witcurve.domain.Institute;
import com.witcurve.domain.SchoolInfo;
import com.witcurve.domain.StudentStandard;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.repository.SchoolInfoRepository;
import com.witcurve.repository.StaffRepository;
import com.witcurve.repository.StudentRepository;
import com.witcurve.repository.StudentStandardRepository;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.vm.SmsVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SmsService {
    private static final Logger log = LoggerFactory.getLogger("SmsService");

    @Autowired
    StudentStandardRepository studentStandardRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    SchoolInfoRepository schoolInfoRepository;

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    ApplicationProperties applicationProperties;

    private static String API_URL="http://api.msg91.com/api/sendhttp.php?";

    private static final String COUNTRY_CODE = "91";
    // route=4 is transactional
    private static final String ROUTE="4";

    @Async
    public void sendSms(String mobileNumber, String body, String smsSignature) throws UnsupportedEncodingException {

        URLConnection myURLConnection;
        URL myURL;
        BufferedReader reader;

        //Send SMS API
        //Prepare parameter string
        StringBuilder sbPostData= new StringBuilder(API_URL);
        sbPostData.append("authkey="+ applicationProperties.getSms().getAuthKey());
        sbPostData.append("&mobiles="+COUNTRY_CODE+mobileNumber);
        sbPostData.append("&message="+ URLEncoder.encode(body, "UTF-8"));
        sbPostData.append("&route="+ROUTE);
        sbPostData.append("&sender="+(Strings.isNullOrEmpty(smsSignature)? applicationProperties.getSms().getSenderId(): smsSignature));
        sbPostData.append("&country="+0);

        String finalApiUrl = sbPostData.toString();
        try
        {
            myURL = new URL(finalApiUrl);
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader= new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
            String response;
            while ((response = reader.readLine()) != null)
                System.out.println("Sms request response :"+response);

            reader.close();
        }
        catch (IOException e) {
            log.error("Error while sending otp as sms"+ e.getMessage());
            throw new WitcurveException("Error while sending otp as sms"+ e.getMessage());
        }
        log.info("Sms sent successfully to '{}'", COUNTRY_CODE+mobileNumber);
    }

    @Async
    public void sendBulkSMS(Long schoolInfoId, SmsVM smsVM) throws UnsupportedEncodingException {

        Optional<SchoolInfo> schoolInfo = schoolInfoRepository.findById(schoolInfoId);

        if (!schoolInfo.isPresent()) {
            throw new WitcurveException("No school Info found for ID: " + schoolInfo);
        }
        Set<String> recipientsList = new HashSet<>();
        if (!Strings.isNullOrEmpty(smsVM.getStudentList())) {
            if (smsVM.getStudentList().equals("-1")) {
                recipientsList.addAll(studentStandardRepository.getActiveStudentPhoneNumbersBySchoolInfoId(schoolInfoId));
            } else {
                List<Long> studentIds = Arrays.asList(smsVM.getStudentList().split(","))
                    .stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                recipientsList.addAll(studentRepository.getPhoneNumbersBySchoolInfoAndStudentIds(schoolInfoId, studentIds));
            }
        }
        if (!Strings.isNullOrEmpty(smsVM.getStaffList())) {
            if (smsVM.getStaffList().equals("-1")) {
                recipientsList.addAll(staffRepository.findActiveStaffPhoneNumbersInSchoolInfoId(schoolInfoId));
            } else {
                List<Long> staffIds = Arrays.asList(smsVM.getStaffList().split(","))
                    .stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                recipientsList.addAll(staffRepository.getPhoneNumbersBySchoolInfoAndStaffIds(schoolInfoId, staffIds));
            }
        }
        if (!Strings.isNullOrEmpty(smsVM.getStandardList())) {
            List<Long> standardIds = Arrays.asList(smsVM.getStandardList().split(","))
                .stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            recipientsList.addAll(studentStandardRepository.getActiveStudentPhoneNumbersBySchoolInfoIdAndStandardIds(schoolInfoId, standardIds));
        }
        if (!Strings.isNullOrEmpty(smsVM.getGradeList())) {
            List<Grade> gradeList;
            try {
                 gradeList = Arrays.asList(smsVM.getGradeList().split(","))
                    .stream().map(s -> Grade.valueOf(s.trim())).collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                throw new WitcurveException("Invalid grade value");
            }

            recipientsList.addAll(studentStandardRepository.getActiveStudentPhoneNumbersBySchoolInfoIdAndGradeList(schoolInfoId, gradeList));
        }
        if(recipientsList.size()!=0) {
            String mobileNumbers = String.join(",91", recipientsList);
            sendSms(mobileNumbers, smsVM.getBody(), schoolInfo.get().getSchool().getInstitute().getSmsSignature());
        } else {
            throw new WitcurveException("There are no phone records available for given recipient list");
        }
    }

    public void sendBulkIntroSMS(Long schoolInfoId, SmsVM smsVM) throws UnsupportedEncodingException, WitcurveException {
        Optional<SchoolInfo> schoolInfo = schoolInfoRepository.findById(schoolInfoId);

        if (!schoolInfo.isPresent()) {
            throw new WitcurveException("No school Info found for ID: " + schoolInfo);
        }
        Institute institute = schoolInfo.get().getSchool().getInstitute();
        List<StudentStandard> studentStandards = new ArrayList<>();
        if (!Strings.isNullOrEmpty(smsVM.getStudentList())) {
            if (smsVM.getStudentList().equals("-1")) {
                studentStandards = studentStandardRepository.getBySchoolInfoId(schoolInfoId);
            } else {
                List<Long> studentIds = Arrays.asList(smsVM.getStudentList().split(","))
                    .stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                studentStandards = studentStandardRepository.getByStudentIdsAndSchoolInfoId(schoolInfoId, studentIds);
            }
            for(StudentStandard studentStandard : studentStandards) {
                String body = "Hi Parent, \n" +
                    "Your username is '"+studentStandard.getStudent().getAdmissionId()+"' for the '"+institute.getMobileAppName()+"' app available at  \n" +
                    "Android : "+institute.getAndroidUrl()+"\n" +
                    "Apple:  "+institute.getIosUrl()+"\n" +
                    "\n" +
                    "Login with one time password (OTP) to create password.\n" +
                    "Kindly contact your school administration for assistance.";
                    schoolInfo.get().getSchool().getInstitute().getIosUrl();
                sendSms(studentStandard.getStudent().getRegisteredMobileNumber(),
                    body, schoolInfo.get().getSchool().getInstitute().getSmsSignature());
            }
        } else {
            throw new WitcurveException("This api currently works for only students");
        }
    }
}

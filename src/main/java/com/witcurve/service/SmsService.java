package com.witcurve.service;

import com.google.common.base.Strings;
import com.witcurve.config.ApplicationProperties;
import com.witcurve.domain.enumeration.Grade;
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
    StaffRepository staffRepository;

    @Autowired
    ApplicationProperties applicationProperties;

    private static String API_URL="http://api.msg91.com/api/sendhttp.php?";

    private static final String COUNTRY_CODE = "91";
    // route=4 is transactional
    private static final String ROUTE="4";

    @Async
    public void sendSms(String mobileNumber, String body) throws WitcurveException {

        URLConnection myURLConnection;
        URL myURL;
        BufferedReader reader;

        //Send SMS API
        //Prepare parameter string
        StringBuilder sbPostData= new StringBuilder(API_URL);
        sbPostData.append("authkey="+ applicationProperties.getSms().getAuthKey());
        sbPostData.append("&mobiles="+COUNTRY_CODE+mobileNumber);
        sbPostData.append("&message="+ URLEncoder.encode(body));
        sbPostData.append("&route="+ROUTE);
        sbPostData.append("&sender="+applicationProperties.getSms().getSenderId());
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
        log.info("Sms sent successfully");
    }

    public void sendBulkSMS(Long schoolInfoId, SmsVM smsVM) {
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
            List<Grade> gradeList= new ArrayList<>();
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
            sendSms(mobileNumbers, smsVM.getBody());
        } else {
            throw new WitcurveException("There are no phone records available for given recipient list");
        }
    }
}

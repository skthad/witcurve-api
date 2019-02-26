package com.witcurve.service;

import com.google.common.base.Strings;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SmsService {
    private static final Logger log = LoggerFactory.getLogger("SmsService");

    private static String apiUrl="http://api.msg91.com/api/sendhttp.php?";
    private static final String authkey = "228729Ax0VORwQvEQ25b9cfff6";
    private static final String senderId = "WCURVE";
    private static final String countryCode = "91";
    // route=4 is transactional
    private static final String route="4";

    @Autowired
    StudentStandardRepository studentStandardRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StaffRepository staffRepository;

    @Async
    public static void sendSms(String mobileNumber, String body) throws WitcurveException {

        URLConnection myURLConnection;
        URL myURL;
        BufferedReader reader;

        //Send SMS API
        //Prepare parameter string
        StringBuilder sbPostData= new StringBuilder(apiUrl);
        sbPostData.append("authkey="+authkey);
        sbPostData.append("&mobiles="+countryCode+mobileNumber);
        sbPostData.append("&message="+ body);
        sbPostData.append("&route="+route);
        sbPostData.append("&sender="+senderId);
        sbPostData.append("&country="+0);

        apiUrl = sbPostData.toString();
        try
        {
            myURL = new URL(apiUrl);
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
                recipientsList.addAll(studentRepository.getPhoneNumbersByStudentIds(studentIds));
            }
        }
        if (!Strings.isNullOrEmpty(smsVM.getStaffList())) {
            if (smsVM.getStaffList().equals("-1")) {
                recipientsList.addAll(staffRepository.findStaffPhoneNumbersInSchoolInfoId(schoolInfoId));
            } else {
                List<Long> staffIds = Arrays.asList(smsVM.getStaffList().split(","))
                    .stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                recipientsList.addAll(staffRepository.getPhoneNumbersByStaffIds(staffIds));
            }
        }
        if (!Strings.isNullOrEmpty(smsVM.getStandardList())) {
            List<Long> standardIds = Arrays.asList(smsVM.getStandardList().split(","))
                .stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            recipientsList.addAll(studentStandardRepository.getActiveStudentPhoneNumbersBySchoolInfoIdAndStandardIds(schoolInfoId, standardIds));
        }
        if (!Strings.isNullOrEmpty(smsVM.getGradeList())) {
            List<String> gradeList = Arrays.asList(smsVM.getGradeList().split(","))
                .stream().map(s -> s.trim()).collect(Collectors.toList());
            recipientsList.addAll(studentStandardRepository.getActiveStudentPhoneNumbersBySchoolInfoIdAndGradeList(schoolInfoId, gradeList));
        }
        String mobileNumbers = String.join(",91", recipientsList);
        sendSms(mobileNumbers, smsVM.getBody());
    }
}

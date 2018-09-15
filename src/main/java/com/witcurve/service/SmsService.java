package com.witcurve.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

@Service
public class SmsService {
    private static final Logger log = LoggerFactory.getLogger("SmsService");

    private static String apiUrl="http://api.msg91.com/api/sendhttp.php?";
    private static final String authkey = "228729Ax0VORwQvEQ25b9cfff6";
    private static final String senderId = "WITCURVE";
    private static final String countryCode = "91";
    // route=4 is transactional
    private static final String route="4";
    private static final String messageTemplate = "Hello User, Your OTP for logging in to Witcurve is ";
    public static boolean sendSms(String mobileNumber,String otp)
    {

        URLConnection myURLConnection=null;
        URL myURL=null;
        BufferedReader reader=null;

        //Send SMS API
        //Prepare parameter string
        StringBuilder sbPostData= new StringBuilder(apiUrl);
        sbPostData.append("authkey="+authkey);
        sbPostData.append("&mobiles="+countryCode+mobileNumber);
        sbPostData.append("&message="+messageTemplate+" "+otp);
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
                System.out.println(response);

            reader.close();
        }
        catch (IOException e)
        {
            log.error("Error while sending otp as sms"+ e.getMessage());
            return false;

        }
        return true;
    }
}

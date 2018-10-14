package com.witcurve.service.util;

import com.witcurve.domain.enumeration.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhoneNumberUtil {

    private static final Logger logger = LoggerFactory.getLogger("PhoneNumberUtil");

    private static final ArrayList<Integer> UN_MARK_POSNS = new ArrayList<Integer>(
        Arrays.asList(0,1,8,9));

    public static List<String> maskePhoneNumber(List<String> numbers) {
        List<String> result = new ArrayList<>();
        for(String number : numbers) {
            StringBuilder numberString = new StringBuilder(number);
            for(Integer i=0; i<9; i++) {
                if(!UN_MARK_POSNS.contains(i) ) {
                    numberString.setCharAt(i, 'x');
                }
            }
            result.add(number);
        }
        return result;
    }
}

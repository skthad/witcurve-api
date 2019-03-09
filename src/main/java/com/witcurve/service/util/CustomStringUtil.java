package com.witcurve.service.util;

import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class CustomStringUtil {

    private static final Logger logger = LoggerFactory.getLogger("CustomStringUtil");

    public static String getCustomString(Object entity) throws WitcurveException {
        if(entity == null) {
            return null;
        }
        String result = "";
        result += entity.getClass().getName()+"{";
        for(int i =0; i<entity.getClass().getFields().length;i++) {
            result += ", "+entity.getClass().getFields()[i]+"='";


        }
        return result;

    }
}

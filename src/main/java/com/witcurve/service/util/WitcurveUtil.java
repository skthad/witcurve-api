package com.witcurve.service.util;


import com.witcurve.web.rest.errors.WitcurveException;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.time.Instant;

public class WitcurveUtil {

    private static final Logger log = LoggerFactory.getLogger(WitcurveUtil.class);

    public static LocalDate getLocalDate(String localDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(WitCurveConstants.DEFAULT_DATE_FORMAT);
        return LocalDate.parse(localDate, formatter);
    }

    public static <T> Boolean  isObjectEmpty(T t) throws IllegalAccessException, InvocationTargetException {
        Set<Method> getters = ReflectionUtils.getAllMethods(t.getClass(),
            ReflectionUtils.withModifier(Modifier.PUBLIC), ReflectionUtils.withPrefix("get"));
        for(Method getterMethod : getters) {
            if(getterMethod.invoke(t) != null) {
                if(!getterMethod.invoke(t).toString().replaceAll("\\s", "").isEmpty()) {
                    return false;
                }
            }
        }
        return  true;
    }

    public static File getFile(MultipartFile file) throws WitcurveException {
        File convFile;
        try {
            String tmpLocation = System.getProperty("java.io.tmpdir")+Instant.now();
            File tmpDirectory = new File(tmpLocation);
            if(!tmpDirectory.exists()) {
                tmpDirectory.mkdir();
            }
            convFile = new File( tmpLocation+ File.separator+file.getOriginalFilename());
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
            return convFile;
        } catch(IOException e) {
            log.debug("Error while reading file : {}",e.getMessage());
            throw new WitcurveException("Error while reading the file, please check if the file is in correct format.");
        }
    }

    public static Double roundToTwoDecimal(Double value) {
        return (double) Math.round(value * 100) / 100;
    }

}

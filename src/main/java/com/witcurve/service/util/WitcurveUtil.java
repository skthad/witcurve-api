package com.witcurve.service.util;


import com.witcurve.web.rest.errors.WitcurveException;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WitcurveUtil {
    private static final Logger log = LoggerFactory.getLogger(WitcurveUtil.class);

    public static LocalDate getLocalDate(String localDate, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(localDate, formatter);
    }

    public static void correctDateFormat(LocalDate startDate, LocalDate endDate) throws WitcurveException {
        if ((startDate == null) ^ (endDate == null)) {
            throw new WitcurveException("Dude send the both dates brah! >:(");
        }
        if (startDate.isAfter(endDate)) {
            throw new WitcurveException("Come on bro! you know start date cannot be before end date");
        }
    }

    public static <T> Boolean isObjectEmpty(T t) throws IllegalAccessException, InvocationTargetException {
        Set<Method> getters = ReflectionUtils.getAllMethods(t.getClass(),
            ReflectionUtils.withModifier(Modifier.PUBLIC), ReflectionUtils.withPrefix("get"));
        for (Method getterMethod : getters) {
            if (getterMethod.invoke(t) != null) {
                if (!getterMethod.invoke(t).toString().replaceAll("\\s", "").isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static File getFile(MultipartFile file) throws WitcurveException {
        File convFile;
        try {
            String tmpLocation = System.getProperty("java.io.tmpdir") + File.separator + Instant.now().getEpochSecond();
            File tmpDirectory = new File(tmpLocation);
            if (!tmpDirectory.exists()) {
                tmpDirectory.mkdir();
            }
            convFile = new File(tmpLocation + File.separator + file.getOriginalFilename());
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
            return convFile;
        } catch (IOException e) {
            log.debug("Error while reading file : {}", e.getMessage());
            throw new WitcurveException("Error while reading the file, please check if the file is in correct format.");
        }
    }

    public static Resource getResourceFromFile(File file) {
        try {
            return new InputStreamResource(new FileInputStream(file));
        } catch (IOException e) {
            log.debug("Error while reading contents : {}",e.getMessage());
            throw new WitcurveException("There was a problem generating resource");
        }

    }

    public static File createTempFile(String name) throws WitcurveException {
        String directoryPath;
        if (name == null || name.isEmpty()) {
            directoryPath = System.getProperty("java.io.tmpdir") + File.separator + Instant.now().getEpochSecond() + ".tmp";
        } else {
            directoryPath = System.getProperty("java.io.tmpdir") + File.separator + name;
        }
        File file = new File(directoryPath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            log.debug("Error while writing temp file : {}", e.getMessage());
            throw new WitcurveException("Error while creating temp file");
        }

        return file;
    }

    public static String formatDouble(double d) {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }



    public static List<Long> convertBigIntToLong(List<BigInteger> list) {
        List<Long> result = new ArrayList<>();
        for (BigInteger num : list) {
            result.add(num.longValue());
        }
        return result;
    }

    public static Double roundToTwoDecimal(Double value) {
        if(value == null){
            return null;
        }
        return (double) Math.round(value * 100) / 100;
    }

    public static String format(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(WitCurveConstants.DEFAULT_IMPORT_DATE_FORMAT));
    }

    public static String timeFormat(String str) {
        if (str == null) {
            return null;
        }
        String s1 = str.substring(0, 2);
        String s2 = str.substring(2, 4);

        String time = s1 + ":" + s2;//"22:18:00";
        return LocalTime.parse(time).format(DateTimeFormatter.ofPattern("h:mma"));
    }

    public static String replacePlaceHolder(Map<String, String> variableMap, String message) {
        return variableMap.entrySet().stream().reduce(message, (s, e) -> s.replace("{{" + e.getKey() + "}}", e.getValue()),
            (s, s2) -> s);
    }

}



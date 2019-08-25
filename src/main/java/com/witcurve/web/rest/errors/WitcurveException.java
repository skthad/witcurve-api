package com.witcurve.web.rest.errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WitcurveException extends RuntimeException{

    private final Logger log  = LoggerFactory.getLogger(WitcurveException.class);

    public WitcurveException(String message) {
        super(message);
    }

    public WitcurveException(String message, Exception e) {
        log.error("Witcurve Exception message : {}",e.getMessage());
        e.printStackTrace();
        super.getMessage();
    }

}

package com.witcurve.service.impl;

import com.witcurve.service.UserContextService;
import com.witcurve.service.UserService;
import com.witcurve.service.dto.UserContextDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserContextServiceImpl implements UserContextService {

    private final Logger log  = LoggerFactory.getLogger(StaffServiceImpl.class);

    @Autowired
    UserService userService;

    @Override
    public UserContextDTO getCurrentUserContext() throws WitcurveException {
        return null;
    }
}

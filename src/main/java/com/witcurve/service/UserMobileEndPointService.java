package com.witcurve.service;

import com.witcurve.service.dto.UserMobileEndPointDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface UserMobileEndPointService {

    UserMobileEndPointDTO addEndPoint(UserMobileEndPointDTO userMobileEndPointDTO);

    UserMobileEndPointDTO getByUserMobileEndPointId(Long userMobileEndPointId) throws WitcurveException;

    List<UserMobileEndPointDTO> findAll();

    void deleteEndPoint(Long userId, String deviceToken);
}

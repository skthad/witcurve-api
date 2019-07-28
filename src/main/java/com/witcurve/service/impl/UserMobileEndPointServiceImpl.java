package com.witcurve.service.impl;

import com.witcurve.domain.UserMobileEndPoint;
import com.witcurve.repository.UserMobileEndPointRepository;
import com.witcurve.service.UserMobileEndPointService;
import com.witcurve.service.dto.UserMobileEndPointDTO;
import com.witcurve.service.mapper.UserMobileEndPointMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserMobileEndPointServiceImpl implements UserMobileEndPointService {

    @Autowired
    UserMobileEndPointMapper userMobileEndPointMapper;

    @Autowired
    UserMobileEndPointRepository userMobileEndPointRepository;

    public UserMobileEndPointDTO addEndPoint(UserMobileEndPointDTO userMobileEndPointDTO) {
        UserMobileEndPoint userMobileEndPoint = userMobileEndPointMapper.toEntity(userMobileEndPointDTO);
        userMobileEndPointRepository.save(userMobileEndPoint);
        return userMobileEndPointMapper.toDto(userMobileEndPoint);
    }

    public UserMobileEndPointDTO getByUserMobileEndPointId(Long userMobileEndPointId) throws WitcurveException {
        Optional<UserMobileEndPoint> userMobileEndPoint = userMobileEndPointRepository.findById(userMobileEndPointId);
        if(!userMobileEndPoint.isPresent()) {
            throw new WitcurveException("No userMobileEndPoint is found with given id");
        }
        return userMobileEndPointMapper.toDto(userMobileEndPoint.get());
    }

    public List<UserMobileEndPointDTO> findAll() {
        return userMobileEndPointMapper.toDto(userMobileEndPointRepository.findAll());
    }

    public void deleteEndPoint(Long userId, String deviceToken) {
        userMobileEndPointRepository.deleteByUserIdAndToken(userId, deviceToken);
    }
}

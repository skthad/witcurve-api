package com.witcurve.service.impl;

import com.witcurve.domain.TopicRecord;
import com.witcurve.domain.UserMobileEndPoint;
import com.witcurve.domain.enumeration.TopicType;
import com.witcurve.repository.TopicRecordRepository;
import com.witcurve.repository.UserMobileEndPointRepository;
import com.witcurve.service.SchoolInfoService;
import com.witcurve.service.SnsService;
import com.witcurve.service.UserMobileEndPointService;
import com.witcurve.service.dto.UserMobileEndPointDTO;
import com.witcurve.service.mapper.UserMobileEndPointMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserMobileEndPointServiceImpl implements UserMobileEndPointService {

    Logger log = LoggerFactory.getLogger(UserMobileEndPointServiceImpl.class);

    @Autowired
    UserMobileEndPointMapper userMobileEndPointMapper;

    @Autowired
    UserMobileEndPointRepository userMobileEndPointRepository;

    @Autowired
    SnsService snsService;

    @Autowired
    TopicRecordRepository topicRecordRepository;

    @Autowired
    SchoolInfoService schoolInfoService;

    public UserMobileEndPointDTO addEndPoint(UserMobileEndPointDTO userMobileEndPointDTO) {
        log.debug("Add user mobile end point : {}", userMobileEndPointDTO);
        UserMobileEndPoint existingUserMobileEndPoint = userMobileEndPointRepository.findByUserIdAndToken(userMobileEndPointDTO.getUserId(), userMobileEndPointDTO.getDeviceToken());
        if(existingUserMobileEndPoint != null) {
            userMobileEndPointDTO = userMobileEndPointMapper.toDto(existingUserMobileEndPoint);
        }
        Long schoolInfoId = schoolInfoService.getSchoolInfoIdByUserId(userMobileEndPointDTO.getUserId());
        String schoolInfoTopicSubscriptionArn = null;
        String globalSubscriptionArn = null;
        String endPoint = snsService.createEndPointWithToken(userMobileEndPointDTO.getDeviceToken());
        if(schoolInfoId != null) {
            //add it to school info based topic
            TopicRecord topicRecord = topicRecordRepository.findBySchoolInfoId(schoolInfoId);
            if(topicRecord != null) {
                schoolInfoTopicSubscriptionArn = snsService.addSubscription(topicRecord.getTopicEndPoint(), endPoint);
            }
        }
        //add it to global topic
        List<TopicRecord> topicRecords = topicRecordRepository.findByType(TopicType.GLOBAL);
        if(!topicRecords.isEmpty()) {
            globalSubscriptionArn = snsService.addSubscription(topicRecords.get(0).getTopicEndPoint(), endPoint);
        }
        UserMobileEndPoint userMobileEndPoint = userMobileEndPointMapper.toEntity(userMobileEndPointDTO);
        userMobileEndPoint.setEndPoint(endPoint);
        userMobileEndPoint.setSchoolInfoSubscriptionEndPoint(schoolInfoTopicSubscriptionArn);
        userMobileEndPoint.setGlobalSubscriptionEndPoint(globalSubscriptionArn);
        userMobileEndPointRepository.save(userMobileEndPoint);
        List<UserMobileEndPoint> userMobileEndPoints = userMobileEndPointRepository.findByUserId(userMobileEndPoint.getUser().getId());
        if(userMobileEndPoints.size() > 3) {
            for(int i=3; i<userMobileEndPoints.size(); i++) {
                deleteEndPoint(userMobileEndPoint.getUser().getId(), userMobileEndPoint.getDeviceToken());
            }
        }
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
        Long schoolInfoId = schoolInfoService.getSchoolInfoIdByUserId(userId);
        UserMobileEndPoint userMobileEndPoint = userMobileEndPointRepository.findByUserIdAndToken(userId, deviceToken);
        if(userMobileEndPoint == null) {
            throw new WitcurveException("There is no user mobile end point record with this device token for this user");
        }
        userMobileEndPointRepository.deleteByUserIdAndToken(userId, deviceToken);
        if(userMobileEndPointRepository.getTokenCount(deviceToken).equals(0)) {
            snsService.unSubscribe(userMobileEndPoint.getSchoolInfoSubscriptionEndPoint());
            snsService.unSubscribe(userMobileEndPoint.getGlobalSubscriptionEndPoint());
            snsService.deleteEndpoint(userMobileEndPoint.getEndPoint());
        }
    }
}

package com.witcurve.service.impl;

import com.witcurve.domain.TopicRecord;
import com.witcurve.domain.User;
import com.witcurve.domain.UserMobileEndPoint;
import com.witcurve.domain.enumeration.TopicType;
import com.witcurve.domain.enumeration.UserType;
import com.witcurve.repository.StudentStandardRepository;
import com.witcurve.repository.TopicRecordRepository;
import com.witcurve.repository.UserMobileEndPointRepository;
import com.witcurve.repository.UserRepository;
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

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudentStandardRepository studentStandardRepository;

    public UserMobileEndPointDTO addEndPoint(UserMobileEndPointDTO userMobileEndPointDTO) {
        log.debug("Add user mobile end point : {}", userMobileEndPointDTO);
        Optional<User> user = userRepository.findById(userMobileEndPointDTO.getUserId());
        if (!user.isPresent()) {
            throw new WitcurveException("No user is present with user id : " + userMobileEndPointDTO.getUserId());
        }
        UserMobileEndPoint existingUserMobileEndPoint = userMobileEndPointRepository.findByUserIdAndToken(userMobileEndPointDTO.getUserId(), userMobileEndPointDTO.getDeviceToken());
        if (existingUserMobileEndPoint != null) {
            userMobileEndPointDTO = userMobileEndPointMapper.toDto(existingUserMobileEndPoint);
        }
        Long schoolInfoId = schoolInfoService.getSchoolInfoIdByUserId(userMobileEndPointDTO.getUserId());
        String schoolInfoTopicSubscriptionArn = null;
        String globalSubscriptionArn = null;
        String standardSubscriptionArn = null;
        String staffSchoolInfoSubscriptionArn = null;

        String endPoint = snsService.createEndPointWithToken(userMobileEndPointDTO.getDeviceToken());
        if (schoolInfoId != null) {
            //add it to school info based topic
            List<TopicRecord> topicRecord = topicRecordRepository.findByTypeAndSchoolInfoId(TopicType.SCHOOL_INFO, schoolInfoId);
            if (!topicRecord.isEmpty()) {
                schoolInfoTopicSubscriptionArn = snsService.addSubscription(topicRecord.get(0).getTopicEndPoint(), endPoint);
            }
        }
        //add it to global topic
        List<TopicRecord> topicRecords = topicRecordRepository.findByType(TopicType.GLOBAL);
        if (!topicRecords.isEmpty()) {
            globalSubscriptionArn = snsService.addSubscription(topicRecords.get(0).getTopicEndPoint(), endPoint);
        }
        //add it to staff_Info or standard topic based on userId
        if (user.get().getType().equals(UserType.PARENT)) {
            Long standardId = studentStandardRepository.getStandardIdByUserId(userMobileEndPointDTO.getUserId());
            List<TopicRecord> topicRecord = topicRecordRepository.findByStandardId(standardId);
            if (!topicRecord.isEmpty()) {
                standardSubscriptionArn = snsService.addSubscription(topicRecord.get(0).getTopicEndPoint(), endPoint);
            }
        } else if (user.get().getType().equals(UserType.TEACHING_STAFF)) {
            List<TopicRecord> topicRecord = topicRecordRepository.findByTypeAndSchoolInfoId(TopicType.STAFF_SCHOOL_INFO, schoolInfoId);
            if (!topicRecords.isEmpty()) {
                staffSchoolInfoSubscriptionArn = snsService.addSubscription(topicRecord.get(0).getTopicEndPoint(), endPoint);
            }
        }
        UserMobileEndPoint userMobileEndPoint = userMobileEndPointMapper.toEntity(userMobileEndPointDTO);
        userMobileEndPoint.setEndPoint(endPoint);
        userMobileEndPoint.setSchoolInfoSubscriptionEndPoint(schoolInfoTopicSubscriptionArn);
        userMobileEndPoint.setGlobalSubscriptionEndPoint(globalSubscriptionArn);
        userMobileEndPoint.setStaffInfoSubscriptionEndPoint(staffSchoolInfoSubscriptionArn);
        userMobileEndPoint.setStandardSubscriptionEndPoint(standardSubscriptionArn);
        userMobileEndPointRepository.save(userMobileEndPoint);
        List<UserMobileEndPoint> userMobileEndPoints = userMobileEndPointRepository.findByUserId(userMobileEndPoint.getUser().getId());
        if (userMobileEndPoints.size() > 3) {
            for (int i = 3; i < userMobileEndPoints.size(); i++) {
                deleteEndPoint(userMobileEndPoints.get(i).getUser().getId(), userMobileEndPoints.get(i).getDeviceToken());
            }
        }
        return userMobileEndPointMapper.toDto(userMobileEndPoint);
    }

    public UserMobileEndPointDTO getByUserMobileEndPointId(Long userMobileEndPointId) throws WitcurveException {
        Optional<UserMobileEndPoint> userMobileEndPoint = userMobileEndPointRepository.findById(userMobileEndPointId);
        if (!userMobileEndPoint.isPresent()) {
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
        if (userMobileEndPoint == null) {
            throw new WitcurveException("There is no user mobile end point record with this device token for this user");
        }
        userMobileEndPointRepository.deleteByUserIdAndToken(userId, deviceToken);
        if (userMobileEndPointRepository.getTokenCount(deviceToken).equals(0)) {
            snsService.deleteEndpoint(userMobileEndPoint.getEndPoint());
        }
        if (userMobileEndPoint.getStaffInfoSubscriptionEndPoint() != null) {
            if (userMobileEndPointRepository.getCountByStaffInfoSubscriptionEndPoint(userMobileEndPoint.getStaffInfoSubscriptionEndPoint()).equals(0)) {
                snsService.unSubscribe(userMobileEndPoint.getStaffInfoSubscriptionEndPoint());
            }
        }
        if (userMobileEndPoint.getStandardSubscriptionEndPoint() != null) {
            if (userMobileEndPointRepository.getCountByStandardSubscriptionEndPoint(userMobileEndPoint.getStandardSubscriptionEndPoint()).equals(0)) {
                snsService.unSubscribe(userMobileEndPoint.getStandardSubscriptionEndPoint());
            }
        }
        if (userMobileEndPoint.getSchoolInfoSubscriptionEndPoint() != null) {
            if (userMobileEndPointRepository.getCountBySchoolInfoSubscriptionEndPoint(userMobileEndPoint.getSchoolInfoSubscriptionEndPoint()).equals(0)) {
                snsService.unSubscribe(userMobileEndPoint.getSchoolInfoSubscriptionEndPoint());
            }
        }
        if (userMobileEndPoint.getGlobalSubscriptionEndPoint() != null) {
            if (userMobileEndPointRepository.getCountByGlobalSubscriptionEndPoint(userMobileEndPoint.getGlobalSubscriptionEndPoint()).equals(0)) {
                snsService.unSubscribe(userMobileEndPoint.getGlobalSubscriptionEndPoint());
            }
        }
    }
}

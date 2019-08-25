package com.witcurve.service.impl;

import com.witcurve.domain.SchoolInfo;
import com.witcurve.domain.TopicRecord;
import com.witcurve.domain.enumeration.TopicType;
import com.witcurve.repository.TopicRecordRepository;
import com.witcurve.service.SnsService;
import com.witcurve.service.TopicRecordService;
import com.witcurve.service.dto.TopicRecordDTO;
import com.witcurve.service.mapper.TopicRecordMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TopicRecordServiceImpl implements TopicRecordService {

    Logger log = LoggerFactory.getLogger(TopicRecordServiceImpl.class);

    @Autowired
    TopicRecordMapper topicRecordMapper;

    @Autowired
    TopicRecordRepository topicRecordRepository;

    @Autowired
    SnsService snsService;

    public TopicRecordDTO addTopic(TopicType type, Long schoolInfoId) {
        log.debug("Request to add topic of type : {} and for schoolInfoId : {}", type, schoolInfoId);
        TopicRecord topicRecord = new TopicRecord();
        SchoolInfo schoolInfo = new SchoolInfo();
        if(type.equals(TopicType.GLOBAL)) {
            List<TopicRecord> topicRecords = topicRecordRepository.findByType(type);
            if(topicRecords.size() != 0) {
                throw new WitcurveException("There can only be only one Global type record and it already exists");
            }
            topicRecord.setType(type);
            String topicArn = snsService.createTopic(type.toString());
            topicRecord.setTopicEndPoint(topicArn);
            topicRecord = topicRecordRepository.save(topicRecord);

        } else {
            TopicRecord existingTopicRecord = topicRecordRepository.findBySchoolInfoId(schoolInfoId);
            if(schoolInfoId == null) {
                throw new WitcurveException("SchoolInfo Id is required for type : "+ type);
            }
            if(existingTopicRecord != null) {
                throw new WitcurveException("There already exists a topic record for this school info");
            }
            topicRecord.setType(type);
            schoolInfo.setId(schoolInfoId);
            topicRecord.setSchoolInfo(schoolInfo);
            String topicArn = snsService.createTopic(type.toString()+"-"+schoolInfoId.toString());
            topicRecord.setTopicEndPoint(topicArn);
            topicRecord = topicRecordRepository.save(topicRecord);
        }
        return topicRecordMapper.toDto(topicRecord);
    }

    public List<TopicRecordDTO> findAllTopicRecords() {
        log.debug("Request to find all topic records");
        return topicRecordMapper.toDto(topicRecordRepository.findAll());
    }

    public TopicRecordDTO findTopicRecordBySchoolInfoId(Long schoolInfoId) throws WitcurveException {
        log.debug("Request to find TopicRecord for school info with id : {}",schoolInfoId);
        TopicRecord topicRecord = topicRecordRepository.findBySchoolInfoId(schoolInfoId);
        if(topicRecord == null) {
            throw new WitcurveException("Topic Record doesn't exists for this school info with id : " + schoolInfoId);
        }
        return topicRecordMapper.toDto(topicRecord);

    }

    //TODO Delete topic can be done from aws for now


}

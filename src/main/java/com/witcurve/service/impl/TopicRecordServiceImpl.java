package com.witcurve.service.impl;

import com.witcurve.domain.SchoolInfo;
import com.witcurve.domain.Standard;
import com.witcurve.domain.TopicRecord;
import com.witcurve.domain.enumeration.TopicType;
import com.witcurve.repository.StandardRepository;
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
    StandardRepository standardRepository;

    @Autowired
    SnsService snsService;

    public TopicRecordDTO addTopic(TopicType type, Long schoolInfoId,Long standardId) {
        log.debug("Request to add topic of type : {} and for schoolInfoId : {}", type, schoolInfoId);
        TopicRecord topicRecord = new TopicRecord();
        SchoolInfo schoolInfo = new SchoolInfo();
        Standard standard=new Standard();

        if(type.equals(TopicType.GLOBAL)) {
            List<TopicRecord> topicRecords = topicRecordRepository.findByType(type);
            if(topicRecords.size() != 0) {
                throw new WitcurveException("There can only be only one Global type record and it already exists");
            }
            topicRecord.setType(type);
            String topicArn = snsService.createTopic(type.toString());
            topicRecord.setTopicEndPoint(topicArn);
            topicRecord = topicRecordRepository.save(topicRecord);

        } else if(type.equals(TopicType.STANDARD)){
            if(standardId==null){
                throw new WitcurveException("Standard Id is required for type : "+ type);
            }
            List<TopicRecord> topicRecords = topicRecordRepository.findByStandardId(standardId);
            if(!topicRecords.isEmpty()){
                throw new WitcurveException("There is already a TopicRecord exists with given standardId");
            }else if(topicRecords.size()>1){
                throw new WitcurveException("more than one TopicRecord exists with given standardId");
            }
            topicRecord.setType(type);
            standard.setId(standardId);
            topicRecord.setStandard(standard);
            String topicArn = snsService.createTopic(type.toString()+"-"+standardId.toString());
            topicRecord.setTopicEndPoint(topicArn);
            topicRecord = topicRecordRepository.save(topicRecord);

        }else if(type.equals(TopicType.STAFF_SCHOOL_INFO)){
            List<TopicRecord> topicRecords=topicRecordRepository.findByTypeAndSchoolInfoId(TopicType.STAFF_SCHOOL_INFO,schoolInfoId);
            if(!topicRecords.isEmpty()){
                throw new WitcurveException("There is already a TopicRecord exists for type :"+type);
            }else if(topicRecords.size()>1){
                throw new WitcurveException("more than one TopicRecord exists for type : "+type);
            }
            topicRecord.setType(type);
            schoolInfo.setId(schoolInfoId);
            topicRecord.setSchoolInfo(schoolInfo);
            String topicArn = snsService.createTopic(type.toString()+"-"+schoolInfoId.toString());
            topicRecord.setTopicEndPoint(topicArn);
            topicRecord = topicRecordRepository.save(topicRecord);

        } else if(type.equals(TopicType.SCHOOL_INFO)){
            if(schoolInfoId == null) {
                throw new WitcurveException("SchoolInfo Id is required for type : "+ type);
            }
            List<TopicRecord> existingTopicRecord = topicRecordRepository.findByTypeAndSchoolInfoId(TopicType.SCHOOL_INFO,schoolInfoId);
            if(!existingTopicRecord.isEmpty()) {
                throw new WitcurveException("There already exists a topic record for this school info");
            }
            List<Standard> standards=standardRepository.findBySchoolInfoId(schoolInfoId);
            for(Standard stndard:standards) {
                addTopic(TopicType.STANDARD, schoolInfoId, stndard.getId());
            }
            addTopic(TopicType.STAFF_SCHOOL_INFO, schoolInfoId,null);
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

    public List<TopicRecordDTO> findTopicRecordBySchoolInfoId(Long schoolInfoId) throws WitcurveException {
        log.debug("Request to find TopicRecord for school info with id : {}",schoolInfoId);
        List<TopicRecord> topicRecords = topicRecordRepository.findBySchoolInfoId(schoolInfoId);
        if(topicRecords == null) {
            throw new WitcurveException("Topic Record doesn't exists for this school info with id : " + schoolInfoId);
        }

        return topicRecordMapper.toDto(topicRecords);

    }

    //TODO Delete topic can be done from aws for now


}

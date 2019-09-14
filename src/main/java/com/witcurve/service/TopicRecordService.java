package com.witcurve.service;

import com.witcurve.domain.enumeration.TopicType;
import com.witcurve.service.dto.TopicRecordDTO;

import java.util.List;

public interface TopicRecordService {

    TopicRecordDTO addTopic(TopicType type, Long schoolInfoId,Long standardId);

    List<TopicRecordDTO> findAllTopicRecords();

    List<TopicRecordDTO> findTopicRecordBySchoolInfoId(Long schoolInfoId);
}

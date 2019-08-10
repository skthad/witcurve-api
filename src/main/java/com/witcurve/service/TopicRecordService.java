package com.witcurve.service;

import com.witcurve.domain.enumeration.TopicType;
import com.witcurve.service.dto.TopicRecordDTO;

import java.util.List;

public interface TopicRecordService {

    TopicRecordDTO addTopic(TopicType type, Long schoolInfoId);

    List<TopicRecordDTO> findAllTopicRecords();

    TopicRecordDTO findTopicRecordBySchoolInfoId(Long schoolInfoId);
}

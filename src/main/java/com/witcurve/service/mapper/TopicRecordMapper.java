package com.witcurve.service.mapper;

import com.witcurve.domain.TopicRecord;
import com.witcurve.service.dto.TopicRecordDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SchoolInfoMapper.class})
public interface TopicRecordMapper  extends EntityMapper<TopicRecordDTO, TopicRecord> {

    @Mapping(source = "schoolInfo.id", target = "schoolInfoId" )
    TopicRecordDTO toDto(TopicRecord topicRecord);

    @Mapping(source = "schoolInfoId", target = "schoolInfo" )
    TopicRecord toEntity(TopicRecordDTO topicRecordDTO);

    default TopicRecord fromId(Long id) {
        if(id == null) {
            return null;
        }
        TopicRecord topicRecord = new TopicRecord();
        topicRecord.setId(id);
        return topicRecord;
    }
}

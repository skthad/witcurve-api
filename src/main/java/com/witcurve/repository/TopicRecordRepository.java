package com.witcurve.repository;

import com.witcurve.domain.TopicRecord;
import com.witcurve.domain.enumeration.TopicType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRecordRepository extends JpaRepository<TopicRecord, Long> {

    TopicRecord findBySchoolInfoId(Long schoolInfoId);

    List<TopicRecord> findByType(TopicType topicType);
}

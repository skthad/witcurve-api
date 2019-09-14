package com.witcurve.repository;

import com.witcurve.domain.TopicRecord;
import com.witcurve.domain.enumeration.TopicType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface TopicRecordRepository extends JpaRepository<TopicRecord, Long> {

    @Query("Select tr from  TopicRecord tr where " +
        "tr.id in (select tr1.id from TopicRecord tr1 where tr1.schoolInfo.id=?1)" +
        "or tr.id in (select tr2.id from TopicRecord tr2 where tr2.standard.schoolInfo.id =?1)")
    List<TopicRecord> findBySchoolInfoId(Long schoolInfoId);

    List<TopicRecord> findByType(TopicType topicType);

    List<TopicRecord> findByStandardId(Long standardId);

    List<TopicRecord> findByTypeAndSchoolInfoId(TopicType type, Long schoolInfoId);

}

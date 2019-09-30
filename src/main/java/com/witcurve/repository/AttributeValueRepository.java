package com.witcurve.repository;

import com.witcurve.domain.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValue,Long> {

    @Query("select av from AttributeValue av where av.attribute.reportCardDesign.id = ?1")
    List<AttributeValue> findByReportCardDesignId(Long recId);

    @Query("select av from AttributeValue av where av.student.id = ?1 and av.attribute.reportCardDesign.id = ?2")
    List<AttributeValue> findByStudentIdAndRcdId(Long studentId,Long rcdId);

    @Modifying
    @Query("delete from AttributeValue av where av.id in ?1")
    void deleteAttributeValueByIds(List<Long> ids);

}

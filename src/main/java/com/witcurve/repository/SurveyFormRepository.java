package com.witcurve.repository;

import com.witcurve.domain.SurveyForm;
import com.witcurve.domain.enumeration.SurveyFormCreator;
import com.witcurve.domain.enumeration.SurveyFormStatus;
import com.witcurve.domain.enumeration.SurveyUserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyFormRepository extends JpaRepository<SurveyForm, Long> {

    @Query("Select sf from SurveyForm  sf where sf.schoolInfo.id=?1 and sf.creator=?2 and sf.status in ?3")
    List<SurveyForm> findBySchoolInfoIdAndCreatorAndStatusList(Long schoolInfoId, SurveyFormCreator creator, List<SurveyFormStatus> statuses);

    @Query("Select sf from SurveyForm  sf where sf.schoolInfo.id=?1 and sf.type in ?2 and sf.status in ?3")
    List<SurveyForm> findBySchoolInfoIdAndTypesAndStatusList(Long schoolInfoId, List<SurveyUserType> types, List<SurveyFormStatus> statuses);

    @Query("Select sf from SurveyForm  sf where  (sf.type = 'ALL') or (sf.type = 'PARENT' and sf.standard.id = ?3) or (sf.type = 'PARENT' and sf.standard.id = null) and sf.status in ?2 and sf.schoolInfo.id = ?1")
    List<SurveyForm> findBySchoolInfoIdAndStatusAndStandardId(Long schoolInfoId, List<SurveyFormStatus> statuses, Long standardId);
}

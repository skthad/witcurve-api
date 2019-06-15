package com.witcurve.repository;

import com.witcurve.domain.ConfigSettings;
import com.witcurve.domain.enumeration.ConfigFieldName;
import com.witcurve.domain.enumeration.ConfigType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigSettingsRepository extends JpaRepository<ConfigSettings, Long> {

    @Query("select cs from ConfigSettings cs where cs.schoolId = ?1 order by cs.configType, cs.displayOrder")
    List<ConfigSettings> getConfigSettingsBySchoolId(Long schoolId);

    @Query("select cs from ConfigSettings cs where cs.schoolId = ?1 and cs.fieldName = ?2")
    List<ConfigSettings> getConfigSettingsBySchoolIdAndFieldName(Long schoolId, ConfigFieldName fieldName);

    @Query("select cs from ConfigSettings cs where cs.schoolId = ?1 and cs.configType in ?2 order by cs.displayOrder")
    List<ConfigSettings> getConfigSettingsBySchoolIdAndTypes(Long schoolId, ConfigType[] configTypes);

    @Query("select cs from ConfigSettings cs where cs.schoolId = ?1 and cs.configType = ?2 " +
        "and (cs.displayFieldName = ?3 or cs.fieldValue = ?4 or cs.displayOrder = ?5)")
    List<ConfigSettings> getConfigSettingsBySchoolIdAndTypeAndDisplayNameAndValueAndOrder(Long schoolId, ConfigType type, String displayName, String value, Integer order);

    @Modifying
    @Query("delete from ConfigSettings where schoolId = ?1")
    void deleteBySchoolId(Long schoolId);

    @Modifying
    @Query("delete from ConfigSettings where schoolId = ?1 and configType in ?2")
    void deleteBySchoolIdAndType(Long schoolId, ConfigType[] configTypes);

}


package com.witcurve.repository;

import com.witcurve.domain.ConfigSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigSettingsRepository extends JpaRepository<ConfigSettings, Long> {

    @Query("select cs from ConfigSettings cs where cs.schoolInfoId = ?1")
    List<ConfigSettings> getConfigSettingsBySchoolInfoId(Long schoolInfoId);

}


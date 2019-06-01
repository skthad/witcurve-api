package com.witcurve.service;

import com.witcurve.domain.enumeration.ConfigType;
import com.witcurve.service.dto.ConfigSettingsDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface ConfigSettingsService {

    List<ConfigSettingsDTO> resetConfigSettingsToDefault(Long schoolId, ConfigType[] configTypes) throws WitcurveException;

    ConfigSettingsDTO updateConfigSetting(Long schoolId, ConfigSettingsDTO configSetting) throws WitcurveException;

    ConfigSettingsDTO createConfigSetting(Long schoolId, ConfigSettingsDTO configSetting) throws WitcurveException;

    List<ConfigSettingsDTO> getSettingsBySchoolIdAndTypes(Long schoolId, ConfigType[] configTypes) throws WitcurveException;

    void deleteByConfigSettingAndSchoolId(Long configSettingId, Long schoolId);
}

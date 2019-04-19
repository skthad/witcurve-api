package com.witcurve.service;

import com.witcurve.domain.enumeration.ConfigType;
import com.witcurve.service.dto.ConfigSettingsDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface ConfigSettingsService {

    List<ConfigSettingsDTO> resetConfigSettingsToDefault(Long schoolId, ConfigType configType) throws WitcurveException;

    List<ConfigSettingsDTO> updateConfigSetting(Long schoolId, List<ConfigSettingsDTO> configSettings) throws WitcurveException;

    List<ConfigSettingsDTO> getSettingsBySchoolId(Long schoolId, ConfigType configType) throws WitcurveException;

}

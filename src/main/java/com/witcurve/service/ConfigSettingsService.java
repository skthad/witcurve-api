package com.witcurve.service;

import com.witcurve.service.dto.ConfigSettingsDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface ConfigSettingsService {

    List<ConfigSettingsDTO> saveOrUpdateConfigSettings(List<ConfigSettingsDTO> configSettings) throws WitcurveException;

    List<ConfigSettingsDTO> getSettingsBySchoolInfoId(Long schoolInfoId);

}

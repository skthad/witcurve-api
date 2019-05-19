package com.witcurve.service.impl;

import com.witcurve.domain.ConfigSettings;
import com.witcurve.domain.School;
import com.witcurve.domain.enumeration.ConfigType;
import com.witcurve.repository.ConfigSettingsRepository;
import com.witcurve.repository.SchoolRepository;
import com.witcurve.service.ConfigSettingsService;
import com.witcurve.service.dto.ConfigSettingsDTO;
import com.witcurve.service.mapper.ConfigSettingsMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class ConfigSettingsServiceImpl implements ConfigSettingsService {
    
    private final Logger log  = LoggerFactory.getLogger(ConfigSettingsServiceImpl.class);
    
    @Autowired
    ConfigSettingsMapper configSettingsMapper;

    @Autowired
    ConfigSettingsRepository configSettingsRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Override
    public List<ConfigSettingsDTO> resetConfigSettingsToDefault(Long schoolId, ConfigType[] configTypes) throws WitcurveException {
        Optional<School> school = schoolRepository.findById(schoolId);
        if (!school.isPresent()) {
            throw new WitcurveException("No school found with ID: " + schoolId);
        }
        log.debug("Request to create Config settings for school ID {} and types {} ", schoolId, configTypes);
        if (configTypes == null || configTypes.length == 0) {
            configSettingsRepository.deleteBySchoolId(schoolId);
        } else {
            configSettingsRepository.deleteBySchoolIdAndType(schoolId, configTypes);
        }
        List<ConfigSettingsDTO> defaultSettings = getDefaultSettings(configTypes);
        defaultSettings.stream().forEach(cs -> {
            cs.setId(null);
            cs.setSchoolId(schoolId);
        });
        return configSettingsMapper.toDto(configSettingsRepository.saveAll(configSettingsMapper.toEntity(defaultSettings)));
    }

    @Override
    public List<ConfigSettingsDTO> updateConfigSetting(Long schoolId, List<ConfigSettingsDTO> configSettings) throws WitcurveException {
        Optional<School> school = schoolRepository.findById(schoolId);
        if (!school.isPresent()) {
            throw new WitcurveException("No school found with ID: " + schoolId);
        }
        log.debug("Request to update Config settings");
        configSettings.stream().forEach(cs -> {
            if (cs.getId() == null) {
                throw new WitcurveException("ID cannot be null for an update request");
            }
            if (!schoolId.equals(cs.getSchoolId())) {
                throw new WitcurveException("School ID provided does not match the schoolId in the object to be updated");
            }
        });
        return configSettingsMapper.toDto(configSettingsRepository.saveAll(configSettingsMapper.toEntity(configSettings)));
    }

    @Override
    public List<ConfigSettingsDTO> getSettingsBySchoolIdAndTypes(Long schoolId, ConfigType[] configTypes) throws WitcurveException {
        Optional<School> school = schoolRepository.findById(schoolId);
        if (!school.isPresent()) {
            throw new WitcurveException("No school found with ID: " + schoolId);
        }
        log.debug("Request to get Config settings for school ID: {}", schoolId);
        List<ConfigSettingsDTO> settings;
        if (configTypes == null || configTypes.length == 0) {
            settings = configSettingsMapper.toDto(configSettingsRepository
                .getConfigSettingsBySchoolId(schoolId));
        } else {
            settings = configSettingsMapper.toDto(configSettingsRepository
                .getConfigSettingsBySchoolIdAndTypes(schoolId, configTypes));
        }
        if (settings.size() == 0) {
            return resetConfigSettingsToDefault(schoolId, configTypes);
        } else {
            return settings;
        }
    }

    private List<ConfigSettingsDTO> getDefaultSettings(ConfigType[] configTypes) throws WitcurveException {
        List<ConfigSettings> defaultSettings;
        if (configTypes == null || configTypes.length == 0) {
            defaultSettings = configSettingsRepository.getConfigSettingsBySchoolId(-1l);
        } else {
            defaultSettings = configSettingsRepository.getConfigSettingsBySchoolIdAndTypes(-1l, configTypes);
        }
        return configSettingsMapper.toDto(defaultSettings);
    }
}

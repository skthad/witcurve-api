package com.witcurve.service.impl;

import com.witcurve.domain.ConfigSettings;
import com.witcurve.repository.ConfigSettingsRepository;
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


@Service
@Transactional
public class ConfigSettingsServiceImpl implements ConfigSettingsService {
    
    private final Logger log  = LoggerFactory.getLogger(ConfigSettingsServiceImpl.class);
    
    @Autowired
    ConfigSettingsMapper configSettingsMapper;

    @Autowired
    ConfigSettingsRepository configSettingsRepository;

    @Override
    public List<ConfigSettingsDTO> saveOrUpdateConfigSettings(List<ConfigSettingsDTO> configSettingsDTO) throws WitcurveException {
        log.debug("Request to save or update Config settings: {}", configSettingsDTO);
        if(configSettingsDTO.size() != 0) {
            List<ConfigSettings> configSettings = configSettingsMapper.toEntity(configSettingsDTO);
            configSettings = configSettingsRepository.saveAll(configSettings);
            return configSettingsMapper.toDto(configSettings);
        } else{
            throw new WitcurveException("No records to update");
        }
    }

    @Override
    public List<ConfigSettingsDTO> getSettingsBySchoolInfoId(Long schoolInfoId) {
        log.debug("Request to get Config settings for schoolInfo ID: {}", schoolInfoId);
        List<ConfigSettings> configSettings = configSettingsRepository
            .getConfigSettingsBySchoolInfoId(schoolInfoId);
        return configSettingsMapper.toDto(configSettings);
    }
}

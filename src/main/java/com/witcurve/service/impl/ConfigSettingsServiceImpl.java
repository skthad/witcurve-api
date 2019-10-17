package com.witcurve.service.impl;

import com.witcurve.domain.ConfigSettings;
import com.witcurve.domain.School;
import com.witcurve.domain.enumeration.ConfigFieldName;
import com.witcurve.domain.enumeration.ConfigFieldType;
import com.witcurve.domain.enumeration.ConfigType;
import com.witcurve.domain.enumeration.GradingMethod;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    public ConfigSettingsDTO createConfigSetting(Long schoolId, ConfigSettingsDTO configSetting) throws WitcurveException {

        if (!schoolId.equals(-1l)) {
            Optional<School> school = schoolRepository.findById(schoolId);
            if (!school.isPresent()) {
                throw new WitcurveException("No school found with ID: " + schoolId);
            }
        }

        log.debug("Request to create Config setting");

        if (configSetting.getConfigType().equals(ConfigType.GRADING_SCALE)) {
            configSetting.setFieldType(ConfigFieldType.INTEGER);
            configSetting.setFieldName(ConfigFieldName.GRADE);

            List<ConfigSettings> exisingSettings = configSettingsRepository.getConfigSettingsBySchoolIdAndTypeAndDisplayNameAndValueAndOrder(schoolId,
                ConfigType.GRADING_SCALE, configSetting.getDisplayFieldName().trim(), configSetting.getFieldValue().trim(), configSetting.getDisplayOrder());

            if (exisingSettings.size() > 0) {
                throw new WitcurveException("Invalid configuration values provided");
            }
        } else if (configSetting.getConfigType().equals(ConfigType.GRADING_SCALE_COLOR)) {
            configSetting.setFieldType(ConfigFieldType.STRING);
            configSetting.setFieldName(ConfigFieldName.GRADE_COLOR);

            List<ConfigSettings> exisingSettings = configSettingsRepository.getConfigSettingsBySchoolIdAndTypeAndDisplayNameAndOrder(schoolId,
                ConfigType.GRADING_SCALE_COLOR, configSetting.getDisplayFieldName().trim(), configSetting.getDisplayOrder());

            if (exisingSettings.size() > 0) {
                throw new WitcurveException("Invalid configuration values provided");
            }
        } else if (configSetting.getConfigType().equals(ConfigType.SCHOOL_PRIMARY_COLOR)) {
            configSetting.setFieldType(ConfigFieldType.STRING);
            configSetting.setFieldName(ConfigFieldName.SCHOOL_PRIMARY_COLOR);

            List<ConfigSettings> exisingSettings = configSettingsRepository.getConfigSettingsBySchoolIdAndTypeAndDisplayNameAndValueAndOrder(schoolId,
                ConfigType.SCHOOL_PRIMARY_COLOR, configSetting.getDisplayFieldName().trim(), configSetting.getFieldValue().trim(), configSetting.getDisplayOrder());

            if (exisingSettings.size() > 0) {
                throw new WitcurveException("Invalid configuration values provided");
            }
        } else if (configSetting.getConfigType().equals(ConfigType.STUDENT_HOUSE)) {
            configSetting.setFieldType(ConfigFieldType.STRING);
            configSetting.setFieldName(ConfigFieldName.HOUSE);
            configSetting.setDisplayFieldName("House");

            List<ConfigSettings> exisingSettings = configSettingsRepository.getConfigSettingsBySchoolIdAndTypeAndFieldNameAndValueAndOrder(schoolId,
                ConfigType.STUDENT_HOUSE, configSetting.getFieldName(), configSetting.getFieldValue().trim(), configSetting.getDisplayOrder());

            if (exisingSettings.size() > 0) {
                throw new WitcurveException("Invalid configuration values provided");
            }
        } else{
            throw new WitcurveException("Create not supported for given config type");
        }
        ConfigSettings result = configSettingsRepository.save(configSettingsMapper.toEntity(configSetting));
        return configSettingsMapper.toDto(result);
    }

    @Override
    public ConfigSettingsDTO updateConfigSetting(Long schoolId, ConfigSettingsDTO configSetting) throws WitcurveException {

        Optional<ConfigSettings> result = configSettingsRepository.findById(configSetting.getId());
        if (!result.isPresent()) {
            throw new WitcurveException("No config setting found with ID: " + configSetting.getId());
        }
        ConfigSettings existingSetting = result.get();

        if (!existingSetting.getSchoolId().equals(schoolId)) {
            throw new WitcurveException("School ID mismatch occured.");
        }
        log.debug("Request to update Config setting");

        String fieldValue = configSetting.getFieldValue().trim();
        switch (existingSetting.getFieldType()) {
            case TIME:
                Pattern pattern = Pattern.compile("^([01]\\d|2[0-3])([0-5]\\d)$");
                Matcher matcher = pattern.matcher(fieldValue);
                if (!matcher.matches()){
                    throw new WitcurveException("Invalid time format provided");
                }
                break;
            case INTEGER:
                try {
                    Integer.parseInt(fieldValue);
                } catch (NumberFormatException e) {
                    throw new WitcurveException("Cannot parse the value into an integer");
                }
                if (existingSetting.getConfigType().equals(ConfigType.GRADING_SCALE)) {
                    existingSetting.setDisplayFieldName(configSetting.getDisplayFieldName().trim());
                }
                break;
            case ENUM:
                try {
                    if (existingSetting.getConfigType().equals(ConfigType.GRADING_METHOD)) {
                        GradingMethod.valueOf(fieldValue.toUpperCase());
                        fieldValue = fieldValue.toUpperCase();
                        break;
                    }
                } catch (IllegalArgumentException e) {
                    throw new WitcurveException("Invalid enum provided");
                }
                break;
            case STRING:
                if (existingSetting.getConfigType().equals(ConfigType.GRADING_SCALE_COLOR)) {
                    existingSetting.setDisplayFieldName(configSetting.getDisplayFieldName().trim());
                }
                // no validation required
                break;
            case BOOLEAN:
                if (!fieldValue.equalsIgnoreCase("TRUE") && !fieldValue.equalsIgnoreCase("FALSE")) {
                    throw new WitcurveException("Wrong boolean value provided");
                }
                fieldValue = fieldValue.toUpperCase();
                break;
            default:
        }
        existingSetting.setFieldValue(fieldValue);
        existingSetting.setFieldDescription(configSetting.getFieldDescription());
        return configSettingsMapper.toDto(existingSetting);
    }

    @Override
    public List<ConfigSettingsDTO> getSettingsBySchoolIdAndTypes(Long schoolId, ConfigType[] configTypes) throws WitcurveException {

        if (!schoolId.equals(-1l)) {
            Optional<School> school = schoolRepository.findById(schoolId);
            if (!school.isPresent()) {
                throw new WitcurveException("No school found with ID: " + schoolId);
            }
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

    @Override
    public void deleteByConfigSettingAndSchoolId(Long configSettingId, Long schoolId) {
        Optional<ConfigSettings> result = configSettingsRepository.findById(configSettingId);
        if (!result.isPresent()) {
            throw new WitcurveException("No config setting found with ID: " + configSettingId);
        }
        ConfigSettings existingConfigSetting = result.get();
        if (!existingConfigSetting.getSchoolId().equals(schoolId)) {
            throw new WitcurveException("School ID mismatch occured");
        }
        configSettingsRepository.deleteById(configSettingId);
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

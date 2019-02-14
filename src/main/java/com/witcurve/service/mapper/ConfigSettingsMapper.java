package com.witcurve.service.mapper;

import com.witcurve.domain.ConfigSettings;
import com.witcurve.service.dto.ConfigSettingsDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConfigSettingsMapper extends EntityMapper<ConfigSettingsDTO, ConfigSettings>{

    ConfigSettingsDTO toDto(ConfigSettings configSettings);

    ConfigSettings toEntity(ConfigSettingsDTO configSettingsDTO);

    default ConfigSettings fromId(Long id) {
        if(id == null) {
            return null;
        }
        ConfigSettings configSettings = new ConfigSettings();
        configSettings.setId(id);
        return configSettings;
    }
}

package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.ConfigType;
import com.witcurve.service.ConfigSettingsService;
import com.witcurve.service.dto.ConfigSettingsDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ConfigSettingsResource {
    private final Logger log = LoggerFactory.getLogger(StandardResource.class);

    @Autowired
    ConfigSettingsService configSettingsService;

    /**
      * creates config settings
      * @param schoolId
      * @param configType
      * @return
      * @throws WitcurveException
      * @throws URISyntaxException
      */
    @PostMapping("/config-settings/school/{schoolId}")
    @Timed
    public ResponseEntity<List<ConfigSettingsDTO>> resetConfigSettings(@PathVariable Long schoolId,
                                                                        @RequestParam(required = false) ConfigType configType) {
        log.debug("Request to create config settings");
        List<ConfigSettingsDTO> result = configSettingsService.resetConfigSettingsToDefault(schoolId, configType);
        return ResponseEntity.ok()
            .body(result);
    }

    /**
     * updates config settings
     * @param configSettings
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PutMapping("/config-settings/school/{schoolId}")
    @Timed
    public ResponseEntity<List<ConfigSettingsDTO>> updateConfigSettings(@RequestBody @Valid List<ConfigSettingsDTO> configSettings,
                                                                        @PathVariable Long schoolId) {
        log.debug("Request to update config settings ");
        List<ConfigSettingsDTO> result = configSettingsService.updateConfigSetting(schoolId, configSettings);
        return ResponseEntity.ok()
            .body(result);
    }

    @GetMapping("/config-settings/school/{schoolId}")
    @Timed
    public ResponseEntity<List<ConfigSettingsDTO>> getConfigSettingsBySchool(@PathVariable Long schoolId, @RequestParam(required = false) ConfigType configType) {
        log.debug("Request to get config settings for school ID: " + schoolId + (configType != null ? (" for configType: " + configType) : ""));
        List<ConfigSettingsDTO> result = configSettingsService.getSettingsBySchoolId(schoolId, configType);
        return ResponseEntity.ok()
            .body(result);
    }
}



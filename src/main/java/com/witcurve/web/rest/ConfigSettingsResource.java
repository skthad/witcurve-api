package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.ConfigFieldName;
import com.witcurve.domain.enumeration.ConfigType;
import com.witcurve.service.ConfigSettingsService;
import com.witcurve.service.dto.ConfigSettingsDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
      * @param configTypes
      * @return
      * @throws WitcurveException
      * @throws URISyntaxException
      */
    @PostMapping("/config-settings/schools/{schoolId}/reset")
    @Timed
    public ResponseEntity<List<ConfigSettingsDTO>> resetConfigSettings(@PathVariable Long schoolId,
                                                                        @RequestParam(required = false) ConfigType[] configTypes) {
        log.debug("Request to create config settings");
        List<ConfigSettingsDTO> result = configSettingsService.resetConfigSettingsToDefault(schoolId, configTypes);
        return ResponseEntity.ok()
            .body(result);
    }

    /**
     * updates a config setting
     * @param configSetting
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PutMapping("/config-settings/schools/{schoolId}")
    @Timed
    public ResponseEntity<ConfigSettingsDTO> updateConfigSettings(@RequestBody @Valid ConfigSettingsDTO configSetting,
                                                                        @PathVariable Long schoolId) {
        log.debug("Request to update config setting");
        if (configSetting.getId() == null) {
            throw new WitcurveException("ID cannot be null for an update request");
        } else if (!schoolId.equals(configSetting.getSchoolId())) {
            throw new WitcurveException("School ID provided does not match the schoolId in the object to be updated");
        }
        ConfigSettingsDTO result = configSettingsService.updateConfigSetting(schoolId, configSetting);
        return ResponseEntity.ok()
            .body(result);
    }

    /**
     * creates a config setting
     * @param configSetting
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/config-settings/schools/{schoolId}")
    @Timed
    public ResponseEntity<ConfigSettingsDTO> createConfigSettings(@RequestBody @Valid ConfigSettingsDTO configSetting,
                                                                  @PathVariable Long schoolId) {
        log.debug("Request to create config setting");
        if (configSetting.getId() != null) {
            throw new WitcurveException("ID must be null for a create request");
        } else if (!configSetting.getConfigType().equals(ConfigType.GRADING_SCALE)
            && !(configSetting.getConfigType().equals(ConfigType.STUDENT_HOUSE)
            && configSetting.getFieldName().equals(ConfigFieldName.HOUSE))) {
            throw new WitcurveException("Create not supported for given config type/field name");
        } else if (!schoolId.equals(configSetting.getSchoolId())) {
            throw new WitcurveException("School ID provided does not match the schoolId in the object to be created");
        }
        ConfigSettingsDTO result = configSettingsService.createConfigSetting(schoolId, configSetting);
        return ResponseEntity.ok()
            .body(result);
    }

    @GetMapping("/config-settings/schools/{schoolId}")
    @Timed
    public ResponseEntity<List<ConfigSettingsDTO>> getConfigSettingsBySchool(@PathVariable Long schoolId,
                                                                             @RequestParam(required = false) ConfigType[] configTypes) {
        log.debug("Request to get config settings for school ID: " + schoolId + (configTypes != null && configTypes.length != 0 ? (" for configTypes: " + configTypes) : ""));
        List<ConfigSettingsDTO> result = configSettingsService.getSettingsBySchoolIdAndTypes(schoolId, configTypes);
        return ResponseEntity.ok()
            .body(result);
    }

    /**
     * delete the config setting
     * @param configSettingId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/config-settings/{configSettingId}/schools/{schoolId}")
    @Timed
    public ResponseEntity<Void> deleteConfigSetting(@PathVariable Long configSettingId, @PathVariable Long schoolId) throws WitcurveException {
        log.debug("REST request to delete config setting");
        try {
            configSettingsService.deleteByConfigSettingAndSchoolId(configSettingId, schoolId);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("A config setting is deleted with identifier " + configSettingId,
                schoolId.toString())).build();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key constraint might have failed while deleting");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }
}



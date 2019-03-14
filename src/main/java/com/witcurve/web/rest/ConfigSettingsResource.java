package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
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
      * creates or updates config settings
      * @param configSettings
      * @return
      * @throws WitcurveException
      * @throws URISyntaxException
      */
    @PostMapping("/config-settings")
    @Timed
    public ResponseEntity<List<ConfigSettingsDTO>> createConfigSettings(@RequestBody @Valid List<ConfigSettingsDTO> configSettings) throws WitcurveException, URISyntaxException {
        log.debug("Request to create config settings ");
        List<ConfigSettingsDTO> result = configSettingsService.saveOrUpdateConfigSettings(configSettings);

        return ResponseEntity.ok()
            .body(result);
    }

    @GetMapping("/config-settings/school-info/{schoolInfoId}")
    @Timed
    public ResponseEntity<Void> getSettingsBySchoolInfo(@PathVariable Long schoolInfoId) {
        log.debug("Request to get config settings for schoolInfo ID: " + schoolInfoId);
        configSettingsService.getSettingsBySchoolInfoId(schoolInfoId);
        return (ResponseEntity<Void>) ResponseEntity.ok();
    }
}



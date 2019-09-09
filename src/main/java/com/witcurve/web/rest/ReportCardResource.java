package com.witcurve.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.witcurve.service.util.HtmlToPdfUtil;
import com.witcurve.service.util.WitcurveUtil;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.vm.ReportCardVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ReportCardResource {

    private final Logger log = LoggerFactory.getLogger(ReportCardResource.class);

    @GetMapping("/report-card/preview")
    public ResponseEntity<Resource> getReportCardTemplate(@Valid @RequestBody ReportCardVM reportCardVM, @RequestParam(required = false) Long schoolInfoId) {
        try {
            HtmlToPdfUtil htmlToPdfUtil = new HtmlToPdfUtil();
            File resourceFile = htmlToPdfUtil.getFileFromResources("templates/report-card/report-template.html");
            if(!resourceFile.exists()) {
                throw new WitcurveException("File doesn't exists");
            }
            String contents = null;
            File inputFile = null;
            contents = new String(Files.readAllBytes(Paths.get("src/main/resources/templates/report-card/report-template.html")));
            Map<String, String> placeholderMap = new HashMap<>();
            ObjectMapper mapper = new ObjectMapper();
            String objectJson = mapper.writeValueAsString(reportCardVM);
            contents = contents.replace("{{report}}", objectJson);
            inputFile = WitcurveUtil.createTempFile("input.html");
            inputFile.exists();
            FileWriter fw=new FileWriter(inputFile);
            fw.write(contents);
            fw.close();
            File result = htmlToPdfUtil.htmlToPdf(inputFile);
            Resource  resource = new InputStreamResource(new FileInputStream(result));
            return ResponseEntity.ok(resource);
        } catch (IOException e) {
            log.debug("Error while reading contents : {}",e.getMessage());
            throw new WitcurveException("There was a problem generating ");
        }

    }

    @GetMapping("/report-card/template-html")
    public ResponseEntity<Resource> getReportCardTemplate() {
        try {
            HtmlToPdfUtil htmlToPdfUtil = new HtmlToPdfUtil();
            File resourceFile = htmlToPdfUtil.getFileFromResources("templates/report-card/report-template.html");
            Resource  resource = new InputStreamResource(new FileInputStream(resourceFile));
            return ResponseEntity.ok(resource);
        } catch (IOException e) {
            log.debug("Error while reading contents : {}",e.getMessage());
            throw new WitcurveException("There was a problem generating ");
        }

    }


}

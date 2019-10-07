package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.Attachment;
import com.witcurve.domain.enumeration.AttachmentType;
import com.witcurve.service.AttachmentService;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AttachmentResource {

    private final Logger log = LoggerFactory.getLogger(AttachmentResource.class);

    @Autowired
    AttachmentService attachmentService;

    /**
     * creates an attachment
     * @param file
     * @param type
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/attachments")
    @Timed
    public ResponseEntity<Attachment> createAttachment(@RequestParam MultipartFile file, @RequestParam AttachmentType type) throws WitcurveException, URISyntaxException {
        log.debug("Request to save for file : {} and of type : {}", file.getName(), type);
        Attachment result = attachmentService.saveAttachmentWithMultipart(file, type, null);
        return ResponseEntity.created(new URI("/api/attachments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("attachment", result.getId().toString()))
            .body(result);
    }

    /**
     * get attachemnt by id
     * @param attachmentId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/attachments/{attachmentId}")
    @Timed
    public ResponseEntity<Attachment> getAttachmentById(@PathVariable("attachmentId") Long attachmentId) throws WitcurveException {
        log.debug("Request to get Attachment with id {}", attachmentId);
        Attachment result = attachmentService.findById(attachmentId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * download attachemnt by id
     * @param attachmentId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/attachments/{attachmentId}/download")
    @Timed
    public ResponseEntity<Resource> downloadAttachmentById(@PathVariable("attachmentId") Long attachmentId) throws WitcurveException {
        log.debug("Request to get Attachment with id {}", attachmentId);
        File result = attachmentService.download(attachmentId);
        InputStreamResource resource = null;
        try{
           resource = new InputStreamResource(new FileInputStream(result));
        } catch (Exception e) {
            log.debug("Error while converting file to stream : {}", e.getMessage());
            throw new WitcurveException("There is a problem downloading the file!");
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
    }

    /**
     * get attachemnts
     * @param type
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/attachments")
    @Timed
    public ResponseEntity<List<Attachment>> getAttachments(@RequestParam(required = false) AttachmentType type) throws WitcurveException {
        log.debug("Request to get Attachment with type : {}", type);
        List<Attachment> result = attachmentService.findAll(type);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * delete attachemnt by id
     * @param attachmentId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/attachments/{attachmentId}")
    @Timed
    public ResponseEntity<Void> deleteAttachment(@PathVariable("attachmentId") Long attachmentId) throws WitcurveException {
        log.debug("Request to delete Attachment with id {}", attachmentId);
        attachmentService.delete(attachmentId);
        return ResponseEntity.ok(null);
    }
}

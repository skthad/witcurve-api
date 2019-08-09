package com.witcurve.service.impl;


import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.witcurve.config.ApplicationProperties;
import com.witcurve.domain.Attachment;
import com.witcurve.domain.enumeration.AttachmentType;
import com.witcurve.repository.AttachmentRepository;
import com.witcurve.service.AttachmentService;
import com.witcurve.service.util.S3FileManager;
import com.witcurve.service.util.WitcurveUtil;
import com.witcurve.web.rest.errors.WitcurveException;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing attachments.
 */

@Service
@Transactional
public class AttachmentServiceImpl implements AttachmentService {

    private final Logger log  = LoggerFactory.getLogger(AttachmentServiceImpl.class);

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    S3FileManager fileManager;

    @Autowired
    ApplicationProperties applicationProperties;

    public Attachment saveAttachment(MultipartFile file, AttachmentType type, String destinationDirectory) throws WitcurveException {
        log.debug("Request to save attachment for file : {} and of type : {}", file.getName(), type);
        String newFileName = RandomStringUtils.randomAlphabetic(8);
        if(destinationDirectory == null) {
            destinationDirectory = newFileName;
        } else {
            destinationDirectory += File.separator+newFileName;
        }
        File normalFile = WitcurveUtil.getFile(file);
        Attachment attachment = new Attachment();
        attachment.setType(type);
        attachment.setFilePath(destinationDirectory);
        attachment.setOriginalFileName(file.getOriginalFilename());
        fileManager.uploadFile(applicationProperties.getAws().getBucketName(), newFileName, normalFile, null, false);
        attachment = attachmentRepository.save(attachment);
        return attachment;
    }

    public Attachment findById(Long id) throws WitcurveException {
        log.debug("Request to get an attachment with id : {}", id);
        Optional<Attachment> attachment = attachmentRepository.findById(id);
        if(!attachment.isPresent()) {
            throw new WitcurveException("Attachment with id : "+id+" doesn't exist");
        }
        return attachment.get();
    }

    public File download(Long id) throws WitcurveException {
        log.debug("Request to download attachment with id : {}", id);
        Optional<Attachment> attachment = attachmentRepository.findById(id);
        if(!attachment.isPresent()) {
            throw new WitcurveException("Attachment with id : "+id+" doesn't exist");
        }
        S3Object s3Object = fileManager.getObject(applicationProperties.getAws().getBucketName(), attachment.get().getFilePath());
        File file = WitcurveUtil.createTempFile(attachment.get().getOriginalFileName());
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(IOUtils.toByteArray(s3Object.getObjectContent()));
            fos.close();
        } catch (IOException e) {
            log.error("Error while creating file : {}", e.getMessage());
            throw new WitcurveException("Error while downlaoding the file");
        }
        return file;
    }

    public List<Attachment> findAll(AttachmentType type) {
        log.debug("Request to get an attachments with type : {}", type);
        if(type == null) {
            return attachmentRepository.findAll();
        } else {
            return attachmentRepository.findByType(type);
        }
    }

    public void delete(Long id) throws WitcurveException {
        log.debug("Request to delete an attachment with id : {}", id);
        Attachment attachment = findById(id);
        fileManager.deleteFile(applicationProperties.getAws().getBucketName(), attachment.getFilePath());
        attachmentRepository.deleteById(id);

    }
}

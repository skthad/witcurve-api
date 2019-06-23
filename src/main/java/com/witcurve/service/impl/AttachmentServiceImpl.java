package com.witcurve.service.impl;


import com.witcurve.domain.Attachment;
import com.witcurve.domain.enumeration.AttachmentType;
import com.witcurve.repository.AttachmentRepository;
import com.witcurve.service.AttachmentService;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    public Attachment saveAttachment(MultipartFile file, AttachmentType type) {
        log.debug("Request to save attachment for file : {} and of type : {}", file.getName(), type);
        return null;
    }

    public Attachment updateAttachment(MultipartFile file, Long attachmentId) {
        log.debug("Request to update attachment for id : {} with file : {}", attachmentId, file.getName());
        return null;
    }

    public Attachment findById(Long id) throws WitcurveException {
        log.debug("Request to get an attachment with id : {}", id);
        Optional<Attachment> attachment = attachmentRepository.findById(id);
        if(!attachment.isPresent()) {
            throw new WitcurveException("Attachment with id : "+id+" doesn't exist");
        }
        return attachment.get();
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
        findById(id);
        attachmentRepository.deleteById(id);
        //s3 delete logic

    }
}

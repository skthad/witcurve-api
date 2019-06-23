package com.witcurve.service;

import com.witcurve.domain.Attachment;
import com.witcurve.domain.enumeration.AttachmentType;
import com.witcurve.web.rest.errors.WitcurveException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service for managing attachments.
 */
@Service
@Transactional
public interface AttachmentService {

    Attachment saveAttachment(MultipartFile file, AttachmentType type) throws WitcurveException;

    Attachment updateAttachment(MultipartFile file, Long attachmentId) throws WitcurveException;

    Attachment findById(Long id) throws WitcurveException;

    List<Attachment> findAll(AttachmentType type);

    void delete(Long id) throws WitcurveException;


}

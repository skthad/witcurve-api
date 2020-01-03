package com.witcurve.repository;

import com.witcurve.domain.Attachment;
import com.witcurve.domain.enumeration.AttachmentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Attachment entity.
 */
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findByType(AttachmentType attachmentType);


}

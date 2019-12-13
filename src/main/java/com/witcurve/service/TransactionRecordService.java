package com.witcurve.service;

import com.witcurve.domain.enumeration.AttachmentType;
import com.witcurve.service.dto.TransactionRecordDTO;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRecordService {

    TransactionRecordDTO saveOrUpdate(TransactionRecordDTO transactionRecordDTO);

    List<TransactionRecordDTO> getBySchoolInfoIdAndDateRange(Long schoolInfoId, LocalDate fromDate, LocalDate endDate);

    TransactionRecordDTO getById(Long id);

    TransactionRecordDTO addAttachment(Long id, List<MultipartFile> file);

}

package com.witcurve.service;

import com.witcurve.service.dto.csv.ImportResponse;
import com.witcurve.web.rest.errors.WitcurveException;
import org.springframework.web.multipart.MultipartFile;

public interface BulkImportService {

    ImportResponse bulkStaffImport(MultipartFile file, Long schoolInfoId) throws WitcurveException;

    ImportResponse bulkStudentImport(MultipartFile file, Long schoolInfoId) throws WitcurveException;
}

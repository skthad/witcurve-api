package com.witcurve.service;

import com.witcurve.domain.enumeration.ConfigType;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.dto.ReportCardDTO;
import com.witcurve.web.rest.vm.ReportCardVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface ReportCardService {

    File getReportCardTemplatePdf(ReportCardVM reportCardVM, String templateUrl, String fileName);

    File getReportCardTemplateHtml(ReportCardVM reportCardVM, String templateUrl);

    ReportCardDTO saveOrUpdate(ReportCardDTO reportCardDTO);

    List<ReportCardDTO> getReportCardByExam(Long examId, Grade grade);

    Page<File> getReportCardPreviewForStandard(Long reportCardId, Long standardId, Pageable pageable, Boolean showHeader);

    Map<String,String> getGradeDetailsByExamIdAndConfigType(Long examId, ConfigType configType);

    ReportCardDTO findOne(Long id);

}

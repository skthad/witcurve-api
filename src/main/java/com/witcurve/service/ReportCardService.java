package com.witcurve.service;

import com.witcurve.web.rest.vm.ReportCardVM;

import java.io.File;
import java.util.List;

public interface ReportCardService {

    File getReportCardTemplatePdf(ReportCardVM reportCardVM, String templateUrl);

    File getReportCardTemplateHtml(ReportCardVM reportCardVM, String templateUrl);

    List<ReportCardVM> getReportCardDetailsForStandard(Long standardId, Long examId, String bindingId);


}

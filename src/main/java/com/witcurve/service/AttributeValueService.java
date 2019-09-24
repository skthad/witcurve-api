package com.witcurve.service;

import com.witcurve.service.dto.AttributeValueDTO;

import java.util.List;

public interface AttributeValueService {

    List<AttributeValueDTO> saveOrUpdate(List<AttributeValueDTO> attributeValueDTOS);

    List<AttributeValueDTO> getAttributeValueByReportCardDesign(Long rcdId,Long studentId);
}

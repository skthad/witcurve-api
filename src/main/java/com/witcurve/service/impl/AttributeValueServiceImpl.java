package com.witcurve.service.impl;

import com.witcurve.domain.AttributeValue;
import com.witcurve.domain.ReportCardDesign;
import com.witcurve.repository.AttributeValueRepository;
import com.witcurve.repository.ReportCardDesignRepository;
import com.witcurve.service.AttributeValueService;
import com.witcurve.service.dto.AttributeValueDTO;
import com.witcurve.service.mapper.AttributeValueMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttributeValueServiceImpl implements AttributeValueService {

    private final Logger log = LoggerFactory.getLogger(AttributeValueServiceImpl.class);

    @Autowired
    ReportCardDesignRepository reportCardDesignRepository;

    @Autowired
    AttributeValueRepository attributeValueRepository;

    @Autowired
    AttributeValueMapper attributeValueMapper;

    @Override
    public List<AttributeValueDTO> saveOrUpdate(List<AttributeValueDTO> attributeValueDTOS){
        log.debug("Request to save or update AttributeValue : {}",attributeValueDTOS);
        List<AttributeValue> attributeValues = attributeValueMapper.toEntity(attributeValueDTOS);
        attributeValues = attributeValueRepository.saveAll(attributeValues);
        return attributeValueMapper.toDto(attributeValues);
    }


    @Override
    public List<AttributeValueDTO> getAttributeValueByReportCardDesign(Long rcdId,Long studentId){
        log.debug("Request to get attributeValue with recId : {} ",rcdId);
        List<AttributeValue> listOfAttributeValue;
        Optional<ReportCardDesign> reportCardDesign=reportCardDesignRepository.findById(rcdId);
        if(!reportCardDesign.isPresent()){
            throw new WitcurveException("No ReportCardDesign with given id "+rcdId);
        }
        if(studentId==null){
            listOfAttributeValue= attributeValueRepository.findByReportCardDesignId(rcdId);
        }else {
            listOfAttributeValue = attributeValueRepository.findByStudentIdAndRcdId(studentId, rcdId);
        }
       return attributeValueMapper.toDto(listOfAttributeValue);
    }

    @Override
    public void deleteAttributeValues(List<Long> ids) {
        log.debug("Request to delete attribute values by ids : {}", ids);
        attributeValueRepository.deleteAttributeValueByIds(ids);

    }

}

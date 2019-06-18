package com.witcurve.service.impl;

import com.witcurve.domain.SurveySection;
import com.witcurve.repository.SurveySectionRepository;
import com.witcurve.service.SurveySectionService;
import com.witcurve.service.dto.SurveySectionDTO;
import com.witcurve.service.mapper.SurveySectionMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class SurveySectionServiceImpl implements SurveySectionService {

    private final Logger log  = LoggerFactory.getLogger(SurveySectionServiceImpl.class);

    @Autowired
    SurveySectionMapper surveySectionMapper;

    @Autowired
    SurveySectionRepository surveySectionRepository;

    @Override
    public SurveySectionDTO saveOrUpdate(SurveySectionDTO surveySectionDTO) throws WitcurveException {
        log.debug("Request to save or update surveySection : {}",surveySectionDTO);
        SurveySection surveySection = surveySectionMapper.toEntity(surveySectionDTO);
        surveySection = surveySectionRepository.save(surveySection);
        return surveySectionMapper.toDto(surveySection);
    }

    public void deleteOne(Long surveySectionId) throws WitcurveException {
        log.debug("Request to delete surveySection with id : {}", surveySectionId);
        Optional<SurveySection> surveySection = surveySectionRepository.findById(surveySectionId);
        if(!surveySection.isPresent()) {
            throw new WitcurveException("No surveySection found with id : "+surveySectionId);
        }
        surveySectionRepository.delete(surveySection.get());
    }
    
}

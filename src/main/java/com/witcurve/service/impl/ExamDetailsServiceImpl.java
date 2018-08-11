package com.witcurve.service.impl;

import com.witcurve.domain.ExamDetails;
import com.witcurve.repository.ExamDetailsRepository;
import com.witcurve.service.ExamDetailsService;
import com.witcurve.service.dto.ExamDetailsDTO;
import com.witcurve.service.mapper.ExamDetailsMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamDetailsServiceImpl implements ExamDetailsService {

    private final Logger log  = LoggerFactory.getLogger(ExamDetailsServiceImpl.class);

    @Autowired
    ExamDetailsRepository examDetailsRepository;

    @Autowired
    ExamDetailsMapper examDetailsMapper;


    @Override
    public ExamDetailsDTO saveOrUpdate(ExamDetailsDTO examDetailsDTO) {
        log.debug("Request to save or update exam details: {}", examDetailsDTO);
        ExamDetails examDetails = examDetailsMapper.examDetailsDTOToExamDetails(examDetailsDTO);
        examDetails = examDetailsRepository.save(examDetails);

        return examDetailsMapper.examDetailsToExamDetailsDTO(examDetails);
    }

    @Override
    public ExamDetailsDTO getExamDetailsById(Long examDetailsId) throws WitcurveException {
        log.debug("Request to get exam details with id : {}", examDetailsId);
        ExamDetails examDetails = examDetailsRepository.findById(examDetailsId).get();
        if (examDetails == null) {
           throw new WitcurveException("No Exam details with gievn id");
        }
        return examDetailsMapper.examDetailsToExamDetailsDTO(examDetails);
    }

    @Override
    public void deleteExamDetails(Long examDetailsId) throws WitcurveException {
        log.debug("Request to delete exam details with id : {}", examDetailsId);
        ExamDetails examDetails = examDetailsRepository.findById(examDetailsId).get();

        if (examDetails == null) {
            throw new WitcurveException("No Exam details with gievn id");
        }
        examDetailsRepository.delete(examDetails);
    }
}

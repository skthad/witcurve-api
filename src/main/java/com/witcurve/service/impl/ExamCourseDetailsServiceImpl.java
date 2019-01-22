package com.witcurve.service.impl;

import com.witcurve.domain.ExamCourseDetails;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.repository.ExamCourseDetailsRepository;
import com.witcurve.service.ExamCourseDetailsService;
import com.witcurve.service.dto.ExamCourseDetailsDTO;
import com.witcurve.service.mapper.ExamCourseDetailsMapper;
import com.witcurve.service.mapper.ExamCourseDetailsMapperLite;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ExamCourseDetailsServiceImpl implements ExamCourseDetailsService {

    private final Logger log  = LoggerFactory.getLogger(ExamCourseDetailsServiceImpl.class);

    @Autowired
    private ExamCourseDetailsMapper examCourseDetailsMapper;

    @Autowired
    private ExamCourseDetailsMapperLite examCourseDetailsMapperLite;

    @Autowired
    private ExamCourseDetailsRepository examCourseDetailsRepository;

    @Override
    public ExamCourseDetailsDTO saveOrUpdate(ExamCourseDetailsDTO examCourseDetailsDTO) {
        log.debug("Request to save or update examCourseDetailsDTO");

        //TODO: DO we need any validation like SCD?
        ExamCourseDetails examCourseDetails = examCourseDetailsMapperLite.toEntity(examCourseDetailsDTO);
        examCourseDetails = examCourseDetailsRepository.save(examCourseDetails);
        return examCourseDetailsMapperLite.toDto(examCourseDetails);
    }

    @Override
    public ExamCourseDetailsDTO getExamCourseDetailsById(Long examCourseDetailsId) throws WitcurveException {
        log.debug("Request to get examCourseDetails by id : {}", examCourseDetailsId);
        ExamCourseDetails examCourseDetails = examCourseDetailsRepository.findById(examCourseDetailsId).get();
        if(examCourseDetails == null) {
            throw new WitcurveException("No ExamCourseDetails exists for given id");
        }
        return examCourseDetailsMapper.toDto(examCourseDetails);
    }

    @Override
    public void deleteExamCourseDetails(Long examCourseDetailsId) throws WitcurveException {
        log.debug("Request to delete examCourseDetails by id : {}", examCourseDetailsId);
        ExamCourseDetails examCourseDetails = examCourseDetailsRepository.findById(examCourseDetailsId).get();
        if(examCourseDetails == null) {
            throw new WitcurveException("No ExamCourseDetails exists for given id");
        }
        examCourseDetailsRepository.delete(examCourseDetails);
    }

    @Override
    public List<ExamCourseDetailsDTO> getExamCourseDetailsByGradeAndExamId(Grade grade, Long examId) {
        log.debug("Request to get list of examCourseDetails for given grade : {} and examId : {}", grade, examId);
        List<ExamCourseDetails> examCourseDetailsList = examCourseDetailsRepository.findByGradeOrderByGsdStart(grade, examId);
        return examCourseDetailsMapper.toDto(examCourseDetailsList);
    }

    @Override
    public List<ExamCourseDetailsDTO> getExamCourseDetailsByTeacherIdAndExamId(Long teacherId, Long examId) {
        log.debug("Request to get list of examCourseDetails for given teacherId: {} and examId: {}", teacherId, examId);
        List<ExamCourseDetails> examCourseDetailsList = examCourseDetailsRepository.findByTeacherIdAndExamIdOrderByGsdStart(teacherId, examId);
        return examCourseDetailsMapper.toDto(examCourseDetailsList);
    }

}

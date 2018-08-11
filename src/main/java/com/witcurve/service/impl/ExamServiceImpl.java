package com.witcurve.service.impl;

import com.witcurve.domain.Exam;
import com.witcurve.repository.ExamRepository;
import com.witcurve.service.ExamService;
import com.witcurve.service.dto.ExamDTO;
import com.witcurve.service.mapper.ExamMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamServiceImpl implements ExamService {

    private final Logger log  = LoggerFactory.getLogger(ExamServiceImpl.class);


    @Autowired
    ExamRepository examRepository;

    @Autowired
    ExamMapper examMapper;

    @Override
    public ExamDTO saveOrUpdate(ExamDTO examDTO) {
        log.debug("Request to save or update exam: {}", examDTO);
        Exam exam = examMapper.examDTOToExam(examDTO);
        exam = examRepository.save(exam);
        return examMapper.examToExamDTO(exam);
    }

    @Override
    public ExamDTO getExamById(Long examId) throws WitcurveException {
        log.debug("Request to get exam with id {}", examId);
        Exam exam = examRepository.findById(examId).get();

        if (exam ==  null) {
            throw  new WitcurveException("No Exam with given Id");
        }
        return examMapper.examToExamDTO(exam);
    }

    @Override
    public void deleteExam(Long examId) throws WitcurveException {
        log.debug("Request to delete exam with id {}", examId);
        Exam exam = examRepository.findById(examId).get();

        if (exam == null){
            throw new WitcurveException("No exam with given Id");
        }
        examRepository.delete(exam);
    }
}

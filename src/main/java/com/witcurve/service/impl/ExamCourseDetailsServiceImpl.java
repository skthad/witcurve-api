package com.witcurve.service.impl;

import com.witcurve.domain.Exam;
import com.witcurve.domain.ExamCourseDetails;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.repository.CourseTeacherRepository;
import com.witcurve.repository.ExamCourseDetailsRepository;
import com.witcurve.repository.ExamRepository;
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
import java.util.Optional;

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

    @Autowired
    private CourseTeacherRepository courseTeacherRepository;

    @Autowired
    private ExamRepository examRepository;

    @Override
    public ExamCourseDetailsDTO saveOrUpdate(ExamCourseDetailsDTO examCourseDetailsDTO, Long examId) throws WitcurveException {
        log.debug("Request to save or update examCourseDetailsDTO for examId: " + examId);

        Optional<Exam> exam = examRepository.findById(examId);
        if (!exam.isPresent()) {
            throw new WitcurveException("No exam found with id: " + examId);
        }

        if (examCourseDetailsDTO.getDate().isBefore(exam.get().getStartDate())
            || examCourseDetailsDTO.getDate().isAfter(exam.get().getEndDate())) {
            throw new WitcurveException("Date cannot be out of boundary of the exam startDate and endDate");
        }
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
    public List<ExamCourseDetailsDTO> getExamCourseDetailsByGradeAndExamId(Grade grade, Long examId) {
        log.debug("Request to get list of examCourseDetails for given grade : {} and examId : {}", grade, examId);
        List<ExamCourseDetails> examCourseDetailsList = examCourseDetailsRepository.findByGradeAndExamIdOrderByGsdStart(grade, examId);
        return examCourseDetailsMapper.toDto(examCourseDetailsList);
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

}

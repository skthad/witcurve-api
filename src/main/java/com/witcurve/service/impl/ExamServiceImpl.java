package com.witcurve.service.impl;

import com.witcurve.domain.Exam;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.repository.ExamRepository;
import com.witcurve.repository.GeneralSlotDetailsRepository;
import com.witcurve.service.ExamService;
import com.witcurve.service.dto.ExamDTO;
import com.witcurve.service.mapper.ExamMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ExamServiceImpl implements ExamService {

    private final Logger log  = LoggerFactory.getLogger(ExamServiceImpl.class);


    @Autowired
    ExamRepository examRepository;

    @Autowired
    GeneralSlotDetailsRepository generalSlotDetailsRepository;

    @Autowired
    ExamMapper examMapper;

    @Override
    public ExamDTO saveOrUpdate(ExamDTO examDTO) throws WitcurveException {
        log.debug("Request to save or update exam: {}", examDTO);

        if (examDTO.getStartDate().isAfter(examDTO.getEndDate())) {
            throw new WitcurveException("StartDate cannot be after EndDate");
        }
        List<Long> overlappingExamIds;
        if (examDTO.getId() == null) {
            overlappingExamIds = examRepository.findOverlappingExams(
                examDTO.getSchoolInfoId(), examDTO.getGrade(), examDTO.getStartDate(), examDTO.getEndDate());
        } else {
            overlappingExamIds = examRepository.findOverlappingExams(
                examDTO.getSchoolInfoId(), examDTO.getGrade(),
                examDTO.getStartDate(), examDTO.getEndDate(), examDTO.getId());
        }

        if (overlappingExamIds.size() > 0) {
            throw new WitcurveException("Date range provided overlaps with another exam");
        }
        return examMapper.toDto(examRepository.save(examMapper.toEntity(examDTO)));
    }

    @Override
    public ExamDTO getExamById(Long examId) throws WitcurveException {
        log.debug("Request to get exam with id {}", examId);
        Optional<Exam> exam = examRepository.findById(examId);
        if (!exam.isPresent()) {
            throw  new WitcurveException("No Exam with given Id " + examId);
        }
        return examMapper.toDto(exam.get());
    }

    @Override
    public List<ExamDTO> getExamsBySchoolInfoAndGrade(Long schoolInfoId, Grade grade, LocalDate startDate, LocalDate endDate) throws WitcurveException {
        log.debug("Request to get Exams for grade {} in schoolInfoId {}", grade, schoolInfoId);
        List<Exam> exams;
        if (startDate != null && endDate != null) {
            if (startDate.isAfter(endDate)) {
                throw new WitcurveException("StartDate cannot be after EndDate");
            }
            exams = examRepository.findAllBySchoolInfoAndGradeAndDateRange(schoolInfoId, grade, startDate, endDate);
        } else {
            exams = examRepository.findAllBySchoolInfoAndGrade(schoolInfoId, grade);
        }

        return examMapper.toDto(exams);
    }

    @Override
    public void deleteExam(Long examId) throws WitcurveException {
        log.debug("Request to delete exam with id {}", examId);
        Optional<Exam> exam = examRepository.findById(examId);
        if (!exam.isPresent()) {
            throw  new WitcurveException("No Exam with given Id " + examId);
        }
        generalSlotDetailsRepository.deleteByExamId(examId);
        examRepository.delete(exam.get());
    }
}

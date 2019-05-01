package com.witcurve.service.impl;

import com.witcurve.domain.AcademicSession;
import com.witcurve.domain.Exam;
import com.witcurve.domain.enumeration.ExamStatus;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.repository.AcademicSessionRepository;
import com.witcurve.repository.ExamCourseDetailsRepository;
import com.witcurve.repository.ExamRepository;
import com.witcurve.repository.GeneralSlotDetailsRepository;
import com.witcurve.service.ExamService;
import com.witcurve.service.dto.ExamDTO;
import com.witcurve.service.mapper.ExamMapper;
import com.witcurve.service.util.WitcurveUtil;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    ExamCourseDetailsRepository examCourseDetailsRepository;

    @Autowired
    AcademicSessionRepository academicSessionRepository;

    @Autowired
    ExamMapper examMapper;

    @Override
    public ExamDTO saveOrUpdate(ExamDTO examDTO) throws WitcurveException {
        log.debug("Request to save or update exam: {}", examDTO);
        WitcurveUtil.correctDateFormat(examDTO.getStartDate(), examDTO.getEndDate());
        return examMapper.toDto(examRepository.save(examMapper.toEntity(examDTO)));
    }

    @Override
    public ExamDTO updateExamStatus(Long examId, ExamStatus status) throws WitcurveException{
        log.debug("Request to update exam with id {} with status : {}", examId, status);
        Optional<Exam> exam = examRepository.findById(examId);
        if (!exam.isPresent()) {
            throw new WitcurveException("No Exam with given Id " + examId);
        }
        if(ExamStatus.DRAFT.equals(exam.get().getStatus())) {
            if(ExamStatus.CLOSED.equals(status)) {
                throw new WitcurveException("Drafted exams cannot be closed");
            }
        }
        if(ExamStatus.CLOSED.equals(exam.get().getStatus())) {
            if(ExamStatus.DRAFT.equals(status)) {
                throw new WitcurveException("Closed exams cannot be drafted");
            }
        }
        exam.get().setStatus(status);
        return examMapper.toDto(exam.get());
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
    public List<ExamDTO> getExamsBySessionId(Long sessionId) throws WitcurveException {
        log.debug("Get the list off exams in academic session with id : {}", sessionId);
        Optional<AcademicSession> academicSession = academicSessionRepository.findById(sessionId);
        if (!academicSession.isPresent()) {
            throw new WitcurveException("No Academic Session with given id");
        }
        List<Exam> exams = examRepository.findAllBySchoolInfoAndDateRange(academicSession.get().getSchoolInfo().getId(),
            academicSession.get().getStartDate(), academicSession.get().getStartDate().plusYears(1));
        return examMapper.toDto(exams);
    }

    @Override
    public void deleteExam(Long examId) throws WitcurveException {
        log.debug("Request to delete exam with id {}", examId);
        Optional<Exam> exam = examRepository.findById(examId);
        if (!exam.isPresent()) {
            throw  new WitcurveException("No Exam with given Id " + examId);
        }
        if(ExamStatus.CLOSED.equals(exam.get().getStatus())) {
            throw new WitcurveException("CLOSED exams cannot be deleted");
        }
        if(ExamStatus.PUBLISHED.equals(exam.get().getStatus()) && LocalDate.now().isAfter(exam.get().getStartDate().minusDays(1))) {
            throw new WitcurveException("Exam cannot be deleted as it has already been conducted");
        }
        try {
            examCourseDetailsRepository.deleteByExamId(examId);
            generalSlotDetailsRepository.deleteByExamId(examId);
            examRepository.delete(exam.get());
        }  catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key constraint might have failed while deleting");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }
}

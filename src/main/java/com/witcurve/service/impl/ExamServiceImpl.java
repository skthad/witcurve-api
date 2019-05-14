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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
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
        if(examDTO.getId() != null) {
            Optional<Exam> exam = examRepository.findById(examDTO.getId());
            if (!exam.isPresent()) {
                throw new WitcurveException("No Exam with given Id " + examDTO.getId());
            }
            if(!exam.get().getStatus().equals(ExamStatus.DRAFT)) {
                throw new WitcurveException("Only DRAFT exams can be updated");
            }
            if ((!exam.get().getStartDate().equals(examDTO.getStartDate()))
            || (!exam.get().getEndDate().equals(examDTO.getEndDate()))) {
                examCourseDetailsRepository.deleteByExamId(examDTO.getId());
                generalSlotDetailsRepository.deleteByExamId(examDTO.getId());
            }

        }
        return examMapper.toDto(examRepository.save(examMapper.toEntity(examDTO)));
    }

    @Override
    public ExamDTO updateExamStatus(Long examId, ExamStatus status) throws WitcurveException{
        log.debug("Request to update exam with id {} with status : {}", examId, status);
        Optional<Exam> exam = examRepository.findById(examId);
        List<Grade> gradesWithOutCourse = new ArrayList<>();
        if (!exam.isPresent()) {
            throw new WitcurveException("No Exam with given Id " + examId);
        }
        Boolean isValidStatusChange = (exam.get().getStatus().equals(ExamStatus.DRAFT) && status.equals(ExamStatus.PUBLISHED))
            || (exam.get().getStatus().equals(ExamStatus.PUBLISHED) && status.equals(ExamStatus.RESULTS_DECLARED));
        if(!isValidStatusChange) {
            throw new WitcurveException("Exam status cannot be changed from "+exam.get().getStatus()+" to "+status);
        }
        ExamDTO examDTO = examMapper.toDto(exam.get());
        if(status.equals(ExamStatus.PUBLISHED)) {
            if(examDTO.getGrades() != null && examDTO.getGrades().size()!=0) {
                for(Grade grade : examDTO.getGrades()) {
                    List<Grade> grades = new ArrayList<>();
                    grades.add(grade);
                    if(examCourseDetailsRepository.findByGradesAndExamId(grades, examId).size()==0) {
                        gradesWithOutCourse.add(grade);
                    }
                }
                if(gradesWithOutCourse.size() !=0) {
                    throw new WitcurveException("Status cannot be changed to published," +
                        " slots have been assigned for grades : "+gradesWithOutCourse.toString()+
                        "  but not courses. Please assign them or remove the slots for these grade");
                }
            } else {
                throw new WitcurveException("Status cannot be changed to published," +
                    " please assign slot for at least one grade");
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
    public Page<ExamDTO> getExamsBetweenDates(Long schoolInfoId, LocalDate fromDate, LocalDate endDate, Grade grade, List<ExamStatus> statusList, Pageable pageable) throws WitcurveException {
        log.debug("Get the list of exams between dates {} and {} for school info id : {} and grade : {} of status : {}", fromDate, endDate, schoolInfoId, grade, statusList);
        WitcurveUtil.correctDateFormat(fromDate, endDate);
        Page<Exam> exams;
        if(statusList != null && !statusList.isEmpty()) {
            if(grade == null) {
                exams = examRepository.findAllBySchoolInfoAndDateRangeAndStatuses(schoolInfoId, fromDate, endDate, statusList, pageable);
            } else {
                exams = examRepository.findAllBySchoolInfoAndGradeAndDateRangeAndStatuses(schoolInfoId, grade, fromDate, endDate, statusList, pageable);
            }
        } else {
            if(grade == null) {
                exams = examRepository.findAllBySchoolInfoAndDateRange(schoolInfoId, fromDate, endDate, pageable);
            } else {
                exams = examRepository.findAllBySchoolInfoAndGradeAndDateRange(schoolInfoId, grade, fromDate, endDate, pageable);
            }
        }
        return exams.map(examMapper::toDto);
    }

    @Override
    public void deleteExam(Long examId) throws WitcurveException {
        log.debug("Request to delete exam with id {}", examId);
        Optional<Exam> exam = examRepository.findById(examId);
        if (!exam.isPresent()) {
            throw  new WitcurveException("No Exam with given Id " + examId);
        }
        if(!(ExamStatus.DRAFT.equals(exam.get().getStatus()) || ExamStatus.PUBLISHED.equals(exam.get().getStatus()))) {
            throw new WitcurveException("Only PUBLISHED or DRAFT exams can be deleted");
        }
        examCourseDetailsRepository.deleteByExamId(examId);
        generalSlotDetailsRepository.deleteByExamId(examId);
        examRepository.delete(exam.get());
    }
}

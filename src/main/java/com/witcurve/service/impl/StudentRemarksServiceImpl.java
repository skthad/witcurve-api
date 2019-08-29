package com.witcurve.service.impl;

import com.witcurve.domain.Event;
import com.witcurve.domain.Exam;
import com.witcurve.domain.StudentRemarks;
import com.witcurve.domain.enumeration.ExamStatus;
import com.witcurve.repository.EventRepository;
import com.witcurve.repository.ExamRepository;
import com.witcurve.repository.StudentRemarksRepository;
import com.witcurve.service.StudentRemarksService;
import com.witcurve.service.dto.StudentRemarksDTO;
import com.witcurve.service.mapper.StudentRemarksMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class StudentRemarksServiceImpl implements StudentRemarksService {

    private final Logger log  = LoggerFactory.getLogger(StudentRemarksServiceImpl.class);

    @Autowired
    StudentRemarksMapper studentRemarksMapper;

    @Autowired
    StudentRemarksRepository studentRemarksRepository;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    EventRepository eventRepository;

    @Override
    public List<StudentRemarksDTO> saveOrUpdate(List<StudentRemarksDTO> studentRemarksDTOs, Long examId, String bindingId) {
        log.debug("Request to save student remarks : {}", studentRemarksDTOs);
        studentRemarksDTOs = validAndFormat(studentRemarksDTOs, examId, bindingId);
        List<StudentRemarks> studentRemarksList = studentRemarksMapper.toEntity(studentRemarksDTOs);
        studentRemarksList = studentRemarksRepository.saveAll(studentRemarksList);
        return studentRemarksMapper.toDto(studentRemarksList);

    }

    @Override
    public List<StudentRemarksDTO> getByBindingId(String bindingId) {
        log.debug("Request to get all student remarks with bindingId : {}", bindingId);
        List<StudentRemarks> studentRemarksList = studentRemarksRepository.findByBindingId(bindingId);
        return studentRemarksMapper.toDto(studentRemarksList);
    }

    @Override
    public List<StudentRemarksDTO> getByExamId(Long examId) {
        log.debug("Request to get all student remarks with examId : {}", examId);
        List<StudentRemarks> studentRemarksList = studentRemarksRepository.findByExamId(examId);
        return studentRemarksMapper.toDto(studentRemarksList);
    }

    @Override
    public void deleteStudentMarks(List<Long> ids) {
        log.debug("Request to delete all student remarks with ids : {}", ids);
        studentRemarksRepository.deleteByIds(ids);
    }

    private List<StudentRemarksDTO> validAndFormat(List<StudentRemarksDTO> studentRemarksDTOs, Long examId, String bindingId) {
        List<StudentRemarks> existingStudentRemarksList = new ArrayList<>();
        List<Long> requestStudentIds = new ArrayList<>();
        if(examId == null && bindingId == null) {
            throw new WitcurveException("Both examId and bindingId cannot be null");
        }
        if(examId != null && bindingId != null) {
            throw new WitcurveException("Only one of examId and bindingId can be not null");
        }
        Boolean forExam = false;
        if(examId != null) {
            Optional<Exam> exam = examRepository.findById(examId);
            if(!exam.isPresent()) {
                throw new WitcurveException("No Exam with given Id " + examId);
            }
            if(exam.get().getStatus().equals(ExamStatus.DRAFT)) {
                throw new WitcurveException("Draft exams cannot have report card design");
            }
            existingStudentRemarksList = studentRemarksRepository.findByExamId(examId);
            forExam = true;
        } else {
            List<Event> events = eventRepository.findPeriodicEventsByBindingId(bindingId);
            if(events.size() == 0) {
                throw new WitcurveException("No Periodic Test exists with given bindingId " + bindingId);
            }
            existingStudentRemarksList = studentRemarksRepository.findByBindingId(bindingId);
        }
        Map<Long, StudentRemarks> existingMap = new HashMap<>();
        for(StudentRemarks studentRemarks : existingStudentRemarksList) {
            existingMap.put(studentRemarks.getId(), studentRemarks);
        }
        for(StudentRemarksDTO studentRemarksDTO : studentRemarksDTOs) {
            if(requestStudentIds.contains(studentRemarksDTO.getStudentId())) {
                throw new WitcurveException("There should be only one record for a student in the request");
            }
            StudentRemarks existingStudentRemarks  = existingMap.get(studentRemarksDTO.getStudentId());
            if(studentRemarksDTO.getId() == null) {
                if(existingStudentRemarks != null) {
                    throw new WitcurveException("There already exists a student remarks for this student with id "+studentRemarksDTO.getStudentId()+", so new record cannot be created");
                }
            } else {
                if(existingStudentRemarks == null) {
                    throw new WitcurveException("There is no existing student marks record with this student id "+studentRemarksDTO.getStudentId()+"to update");
                } else {
                    if(!existingStudentRemarks.getId().equals(studentRemarksDTO.getId())) {
                        throw new WitcurveException("Student marks id cannot be changed while updating for student id "+studentRemarksDTO.getStudentId());
                    }
                }
            }
            if(forExam) {
                studentRemarksDTO.setExamId(examId);
                studentRemarksDTO.setBindingId(null);
            } else {
                studentRemarksDTO.setExamId(null);
                studentRemarksDTO.setBindingId(bindingId);
            }
        }
        return studentRemarksDTOs;
    }
}

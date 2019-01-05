package com.witcurve.service.impl;

import com.witcurve.domain.StudentMarks;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.repository.StudentMarksRepository;
import com.witcurve.service.StudentMarksService;
import com.witcurve.service.dto.StudentMarksDTO;
import com.witcurve.service.mapper.StudentMarksMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class StudentMarksServiceImpl implements StudentMarksService {
    private final Logger log  = LoggerFactory.getLogger(StandardServiceImpl.class);
    @Autowired
    StudentMarksMapper studentMarksMapper;

    @Autowired
    StudentMarksRepository studentMarksRepository;

    @Override
    public List<StudentMarksDTO> saveOrUpdateStudentMarks(List<StudentMarksDTO> studentMarksDTO) {
        log.debug("Request to save or update Student Marks: {}", studentMarksDTO);
        List<StudentMarks> studentMarks = studentMarksMapper.toEntity(studentMarksDTO);
        studentMarks = studentMarksRepository.saveAll(studentMarks);
        return studentMarksMapper.toDto(studentMarks);
    }

    @Override
     public StudentMarksDTO getStudentMarksById(Long studentMarksId) throws WitcurveException {
         log.debug("Request to get student Marks with id : {}", studentMarksId);
         StudentMarks studentMarks = studentMarksRepository.findByStudentMarksId(studentMarksId);
         if (studentMarks == null) {
             throw new WitcurveException("No student marks relation exits with given id");
         }
         return studentMarksMapper.toDto(studentMarks);
    }

    @Override
     public void deleteStudentMarks(Long studentMarksId) throws WitcurveException {
         log.debug("Request to delete student Marks with id {}", studentMarksId);
         StudentMarks studentMarks = studentMarksRepository.findByStudentMarksId(studentMarksId);
         if (studentMarks == null){
             throw new WitcurveException("No student Marks relation with given Id");
         }
         studentMarksRepository.delete(studentMarks);
    }

    @Override
    public List<StudentMarksDTO> getListStudentMarksByCourseTeacher(Long courseTeacherId, EventType eventType) throws WitcurveException {
        log.debug("Request to get student Marks List with event type exam or test and course teacher id : {}", courseTeacherId);
        List<StudentMarks> studentMarks= new ArrayList<>();
        if(EventType.TEST.equals(eventType) || EventType.EXAM.equals(eventType)){
            studentMarks= studentMarksRepository.getByCourseTeacherIdAndEventType(courseTeacherId,eventType) ;
        } else if(EventType.ASSIGNMENT.equals(eventType)){
            studentMarks= studentMarksRepository.getByCourseTeacherIdAndEventTypeForAssignment(courseTeacherId,eventType) ;
        }
          else  {
            throw new WitcurveException("Event type entered is neither Exam nor Test !!");
        }
        return studentMarksMapper.toDto(studentMarks);
    }

    @Override
    public Map<EventType,List<StudentMarksDTO>> getAllMarksForStudent(Long courseTeacherId,Long studentId) throws WitcurveException{
        log.debug("Request to get all marks List for test, assignment,exam of a student");
        Map<EventType,List<StudentMarksDTO>> mapMarksEventType= new HashMap<>();
        mapMarksEventType.put(EventType.TEST,studentMarksMapper.toDto(studentMarksRepository.getByCourseTeacherIdAndEventTypeAndStudentId(courseTeacherId,EventType.TEST,studentId)));
        mapMarksEventType.put(EventType.ASSIGNMENT,studentMarksMapper.toDto(studentMarksRepository.getByCourseTeacherIdAndEventTypeAndStudentIdForAssignment(courseTeacherId,EventType.ASSIGNMENT,studentId)));
        mapMarksEventType.put(EventType.EXAM,studentMarksMapper.toDto(studentMarksRepository.getByCourseTeacherIdAndEventTypeAndStudentId(courseTeacherId,EventType.EXAM,studentId)));
        return mapMarksEventType;
    }
}

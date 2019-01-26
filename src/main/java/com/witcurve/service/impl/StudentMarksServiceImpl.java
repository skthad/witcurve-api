package com.witcurve.service.impl;

import com.witcurve.domain.ExamCourseDetails;
import com.witcurve.domain.StudentMarks;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.repository.ExamCourseDetailsRepository;
import com.witcurve.repository.StudentMarksRepository;
import com.witcurve.service.EventService;
import com.witcurve.service.StudentMarksService;
import com.witcurve.service.dto.EventDTO;
import com.witcurve.service.dto.StudentMarksDTO;
import com.witcurve.service.mapper.StudentMarksMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional
public class StudentMarksServiceImpl implements StudentMarksService {
    private final Logger log  = LoggerFactory.getLogger(StandardServiceImpl.class);
    @Autowired
    StudentMarksMapper studentMarksMapper;

    @Autowired
    StudentMarksRepository studentMarksRepository;

    @Autowired
    EventService eventService;

    @Autowired
    ExamCourseDetailsRepository examCourseDetailsRepository;


    @Override
    public List<StudentMarksDTO> saveOrUpdateStudentMarks(List<StudentMarksDTO> studentMarksDTO) throws WitcurveException {
        log.debug("Request to save or update Student Marks: {}", studentMarksDTO);
        List<StudentMarks> studentMarks = studentMarksMapper.toEntity(studentMarksDTO);
        if(studentMarksDTO.size() !=0) {
            for (int i = 0; i < studentMarksDTO.size(); i++) {
                if (studentMarksDTO.get(i).getEventDTO() != null) {
                    if (studentMarksRepository.getByStudentAndTestId(studentMarksDTO.get(i).getStudentId(), studentMarksDTO.get(i).getEventDTO().getId()).size() != 0) {
                        throw new WitcurveException("Student id and event id combination already exists with marks !!");
                    }
                } else if (studentMarksDTO.get(i).getExamCourseDetailsDTO() != null) {
                    if (studentMarksRepository.getByStudentIdAndExamCourseDetailsId(studentMarksDTO.get(i).getStudentId(), studentMarksDTO.get(i).getExamCourseDetailsDTO().getId()).size() != 0) {
                        throw new WitcurveException("Student id and exam course details id combination already exists with marks !!");
                    }
                }
            }
            studentMarks = studentMarksRepository.saveAll(studentMarks);
            return studentMarksMapper.toDto(studentMarks);
        } else{
            throw new WitcurveException("student marks DTO is null !! ");
        }
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
    public List<StudentMarksDTO> getListStudentMarksInACourseTeacherByEventId(Long eventId) throws WitcurveException {
        log.debug("Request to get student Marks List with event type exam or test and event id : {}", eventId);
        List<StudentMarks> studentMarks= new ArrayList<>();
        EventDTO eventDTO= eventService.getEventById(eventId);
        if(EventType.TEST.equals(eventDTO.getType()) || EventType.ASSIGNMENT.equals(eventDTO.getType())){
            studentMarks=studentMarksRepository.getStudentMarksByEventId(eventId);
        } else  {
            throw new WitcurveException("Event id entered is neither Assignment nor Test !!");
        }
        return studentMarksMapper.toDto(studentMarks);
    }

    @Override
    public List<StudentMarksDTO> getListStudentMarksForExamInACourseTeacher(Long examCourseDetailsId) throws WitcurveException {
        log.debug("Request to get student Marks List with event type exam or test and ecd id : {}", examCourseDetailsId);
        List<StudentMarks> studentMarks= new ArrayList<>();
        Optional<ExamCourseDetails> examCourseDetails = examCourseDetailsRepository.findById(examCourseDetailsId);
        if(examCourseDetails.isPresent()) {
            studentMarks = studentMarksRepository.getStudentMarksByExamCourseDetailsId(examCourseDetailsId);
        } else  {
            throw new WitcurveException("ExamCourseDetails id entered is not EXAM type !!");
        }
        return studentMarksMapper.toDto(studentMarks);
    }

    @Override
    public Map<EventType,List<StudentMarksDTO>> getAllMarksForStudentInACourse(Long courseId, Long studentId) throws WitcurveException{
        log.debug("Request to get all marks List for test, assignment,exam of a student");
        Map<EventType,List<StudentMarksDTO>> mapMarksEventType= new HashMap<>();
        mapMarksEventType.put(EventType.TEST,studentMarksMapper.toDto(studentMarksRepository.getByCourseIdAndEventTypeAndStudentId(courseId,EventType.TEST,studentId)));
        mapMarksEventType.put(EventType.ASSIGNMENT,studentMarksMapper.toDto(studentMarksRepository.getByCourseIdAndEventTypeAndStudentIdForAssignment(courseId,EventType.ASSIGNMENT,studentId)));
        mapMarksEventType.put(EventType.EXAM,studentMarksMapper.toDto(studentMarksRepository.getStudentMarksForExamByStudentIdAndCourseId(courseId,studentId)));
        return mapMarksEventType;
    }
}

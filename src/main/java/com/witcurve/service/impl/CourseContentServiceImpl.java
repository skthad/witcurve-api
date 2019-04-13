package com.witcurve.service.impl;

import com.witcurve.domain.Course;
import com.witcurve.domain.CourseContent;
import com.witcurve.domain.Event;
import com.witcurve.domain.Exam;
import com.witcurve.repository.*;
import com.witcurve.service.CourseContentService;
import com.witcurve.service.dto.CourseContentDTO;
import com.witcurve.service.mapper.CourseContentMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseContentServiceImpl implements CourseContentService {

    private final Logger log  = LoggerFactory.getLogger(CourseContentServiceImpl.class);

    @Autowired
    CourseContentRepository courseContentRepository;

    @Autowired
    CourseContentMapper courseContentMapper;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    EventContentRepository eventContentRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ExamRepository examRepository;

    @Override
    public List<CourseContentDTO> saveOrUpdateForCourse(Long courseId, List<CourseContentDTO> courseContentDTOs) throws WitcurveException {
        log.debug("Request to save or update CourseContents");
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new WitcurveException("No Course with given id " + courseId);
        }
        for (CourseContentDTO courseContentDTO : courseContentDTOs) {
            if (!courseContentDTO.getCourseId().equals(courseId)) {
                throw new WitcurveException("CourseId provided does not match with courseId in one or more courseContent");
            }
        }
        List<CourseContent> courseContents  = courseContentMapper.toEntity(courseContentDTOs);
        return courseContentMapper.toDto(courseContentRepository.saveAll(courseContents));
    }

    @Override
    public CourseContentDTO getCourseContentById(Long courseContentId) throws WitcurveException {
        Optional<CourseContent> courseContent = courseContentRepository.findById(courseContentId);
        if (!courseContent.isPresent()) {
            throw new WitcurveException("No CourseContent with given id " + courseContentId);
        }
        return courseContentMapper.toDto(courseContent.get());
    }

    @Override
    public List<CourseContentDTO> getCourseContentsByCourseId(Long courseId) throws WitcurveException {
        log.debug("Request to get CourseContents by course ID: " + courseId);
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new WitcurveException("No Course with given id: " + courseId);
        }
        List<CourseContentDTO> courseContents = courseContentMapper.toDto(courseContentRepository.findByCourseId(courseId));
        return addIndicesToCourseContents(courseContents);
    }

    @Override
    public List<CourseContentDTO> getCourseContentsByEventId(Long eventId, Boolean forExam) throws WitcurveException {
        log.debug("Request to get CourseContents for {} with ID: {}", forExam ? "exam" : "event", eventId);
        if (forExam) {
            Optional<Exam> exam = examRepository.findById(eventId);
            if (!exam.isPresent()) {
                throw new WitcurveException("No Exam with given id " + eventId);
            }
        } else {
            Optional<Event> event = eventRepository.findById(eventId);
            if (!event.isPresent()) {
                throw new WitcurveException("No Event with given id " + eventId);
            }
        }
        List<CourseContentDTO> courseContents = courseContentMapper.toDto(eventContentRepository.findCourseContentsByEventId(eventId, forExam));
        return courseContents;
    }

    @Override
    public void deleteCourseContent(Long courseContentId) throws WitcurveException {
        log.debug("Request to delete courseContent with id {}", courseContentId);
        Optional<CourseContent> courseContent = courseContentRepository.findById(courseContentId);
        if (!courseContent.isPresent()) {
            throw new WitcurveException("No CourseContent with given id " + courseContentId);
        }
        courseContentRepository.delete(courseContent.get());
    }

    @Override
    public void deleteCourseContentsByCourseId(Long courseId) throws WitcurveException {
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new WitcurveException("No Course with given id " + courseId);
        }
        courseContentRepository.deleteInBatch(
            courseContentRepository.findAllSubTopicsInCourse(courseId));
        courseContentRepository.deleteInBatch(
            courseContentRepository.findAllTopicsInCourse(courseId));
    }

    private List<CourseContentDTO> addIndicesToCourseContents(List<CourseContentDTO> contents) {
        if (contents == null) {
            return null;
        } else if (contents.size() == 0) {
            return contents;
        } else {
            Map<Long, CourseContentDTO> parentContentMap = new HashMap<>();
            for (CourseContentDTO courseContentDTO : contents) {
                if (courseContentDTO.getParentContentId() == null) {
                    parentContentMap.put(courseContentDTO.getId(), courseContentDTO);
                }
            }
            log.info("No. of course contents: " + contents.size());
            log.info("No. of parent contents: " + parentContentMap.keySet().size());
            for (CourseContentDTO courseContentDTO : contents) {
                if (courseContentDTO.getParentContentId() == null) {
                    log.info("Index of parent content: " + courseContentDTO.getContentOrder().toString());
                    courseContentDTO.setIndex(courseContentDTO.getContentOrder().toString());
                } else {
                    log.info("Index of child content: " + courseContentDTO.getContentOrder().toString());
                    courseContentDTO.setIndex(
                        parentContentMap.get(courseContentDTO.getParentContentId())
                            .getContentOrder() + "." + courseContentDTO.getContentOrder());
                }
            }
            log.info("No. of course contents: " + contents.size());
            return contents.stream().sorted(Comparator.comparing(CourseContentDTO::getIndex)).collect(Collectors.toList());
        }
    }
}

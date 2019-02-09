package com.witcurve.service.impl;

import com.witcurve.domain.Course;
import com.witcurve.domain.CourseContent;
import com.witcurve.domain.Event;
import com.witcurve.repository.CourseContentRepository;
import com.witcurve.repository.CourseRepository;
import com.witcurve.repository.EventContentRepository;
import com.witcurve.repository.EventRepository;
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
        Map<Long, CourseContentDTO> parentContentMap = new HashMap<>();
        for (CourseContentDTO courseContentDTO : courseContents) {
            if (courseContentDTO.getParentContentId() == null) {
                parentContentMap.put(courseContentDTO.getId(), courseContentDTO);
            }
        }
        log.info("no. of course contents: " + courseContents.size());
        log.info("no. of course contents in map: " + parentContentMap.keySet().size());
        for (CourseContentDTO courseContentDTO : courseContents) {
            if (courseContentDTO.getParentContentId() == null) {
                log.info("index: " + courseContentDTO.getContentOrder().toString());
                courseContentDTO.setIndex(courseContentDTO.getContentOrder().toString());
            } else {
                courseContentDTO.setIndex(
                    parentContentMap.get(courseContentDTO.getParentContentId())
                        .getContentOrder() + "." + courseContentDTO.getContentOrder());
            }
        }
        log.info("no. of course contents: " + courseContents.size());
        return courseContents.stream().sorted(Comparator.comparing(CourseContentDTO::getIndex)).collect(Collectors.toList());
    }

    @Override
    public List<CourseContentDTO> getCourseContentsByEventId(Long eventId) throws WitcurveException {
        log.debug("Request to get EventContents by event ID: " + eventId);
        Optional<Event> event = eventRepository.findById(eventId);
        if (!event.isPresent()) {
            throw new WitcurveException("No Event with given id " + eventId);
        }
        List<CourseContent> courseContents = eventContentRepository.findCourseContentsByEventId(eventId);
        return courseContentMapper.toDto(courseContents);
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
}

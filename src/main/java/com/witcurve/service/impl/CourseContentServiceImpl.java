package com.witcurve.service.impl;

import com.witcurve.domain.*;
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
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    ExamCourseDetailsRepository examCourseDetailsRepository;

    @Override
    public List<CourseContentDTO> saveOrUpdateForCourse(Long courseId, List<CourseContentDTO> courseContentDTOs) throws WitcurveException {
        log.debug("Request to save or update CourseContents");
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new WitcurveException("No Course with given id " + courseId);
        }
        if (Boolean.TRUE.equals(course.get().getContentPublished())) {
            throw new WitcurveException("Course content for this course is published. Cannot add or update anymore");
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
    public CourseContentDTO updateSingleCourseContent(Long courseId, CourseContentDTO courseContentDTO) throws WitcurveException {
        log.debug("Request to update single CourseContent");
        Optional<CourseContent> courseContent = courseContentRepository.findById(courseContentDTO.getId());
        if (!courseContent.isPresent()) {
            throw new WitcurveException("No CourseContent with given id " + courseContentDTO.getId());
        }
        if (!courseId.equals(courseContentDTO.getCourseId())) {
            throw new WitcurveException("Course ID in existing courseContent does not match the given course ID: " + courseId);
        }
        if (Boolean.TRUE.equals(courseContent.get().getCourse().getContentPublished())) {
            throw new WitcurveException("Course content for this course is published. Cannot update anymore");
        }
        CourseContent existingContent = courseContent.get();
        existingContent.setContentName(courseContentDTO.getContentName());
        existingContent.setDescription(courseContentDTO.getDescription());

        return courseContentMapper.toDto(existingContent);
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
    public List<CourseContentDTO> getCourseContentsByCourseId(Long courseId, Boolean admin) throws WitcurveException {
        log.debug("Request to get CourseContents by course ID: " + courseId);
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            return new ArrayList<>();
        }
        if (!Boolean.TRUE.equals(admin) && !Boolean.TRUE.equals(course.get().getContentPublished())) {
            return new ArrayList<>();
        }
        List<CourseContentDTO> courseContents = courseContentMapper.toDto(courseContentRepository.findByCourseId(courseId));
        return addIndicesToCourseContents(courseContents);
    }

    @Override
    public List<CourseContentDTO> getCourseContentsByEventId(Long eventId, Boolean forExam) throws WitcurveException {
        log.debug("Request to get CourseContents for {} with ID: {}", forExam ? "exam" : "event", eventId);
        Long courseId;
        if (forExam) {
            Optional<ExamCourseDetails> ecd = examCourseDetailsRepository.findById(eventId);
            if (!ecd.isPresent()) {
                throw new WitcurveException("No ECD with given id " + eventId);
            }
            courseId = ecd.get().getCourse().getId();
        } else {
            Optional<Event> event = eventRepository.findById(eventId);
            if (!event.isPresent()) {
                throw new WitcurveException("No Test/Assignment with given id " + eventId);
            }

            switch (event.get().getType()) {
                case ASSIGNMENT:
                    if (event.get().getCourseTeacher() == null) {
                        throw new WitcurveException("Event ID: " + eventId + " is not linked to any course and/or teacher");
                    }
                    courseId = event.get().getCourseTeacher().getCourse().getId();
                    break;
                case TEST:
                    if (event.get().getScd() == null || event.get().getScd().getCourseTeacher() == null) {
                        throw new WitcurveException("Event ID: " + eventId + " is not linked to any course and/or teacher");
                    }
                    courseId = event.get().getScd().getCourseTeacher().getCourse().getId();
                    break;
                default:
                    throw new WitcurveException("No Test/Assignment with given id " + eventId);
            }
        }
        Course course = courseRepository.findById(courseId).get();
        if (!Boolean.TRUE.equals(course.getContentPublished())) {
            return new ArrayList<>();
        }
        List<CourseContentDTO> courseContents = addIndicesToCourseContents(courseContentMapper.toDto(courseContentRepository.findByCourseId(courseId)));
        Map<Long, String> courseContentIndexMap = new HashMap<>();
        for (CourseContentDTO cc : courseContents) {
            courseContentIndexMap.put(cc.getId(), cc.getIndex());
        }

        List<CourseContentDTO> result;
        if (forExam) {
            result = courseContentMapper.toDto(eventContentRepository.findCourseContentsByEcdId(eventId));
        } else {
            result = courseContentMapper.toDto(eventContentRepository.findCourseContentsByEventId(eventId));
        }

        for (CourseContentDTO cc : result) {
            cc.setIndex(courseContentIndexMap.get(cc.getId()));
        }
        return result.stream().sorted(Comparator.comparing(CourseContentDTO::getIndex)).collect(Collectors.toList());
    }

    @Override
    public void deleteCourseContent(Long courseContentId) throws WitcurveException {
        log.debug("Request to delete courseContent with id {}", courseContentId);
        Optional<CourseContent> courseContent = courseContentRepository.findById(courseContentId);
        if (!courseContent.isPresent()) {
            throw new WitcurveException("No CourseContent with given id " + courseContentId);
        }
        if (Boolean.TRUE.equals(courseContent.get().getCourse().getContentPublished())) {
            throw new WitcurveException("Course content for this course is published. Cannot delete anymore");
        }
        CourseContent existingCourseContent = courseContent.get();
        Long courseId = existingCourseContent.getCourse().getId();
        Integer contentOrder = existingCourseContent.getContentOrder();
        CourseContent parentContent = existingCourseContent.getParentContent();

        List<CourseContent> laterCourseContents;
        if (parentContent != null) {
            laterCourseContents = courseContentRepository.findLaterSubtopics(courseId, contentOrder, parentContent.getId());
        } else {
            laterCourseContents = courseContentRepository.findLaterTopics(courseId, contentOrder);
            List<CourseContent> subTopics = courseContentRepository.findByCourseIdAndParentContentId(courseId, existingCourseContent.getId());
            if (!CollectionUtils.isEmpty(subTopics)) {
                eventContentRepository.deleteByCourseContentIds(subTopics.stream().map(CourseContent::getId).collect(Collectors.toList()));
                courseContentRepository.deleteAll(subTopics);
            }
        }
        eventContentRepository.deleteByCourseContentId(existingCourseContent.getId());
        courseContentRepository.delete(existingCourseContent);
        if (laterCourseContents.size() > 0) {
            for (CourseContent lc : laterCourseContents) {
                lc.setContentOrder(lc.getContentOrder() - 1);
            }
        }
    }

    @Override
    public void deleteCourseContentsByCourseId(Long courseId) throws WitcurveException {
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new WitcurveException("No Course with given id " + courseId);
        }
        if (Boolean.TRUE.equals(course.get().getContentPublished())) {
            throw new WitcurveException("Course content for this course is published. Cannot delete course contents");
        }
        List<CourseContent> subTopics = courseContentRepository.findAllSubTopicsInCourse(courseId);
        List<CourseContent> topics = courseContentRepository.findAllTopicsInCourse(courseId);

        List<Long> courseContentIds = Stream.concat(subTopics.stream(), topics.stream()).map(CourseContent::getId)
            .collect(Collectors.toList());
        eventContentRepository.deleteByCourseContentIds(courseContentIds);

        courseContentRepository.deleteInBatch(subTopics);
        courseContentRepository.deleteInBatch(topics);
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

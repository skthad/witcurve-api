package com.witcurve.service.impl;

import com.witcurve.domain.Course;
import com.witcurve.domain.CourseContent;
import com.witcurve.repository.CourseContentRepository;
import com.witcurve.repository.CourseRepository;
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

    @Override
    public List<CourseContentDTO> saveOrUpdateForCourse(Long courseId, List<CourseContentDTO> courseContentDTOs) throws WitcurveException {
        log.debug("Request to save or update CourseContents");
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new WitcurveException("No Course with given id " + courseId);
        }
        for (CourseContentDTO courseContentDTO : courseContentDTOs) {
            courseContentDTO.setCourseId(courseId);
        }
        List<CourseContent> courseContents  = courseContentMapper.toEntity(courseContentDTOs);
        courseContents  = courseContentRepository.saveAll(courseContents);
        return courseContentMapper.toDto(courseContents );
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
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new WitcurveException("No Course with given id " + courseId);
        }
        List<CourseContentDTO> courseContents = courseContentMapper.toDto(courseContentRepository.findByCourseId(courseId));
        Map<Long, CourseContentDTO> parentContentMap = new HashMap<>();
        for (CourseContentDTO courseContentDTO : courseContents) {
            if (courseContentDTO.getParentContentId() == null) {
                parentContentMap.put(courseContentDTO.getId(), courseContentDTO);
            }
        }
        for (CourseContentDTO courseContentDTO : courseContents) {
            if (courseContentDTO.getParentContentId() == null) {
                courseContentDTO.setIndex(courseContentDTO.getContentOrder().toString());
            } else {
                courseContentDTO.setIndex(
                    parentContentMap.get(courseContentDTO.getParentContentId())
                        .getContentOrder() + "." + courseContentDTO.getContentOrder());
            }
        }
        return courseContents.stream().sorted(Comparator.comparing(CourseContentDTO::getIndex)).collect(Collectors.toList());
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
    public void deleteCourseContentByCourseId(Long courseId) throws WitcurveException {
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new WitcurveException("No Course with given id " + courseId);
        }
        //courseContentRepository.deleteByParentContentIdNotNullAndCourseId();
    }
}

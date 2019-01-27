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
    public List<CourseContentDTO> saveOrUpdate(List<CourseContentDTO> courseContentDTOs) {
        log.debug("Request to save or update CourseContents");

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
    public Map<CourseContentDTO, List<CourseContentDTO>> getCourseContentsByCourseId(Long courseId) throws WitcurveException {
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new WitcurveException("No Course with given id " + courseId);
        }
        List<CourseContentDTO> courseContents = courseContentMapper.toDto(courseContentRepository.findByCourseId(courseId));
        Map<Long, CourseContentDTO> parentContentMap = new HashMap<>();
        for (CourseContentDTO cc : courseContents) {
            if (cc.getParentContentId() == null) {
                parentContentMap.put(cc.getId(), cc);
            }
        }
        Map<CourseContentDTO, List<CourseContentDTO>> contentMap = new HashMap<>();
        for (CourseContentDTO cc : courseContents) {
            Long parentContentId = cc.getParentContentId();
            if (parentContentId != null) {
                CourseContentDTO parentContent = parentContentMap.get(parentContentId);
                if (contentMap.get(parentContent) == null) {
                    parentContent.setIndex(parentContent.getContentOrder().toString());
                    contentMap.put(parentContent, new ArrayList<>());
                }
                String index = parentContent.getContentOrder() + "." + cc.getContentOrder();
                cc.setIndex(index);
                contentMap.get(parentContent).add(cc);
            }
        }
        return contentMap;
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
}

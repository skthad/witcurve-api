package com.witcurve.service.impl;

import com.witcurve.service.AnalyticsService;
import com.witcurve.service.StudentMarksService;
import com.witcurve.service.StudentService;
import com.witcurve.service.dto.StudentMarksDTO;
import com.witcurve.service.dto.SectionPerformanceDTO;
import com.witcurve.service.dto.SubjectPerformanceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class AnalyticsServiceImpl implements AnalyticsService {

    private final Logger log = LoggerFactory.getLogger(AnalyticsServiceImpl.class);

    @Autowired
    private StudentMarksService studentMarksService;

    @Autowired
    private StudentService studentService;

    @Override
    public List<SectionPerformanceDTO> getSectionPerformance(Long examId) {
        Map<String, Map<String, Double>> sectionSubjectTotalMap = new HashMap<>();
        Map<String, Map<String, Integer>> sectionSubjectCountMap = new HashMap<>();
        Map<String, Integer> subjectFullMarksMap = new HashMap<>();
        Map<String, Long> sectionStandardIdMap = new HashMap<>();

        List<SectionPerformanceDTO> sectionPerformanceList = new ArrayList<>();

        List<StudentMarksDTO> studentMarksDTOList = studentMarksService.getStudentMarksByExamId(examId, null, null);

        for (StudentMarksDTO studentMarksDTO : studentMarksDTOList) {
            String masterSubject = studentMarksDTO.getExamCourseDetailsDTO().getCourse().getMasterSubject();
            String sectionName = studentMarksDTO.getSection();

            if (sectionSubjectCountMap.get(sectionName) == null) {
                sectionSubjectTotalMap.put(sectionName, new HashMap<>());
                sectionSubjectCountMap.put(sectionName, new HashMap<>());
                sectionStandardIdMap.put(sectionName, studentMarksDTO.getStandardId());
            }

            Map<String, Double> subjectTotalMap = sectionSubjectTotalMap.get(sectionName);
            Map<String, Integer> subjectCountMap = sectionSubjectCountMap.get(sectionName);

            if (subjectCountMap.get(masterSubject) == null) {
                subjectTotalMap.put(masterSubject, 0.0);
                subjectCountMap.put(masterSubject, 0);
                subjectFullMarksMap.put(masterSubject, studentMarksDTO.getExamCourseDetailsDTO().getFullMarks());
            }

            Double subjectTotal = subjectTotalMap.get(masterSubject);
            Integer subjectCount = subjectCountMap.get(masterSubject);

            subjectCount++;
            subjectTotal += studentMarksDTO.getMarks();

            subjectTotalMap.put(masterSubject, subjectTotal);
            subjectCountMap.put(masterSubject, subjectCount);

        }

        //TODO:fix the average of average logic by taking total marks into account for subject
        for (String sectionName: sectionSubjectCountMap.keySet()) {
            Double totalScore = 0.0;

            SectionPerformanceDTO sectionPerformanceDTO = new SectionPerformanceDTO();

            Map<String, Double> subjectTotalMap = sectionSubjectTotalMap.get(sectionName);
            Map<String, Integer> subjectCountMap = sectionSubjectCountMap.get(sectionName);

            for (String subjectName: subjectTotalMap.keySet()) {
                Integer studentCount = subjectCountMap.get(subjectName);
                Integer fullMarks = subjectFullMarksMap.get(subjectName);
                Double averagePercentage = roundOff((subjectTotalMap.get(subjectName)*100)/ (studentCount*fullMarks));

                SubjectPerformanceDTO subjectPerformanceDTO = new SubjectPerformanceDTO();
                subjectPerformanceDTO.setSubjectName(subjectName);
                subjectPerformanceDTO.setAverageScore(averagePercentage);
                subjectPerformanceDTO.setStudentCount(studentCount);

                sectionPerformanceDTO.getSubjectPerformances().add(subjectPerformanceDTO);

                totalScore += averagePercentage;
            }

            sectionPerformanceDTO.setSectionName(sectionName);
            sectionPerformanceDTO.setAverageScore(roundOff(totalScore/ sectionPerformanceDTO.getSubjectPerformances().size()));
            sectionPerformanceDTO.setStudentCount(studentService.getStudentsByStandardId(sectionStandardIdMap.get(sectionName)).size());

            sectionPerformanceList.add(sectionPerformanceDTO);
        }
        return sectionPerformanceList;
    }

    private Double roundOff(Double value) {
        return (double) Math.round(value * 100) / 100;
    }

}

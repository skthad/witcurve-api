package com.witcurve.service.mapper;

import com.witcurve.domain.Exam;
import com.witcurve.domain.GeneralSlotDetails;
import com.witcurve.domain.ReportCard;
import com.witcurve.domain.ReportCardDesign;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.dto.ExamDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@Mapper(componentModel = "spring", uses = {SchoolInfoMapper.class})
public interface ExamMapper extends EntityMapper<ExamDTO, Exam> {

    @Mapping(source = "schoolInfoId", target = "schoolInfo")
    @Mapping(ignore = true, target = "generalSlotDetails")
    Exam toEntity(ExamDTO examDTO);

    @Mapping(target = "schoolInfoId", source = "schoolInfo.id")
    @Mapping(target = "grades", expression = "java(getGrades(exam.getGeneralSlotDetails()))")
    @Mapping(target = "mainGrades", expression = "java(getGradesFromRcds(exam.getMainReportCardDesigns()))")
    @Mapping(target = "attributesGrades", expression = "java(getGradesFromRcds(exam.getAttributeReportCardDesigns()))")
    @Mapping(target = "remarksGrades", expression = "java(getGradesFromRcds(exam.getRemarkReportCardDesigns()))")
    @Mapping(target = "reportCardGrades", expression = "java(getGradeFromReportCard(exam.getReportCards()))")
    ExamDTO toDto(Exam exam);


    default Exam fromId(Long id) {
        if(id == null) {
            return null;
        }
        Exam exam = new Exam();
        exam.setId(id);
        return exam;
    }

    default Set<Grade> getGrades(Set<GeneralSlotDetails> examGsds) {
        if(examGsds == null || examGsds.size()==0) {
            return null;
        }
        SortedSet<Grade> grades = new TreeSet<>();
        for(GeneralSlotDetails generalSlotDetails : examGsds) {
            grades.add(generalSlotDetails.getGrade());
        }
        return grades;
     }

    default Set<Grade> getGradesFromRcds(Set<ReportCardDesign> rcds) {
        if(rcds == null || rcds.size()==0) {
            return null;
        }
        SortedSet<Grade> grades = new TreeSet<>();
        for(ReportCardDesign reportCardDesign : rcds) {
            grades.add(reportCardDesign.getGrade());
        }
        return grades;
    }

    default Set<Grade> getGradeFromReportCard(Set<ReportCard> reportCards) {
        if(reportCards ==null || reportCards.size()==0) {
            return null;
        }
        SortedSet<Grade> grades = new TreeSet<>();
        for(ReportCard reportCard : reportCards) {
            grades.add(reportCard.getGrade());
        }
        return grades;
    }
}

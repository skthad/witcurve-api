package com.witcurve.service.impl;

import com.witcurve.domain.Exam;
import com.witcurve.domain.GeneralSlotDetails;
import com.witcurve.domain.enumeration.ExamStatus;
import com.witcurve.domain.enumeration.GSDStatus;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.repository.ExamCourseDetailsRepository;
import com.witcurve.repository.ExamRepository;
import com.witcurve.repository.GeneralSlotDetailsRepository;
import com.witcurve.repository.StandardRepository;
import com.witcurve.service.GeneralSlotDetailsService;
import com.witcurve.service.dto.GeneralSlotDetailsDTO;
import com.witcurve.service.mapper.GeneralSlotDetailsMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional
public class GeneralSlotDetailsServiceImpl implements GeneralSlotDetailsService {

    private final Logger log  = LoggerFactory.getLogger(GeneralSlotDetailsServiceImpl.class);

    @Autowired
    private GeneralSlotDetailsMapper generalSlotDetailsMapper;

    @Autowired
    private GeneralSlotDetailsRepository generalSlotDetailsRepository;

    @Autowired
    private StandardRepository standardRepository;

    @Autowired
    private ExamCourseDetailsRepository examCourseDetailsRepository;

    @Autowired
    private ExamRepository examRepository;

    @Override
    public List<GeneralSlotDetailsDTO> createGSDs(List<GeneralSlotDetailsDTO> generalSlotDetailsDTOs) throws WitcurveException {
        log.debug("Request to create generalSlotDetails");
        Map<Long, String> standardIdBindingValueMap = new HashMap<>();
        for (GeneralSlotDetailsDTO gsd : generalSlotDetailsDTOs) {
            Long standardId = gsd.getStandard().getId();
            if (standardId == null) {
                throw new WitcurveException("Standard ID must be provided to create GSDs");
            }
            //gsd.setStartTime(gsd.getHours()*100 + gsd.getMinutes());
            if (standardIdBindingValueMap.get(standardId) == null) {
                String bindingId = UUID.randomUUID().toString();
                standardIdBindingValueMap.put(standardId, bindingId);
            }
            gsd.setBindingId(standardIdBindingValueMap.get(standardId));
        }
        generalSlotDetailsRepository.deactivateSlotDetailsForStandards(standardIdBindingValueMap.keySet());

        List<GeneralSlotDetails> generalSlotDetails = generalSlotDetailsMapper.toEntity(generalSlotDetailsDTOs);
        List<GeneralSlotDetailsDTO> slots = generalSlotDetailsMapper.toDto(generalSlotDetailsRepository.saveAll(generalSlotDetails));
        return slots;
    }

    @Override
    public List<GeneralSlotDetailsDTO> createOrUpdateExamSlots(List<GeneralSlotDetailsDTO> generalSlotDetailsDTOs) throws WitcurveException {
        log.debug("Request to create exam slots");
        Map<Grade, String> gradeBindingValueMap = new HashMap<>();
        for (GeneralSlotDetailsDTO gsd : generalSlotDetailsDTOs) {
            Grade grade = gsd.getGrade();
            Long examId = gsd.getExamId();
            if (grade == null || examId == null) {
                throw new WitcurveException("Grade and examId must be provided to create exam slots");
            }
            if (gradeBindingValueMap.get(grade) == null) {
                String bindingId = UUID.randomUUID().toString();
                gradeBindingValueMap.put(grade, bindingId);
            }
            gsd.setBindingId(gradeBindingValueMap.get(grade));
        }

        List<GeneralSlotDetails> generalSlotDetails = generalSlotDetailsMapper.toEntity(generalSlotDetailsDTOs);
        List<GeneralSlotDetailsDTO> slots = generalSlotDetailsMapper.toDto(generalSlotDetailsRepository.saveAll(generalSlotDetails));
        return slots;
    }

    @Override
    public List<GeneralSlotDetailsDTO> update(List<GeneralSlotDetailsDTO> generalSlotDetailsDTOs) {
        log.debug("Request to update generalSlotDetails");
        for (GeneralSlotDetailsDTO gsd : generalSlotDetailsDTOs) {
            //gsd.setStartTime(gsd.getHours()*100 + gsd.getMinutes());
        }
        List<GeneralSlotDetails> generalSlotDetails = generalSlotDetailsMapper.toEntity(generalSlotDetailsDTOs);
        List<GeneralSlotDetailsDTO> slots = generalSlotDetailsMapper.toDto(generalSlotDetailsRepository.saveAll(generalSlotDetails));
        return slots;
    }

    @Override
    public void activateGSDsForStandard(Long standardId, String bindingId) {
        generalSlotDetailsRepository.deactivateSlotDetailsForStandards(new HashSet<>(Arrays.asList(standardId)));
        generalSlotDetailsRepository.activateSlotDetailsForStandard(standardId, bindingId);
    }

    @Override
    public void deactivateGSDsForStandards(List<Long> standardIds) {
        generalSlotDetailsRepository.deactivateSlotDetailsForStandards(new HashSet<>(standardIds));
    }

    @Override
    public void cloneGSDs(Long sourceStandardId, List<Long> destinationStandardIds) throws WitcurveException {
        log.debug("Request to clone generalSlotDetails ");
        List<GeneralSlotDetails> existingSlots = generalSlotDetailsRepository.findGSDsByStandardIdAndStatus(sourceStandardId, GSDStatus.ACTIVE);

        if (existingSlots.size() == 0) {
            throw new WitcurveException("No ACTIVE exam slots found for standard ID: "  + sourceStandardId);
        }
        generalSlotDetailsRepository.deactivateSlotDetailsForStandards(new HashSet<>(destinationStandardIds));

        List<GeneralSlotDetailsDTO> slotsToCreate = new ArrayList<>();
        for (Long destinationStandardId : destinationStandardIds) {
            String bindingId = UUID.randomUUID().toString();
            for (GeneralSlotDetailsDTO slot : generalSlotDetailsMapper.toDto(existingSlots)) {
                slot.setId(null);
                slot.getStandard().setId(destinationStandardId);
                slot.setBindingId(bindingId);
                slotsToCreate.add(slot);
            }
        }
        generalSlotDetailsRepository.saveAll(generalSlotDetailsMapper.toEntity(slotsToCreate));

    }

    @Override
    public void cloneExamSlots(Grade sourceGrade, List<Grade> destinationGrades, Long examId) throws WitcurveException {
        log.debug("Request to clone generalSlotDetails ");
        List<GeneralSlotDetails> existingSlots = generalSlotDetailsRepository.findExamSlotsByGradeAndExamId(sourceGrade, examId);

        if (existingSlots.size() == 0) {
            throw new WitcurveException("No exam slots found for grade: "  + sourceGrade + " with exam ID: " + examId);
        }

        List<GeneralSlotDetailsDTO> slotsToCreate = new ArrayList<>();
        for (Grade destinationGrade : destinationGrades) {
            //TODO: logic for overlap
            String bindingId = UUID.randomUUID().toString();
            for (GeneralSlotDetailsDTO slot : generalSlotDetailsMapper.toDto(existingSlots)) {
                slot.setId(null);
                slot.setGrade(destinationGrade);
                slot.setBindingId(bindingId);
                slotsToCreate.add(slot);
            }
        }
        generalSlotDetailsRepository.saveAll(generalSlotDetailsMapper.toEntity(slotsToCreate));
    }

    @Override
    public GeneralSlotDetailsDTO getGeneralSlotDetailsById(Long generalSlotDetailsId) throws WitcurveException {
        log.debug("Request to get generalSlotDetail by id : {}", generalSlotDetailsId);
        Optional<GeneralSlotDetails> generalSlotDetails = generalSlotDetailsRepository.findById(generalSlotDetailsId);
        if(generalSlotDetails.isPresent()) {
            throw new WitcurveException("No GeneraSlotDetails exist for given id");
        }
        return generalSlotDetailsMapper.toDto(generalSlotDetails.get());
    }

    @Override
    public List<GeneralSlotDetailsDTO> getGeneralSlotDetailsByStandardId(Long standardId, GSDStatus status) throws WitcurveException {
        log.debug("Request to get generalSlotDetails by standard id : {}", standardId);
        List<GeneralSlotDetails> gsdList;

        if (status == null) {
            gsdList = generalSlotDetailsRepository.findGSDsByStandardId(standardId);
        } else {
            gsdList = generalSlotDetailsRepository.findGSDsByStandardIdAndStatus(standardId, status);
        }
        return generalSlotDetailsMapper.toDto(gsdList);
    }

    @Override
    public List<GeneralSlotDetailsDTO> getExamSlotsByExamAndGrade(Long examId, Grade grade) throws WitcurveException {
        log.debug("Request to get generalSlotDetails by exam with id : {} for grade : {}", examId, grade);
        List<GeneralSlotDetails> gsdList;
        if(grade == null) {
            gsdList = generalSlotDetailsRepository.findExamSlotsByExamId(examId);
        } else {
            gsdList = generalSlotDetailsRepository.findExamSlotsByGradeAndExamId(grade, examId);
        }
        return generalSlotDetailsMapper.toDto(gsdList);
    }

    @Override
    public void deleteGSDsByBindingId(String bindingId) throws WitcurveException {
        generalSlotDetailsRepository.deleteByBindingId(bindingId);
    }

    @Override
    public void deleteExamSlotsByGradesAndExamId(List<Grade> grades, Long examId) throws WitcurveException {
        Optional<Exam> exam = examRepository.findById(examId);
        if (!exam.isPresent()) {
            throw  new WitcurveException("No Exam with given Id " + examId);
        }
        if(!ExamStatus.DRAFT.equals(exam.get().getStatus())) {
            throw new WitcurveException("Only DRAFT exams slots can be deleted");
        }
        generalSlotDetailsRepository.deleteByGradeAndExamId(grades, examId);
    }

    @Override
    public void deleteExamSlotsByIds(List<Long> gsdIds) {
        log.debug("Delete Exam slots with gsd ids : {}", gsdIds);
        for(Long gsdId : gsdIds) {
            Optional<GeneralSlotDetails> generalSlotDetails = generalSlotDetailsRepository.findById(gsdId);
            if(!generalSlotDetails.isPresent() && generalSlotDetails.get().getExam() != null) {
                throw new WitcurveException("There doesn't exist an exam slot with given id");
            }
            Exam exam = generalSlotDetails.get().getExam();
            if(!ExamStatus.DRAFT.equals(exam.getStatus())) {
                throw new WitcurveException("Only DRAFT exams slots can be deleted");
            }
            generalSlotDetailsRepository.delete(generalSlotDetails.get());
            examCourseDetailsRepository.deleteByGsdId(generalSlotDetails.get().getId());
        }


    }


}

package com.witcurve.service.mapper;

import com.witcurve.domain.SessionFeeDescription;
import com.witcurve.domain.SessionFeeStructure;
import com.witcurve.service.dto.SessionFeeStructureDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AcademicSessionMapper.class, FeeDetailsMapper.class, SessionFeeDescriptionMapper.class})
public interface SessionFeeStructureMapper extends EntityMapper<SessionFeeStructureDTO, SessionFeeStructure> {

    @Mapping(source = "sessionId", target = "session.id")
    @Mapping(source = "feeTypeId", target = "feeType.id")
    SessionFeeStructure toEntity(SessionFeeStructureDTO sessionFeeStructureDTO);


    @Mapping(target = "sessionId", source = "session.id")
    @Mapping(target = "feeTypeId", source = "feeType.id")
    @Mapping(target = "amount", expression = "java(getTotalFeeDescriptionAmount(sessionFeeStructure.getSessionFeeDescriptions()))")
    SessionFeeStructureDTO toDto(SessionFeeStructure sessionFeeStructure);

    default SessionFeeStructure fromId(Long id) {
        if (id == null) {
            return null;
        }
        SessionFeeStructure sessionFeeStructure = new SessionFeeStructure();
        sessionFeeStructure.setId(id);
        return sessionFeeStructure;
    }

    default Double getTotalFeeDescriptionAmount(List<SessionFeeDescription> feeDescriptions) {
        if (feeDescriptions == null || feeDescriptions.size() == 0) {
            return 0.0;
        }
        Double amount = 0.0;
        for (SessionFeeDescription feeDescription : feeDescriptions) {
            amount = amount + feeDescription.getAmount();
        }
        return amount;
    }
}

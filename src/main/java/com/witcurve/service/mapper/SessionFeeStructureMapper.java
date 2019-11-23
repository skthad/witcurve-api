package com.witcurve.service.mapper;

import com.witcurve.domain.SessionFeeStructure;
import com.witcurve.service.dto.SessionFeeStructureDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AcademicSessionMapper.class, FeeDetailsMapper.class, SessionFeeDescriptionMapper.class})
public interface SessionFeeStructureMapper extends EntityMapper<SessionFeeStructureDTO, SessionFeeStructure> {

    @Mapping(source = "sessionId", target = "session.id")
    @Mapping(source = "feeTypeId", target = "feeType.id")
    SessionFeeStructure toEntity(SessionFeeStructureDTO sessionFeeStructureDTO);


    @Mapping(target = "sessionId", source = "session.id")
    @Mapping(target = "feeTypeId", source = "feeType.id")
    SessionFeeStructureDTO toDto(SessionFeeStructure sessionFeeStructure);

    default SessionFeeStructure fromId(Long id) {
        if (id == null) {
            return null;
        }
        SessionFeeStructure sessionFeeStructure = new SessionFeeStructure();
        sessionFeeStructure.setId(id);
        return sessionFeeStructure;
    }
}

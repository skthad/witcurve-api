package com.witcurve.service.mapper;

import com.witcurve.domain.GeneralSlotDetails;
import com.witcurve.domain.Standard;
import com.witcurve.service.dto.GeneralSlotDetailsDTO;
import com.witcurve.service.dto.StandardDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ExamMapper.class})
public interface GeneralSlotDetailsMapper extends EntityMapper<GeneralSlotDetailsDTO, GeneralSlotDetails> {


    @Mapping(source = "exam.id", target = "examId")
    @Mapping(source = "exam.name", target = "examName")
    GeneralSlotDetailsDTO toDto(GeneralSlotDetails generalSlotDetails);

    @Mapping(target = "exam", source = "examId")
    GeneralSlotDetails toEntity(GeneralSlotDetailsDTO generalSlotDetailsDTO);

    default GeneralSlotDetails fromId(Long id) {
        if(id == null) {
            return  null;
        }
        GeneralSlotDetails generalSlotDetails = new GeneralSlotDetails();
        generalSlotDetails.setId(id);
        return generalSlotDetails;
    }

    default Standard toStandard(StandardDTO standardDTO) {
        if (standardDTO == null) {
            return null;
        }
        Standard standard = new Standard();
        standard.setId(standardDTO.getId());
        return standard;
    }

    default StandardDTO toStandardDTO(Standard standard) {
        if (standard == null) {
            return null;
        }
        StandardDTO standardDTO = new StandardDTO();
        standardDTO.setId(standard.getId());
        return standardDTO;
    }
}

package com.witcurve.service.mapper;

import com.witcurve.domain.Substitution;
import com.witcurve.service.dto.SubstitutionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {StaffMapperLite.class, SlotCourseDetailsMapper.class})
public interface SubstitutionMapper extends EntityMapper<SubstitutionDTO, Substitution>{

    SubstitutionDTO toDto(Substitution subsititution);

    Substitution toEntity(SubstitutionDTO substitutionDTO);

    default Substitution fromId(Long id) {
        if(id == null) {
            return  null;
        }
        Substitution substitution = new Substitution();
        substitution.setId(id);
        return substitution;
    }


}

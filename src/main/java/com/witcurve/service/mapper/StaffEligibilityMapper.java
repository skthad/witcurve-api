package com.witcurve.service.mapper;

import com.witcurve.domain.StaffEligibility;
import com.witcurve.service.dto.StaffEligibilityDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StaffMapperLite.class})
public interface StaffEligibilityMapper extends EntityMapper<StaffEligibilityDTO, StaffEligibility> {

    @Mapping(source = "masterSubject.name", target = "masterSubject")
    StaffEligibilityDTO toDto(StaffEligibility staffEligibility);

    @Mapping(target = "masterSubject.name", source = "masterSubject")
    StaffEligibility toEntity(StaffEligibilityDTO staffEligibilityDTO);

    default StaffEligibility fromId(Long staffEligibilityId) {
        if(staffEligibilityId == null) {
            return null;
        }
        StaffEligibility staffEligibility = new StaffEligibility();
        staffEligibility.setId(staffEligibilityId);
        return staffEligibility;
    }
}

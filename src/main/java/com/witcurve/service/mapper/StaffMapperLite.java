package com.witcurve.service.mapper;

import com.witcurve.domain.Staff;
import com.witcurve.service.dto.StaffDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StandardMapper.class, UserMapper.class})
public interface StaffMapperLite extends EntityMapper<StaffDTO, Staff> {

    @Mapping(target = "schoolId", source = "school.id")
    @Mapping(target = "user", ignore = true)
    StaffDTO toDto(Staff staff);

    @Mapping(target = "school.id", source = "schoolId")
    @Mapping(target = "user", ignore = true)
    Staff toEntity(StaffDTO staffDTO);

    default Staff fromId(Long id) {
        if(id == null) {
            return null;
        }
        Staff staff = new Staff();
        staff.setId(id);
        return  staff;
    }
}


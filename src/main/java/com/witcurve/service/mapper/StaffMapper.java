package com.witcurve.service.mapper;

import com.witcurve.domain.Staff;
import com.witcurve.service.dto.StaffDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ClassMapper.class, UserMapper.class})
public interface StaffMapper extends EntityMapper<StaffDTO, Staff> {

    @Mapping(target = "schoolId", source = "school.id")
    StaffDTO toDto(Staff staff);

    @Mapping(target = "school.id", source = "schoolId")
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


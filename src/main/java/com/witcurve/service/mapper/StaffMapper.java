package com.witcurve.service.mapper;

import com.witcurve.domain.Staff;
import com.witcurve.service.dto.StaffDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StaffMapper {

    @Mapping(target = "schoolId", source = "school.id")
    StaffDTO staffToStaffDTO(Staff staff);

    @Mapping(target = "school.id", source = "schoolId")
    Staff staffDTOToStaff(StaffDTO staffDTO);

    List<StaffDTO> staffsToStaffDTOs(List<Staff> staffs);

    List<Staff> staffDTOsTOStaff(List<StaffDTO> staffDTOS);
}

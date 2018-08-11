package com.witcurve.service.mapper;

import com.witcurve.domain.TimeTableUnit;
import com.witcurve.service.dto.TimeTableUnitDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TimetableUnitMapper {

    @Mapping(source = "courseTeacherDTO.id", target = "courseTeacher.id")
    @Mapping(source = "slotDTO.id", target = "slot.id")
    TimeTableUnit timetableUnitDTOToTimetableUnit(TimeTableUnitDTO timeTableUnitDTO);

    @Mapping(target = "courseTeacherDTO.id", source = "courseTeacher.id")
    @Mapping(target = "slotDTO.id", source = "slot.id")
    TimeTableUnitDTO timeTableUnitToTimeTableUnitDTO(TimeTableUnit timeTableUnit);

    List<TimeTableUnitDTO> timeTableUnitsToTimeTableUnitDTOs(List<TimeTableUnit> timeTableUnits);

    List<TimeTableUnit> timeTableUnitDTOsToTimeTableUnits(List<TimeTableUnitDTO> timeTableUnitDTOS);

}

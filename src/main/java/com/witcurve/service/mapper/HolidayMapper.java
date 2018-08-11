package com.witcurve.service.mapper;

import com.witcurve.domain.Holiday;
import com.witcurve.service.dto.HolidayDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HolidayMapper {

    Holiday holidayDTOToHoliday(HolidayDTO holidayDTO);

    HolidayDTO holidayToHolidayDTO(Holiday holiday);

    List<Holiday> holidayDTOsToHolidays(List<HolidayDTO> holidayDTOS);

    List<HolidayDTO> holidaysToHolidayDTOs(List<Holiday> holidays);
}

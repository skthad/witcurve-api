package com.witcurve.service.mapper;

import com.witcurve.domain.DailyUpdate;
import com.witcurve.service.dto.DailyUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DailyUpdateMapper {

    @Mapping(target = "timeTableUnitId", source = "timeTableUnit.id")
    DailyUpdateDTO dailyUpdateToDailyUpdateDTO(DailyUpdate dailyUpdate);

    @Mapping(target = "timeTableUnit.id", source = "timeTableUnitId")
    DailyUpdate dailyDTOToDailyUpdate(DailyUpdateDTO dailyUpdateDTO);

    List<DailyUpdateDTO> dailyUpdatesToDailyUpdateDTOs(List<DailyUpdate> dailyUpdates);

    List<DailyUpdate> dailyUpdateDTOsToDailyUpdate(List<DailyUpdateDTO> dailyUpdateDTOS);
}

package com.witcurve.service.mapper;

import com.witcurve.domain.Test;
import com.witcurve.service.dto.TestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TestMapper {

    @Mapping(target = "timeTableUnitId", source = "timeTableUnit.id")
    TestDTO testToTestDTO(Test test);

    @Mapping(source = "timeTableUnitId", target = "timeTableUnit.id")
    Test testDTOToTest(TestDTO testDTO);

    List<TestDTO> testsToTestDTOs(List<Test> tests);

    List<Test> testDTOsToTests(List<TestDTO> testDTOS);
}

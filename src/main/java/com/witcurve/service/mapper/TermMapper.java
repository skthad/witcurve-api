package com.witcurve.service.mapper;

import com.witcurve.domain.Term;
import com.witcurve.service.dto.TermDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AcademicSessionMapper.class})
public interface TermMapper {

    @Mapping(target = "academicSessionId", source = "session.id")
    TermDTO termToTermDTO(Term term);

    @Mapping(source = "academicSessionId", target = "session.id")
    Term termDTOToTerm(TermDTO termDTO);

    List<TermDTO> termsToTermDTOs(List<Term> terms);

    List<Term> termDTOsToTerm(List<TermDTO> termDTOS);
}

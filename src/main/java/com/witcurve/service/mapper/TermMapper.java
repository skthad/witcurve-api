package com.witcurve.service.mapper;

import com.witcurve.domain.Term;
import com.witcurve.service.dto.TermDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AcademicSessionMapper.class})
public interface TermMapper extends EntityMapper<TermDTO, Term> {

    TermDTO toDto(Term term);

    Term toEntity(TermDTO termDTO);

    default Term fromId(Long id) {
        if(id == null) {
            return null;
        }
        Term term = new Term();
        term.setId(id);
        return term;
    }

}


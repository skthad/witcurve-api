package com.witcurve.repository;

import com.witcurve.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, String> {

    @Query("select distinct ek.keywordName from EventKeyword ek join Event e on e.id=ek.eventId where e.schoolInfo.id= ?1")
    List<String> searchKeywordsInASchoolBoard(Long schoolInfoId);

    @Query("select distinct ek.keywordName from EventKeyword ek join Event e on e.id=ek.eventId where lower(ek.keywordName) like concat('%', ?1, '%') and e.schoolInfo.id= ?2")
    List<String> searchKeywords(String search,Long schoolInfoId);

}

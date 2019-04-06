package com.witcurve.repository;

import com.witcurve.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, String> {

    @Query("select ek.keywordName from EventKeyword ek where ek.event.schoolInfo.id= ?2")
    List<String> searchKeywordsInASchoolBoard(Long schoolInfoId);

    @Query("select ek.keywordName from EventKeyword ek where lower(ek.keywordName) like concat('%', ?1, '%') and ek.event.schoolInfo.id= ?2")
    List<String> searchKeywords(String search,Long schoolInfoId);

}

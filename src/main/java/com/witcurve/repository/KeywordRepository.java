package com.witcurve.repository;

import com.witcurve.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, String> {

    @Query("select kw from Keyword kw where lower(kw.name) like concat('%', ?1, '%')")
    List<Keyword> searchKeywords(String search);


}

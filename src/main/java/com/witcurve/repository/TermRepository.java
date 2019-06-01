package com.witcurve.repository;

import com.witcurve.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {

    @Query("select t from Term t where t.session.id = ?1 order by startDate")
    List<Term> findTermsInSession(Long sessionId);
}

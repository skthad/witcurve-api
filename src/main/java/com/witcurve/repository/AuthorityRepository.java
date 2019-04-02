package com.witcurve.repository;

import com.witcurve.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {

    @Modifying
    @Query("delete from Authority where name = ?1")
    void deleteByName(String name);

    @Query("select auth from Authority auth where auth.instituteId is null or auth.instituteId = ?1")
    List<Authority> getByInstituteId(Long instituteId);
}

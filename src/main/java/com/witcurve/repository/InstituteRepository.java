package com.witcurve.repository;

import com.witcurve.domain.Institute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstituteRepository extends JpaRepository<Institute, Long> {

    Institute findInstituteBySubDomainName(String subDomainName);
}

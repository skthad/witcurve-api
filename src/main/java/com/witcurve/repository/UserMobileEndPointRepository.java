package com.witcurve.repository;

import com.witcurve.domain.UserMobileEndPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMobileEndPointRepository extends JpaRepository<UserMobileEndPoint, Long> {

    @Modifying
    @Query("delete from UserMobileEndPoint umep where umep.user.id=?1 and umep.deviceToken=?2")
    void deleteByUserIdAndToken(Long userId, String token);

}

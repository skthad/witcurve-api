package com.witcurve.repository;

import com.witcurve.domain.UserMobileEndPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMobileEndPointRepository extends JpaRepository<UserMobileEndPoint, Long> {

    @Modifying
    @Query("delete from UserMobileEndPoint umep where umep.user.id=?1 and umep.deviceToken=?2")
    void deleteByUserIdAndToken(Long userId, String token);

    @Query("select umep from UserMobileEndPoint umep where umep.user.id=?1 and umep.deviceToken=?2")
    UserMobileEndPoint findByUserIdAndToken(Long userId, String token);

    @Query("Select count(umep) from UserMobileEndPoint umep where umep.deviceToken=?1")
    Integer getTokenCount(String token);

    @Query("Select umep from UserMobileEndPoint umep where umep.user.id=?1 order by umep.createdDate desc")
    List<UserMobileEndPoint> findByUserId(Long userId);

    @Query("Select deviceToken from UserMobileEndPoint umep where  umep.user.id=?1")
    List<String> findDeviceTokenByUserId(Long userId);

    @Query("Select umep from UserMobileEndPoint umep where umep.user.id in (Select ss.student.user.id from StudentStandard ss where ss.active=true and ss.standard.id=?1)")
    List<UserMobileEndPoint> findByStandardId(Long standardId);
}

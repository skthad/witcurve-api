package com.witcurve.repository;

import com.witcurve.domain.UserMobileEndPoint;
import com.witcurve.domain.enumeration.Grade;
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

    @Query("Select count(umep) from UserMobileEndPoint umep where umep.staffInfoSubscriptionEndPoint=?1")
    Integer getCountByStaffInfoSubscriptionEndPoint(String staffInfoSubscriptionEndPoint);

    @Query("Select count(umep) from UserMobileEndPoint umep where umep.standardSubscriptionEndPoint=?1")
    Integer getCountByStandardSubscriptionEndPoint(String standardSubscriptionEndPoint);

    @Query("Select count(umep) from UserMobileEndPoint umep where umep.schoolInfoSubscriptionEndPoint=?1")
    Integer getCountBySchoolInfoSubscriptionEndPoint(String schoolInfoSubscriptionEndPoint);

    @Query("Select count(umep) from UserMobileEndPoint umep where umep.globalSubscriptionEndPoint=?1")
    Integer getCountByGlobalSubscriptionEndPoint(String globalSubscriptionEndPoint);

    @Query("Select umep from UserMobileEndPoint umep where umep.user.id=?1 order by umep.createdDate desc")
    List<UserMobileEndPoint> findByUserId(Long userId);

    @Query("Select umep from UserMobileEndPoint umep where umep.user.id in (Select ss.student.user.id from StudentStandard ss where ss.active=true and ss.standard.id=?1)")
    List<UserMobileEndPoint> findByStandardId(Long standardId);

    @Query("Select umep from UserMobileEndPoint umep where umep.user.id in (Select staff.user.id from Staff staff where staff.schoolInfo.id=?1)")
    List<UserMobileEndPoint> findStaffBySchoolInfoId(Long schoolInfoId);

    @Query("Select umep from UserMobileEndPoint umep where umep.user.id in (Select student.user.id from Student student where student.id=?1)")
    List<UserMobileEndPoint> findStudentEndPointByStudentId(Long studentId);

    @Query("Select umep from UserMobileEndPoint umep where umep.user.id in (Select staff.user.id from Staff staff where staff.id=?1)")
    List<UserMobileEndPoint> findStaffEndPointByStaffId(Long staffId);

    @Query("Select umep from UserMobileEndPoint umep where umep.user.id in (Select ss.student.user.id from StudentStandard ss where ss.active=true and ss.standard.grade=?1 and ss.standard.schoolInfo.id=?2)")
    List<UserMobileEndPoint> findStudentEndPointByGradeAndSchoolInfoId(Grade grade, Long schoolInfoId);

    @Query("Select umep from UserMobileEndPoint umep where umep.user.id in (Select ss.student.user.id from StudentStandard ss where ss.active=true and ss.standard.id in (Select ct.standard.id from CourseTeacher ct where ct.active=true and ct.id=?1))")
    List<UserMobileEndPoint> findStudentEndPointByCourseTeacherId(Long courseTeacherId);

    @Query("Select umep from UserMobileEndPoint umep where umep.user.id in (Select staff.user.id from Staff staff where staff.id in (Select ss.standard.classTeacher.id from StudentStandard ss where ss.active = true and ss.student.id=?1 ))")
    List<UserMobileEndPoint> findClassTeacherEndPointByStudentId(Long studentId);
}

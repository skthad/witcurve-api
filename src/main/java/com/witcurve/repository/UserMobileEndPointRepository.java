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

    @Query("Select umep from UserMobileEndPoint umep where umep.user.id=?1 order by umep.createdDate desc")
    List<UserMobileEndPoint> findByUserId(Long userId);

    @Query("Select umep from UserMobileEndPoint umep where umep.user.id in (Select ss.student.user.id from StudentStandard ss where ss.active=true and ss.standard.id=?1)")
    List<UserMobileEndPoint> findByStandardId(Long standardId);

    @Query("Select umep from UserMobileEndPoint umep where umep.user.id in (Select staff.user.id from Staff staff where staff.schoolInfo.id=?1)")
    List<UserMobileEndPoint> findStaffBySchoolInfoId(Long schoolInfoId);

    @Query("Select umep from UserMobileEndPoint umep where umep.user.id in (Select student.user.id from Student student where student.id=?1)")
    UserMobileEndPoint findByStudentId(Long studentId);

    @Query("Select umep from UserMobileEndPoint umep where umep.user.id in (Select staff.user.id from Staff staff where staff.id=?1)")
    UserMobileEndPoint findByStaffId(Long staffId);

    @Query("Select umep from UserMobileEndPoint umep where umep.user.id in (Select ss.student.user.id from StudentStandard ss where ss.active=true and ss.standard.id in (Select std.id from Standard std where std.grade=?1 and std.schoolInfo.id=?2))")
    List<UserMobileEndPoint> findByGradeAndSchoolInfoId(Grade grade, Long schoolInfoId);

    @Query("Select umep from UserMobileEndPoint umep where umep.user.id in (Select ss.student.user.id from StudentStandard ss where ss.active=true and ss.standard.id in (Select ct.standard.id from CourseTeacher ct where ct.active=true and ct.id=?1))")
    List<UserMobileEndPoint> findByCourseTeacherId(Long courseTeacherId);

    @Query("Select umep from UserMobileEndPoint umep where umep.user.id in (Select staff.user.id from Staff staff where staff.id in (Select std.classTeacher.id from Standard std where std.active=true and std.id in (Select ss.standard.id from StudentStandard ss where ss.student.id=?1 )))")
    UserMobileEndPoint findClassTeacherEndPointByStudentId(Long studentId);

}

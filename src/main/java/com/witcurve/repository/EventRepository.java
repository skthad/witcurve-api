package com.witcurve.repository;

import com.witcurve.domain.Event;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.domain.enumeration.Grade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface EventRepository  extends JpaRepository<Event, Long> {


    @Query(value = "select distinct e.id from event e\n" +
        "left join slot_course_details scd on scd.id = e.scd_id\n" +
        "left join general_slot_details gsd on gsd.id = scd.gsd_id\n" +
        "left join course_teacher ct on ct.id = e.course_teacher_id \n" +
        "where e.date BETWEEN ?1 AND ?2 AND \n" +
        "((e.student_id = ?3 and e.type = 'ATTENDANCE') or \n" +
        "(gsd.standard_id = ?4 and e.type in ?7) or \n" +
        "(ct.standard_id = ?4 and e.type = 'ASSIGNMENT') OR \n" +
        "(e.standard_id = ?4 and e.type = 'SCHOOL_EVENT') OR\n" +
        "((e.grade is null or (e.grade is not null and e.grade = ?5)) \n" +
        " and e.school_info_id = ?6 and (e.type in ('HOLIDAY', 'PERIODIC_TEST','SCHOOL_EVENT'))))", nativeQuery = true)
    List<BigInteger> findEventsByDateRangeForStudent(LocalDate startDate, LocalDate endDate,
                                                     Long studentId, Long standardId, String grade,
                                                     Long schoolInfoId, List<String> types);

    @Query(value = "select distinct e.id from event e\n" +
        "left join slot_course_details scd on scd.id = e.scd_id\n" +
        "left join general_slot_details gsd on gsd.id = scd.gsd_id\n" +
        "where e.date BETWEEN ?1 AND ?2 AND \n" +
        "(gsd.standard_id = ?3 and e.type in ?4)", nativeQuery = true)
    List<BigInteger> findDirayEventsByDateRangeForStudent(LocalDate startDate, LocalDate endDate,
                                                    Long standardId, List<String> types);

    @Query(value = "select distinct e.id from event e\n" +
        "left join slot_course_details scd on scd.id = e.scd_id\n" +
        "left join general_slot_details gsd on gsd.id = scd.gsd_id\n" +
        "left join course_teacher ct on ct.id = e.course_teacher_id \n" +
        "where e.date BETWEEN ?1 AND ?2 AND \n" +
        "((e.staff_id = ?3 and e.type = 'ATTENDANCE') or \n" +
        "(scd.course_teacher_id in ?4 and e.type in ?9) or \n" +
        "(e.course_teacher_id in ?4 and e.type = 'ASSIGNMENT') OR \n" +
        "(ct.course_id in ?5 and e.type = 'PERIODIC_TEST') OR \n" +
        "(e.standard_id in ?6 and e.type = 'SCHOOL_EVENT') OR\n" +
        "((e.grade is null or (e.grade is not null and e.grade in ?7)) \n" +
        " and e.school_info_id = ?8 and (e.type in ('HOLIDAY', 'SCHOOL_EVENT'))))", nativeQuery = true)
    List<BigInteger> findEventsByDateRangeForStaff(LocalDate startDate, LocalDate endDate,
                                                   Long staffId, Set<Long> courseTeacherIds, Set<Long> courseIds, Set<Long> standardIds, Set<String> grades,
                                                   Long schoolInfoId, List<String> types);

    @Query("Select distinct e.date from Event e where e.date between ?1 and ?2 and" +
        "(" +
        "(e.student.id = ?3 and e.type = 'ATTENDANCE') or " +
        "(e.scd.gsd.standard.id = ?4 and e.type in ?7) or " +
        "(e.courseTeacher.standard.id = ?4 and e.type = 'ASSIGNMENT') or " +
        "(e.standard.id = ?4 and e.type = 'SCHOOL_EVENT') or " +
        "((e.grade is null or (e.grade is not null and e.grade = ?5)) and e.schoolInfo.id=?6 and e.type in ('HOLIDAY', 'SCHOOL_EVENT')) " +
        ")")
    List<LocalDate> findEventDatesByDateRangeForStudent(LocalDate startDate, LocalDate endDate,
                                                Long studentId, Long standardId, String grade,
                                                Long schoolInfoId, List<String> types);

    @Query("Select e from Event e where e.date = ?1 and e.type = ?3 and e.scd.id = ?2")
    Event findEventOnDateAndSlot(LocalDate date, Long scdId, EventType type);

    @Query("Select e from Event e where e.date = ?1 and e.type = 'ASSIGNMENT' and e.courseTeacher.id = ?2")
    Event findAssignmentOnDateAndCourseTeacher(LocalDate date, Long courseTeacherId);

    @Query("Select e from Event e where e.date = ?1 and e.type = 'PERIODIC_TEST' and e.courseTeacher.course.id = ?2")
    Event findPeriodicTestOnDateAndCourseId(LocalDate date, Long courseId);

    @Query("Select e from Event e where e.date = ?1 and (e.schoolInfo.id=?3 or e.standard.schoolInfo.id =?3) and e.type in ?2")
    List<Event> eventsBlockingHolidayAndSchoolEvents(LocalDate date, List<EventType> types, Long schoolInfoId);

    @Query("Select e from Event e " +
        "where e.date = ?1 and e.type in ?2 " +
        "and (e.schoolInfo.id=?3 or e.student.id=?4)")
    List<Event> eventsBlockingAttendanceForStudent(LocalDate date, List<EventType> types, Long schoolInfoId, Long studentId);

    @Query("Select e from Event e " +
        "where e.date = ?1 and e.type in ?2 " +
        "and (e.schoolInfo.id=?3 or e.staff.id=?4)")
    List<Event> eventsBlockingAttendanceForStaff(LocalDate date, List<EventType> types, Long schoolInfoId, Long staffId);

    @Query("Select e from Event e where e.date between ?1 and ?2  and e.student.id in " +
        "(Select ss.student.id from StudentStandard ss where ss.standard.id=?3) " +
        "and e.type = 'ATTENDANCE' order by e.date desc")
    List<Event> findAttendanceForStandard(LocalDate fromDate, LocalDate toDate, Long standardId);

    @Query("Select e from Event e where e.date between ?1 and ?2  and e.staff.id = ?3 " +
        "and e.type = 'ATTENDANCE' order by e.date desc")
    List<Event> findAttendanceForStaff(LocalDate fromDate, LocalDate toDate, Long staffId);

    @Query("Select e from Event e where e.date between ?1 and ?2 and e.staff is not null and e.staff.schoolInfo.id = ?3 " +
        "and e.type = 'ATTENDANCE' order by e.staff.firstName, e.staff.middleName, e.staff.lastName, e.staff.id, e.date desc")
    List<Event> findAttendanceForAllStaffInSchoolInfo(LocalDate fromDate, LocalDate toDate, Long schoolInfoId);

    @Query("Select e from Event e where e.date between ?1 and ?2  and e.student.id in " +
        "(Select ss.student.id from StudentStandard ss where ss.standard.schoolInfo.id=?3) " +
        "and e.type = 'ATTENDANCE' order by e.date desc")
    List<Event> findAttendanceForAllStudentInSchoolInfo(LocalDate fromDate, LocalDate toDate, Long schoolInfoId);

    @Query("Select e from Event e where e.date between ?1 and ?2 and e.student.id = ?3 " +
        "and e.type = 'ATTENDANCE' order by e.date desc")
    List<Event> findAttendanceForStudent(LocalDate fromDate, LocalDate toDate, Long student);

    @Query("select distinct e.staff.id from Event e where e.type = 'ATTENDANCE' and e.attendanceType ='ABSENT' " +
        "and e.staff is not null and e.staff.id in ?1 and e.date = ?2")
    List<Long> findAbsentTeacherList(List<Long> teacherIds, LocalDate date);

    @Query("Select e from Event e where e.type = 'NOTICE' and " +
        "(e.standard.id =?1 or " +
        "(e.grade is null and e.standard.id is null and e.schoolInfo.id=?3) or (e.grade = ?2 and e.schoolInfo.id=?3)) " +
        "and e.date between ?4 and ?5 " +
        "order by e.date desc, e.createdDate desc")
    Page<Event> findStudentNotices(Long standardId, Grade grade, Long schoolInfoId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("Select e from Event e where (e.type = 'NOTICE' or e.type = 'STAFF_NOTICE') and " +
        "(e.standard.id =?1 or " +
        "(e.grade is null and e.standard.id is null and e.schoolInfo.id=?3) or (e.grade = ?2 and e.schoolInfo.id=?3)) " +
        "and e.date between ?4 and ?5 " +
        "order by e.date desc, e.createdDate desc")
    Page<Event> findClassTeacherNotices(Long standardId, Grade grade, Long schoolInfoId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("Select e from Event e left join EventKeyword ek on ek.eventId=e.id " +
        "where (e.type = 'NOTICE' or e.type = 'STAFF_NOTICE') and " +
        "(e.standard.id =?1 or " +
        "(e.grade is null and e.standard.id is null and e.schoolInfo.id=?3) or (e.grade = ?2 and e.schoolInfo.id=?3)) " +
        "and e.date between ?4 and ?5 and ek.keywordName in ?6 " +
        "order by e.date desc, e.createdDate desc")
    Page<Event> findClassTeacherNoticesByKeywords(Long standardId, Grade grade, Long schoolInfoId, LocalDate startDate, LocalDate endDate,List<String> keywords, Pageable pageable);

    @Query("Select e from Event e left join EventKeyword ek on ek.eventId=e.id " +
        "where (e.type = 'NOTICE' or e.type = 'STAFF_NOTICE') and " +
        "e.grade is null and e.standard.id is null and e.schoolInfo.id=?1 " +
        "and e.date between ?2 and ?3 and ek.keywordName in ?4 "+
        "order by e.date desc, e.createdDate desc")
    Page<Event> findStaffNoticesBySchoolInfoIdAndKeywords(Long schoolInfoId, LocalDate startDate, LocalDate endDate, List<String> keywords, Pageable pageable);

    @Query("Select e from Event e where (e.type = 'NOTICE' or e.type = 'STAFF_NOTICE') and " +
        "e.grade is null and e.standard.id is null and e.schoolInfo.id=?1 " +
        "and e.date between ?2 and ?3 "+
        "order by e.date desc, e.createdDate desc")
    Page<Event> findStaffNoticesBySchoolInfoId(Long schoolInfoId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("Select e from Event e left join EventKeyword ek on ek.eventId=e.id where " +
        "(e.schoolInfo.id=?1 and (e.type='NOTICE' or e.type='STAFF_NOTICE')) " +
        "and e.date between ?2 and ?3 and ek.keywordName in ?4 and e.standard.id in ?5 "+
        "order by e.date desc, e.createdDate desc")
    Page<Event> findAdminNoticesBySchoolInfoIdAndStandardIdsAndKeywords(Long schoolInfoId, LocalDate startDate, LocalDate endDate, List<String> keywords, List<Long> standardIds, Pageable pageable);

    @Query("Select e from Event e left join EventKeyword ek on ek.eventId=e.id where " +
        "(e.schoolInfo.id=?1 and (e.type='NOTICE' or e.type='STAFF_NOTICE')) " +
        "and e.date between ?2 and ?3 and ek.keywordName in ?4 "+
        "order by e.date desc, e.createdDate desc")
    Page<Event> findAdminNoticesBySchoolInfoIdAndKeywords(Long schoolInfoId, LocalDate startDate, LocalDate endDate, List<String> keywords, Pageable pageable);

    @Query("Select e from Event e where " +
        "(e.schoolInfo.id=?1 and (e.type='NOTICE' or e.type='STAFF_NOTICE')) " +
        "and e.date between ?2 and ?3 and e.standard.id in (?4) "+
        "order by e.date desc, e.createdDate desc")
    Page<Event> findAdminNoticesBySchoolInfoIdAndStandardIds(Long schoolInfoId, LocalDate startDate, LocalDate endDate, List<Long> standardIds, Pageable pageable);

    @Query("Select e from Event e where " +
        "(e.schoolInfo.id=?1 and (e.type='NOTICE' or e.type='STAFF_NOTICE')) " +
        "and e.date between ?2 and ?3 "+
        "order by e.date desc, e.createdDate desc")
    Page<Event> findAdminNoticesBySchoolInfoId(Long schoolInfoId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("Select e from Event e where e.type='PERIODIC_TEST' and e.schoolInfo.id in ?1 and e.date between ?2 and ?3 order by e.date desc")
    List<Event> findPeriodicTestsBySchoolInfoId(Long schoolInfoId, LocalDate eventStart, LocalDate eventEnd);

    @Query("Select e.bindingId from Event e where e.type='PERIODIC_TEST' and e.schoolInfo.id in ?1  and e.grade in ?2 and e.date between ?3 and ?4 order by e.date desc")
    List<String> findPeriodicTestBindingIdBySchoolInfoIdAndGrades(Long schoolInfoId, List<Grade> grades, LocalDate eventStart, LocalDate eventEnd);

    @Query("Select e from Event e where e.type='PERIODIC_TEST' and e.bindingId in ?1 order by e.date desc")
    List<Event> findPeriodicTestByBindingIds(List<String> bindingIds);

    @Query("Select e from Event e where e.type='PERIODIC_TEST' and e.bindingId=?1 order by e.date asc")
    List<Event> findPeriodicEventsByBindingId(String bindingId);

    @Query("Select e.id from Event e where e.type='PERIODIC_TEST' and e.bindingId=?1 order by e.date asc")
    List<Long> findPeriodicEventIdsByBindingId(String bindingId);

    @Modifying
    @Query("delete from Event e where e.type='PERIODIC_TEST' and e.bindingId=?1")
    void deletePeriodicEventByBindingId(String bindingId);

    @Query("Select e from Event e where e.type='PERIODIC_TEST' and e.bindingId=?1 and e.courseTeacher.course.id in ?2 order by e.date asc")
    List<Event> findPeriodicEventsByBindingIdAndCourseIds(String bindingId, List<Long> courseIds);

    @Query("Select count(e) from Event e where (e.date between ?1 and ?2) and e.type = 'HOLIDAY' and e.schoolInfo.id=?3")
    Long findHolidaysBetweenFromDateAndToDate(LocalDate fromDate, LocalDate toDate, Long schoolInfoId);

    @Query("Select e from Event e where e.type='ASSIGNMENT' and e.courseTeacher.teacher.id=?1 and e.date between ?2 and ?3 order by e.date desc")
    List<Event> findAssignmentsByTeacherInDateRange(Long staffId, LocalDate sdate, LocalDate eDate);

    @Query("Select e from Event e where e.type='TEST' and e.scd.courseTeacher.teacher.id=?1 and e.date between ?2 and ?3 order by e.date desc")
    List<Event> findTestsByTeacherInDateRange(Long staffId, LocalDate sdate, LocalDate eDate);

    @Query("Select e from Event e where e.type='PERIODIC_TEST' and e.scd.courseTeacher.course.id in ?1 and e.date between ?2 and ?3 order by e.date desc")
    List<Event> findPeriodicTestsByTeacherInDateRange(Set<Long> courseIds, LocalDate eventStart, LocalDate eventEnd);

    @Query("Select e from Event e where e.type='ASSIGNMENT' and e.courseTeacher.standard.id = ?1 and e.courseTeacher.course.id = ?2 and e.date between ?3 and ?4 order by e.date desc")
    List<Event> findAssignmentsByStandardAndCourseInDateRange(Long standardId, Long courseId, LocalDate sdate, LocalDate eDate);

    @Query("Select e from Event e where e.type='TEST' and e.scd.courseTeacher.standard.id = ?1 and e.scd.courseTeacher.course.id = ?2 and e.date between ?3 and ?4 order by e.date desc")
    List<Event> findTestsByStandardAndCourseInDateRange(Long standardId, Long courseId, LocalDate sdate, LocalDate eDate);

    @Query("Select e from Event e where e.type='DAILY_UPDATE' and e.scd.courseTeacher.standard.id = ?1 and e.scd.courseTeacher.course.id = ?2 and e.date between ?3 and ?4 order by e.date desc")
    List<Event> findDailyUpdatesByStandardAndCourseInDateRange(Long standardId, Long courseId, LocalDate sdate, LocalDate eDate);

    @Query("Select e from Event e where e.type='PERIODIC_TEST' and e.scd.courseTeacher.course.id = ?1 and e.date between ?2 and ?3 order by e.date desc")
    List<Event> findPeriodicTestsByCourseTeachersInDateRange(Long courseId, LocalDate sdate, LocalDate edate);

    @Query("Select e from Event e where e.type= ?1 and e.schoolInfo.id= ?2 and (e.date between ?3 and ?4) order by e.date desc" )
    List<Event> findByTypeAndSchoolInfoIdOrderByDateAsc(EventType eventType, Long schoolInfoId, LocalDate sDate, LocalDate eDate);

    @Query("Select e from Event e where (e.date between ?1 and ?2) and (e.type = 'HOLIDAY' or e.type = 'SCHOOL_EVENT') and e.schoolInfo.id=?3 order by e.date desc ")
    List<Event> findHolidaysAndSchoolEventsBetweenFromDateAndToDate(LocalDate fromDate, LocalDate toDate, Long schoolInfoId);

    @Modifying
    @Query("Delete from Event e where e.scd.id in ?1 and e.type='TEST' and e.date > current_date ")
    void deleteFutureTestByScdIds(Set<Long> scdIds);

    @Modifying
    @Query("Delete from Event e where e.standard.id in ?1 and e.type='TEST' and e.date > current_date ")
    void deleteFutureTestByStandardIds(Set<Long> standardIds);

    @Modifying
    @Query("Delete from Event e where e.courseTeacher.id in ?1 and e.type='ASSIGNMENT' and e.date > current_date ")
    void deleteFutureAssignmentsByCourseTeacherIds(Set<Long> courseTeacherIds);

}


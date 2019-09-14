package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.witcurve.domain.enumeration.AttendanceType;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.domain.enumeration.Grade;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.witcurve.service.util.WitCurveConstants.DEFAULT_DATE_FORMAT;

public class EventDTO extends AbstractAuditingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private EventType type;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate endDate;

    @Pattern(regexp = "^([01]\\d|2[0-3])([0-5]\\d)$")
    private String eventStartTime;

    @Pattern(regexp = "^([01]\\d|2[0-3])([0-5]\\d)$")
    private String eventEndTime;

    @NotNull
    private String name;

    private String description;

    private Long staffId;

    private String staffName;

    private Long studentId;

    private String studentName;

    private SlotCourseDetailsDTO scd;

    private Long standardId;

    private Grade grade;

    private CourseTeacherDTO courseTeacher;

    private CourseDTO course;

    private Long schoolInfoId;

    @NotNull
    private Boolean sendSms = false;

    private AttendanceType attendanceType;

    private Integer fullMarks;

    private List<String> keywords;

    private String standardName;

    private String signature;

    private String bindingId;

    private List<Long> courseContentIds;

    private Boolean doesAllStudentMarksExistForPeriodicTest = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public SlotCourseDetailsDTO getScd() {
        return scd;
    }

    public void setScd(SlotCourseDetailsDTO scd) {
        this.scd = scd;
    }

    public Long getStandardId() {
        return standardId;
    }

    public void setStandardId(Long standardId) {
        this.standardId = standardId;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public CourseTeacherDTO getCourseTeacher() {
        return courseTeacher;
    }

    public void setCourseTeacher(CourseTeacherDTO courseTeacher) {
        this.courseTeacher = courseTeacher;
    }

    public CourseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
    }

    public Long getSchoolInfoId() {
        return schoolInfoId;
    }

    public void setSchoolInfoId(Long schoolInfoId) {
        this.schoolInfoId = schoolInfoId;
    }

    public Boolean getSendSms() {
        return sendSms;
    }

    public void setSendSms(Boolean sendSms) {
        this.sendSms = sendSms;
    }

    public AttendanceType getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(AttendanceType attendanceType) {
        this.attendanceType = attendanceType;
    }

    public Integer getFullMarks() {
        return fullMarks;
    }

    public void setFullMarks(Integer fullMarks) {
        this.fullMarks = fullMarks;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getStandardName() {
        return standardName;
    }

    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getBindingId() {
        return bindingId;
    }

    public void setBindingId(String bindingId) {
        this.bindingId = bindingId;
    }

    public List<Long> getCourseContentIds() {
        return courseContentIds;
    }

    public void setCourseContentIds(List<Long> courseContentIds) {
        this.courseContentIds = courseContentIds;
    }

    public Boolean getDoesAllStudentMarksExistForPeriodicTest() {
        return doesAllStudentMarksExistForPeriodicTest;
    }

    public void setDoesAllStudentMarksExistForPeriodicTest(Boolean doesAllStudentMarksExistForPeriodicTest) {
        this.doesAllStudentMarksExistForPeriodicTest = doesAllStudentMarksExistForPeriodicTest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventDTO eventDTO = (EventDTO) o;
        return Objects.equals(id, eventDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "EventDTO{" +
            "id=" + id +
            '}';
    }
}

package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.EventType;
import com.witcurve.domain.enumeration.Grade;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Objects;

public class EventDTO extends AbstractAuditingDTO {

    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private EventType type;

    @NotNull
    private LocalDate date;

    @Pattern(regexp = "^([01]\\d|2[0-3])([0-5]\\d)$")
    private String eventStartTime;

    @Pattern(regexp = "^([01]\\d|2[0-3])([0-5]\\d)$")
    private String eventEndTime;

    private Long studentId;

    private Long standardId;

    private Long staffId;

    private Grade grade;

    private Long academicSessionId;

    private SlotCourseDetailsDTO scd;

    private CourseTeacherDTO courseTeacher;

    private String bindingId;

    private Boolean sendSms = false;

    private Boolean present;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getStandardId() {
        return standardId;
    }

    public void setStandardId(Long standardId) {
        this.standardId = standardId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Long getAcademicSessionId() {
        return academicSessionId;
    }

    public void setAcademicSessionId(Long academicSessionId) {
        this.academicSessionId = academicSessionId;
    }

    public SlotCourseDetailsDTO getScd() {
        return scd;
    }

    public void setScd(SlotCourseDetailsDTO scd) {
        this.scd = scd;
    }

    public CourseTeacherDTO getCourseTeacher() {
        return courseTeacher;
    }

    public void setCourseTeacher(CourseTeacherDTO courseTeacher) {
        this.courseTeacher = courseTeacher;
    }

    public String getBindingId() {
        return bindingId;
    }

    public void setBindingId(String bindingId) {
        this.bindingId = bindingId;
    }

    public Boolean getSendSms() {
        return sendSms;
    }

    public void setSendSms(Boolean sendSms) {
        this.sendSms = sendSms;
    }

    public Boolean getPresent() {
        return present;
    }

    public void setPresent(Boolean present) {
        this.present = present;
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
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", type=" + type +
            ", date=" + date +
            ", eventStartTime='" + eventStartTime + '\'' +
            ", eventEndTime='" + eventEndTime + '\'' +
            ", studentId=" + studentId +
            ", standardId=" + standardId +
            ", staffId=" + staffId +
            ", grade=" + grade +
            ", academicSessionId=" + academicSessionId +
            ", scd=" + scd +
            ", bindingId='" + bindingId + '\'' +
            ", sendSms=" + sendSms +
            ( type == EventType.LEAVE ? ", present=" + present : "") +
            '}';
    }
}

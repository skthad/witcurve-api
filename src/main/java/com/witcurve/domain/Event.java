package com.witcurve.domain;

import com.witcurve.domain.enumeration.AttendanceType;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.util.LocalDateConverter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="event")
public class Event extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType type;

    @NotNull
    @Column(name = "date", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate date;

    @Column(name = "end_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate endDate;

    @Column(name = "event_start_time")
    @Pattern(regexp = "^([01]\\d|2[0-3])([0-5]\\d)$")
    private String eventStartTime;

    @Column(name = "event_end_time")
    @Pattern(regexp = "^([01]\\d|2[0-3])([0-5]\\d)$")
    private String eventEndTime;

    @NotNull
    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private Staff staff;

    @ManyToOne
    private Student student;

    @ManyToOne
    @JoinColumn(name = "scd_id")
    private SlotCourseDetails scd;

    @ManyToOne
    private Standard standard;

    @Column(name = "grade", length = 50)
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @ManyToOne
    @JoinColumn(name = "course_teacher_id")
    private CourseTeacher courseTeacher;

    @ManyToOne
    private SchoolInfo schoolInfo;

    @NotNull
    @Column(name = "send_sms", nullable = false, columnDefinition = "boolean default false")
    private Boolean sendSms = false;

    @Column(name = "attendance_type", length = 50)
    @Enumerated(EnumType.STRING)
    private AttendanceType attendanceType;

    @Column(name = "full_marks")
    private Integer fullMarks;

    @Column
    private String signature;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
        name = "event_keyword",
        joinColumns = {@JoinColumn(name = "event_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "keyword_name", referencedColumnName = "name")})
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Keyword> keywords = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="event_id", insertable = false, updatable = false)
    private Set<EventContent> eventContents;

    @Column
    private String bindingId;

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

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public SlotCourseDetails getScd() {
        return scd;
    }

    public void setScd(SlotCourseDetails scd) {
        this.scd = scd;
    }

    public Standard getStandard() {
        return standard;
    }

    public void setStandard(Standard standard) {
        this.standard = standard;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public CourseTeacher getCourseTeacher() {
        return courseTeacher;
    }

    public void setCourseTeacher(CourseTeacher courseTeacher) {
        this.courseTeacher = courseTeacher;
    }

    public SchoolInfo getSchoolInfo() {
        return schoolInfo;
    }

    public void setSchoolInfo(SchoolInfo schoolInfo) {
        this.schoolInfo = schoolInfo;
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

    public Set<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<Keyword> keywords) {
        this.keywords = keywords;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Set<EventContent> getEventContents() {
        return eventContents;
    }

    public void setEventContents(Set<EventContent> eventContents) {
        this.eventContents = eventContents;
    }

    public String getBindingId() {
        return bindingId;
    }

    public void setBindingId(String bindingId) {
        this.bindingId = bindingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return Objects.equals(getId(), event.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + id +
            '}';
    }
}

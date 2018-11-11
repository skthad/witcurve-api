package com.witcurve.domain;

import com.witcurve.domain.enumeration.EventType;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="event")
public class Event extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eventIdSeq")
    @SequenceGenerator(name = "eventIdSeq", sequenceName="event_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType type;

    @NotNull
    @Column(name = "date", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate date;

    @Column(name = "event_start_time")
    @Pattern(regexp = "^([01]\\d|2[0-3])([0-5]\\d)$")
    private String eventStartTime;

    @Column(name = "event_end_time")
    @Pattern(regexp = "^([01]\\d|2[0-3])([0-5]\\d)$")
    private String eventEndTime;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Standard standard;

    @ManyToOne
    private Staff staff;

    @Column(name = "grade", length = 50)
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @ManyToOne
    private AcademicSession academicSession;

    @ManyToOne
    @JoinColumn(name = "scd_id")
    private SlotCourseDetails scd;

    @ManyToOne
    @JoinColumn(name = "course_teacher_id")
    private CourseTeacher courseTeacher;

    @Column(name = "binding_id")
    private String bindingId;

    @NotNull
    @Column(name = "send_sms", nullable = false)
    private Boolean sendSms = false;

    private Boolean present;

    private Integer marks;

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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Standard getStandard() {
        return standard;
    }

    public void setStandard(Standard standard) {
        this.standard = standard;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public AcademicSession getAcademicSession() {
        return academicSession;
    }

    public void setAcademicSession(AcademicSession academicSession) {
        this.academicSession = academicSession;
    }

    public SlotCourseDetails getScd() {
        return scd;
    }

    public void setScd(SlotCourseDetails scd) {
        this.scd = scd;
    }

    public CourseTeacher getCourseTeacher() {
        return courseTeacher;
    }

    public void setCourseTeacher(CourseTeacher courseTeacher) {
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

    public Boolean gePresent() {
        return present;
    }

    public void setPresent(Boolean present) {
        this.present = present;
    }

    public Integer getMarks() {
        return marks;
    }

    public void setMarks(Integer marks) {
        this.marks = marks;
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
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", type=" + type +
            ", date=" + date +
            ", eventStartTime='" + eventStartTime + '\'' +
            ", eventEndTime='" + eventEndTime + '\'' +
            ", student=" + student +
            ", standard=" + standard +
            ", staff=" + staff +
            ", grade=" + grade +
            ", academicSession=" + academicSession +
            ", scd=" + scd +
            ", bindingId='" + bindingId + '\'' +
            ", sendSms=" + sendSms +
            ( type == EventType.ATTENDANCE ? ", present=" + present : "") +
            ( type == EventType.TEST ? ", marks=" + marks : "") +
            '}';
    }
}

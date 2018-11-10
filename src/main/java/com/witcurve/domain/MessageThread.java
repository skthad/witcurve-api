package com.witcurve.domain;

import com.witcurve.domain.enumeration.MessageType;
import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="message_thread")
public class MessageThread extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messageThreadIdSeq")
    @SequenceGenerator(name = "messageThreadIdSeq", sequenceName = "message_thread_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @Column
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @ManyToOne
    private CourseTeacher courseTeacher;

    @ManyToOne
    private LeaveApplication leaveApplication;

    @ManyToOne
    private Guardian guardian;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private User fromUser;

    @ManyToOne
    private User toUser;

    @Column
    @Convert(converter = LocalDateConverter.class)
    private LocalDate meetingDate;

    @Column
    @Pattern(regexp = "^([01]\\d|2[0-3])([0-5]\\d)$")
    private String meetingTime;

    @NotNull
    @Column(nullable = false)
    private Boolean approved = false;

    @OneToMany

    private Set<Message> messages;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public CourseTeacher getCourseTeacher() {
        return courseTeacher;
    }

    public void setCourseTeacher(CourseTeacher courseTeacher) {
        this.courseTeacher = courseTeacher;
    }

    public LeaveApplication getLeaveApplication() {
        return leaveApplication;
    }

    public void setLeaveApplication(LeaveApplication leaveApplication) {
        this.leaveApplication = leaveApplication;
    }

    public Guardian getGuardian() {
        return guardian;
    }

    public void setGuardian(Guardian guardian) {
        this.guardian = guardian;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public LocalDate getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(LocalDate meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageThread that = (MessageThread) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MessageThread{" +
            "id=" + id +
            ", messageType=" + messageType +
            ", courseTeacher=" + courseTeacher +
            ", leaveApplication=" + leaveApplication +
            ", guardian=" + guardian +
            ", fromUser=" + fromUser +
            ", toUser=" + toUser +
            ", meetingDate=" + meetingDate +
            ", meetingTime='" + meetingTime + '\'' +
            ", approved=" + approved +
            '}';
    }
}

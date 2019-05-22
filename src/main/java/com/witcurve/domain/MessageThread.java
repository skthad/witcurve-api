package com.witcurve.domain;

import com.witcurve.domain.enumeration.ApprovalStatus;
import com.witcurve.domain.enumeration.MessageType;
import com.witcurve.service.util.InstantTimeConverter;
import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="message_thread")
public class MessageThread extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Column
    @Convert(converter = LocalDateConverter.class)
    private LocalDate meetingDate;

    @Column
    @Pattern(regexp = "^([01]\\d|2[0-3])([0-5]\\d)$")
    private String meetingTime;

    @ManyToOne
    private CourseTeacher courseTeacher;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private User fromUser;

    @NotNull
    @Column(nullable = false)
    private Integer fromUserUnreadCount=0;

    @Column
    @Convert(converter = InstantTimeConverter.class)
    private Instant fromUserLastMessageDate;

    @ManyToOne
    private User toUser;

    @NotNull
    @Column(nullable = false)
    private Integer toUserUnreadCount=1;

    @Column
    @Convert(converter = InstantTimeConverter.class)
    private Instant toUserLastMessageDate;

    @Column
    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;

    @NotNull
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean schoolBoardAdminMessage = false;

    @NotNull
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean superAdminMessage = false;

    @OneToOne
    @JoinColumn(unique = true)
    private LeaveApplication leaveApplication;

    @OneToMany(orphanRemoval = true, fetch=FetchType.EAGER)
    @JoinColumn(name="message_thread_id")
    @OrderBy("created_date ASC")
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

    public CourseTeacher getCourseTeacher() {
        return courseTeacher;
    }

    public void setCourseTeacher(CourseTeacher courseTeacher) {
        this.courseTeacher = courseTeacher;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public Integer getFromUserUnreadCount() {
        return fromUserUnreadCount;
    }

    public void setFromUserUnreadCount(Integer fromUserUnreadCount) {
        this.fromUserUnreadCount = fromUserUnreadCount;
    }

    public Instant getFromUserLastMessageDate() {
        return fromUserLastMessageDate;
    }

    public void setFromUserLastMessageDate(Instant fromUserLastMessageDate) {
        this.fromUserLastMessageDate = fromUserLastMessageDate;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public Integer getToUserUnreadCount() {
        return toUserUnreadCount;
    }

    public void setToUserUnreadCount(Integer toUserUnreadCount) {
        this.toUserUnreadCount = toUserUnreadCount;
    }

    public Instant getToUserLastMessageDate() {
        return toUserLastMessageDate;
    }

    public void setToUserLastMessageDate(Instant toUserLastMessageDate) {
        this.toUserLastMessageDate = toUserLastMessageDate;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }

    public Boolean getSchoolBoardAdminMessage() {
        return schoolBoardAdminMessage;
    }

    public void setSchoolBoardAdminMessage(Boolean schoolBoardAdminMessage) {
        this.schoolBoardAdminMessage = schoolBoardAdminMessage;
    }

    public Boolean getSuperAdminMessage() {
        return superAdminMessage;
    }

    public void setSuperAdminMessage(Boolean superAdminMessage) {
        this.superAdminMessage = superAdminMessage;
    }

    public LeaveApplication getLeaveApplication() {
        return leaveApplication;
    }

    public void setLeaveApplication(LeaveApplication leaveApplication) {
        this.leaveApplication = leaveApplication;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
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
            '}';
    }
}

package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.witcurve.domain.enumeration.ApprovalStatus;
import com.witcurve.domain.enumeration.MessageType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.witcurve.service.util.WitCurveConstants.DEFAULT_DATE_FORMAT;

public class MessageThreadDTO extends AbstractAuditingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private MessageType messageType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate meetingDate;

    @Pattern(regexp = "^([01]\\d|2[0-3])([0-5]\\d)$")
    private String meetingTime;

    private CourseTeacherDTO courseTeacherDTO;

    @NotNull
    private Long fromUserId;

    private String fromUserName;

    @NotNull
    private Integer fromUserUnreadCount=0;

    private Instant fromUserLastMessageDate;

    private Long toUserId;

    private String toUserName;

    @NotNull
    private Integer toUserUnreadCount=1;

    private Instant toUserLastMessageDate;

    private ApprovalStatus status;

    private LeaveApplicationDTO leaveApplicationDTO;

    @NotNull
    private List<MessageDTO> messageDTOs;

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

    public CourseTeacherDTO getCourseTeacherDTO() {
        return courseTeacherDTO;
    }

    public void setCourseTeacherDTO(CourseTeacherDTO courseTeacherDTO) {
        this.courseTeacherDTO = courseTeacherDTO;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
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

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
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

    public LeaveApplicationDTO getLeaveApplicationDTO() {
        return leaveApplicationDTO;
    }

    public void setLeaveApplicationDTO(LeaveApplicationDTO leaveApplicationDTO) {
        this.leaveApplicationDTO = leaveApplicationDTO;
    }

    public List<MessageDTO> getMessageDTOs() {
        return messageDTOs;
    }

    public void setMessageDTOs(List<MessageDTO> messageDTOs) {
        this.messageDTOs = messageDTOs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageThreadDTO that = (MessageThreadDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MessageThreadDTO{" +
            "id=" + id +
            '}';
    }
}

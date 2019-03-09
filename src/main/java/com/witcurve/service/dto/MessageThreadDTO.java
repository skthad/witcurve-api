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

    private Long id;

    @NotNull
    private MessageType messageType;

    private CourseTeacherDTO courseTeacherDTO;

    private LeaveApplicationDTO leaveApplicationDTO;

    private Long guardianId;

    @NotNull
    private Long fromUserId;

    private String fromUserName;

    private Long toUserId;

    @NotNull
    private Integer fromUserUnreadCount=0;

    @NotNull
    private Integer toUserUnreadCount=1;

    private Instant fromUserLastMessageDate;

    private Instant toUserLastMessageDate;

    private String toUserName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate meetingDate;

    @Pattern(regexp = "^([01]\\d|2[0-3])([0-5]\\d)$")
    private String meetingTime;

    private ApprovalStatus status;

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

    public CourseTeacherDTO getCourseTeacherDTO() {
        return courseTeacherDTO;
    }

    public void setCourseTeacherDTO(CourseTeacherDTO courseTeacherDTO) {
        this.courseTeacherDTO = courseTeacherDTO;
    }

    public LeaveApplicationDTO getLeaveApplicationDTO() {
        return leaveApplicationDTO;
    }

    public void setLeaveApplicationDTO(LeaveApplicationDTO leaveApplicationDTO) {
        this.leaveApplicationDTO = leaveApplicationDTO;
    }

    public Long getGuardianId() {
        return guardianId;
    }

    public void setGuardianId(Long guardianId) {
        this.guardianId = guardianId;
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

    public Integer getFromUserUnreadCount() {
        return fromUserUnreadCount;
    }

    public void setFromUserUnreadCount(Integer fromUserUnreadCount) {
        this.fromUserUnreadCount = fromUserUnreadCount;
    }

    public Integer getToUserUnreadCount() {
        return toUserUnreadCount;
    }

    public void setToUserUnreadCount(Integer toUserUnreadCount) {
        this.toUserUnreadCount = toUserUnreadCount;
    }

    public Instant getFromUserLastMessageDate() {
        return fromUserLastMessageDate;
    }

    public void setFromUserLastMessageDate(Instant fromUserLastMessageDate) {
        this.fromUserLastMessageDate = fromUserLastMessageDate;
    }

    public Instant getToUserLastMessageDate() {
        return toUserLastMessageDate;
    }

    public void setToUserLastMessageDate(Instant toUserLastMessageDate) {
        this.toUserLastMessageDate = toUserLastMessageDate;
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

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
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
            ", messageType=" + messageType +
            ", courseTeacherDTO=" + courseTeacherDTO +
            ", leaveApplicationDTO=" + leaveApplicationDTO +
            ", guardianId=" + guardianId +
            ", fromUserId=" + fromUserId +
            ", fromUserName='" + fromUserName + '\'' +
            ", toUserId=" + toUserId +
            ", fromUserUnreadCount=" + fromUserUnreadCount +
            ", toUserUnreadCount=" + toUserUnreadCount +
            ", fromUserLastMessageDate=" + fromUserLastMessageDate +
            ", toUserLastMessageDate=" + toUserLastMessageDate +
            ", toUserName='" + toUserName + '\'' +
            ", meetingDate=" + meetingDate +
            ", meetingTime='" + meetingTime + '\'' +
            ", status=" + status +
            ", messageDTOs=" + messageDTOs +
            '}';
    }
}

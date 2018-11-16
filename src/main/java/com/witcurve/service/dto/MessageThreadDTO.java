package com.witcurve.service.dto;

import com.witcurve.domain.Message;
import com.witcurve.domain.enumeration.MessageType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

public class MessageThreadDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private MessageType messageType;

    private CourseTeacherDTO courseTeacherDTO;

    private LeaveApplicationDTO leaveApplicationDTO;

    private Long guardianId;

    @NotNull
    private Long fromUserId;

    private Long toUserId;

    private LocalDate meetingDate;

    @Pattern(regexp = "^([01]\\d|2[0-3])([0-5]\\d)$")
    private String meetingTime;

    @NotNull
    private Boolean approved = false;

    @NotNull
    private List<MessageDTO> messageDTOs;

    private Boolean read;

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

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
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

    public List<MessageDTO> getMessageDTOs() {
        return messageDTOs;
    }

    public void setMessageDTOs(List<MessageDTO> messageDTOs) {
        if(messageDTOs != null){
            Collections.sort(messageDTOs, new Comparator<MessageDTO>() {
                @Override
                public int compare(MessageDTO o1, MessageDTO o2) {
                    return o1.getCreatedDate().isBefore(o2.getCreatedDate()) ? -1
                        : o1.getCreatedDate().isAfter(o2.getCreatedDate()) ? 1
                        : 0;
                }
            });
        }
        this.messageDTOs = messageDTOs;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
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
            ", toUserId=" + toUserId +
            ", meetingDate=" + meetingDate +
            ", meetingTime='" + meetingTime + '\'' +
            ", approved=" + approved +
            ", messageDTOs=" + messageDTOs +
            ", read=" + read +
            '}';
    }
}

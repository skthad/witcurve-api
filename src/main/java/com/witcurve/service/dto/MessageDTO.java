package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class MessageDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String subject;

    @NotNull
    private String body;

    @NotNull
    private Long fromUserId;

    private String fromUserName;

    private Long toUserId;

    private String toUserName;

    private Long messageThreadId;

    @NotNull
    private Boolean read = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public Long getMessageThreadId() {
        return messageThreadId;
    }

    public void setMessageThreadId(Long messageThreadId) {
        this.messageThreadId = messageThreadId;
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
        MessageDTO that = (MessageDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
            "id=" + id +
            ", subject='" + subject + '\'' +
            ", body='" + body + '\'' +
            ", fromUserId=" + fromUserId +
            ", fromUserName=" + fromUserName +
            ", toUserId=" + toUserId +
            ", toUserName=" + toUserName +
            ", messageThreadId=" + messageThreadId +
            ", read=" + read +
            '}';
    }
}

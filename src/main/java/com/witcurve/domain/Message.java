package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="message")
public class Message extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messageIdSeq")
    @SequenceGenerator(name = "messageIdSeq", sequenceName="message_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String subject;

    @NotNull
    @Column(nullable = false)
    private String body;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private User fromUser;

    @ManyToOne
    private User toUser;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private MessageThread messageThread;

    @NotNull
    @Column(nullable = false)
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

    public MessageThread getMessageThread() {
        return messageThread;
    }

    public void setMessageThread(MessageThread messageThread) {
        this.messageThread = messageThread;
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
        Message message = (Message) o;
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Message{" +
            "id=" + id +
            ", subject='" + subject + '\'' +
            ", body='" + body + '\'' +
            ", fromUser=" + fromUser +
            ", toUser=" + toUser +
            ", messageThread=" + messageThread +
            ", read=" + read +
            '}';
    }


}
